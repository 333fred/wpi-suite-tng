/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steven Kordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

@SuppressWarnings ("serial")
public class TreeTransferHandler extends TransferHandler implements
		ISaveNotifier {
	
	private class NodesTransferable implements Transferable {
		
		DefaultMutableTreeNode[] nodes;
		
		private NodesTransferable(final DefaultMutableTreeNode[] nodes) {
			this.nodes = nodes;
		}
		
		@Override
		public Object getTransferData(final DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			return nodes;
		}
		
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}
		
		@Override
		public boolean isDataFlavorSupported(final DataFlavor flavor) {
			return nodesFlavor.equals(flavor);
		}
	}
	
	DataFlavor nodesFlavor;
	DataFlavor[] flavors = new DataFlavor[1];
	DefaultMutableTreeNode[] nodesToRemove;
	private final MainTabController tabController;
	
	private Requirement draggedRequirement;
	
	public TreeTransferHandler(final MainTabController tabController) {
		this.tabController = tabController;
		try {
			final String mimeType = DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=\""
					+ javax.swing.tree.DefaultMutableTreeNode[].class.getName()
					+ "\"";
			nodesFlavor = new DataFlavor(mimeType);
			flavors[0] = nodesFlavor;
		} catch (final ClassNotFoundException e) {
			System.out.println("ClassNotFound: " + e.getMessage());
		}
	}
	
	@Override
	public boolean canImport(final TransferHandler.TransferSupport support) {
		if (!PermissionModel.getInstance().getUserPermissions()
				.canEditRequirement()) {
			return false;
		}
		if (!support.isDrop()) {
			return false;
		}
		support.setShowDropLocation(true);
		if (!support.isDataFlavorSupported(nodesFlavor)) {
			return false;
		}
		// Do not allow a drop on the drag source selections.
		final JTree.DropLocation dl = (JTree.DropLocation) support
				.getDropLocation();
		final JTree tree = (JTree) support.getComponent();
		// int dropRow = tree.getRowForPath(dl.getPath());
		final int[] selRows = tree.getSelectionRows();
		/*
		 * for (int i = 0; i < selRows.length; i++) { if (selRows[i] == dropRow)
		 * { return false; } }
		 */
		// Do not allow a non-leaf node to be copied to a level
		// which is less than its source level.
		final TreePath dest = dl.getPath();
		if (dest == null) {
			return false;
		}
		final DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		if (selRows == null) {
			return false;
		}
		final TreePath path = tree.getPathForRow(selRows[0]);
		
		final DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		if ((firstNode.getChildCount() > 0)
				&& (target.getLevel() < firstNode.getLevel())) {
			return false;
		}
		// Do not allowing dropping requirements into requirements
		if (target.getLevel() != 1) {
			return false;
		}
		// Do not allow dropping of iterations into iterations
		if (firstNode.getLevel() == 1) {
			return false;
		}
		// Do not allow dropping of deleted requirements
		if (((Requirement) firstNode.getUserObject()).getStatus() == Status.DELETED) {
			return false;
		}
		
		// don't allow dropping into completed iterations
		final Date currentDate = new Date();
		final Iteration iteration = (Iteration) target.getUserObject();
		if ((currentDate.compareTo(iteration.getEndDate()) > 0)
				&& (iteration.getId() != -1)) {
			return false;
		}
		
		// don't allow dropping into deleted iteration
		if (iteration.getId() == -2) {
			return false;
		}
		
		// don't allow dropping into iteration that is already in
		if (firstNode.getParent() == target) {
			return false;
		}
		// Do not allow MOVE-action drops if a non-leaf node is
		// selected unless all of its children are also selected.
		final int action = support.getDropAction();
		if (action == TransferHandler.MOVE) {
			return haveCompleteNode(tree);
		}
		return true;
	}
	
	/** Defensive copy used in createTransferable. */
	private DefaultMutableTreeNode copy(final TreeNode node) {
		return new DefaultMutableTreeNode(node);
	}
	
	@Override
	protected Transferable createTransferable(final JComponent c) {
		final JTree tree = (JTree) c;
		final TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			// Make up a node array of copies for transfer and
			// another for/of the nodes that will be removed in
			// exportDone after a successful drop.
			final List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
			final List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>();
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0]
					.getLastPathComponent();
			final DefaultMutableTreeNode copy = copy(node);
			copies.add(copy);
			toRemove.add(node);
			for (int i = 1; i < paths.length; i++) {
				final DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i]
						.getLastPathComponent();
				// Do not allow higher level nodes to be added to list.
				if (next.getLevel() < node.getLevel()) {
					break;
				} else if (next.getLevel() > node.getLevel()) { // child node
					copy.add(copy(next));
					// node already contains child
				} else { // sibling
					copies.add(copy(next));
					toRemove.add(next);
				}
			}
			final DefaultMutableTreeNode[] nodes = copies
					.toArray(new DefaultMutableTreeNode[copies.size()]);
			nodesToRemove = toRemove
					.toArray(new DefaultMutableTreeNode[toRemove.size()]);
			return new NodesTransferable(nodes);
		}
		return null;
	}
	
	@Override
	protected void exportDone(final JComponent source, final Transferable data,
			final int action) {
		if ((action & TransferHandler.MOVE) == TransferHandler.MOVE) {
			final JTree tree = (JTree) source;
			final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Remove nodes saved in nodesToRemove in createTransferable.
			for (final DefaultMutableTreeNode element : nodesToRemove) {
				model.removeNodeFromParent(element);
			}
		}
	}
	
	@Override
	public void fail(final Exception exception) {
		// TODO Auto-generated method stub
	}
	
	public Requirement getDraggedRequirement() {
		return draggedRequirement;
	}
	
	@Override
	public int getSourceActions(final JComponent c) {
		return TransferHandler.COPY_OR_MOVE;
	}
	
	/**
	 * @return the tabController
	 */
	public MainTabController getTabController() {
		return tabController;
	}
	
	private boolean haveCompleteNode(final JTree tree) {
		final int[] selRows = tree.getSelectionRows();
		TreePath path = tree.getPathForRow(selRows[0]);
		final DefaultMutableTreeNode first = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		final int childCount = first.getChildCount();
		// first has children and no children are selected.
		if ((childCount > 0) && (selRows.length == 1)) {
			return false;
		}
		// first may have children.
		for (int i = 1; i < selRows.length; i++) {
			path = tree.getPathForRow(selRows[i]);
			final DefaultMutableTreeNode next = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (first.isNodeChild(next)) {
				// Found a child of first.
				if (childCount > (selRows.length - 1)) {
					// Not all children of first are selected.
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean importData(final TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}
		// Extract transfer data.
		DefaultMutableTreeNode[] nodes = null;
		try {
			final Transferable t = support.getTransferable();
			nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
		} catch (final UnsupportedFlavorException ufe) {
			System.out.println("UnsupportedFlavor: " + ufe.getMessage());
		} catch (final java.io.IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
		}
		// Get drop location info.
		final JTree.DropLocation dl = (JTree.DropLocation) support
				.getDropLocation();
		final int childIndex = dl.getChildIndex();
		final TreePath dest = dl.getPath();
		final DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		final JTree tree = (JTree) support.getComponent();
		final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		// Configure for drop mode.
		int index = childIndex; // DropMode.INSERT
		if (childIndex == -1) { // DropMode.ON
			index = parent.getChildCount();
		}
		// Add data to model.
		for (final DefaultMutableTreeNode node : nodes) {
			model.insertNodeInto(node, parent, index++);
			final IterationController iterationController = new IterationController();
			final Requirement requirement = (Requirement) (((DefaultMutableTreeNode) node
					.getUserObject()).getUserObject());
			Iteration anIteration;
			try {
				anIteration = IterationDatabase.getInstance().get(
						requirement.getIteration());
				anIteration.removeRequirement(requirement.getrUID());
				final UpdateIterationRequestObserver observer = new UpdateIterationRequestObserver(
						this);
				iterationController.save(anIteration, observer);
			} catch (final IterationNotFoundException e) {
				e.printStackTrace();
			}
			anIteration = (Iteration) ((DefaultMutableTreeNode) node
					.getParent()).getUserObject();
			anIteration.addRequirement(requirement.getrUID());
			final UpdateIterationRequestObserver observer = new UpdateIterationRequestObserver(
					this);
			iterationController.save(anIteration, observer);
			requirement.setIteration(anIteration.getId());
			final RequirementsController requirementController = new RequirementsController();
			final UpdateRequirementRequestObserver reqObserver = new UpdateRequirementRequestObserver(
					this);
			requirementController.save(requirement, reqObserver);
			draggedRequirement = requirement;
		}
		
		return true;
	}
	
	@Override
	public void responseError(final int statusCode, final String statusMessage) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void responseSuccess() {
		if (getTabController() != null) {
			
			final Requirement requirement = getDraggedRequirement();
			
			for (int i = 0; i < getTabController().getTabView().getTabCount(); i++) {
				if (getTabController().getTabView().getComponentAt(i) instanceof DetailPanel) {
					if (((((DetailPanel) getTabController().getTabView()
							.getComponentAt(i))).getModel().getrUID()) == (requirement
							.getrUID())) {
						try {
							(((DetailPanel) getTabController().getTabView()
									.getComponentAt(i))).getComboBoxIteration()
									.setSelectedItem(
											IterationDatabase
													.getInstance()
													.get(requirement
															.getIteration())
													.getName());
						} catch (final IterationNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}
	}
	
	@Override
	public String toString() {
		return getClass().getName();
	}
	
}