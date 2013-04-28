/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick Massa
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.DropMode;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveRequirementByIDRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subreqpopupmenu.SubReqAnywherePopupMenu;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subreqpopupmenu.SubReqRequirementPopupMenu;

public class SubRequirementTreeView extends JPanel implements
		IDatabaseListener, IReceivedAllRequirementNotifier {
	
	/**
	 * Grabs the state of the tree as a string
	 * 
	 * @param tree
	 *            Tree whose state we want
	 * @param row
	 *            The row we are visiting
	 * @return The expansion state in the form of a string
	 */
	public static String getExpansionState(final JTree tree, final int row) {
		final TreePath rowPath = tree.getPathForRow(row);
		final StringBuffer buf = new StringBuffer();
		final int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			final TreePath path = tree.getPathForRow(i);
			if ((i == row)
					|| SubRequirementTreeView.isDescendant(path, rowPath)) {
				if (tree.isExpanded(path)) {
					buf.append("," + String.valueOf(i - row));
				}
			} else {
				break;
			}
		}
		return buf.toString();
	}
	
	/**
	 * Check if path2 contains path1
	 * 
	 * @param path1
	 *            First path we check if is contained
	 * @param path2
	 *            Second path we check if it contains the first
	 * @return Whether or not path1 was in path2
	 */
	public static boolean isDescendant(TreePath path1, final TreePath path2) {
		int count1 = path1.getPathCount();
		final int count2 = path2.getPathCount();
		if (count1 <= count2) {
			return false;
		}
		while (count1 != count2) {
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
	}
	
	/**
	 * Restore the visual state of the tree
	 * 
	 * @param tree
	 *            The tree we are restoring
	 * @param row
	 *            The row we need to update
	 * @param expansionState
	 *            The way the tree is expanded
	 */
	public static void restoreExpanstionState(final JTree tree, final int row,
			final String expansionState) {
		final StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			final int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}
	
	private final JTree tree;
	
	private final JScrollPane treeView;
	
	private final DefaultMutableTreeNode top;
	
	private final RetrieveAllRequirementsRequestObserver retrieveAllRequirementsController;
	
	private final MainTabController tabController;
	
	/** List of all the iterations currently being displayed */
	List<Requirement> requirements;
	
	private boolean firstPaint;
	
	/**
	 * The subrequirement tree of all requirements of a project
	 * It displays requirements by whether it has children or not, and then
	 * by ID
	 * Deleted requirements are placed in a separate folder
	 * 
	 * @param tabController
	 *            The maintabcontroller this is inside of
	 */
	public SubRequirementTreeView(final MainTabController tabController) {
		super(new BorderLayout());
		this.tabController = tabController;
		
		retrieveAllRequirementsController = new RetrieveAllRequirementsRequestObserver(
				this);
		
		firstPaint = true;
		
		top = new DefaultMutableTreeNode("<html><b>Requirements</b></html>");
		tree = new JTree(top);
		tree.setEditable(false);
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON);
		
		tree.setTransferHandler(new SubReqTreeTransferHandler(tabController));
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		treeView = new JScrollPane(tree);
		this.add(treeView, BorderLayout.CENTER);
		
		RequirementDatabase.getInstance().registerListener(this);
		
		requirements = RequirementDatabase.getInstance().getAll();
		updateTreeView();
		final MouseListener ml = new MouseAdapter() {
			
			@Override
			public void mousePressed(final MouseEvent e) { // Listener for when
															// we click on the
															// tree view
				final int selRow = tree.getRowForLocation(e.getX(),
						e.getY());
				final TreePath selPath = tree.getPathForLocation(e.getX(),
						e.getY());
				if (e.getButton() == MouseEvent.BUTTON1) {
					//this was a left click
					if (selRow != -1) { // If we double click, call function to
										// handle double click on that spot
						if (e.getClickCount() == 2) {
							onDoubleClick(selRow, selPath);
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// This was a right click
					onRightClick(e.getX(), e.getY(), selRow, selPath); // Call
																		// function
																		// to
																		// handle
																		// the
																		// right
																		// click
				}
			}
		};
		tree.addMouseListener(ml);
	}
	
	@Override
	public void errorReceivingData(
			final String RetrieveAllRequirementsRequestObserver) {
		
	}
	
	/**
	 * Method called when we double click in the tree view
	 * 
	 * @param selRow
	 *            The int of the index where we clicked
	 * @param selPath
	 *            The treepath where we clicked
	 */
	protected void onDoubleClick(final int selRow, final TreePath selPath) {
		if (((DefaultMutableTreeNode) selPath.getLastPathComponent())
				.getLevel() == 0) {
			return;
		}
		boolean requirementIsOpen = false;
		
		final Requirement requirement = RequirementDatabase.getInstance()
				.getRequirement(
						((DefaultMutableTreeNode) selPath
								.getLastPathComponent()).toString());
		
		// Check to make sure the requirement is not already being
		// displayed. This is assuming that the list view is displayed in
		// the left most tab, index 0
		for (int i = 0; i < tabController.getTabView().getTabCount(); i++) {
			if (tabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				if (((((DetailPanel) tabController.getTabView().getComponentAt(
						i))).getModel().getrUID()) == (requirement.getrUID())) {
					tabController.switchToTab(i);
					requirementIsOpen = true;
				}
			}
		}
		if (!requirementIsOpen) {
			// create the controller for fetching the new requirement
			final RequirementsController controller = new RequirementsController();
			final RetrieveRequirementByIDRequestObserver observer = new RetrieveRequirementByIDRequestObserver(
					new OpenRequirementTabAction(tabController, requirement));
			
			// get the requirement from the server
			controller.get(requirement.getrUID(), observer);
		}
		this.tree.setSelectionPath(selPath); //Prevent null pointers on Mouse Release when focus changes
	}
	
	/**
	 * Function to display the popupmenu when we right click on the subreq tree
	 * 
	 * @param x
	 *            The x-coordinates of the click
	 * @param y
	 *            The y-coordinates of the click
	 * @param selRow
	 *            The int array of locations on the tree we clicked
	 * @param selPath
	 *            The treepath of where we clicked
	 */
	protected void onRightClick(int x, final int y, final int selRow,
			final TreePath selPath) {
		// add a menu offset
		x += 10;
		
		// we right clicked on something in particular
		JPopupMenu menuToShow;
		if (selRow != -1) {
			final int levelClickedOn = ((DefaultMutableTreeNode) selPath
					.getLastPathComponent()).getLevel();
			
			if (tree.getSelectionModel().getSelectionMode() == TreeSelectionModel.SINGLE_TREE_SELECTION) {
				// we are in single selection mode
				tree.setSelectionPath(selPath);
				
			} else {
				// multi selection mode
				tree.addSelectionPath(selPath);
			}
			if (levelClickedOn > 0) {
				final List<Requirement> selectedRequirements = new ArrayList<Requirement>();
				final TreePath path = tree.getPathForRow(selRow);
				final DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				if (firstNode.getUserObject().equals(
						"<html><b><i>Deleted</i></b></html>")) {
					return;
				}
				final Requirement tempReq = (Requirement) firstNode
						.getUserObject();
				selectedRequirements.add(tempReq);
				menuToShow = new SubReqRequirementPopupMenu(tabController,
						selectedRequirements);
				menuToShow.show(this, x, y);
			}
		} else {
			menuToShow = new SubReqAnywherePopupMenu(tabController);
			menuToShow.show(this, x, y);
		}
		
	}
	
	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		if (firstPaint) {
			firstPaint = false;
			
			refresh();
		}
	}
	
	@Override
	public void receivedData(final Requirement[] requirements) {
		refresh();
	}
	
	public void refresh() {
		requirements = RequirementDatabase.getInstance().getAll();
		updateTreeView();
	}
	
	@Override
	public boolean shouldRemove() {
		return false;
	}
	
	/**
	 * Sort a list of subrequirements
	 * First by whether each subreq has children, then by ID
	 * 
	 * @param subreqs
	 *            The integer list of subrequirements
	 * @return The arraylist of all requirements
	 */
	public List<Requirement> sortSubRequirements(final List<Integer> subreqs) {
		List<Requirement> topReqsWithChildren = new ArrayList<Requirement>();
		List<Requirement> topReqsWOChildren = new ArrayList<Requirement>();
		Requirement tempReq = null;
		
		for (final Integer anReq : subreqs) {
			try {
				tempReq = RequirementDatabase.getInstance().get(anReq);
				if (tempReq.getSubRequirements().size() > 0) {
					topReqsWithChildren.add(tempReq);
				} else {
					topReqsWOChildren.add(tempReq);
				}
			} catch (final RequirementNotFoundException e) {
				System.out
						.println("GRRR Requirement not found: SubRequirementTreeView:372"); // TODO:
																							// remove
																							// this?
			}
			
		}
		
		topReqsWOChildren = Requirement.sortRequirements(topReqsWOChildren);
		topReqsWithChildren = Requirement.sortRequirements(topReqsWithChildren);
		
		final List<Requirement> allSubReqs = new ArrayList<Requirement>();
		allSubReqs.addAll(topReqsWithChildren);
		allSubReqs.addAll(topReqsWOChildren);
		
		return allSubReqs;
		
	}
	
	@Override
	public void update() {
	}
	
	/**
	 * Add subrequirements to a node
	 * 
	 * @param anReq
	 *            The requirement we need to add it's subrequirements to
	 * @param node
	 *            The node that the requirement is at, for the subnodes to be
	 *            added to
	 */
	public void updateTreeNodes(final Requirement anReq,
			final DefaultMutableTreeNode node) {
		DefaultMutableTreeNode subRequirementNode = null;
		
		final List<Requirement> subReqs = sortSubRequirements(anReq
				.getSubRequirements());
		
		for (final Requirement aReq : subReqs) {
			subRequirementNode = new DefaultMutableTreeNode(aReq);
			node.add(subRequirementNode);
			updateTreeNodes(aReq, subRequirementNode);
		}
		
	}
	
	/**
	 * Function to update the tree view
	 * It creates a Requirement root, and adds requirements without parents,
	 * first by requirements with children, then by ID (= creation date)
	 */
	public void updateTreeView() {
		final String eState = SubRequirementTreeView.getExpansionState(tree, 0);
		DefaultMutableTreeNode requirementNode = null;
		final DefaultMutableTreeNode subRequirementNode = null;
		final Requirement tempReq = null;
		List<Requirement> topReqsWithChildren = new ArrayList<Requirement>();
		List<Requirement> topReqsWOChildren = new ArrayList<Requirement>();
		List<Requirement> deletedReqs = new ArrayList<Requirement>();
		top.removeAllChildren();
		
		if (requirements != null) {
			for (final Requirement anReq : requirements) {
				if (anReq.getpUID().size() == 0) {
					if (anReq.getSubRequirements().size() > 0) {
						topReqsWithChildren.add(anReq);
					} else if (anReq.getStatus() != Status.DELETED) {
						topReqsWOChildren.add(anReq);
					} else {
						deletedReqs.add(anReq);
					}
				}
			}
			topReqsWOChildren = Requirement.sortRequirements(topReqsWOChildren);
			topReqsWithChildren = Requirement
					.sortRequirements(topReqsWithChildren);
			deletedReqs = Requirement.sortRequirements(deletedReqs);
			
			for (final Requirement anReq : topReqsWithChildren) {
				
				requirementNode = new DefaultMutableTreeNode(anReq);
				
				updateTreeNodes(anReq, requirementNode);
				top.add(requirementNode);
			}
			
			for (final Requirement anReq : topReqsWOChildren) {
				requirementNode = new DefaultMutableTreeNode(anReq);
				updateTreeNodes(anReq, requirementNode);
				top.add(requirementNode);
			}
			
			
			//Removed the Deleted Tree Node code from the hierarchy tree. This was seens as pointless to display here. Saved in case
			//we decide to reimplement it.
			
			/*
			final DefaultMutableTreeNode deletedNode = new DefaultMutableTreeNode(
					"<html><b><i>Deleted</i></b></html>");
			for (final Requirement anReq : deletedReqs) {
				requirementNode = new DefaultMutableTreeNode(anReq);
				deletedNode.add(requirementNode);
			}
			top.add(deletedNode);
			*/
			
			((DefaultTreeModel) tree.getModel()).nodeStructureChanged(top);
			final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree
					.getCellRenderer();
			renderer.setLeafIcon(null);
			tree.setCellRenderer(renderer);
			SubRequirementTreeView.restoreExpanstionState(tree, 0, eState);
			
		}
	}
	
	/** Will disable all of the fields in this View
	 * 
	 */
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tree.setEnabled(enabled);
	}
	
}
