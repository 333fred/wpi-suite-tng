/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steven Kordell, Nick Massa
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

@SuppressWarnings("serial")
public class SubReqTreeTransferHandler extends TransferHandler implements
		ISaveNotifier {
	DataFlavor nodesFlavor;
	DataFlavor[] flavors = new DataFlavor[1];
	DefaultMutableTreeNode[] nodesToRemove;
	private MainTabController tabController;
	private Requirement draggedRequirement;

	/**
	 * Handler for dragging and dropping in the sub-requirement tree
	 * Determines what can and can't be dragged, what it can be dragged to,
	 * and processes the action
	 * Can drag a requirement into another to make the second the parent
	 * Can drag a requirement to the Requirement folder to remove the parent
	 * 
	 * @param tabController Maintabcontroller this is contained inside of
	 */
	public SubReqTreeTransferHandler(MainTabController tabController) {
		this.tabController = tabController;
		try {
			String mimeType = DataFlavor.javaJVMLocalObjectMimeType
					+ ";class=\""
					+ javax.swing.tree.DefaultMutableTreeNode[].class.getName()
					+ "\"";
			nodesFlavor = new DataFlavor(mimeType);
			flavors[0] = nodesFlavor;
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound: " + e.getMessage());
		}
	}

	@Override
	public boolean canImport(TransferHandler.TransferSupport support) {
		//Don't drag and drop if we don't have permission
		if (!PermissionModel.getInstance().getUserPermissions().canEditRequirement())
			return false;
		
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		JTree tree = (JTree) support.getComponent();
		int[] selRows = tree.getSelectionRows(); //Grab our source indexes (implemented as 1)
		
		TreePath dest = dl.getPath(); //Grab our destination
		
		//Don't allow dragging off the tree
		if(dest==null)
			return false;
		
		DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest
				.getLastPathComponent(); //Grab the object we dragged to as node
		TreePath path = tree.getPathForRow(selRows[0]); //Grab the object we dragged from as node
		DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		
		//If the destination is the deleted folder, don't do anything
		if(target.getUserObject().equals("Deleted")||firstNode.getUserObject().equals("Deleted"))
			return false;
		
		if (firstNode.getLevel() != 0 && target.getLevel() != 0) {
			Requirement requirement = (Requirement) firstNode.getUserObject();
			Requirement anRequirement = (Requirement) target.getUserObject(); //Grab the objects as requirements
			if(requirement.getStatus()==Status.DELETED || anRequirement.getStatus()==Status.DELETED)
				return false; //If the requirements are deleted or completed, don't allow changes
			if(requirement.getStatus()==Status.COMPLETE || anRequirement.getStatus()==Status.COMPLETE)
				return false;
			if (containsCurrentRequirement(requirement, anRequirement))
				return false; //If the destination is within the source, don't allow a cycle!
		}
		//Don't allow dragging of the Requirement folder
		if (firstNode.getLevel() == 0)
			return false;
		//Don't allow dragging to itself or the parent
		if (firstNode == target || target == firstNode.getParent())
			return false;
		
		if(firstNode.getLevel()!=0){//Make sure if we're dragging one requirement, it is not deleted or completed
			Requirement anRequirement = (Requirement) firstNode.getUserObject();
			if(anRequirement.getStatus()==Status.DELETED || anRequirement.getStatus()==Status.COMPLETE)
				return false;
		}
		
		//Can transfer!
		return true;
	}

	/**
	 * Make sure a given tree is complete
	 * @param tree The tree to check
	 * @return Whether the tree has complete nodes
	 */
	protected boolean haveCompleteNode(JTree tree) {
		int[] selRows = tree.getSelectionRows();
		TreePath path = tree.getPathForRow(selRows[0]);
		DefaultMutableTreeNode first = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		int childCount = first.getChildCount();
		// first has children and no children are selected.
		if (childCount > 0 && selRows.length == 1)
			return false;
		// first may have children.
		for (int i = 1; i < selRows.length; i++) {
			path = tree.getPathForRow(selRows[i]);
			DefaultMutableTreeNode next = (DefaultMutableTreeNode) path
					.getLastPathComponent();
			if (first.isNodeChild(next)) {
				// Found a child of first.
				if (childCount > selRows.length - 1) {
					// Not all children of first are selected.
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected Transferable createTransferable(JComponent c) {
		JTree tree = (JTree) c;
		TreePath[] paths = tree.getSelectionPaths();
		if (paths != null) {
			// Make up a node array of copies for transfer and
			// another for/of the nodes that will be removed in
			// exportDone after a successful drop.
			List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
			List<DefaultMutableTreeNode> toRemove = new ArrayList<DefaultMutableTreeNode>();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0]
					.getLastPathComponent();
			DefaultMutableTreeNode copy = copy(node);
			copies.add(copy);
			toRemove.add(node);
			for (int i = 1; i < paths.length; i++) {
				DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i]
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
			DefaultMutableTreeNode[] nodes = copies
					.toArray(new DefaultMutableTreeNode[copies.size()]);
			nodesToRemove = toRemove
					.toArray(new DefaultMutableTreeNode[toRemove.size()]);
			return new NodesTransferable(nodes);
		}
		return null;
	}

	/** Defensive copy used in createTransferable. */
	private DefaultMutableTreeNode copy(TreeNode node) {
		return new DefaultMutableTreeNode(node);
	}
	
	/**
	 * Check if a requirement is in the sub-req tree of the other
	 * @param req The requirement we want to see if it is a member
	 * @param current The requirement whose tree we want to check
	 * @return Whether or not req was contained inside current
	 */
	public boolean containsCurrentRequirement(Requirement req,
			Requirement current) {
		Requirement child = null;
		Boolean check = false;
		if (req.getrUID() == current.getrUID()) {
			return true;
		} else {
			for (Integer i : req.getSubRequirements()) {
				try {
					child = RequirementDatabase.getInstance().get(i);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
				check = containsCurrentRequirement(child, current);
				if (check)
					return check;
			}
			return false;
		}
	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		if ((action & MOVE) == MOVE) {
			JTree tree = (JTree) source;
			DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
			// Remove nodes saved in nodesToRemove in createTransferable.
			for (int i = 0; i < nodesToRemove.length; i++) {
				model.removeNodeFromParent(nodesToRemove[i]);
			}
		}
	}

	@Override
	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	@Override
	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}
		// Extract transfer data.
		DefaultMutableTreeNode[] nodes = null;
		try {
			Transferable t = support.getTransferable();
			nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
		} catch (UnsupportedFlavorException ufe) {
			System.out.println("UnsupportedFlavor: " + ufe.getMessage());
		} catch (java.io.IOException ioe) {
			System.out.println("I/O error: " + ioe.getMessage());
		}
		// Get drop location info.
		JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
		int childIndex = dl.getChildIndex();
		TreePath dest = dl.getPath();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		JTree tree = (JTree) support.getComponent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		// Configure for drop mode.
		int index = childIndex; // DropMode.INSERT
		if (childIndex == -1) { // DropMode.ON
			index = parent.getChildCount();
		}
		// Add data to model
		int[] selRows = tree.getSelectionRows();
		dest = dl.getPath();
		TreePath path = tree.getPathForRow(selRows[0]);
		//Grab the target destination and dragged source
		DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest
				.getLastPathComponent();
		DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		//Initialize the controllers and observers
		RequirementsController RequirementsController = new RequirementsController();
		UpdateRequirementRequestObserver reqObserver = new UpdateRequirementRequestObserver(
				this);

		if (target.getLevel() != 0) {//If we dragged a requirement to another requirement			
			Requirement requirement = (Requirement) firstNode.getUserObject();
			Requirement anRequirement = (Requirement) target.getUserObject();
			Requirement parentRequirement = null;

			Integer anReqID = new Integer(anRequirement.getrUID());
			Integer reqID = new Integer(requirement.getrUID());

			//If the source has a parent, remove it
			if (requirement.getpUID().size() > 0) {
				try {
					parentRequirement = RequirementDatabase.getInstance().get(
							requirement.getpUID().get(0));
					parentRequirement.removeSubRequirement(reqID);
					requirement.removePUID(parentRequirement.getrUID());
					RequirementsController.save(parentRequirement, reqObserver);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}

			//Set the source as child of target, and target as parent of child, and save
			anRequirement.addSubRequirement(reqID);
			requirement.addPUID(anReqID);
			RequirementsController.save(anRequirement, reqObserver);
			RequirementsController.save(requirement, reqObserver);
			this.draggedRequirement = requirement;
		} else {
			//We dragged a requirement to the top Requirement folder. Remove the parent and save
			Requirement requirement = (Requirement) firstNode.getUserObject();
			Requirement parentRequirement = null;
			Integer reqID = new Integer(requirement.getrUID());

			if (requirement.getpUID().size() > 0) {
				try {
					parentRequirement = RequirementDatabase.getInstance().get(
							requirement.getpUID().get(0));
					parentRequirement.removeSubRequirement(reqID);
					requirement.removePUID(parentRequirement.getrUID());
					RequirementsController.save(parentRequirement, reqObserver);
					RequirementsController.save(requirement, reqObserver);
				} catch (RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		return true; //Success
	}

	@Override
	public String toString() {
		return getClass().getName();
	}

	private class NodesTransferable implements Transferable {
		DefaultMutableTreeNode[] nodes;

		private NodesTransferable(DefaultMutableTreeNode[] nodes) {
			this.nodes = nodes;
		}

		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException {
			if (!isDataFlavorSupported(flavor))
				throw new UnsupportedFlavorException(flavor);
			return nodes;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return flavors;
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return nodesFlavor.equals(flavor);
		}
	}

	@Override
	public void responseSuccess() {
		//Update all tabs of their total estimates and sub requirement panel
			for (int i = 0; i < getTabController().getTabView().getTabCount(); i++) {
				if (getTabController().getTabView().getComponentAt(i) instanceof DetailPanel) {
					DetailPanel detailPanel = (((DetailPanel) getTabController().getTabView().getComponentAt(i)));
					detailPanel.updateTotalEstimate();
					detailPanel.updateSubReqTab();
					
					
					try {
						detailPanel.determineAvailableStatusOptions(RequirementDatabase.getInstance().get(detailPanel.getRequirement().getrUID()));
					} catch (RequirementNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
	}


	@Override
	public void responseError(int statusCode, String statusMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void fail(Exception exception) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the tabController
	 */
	public MainTabController getTabController() {
		return tabController;
	}

	/**
	 * @return the dragged requirement
	 */
	public Requirement getDraggedRequirement() {
		return draggedRequirement;
	}

}