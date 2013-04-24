/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
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
	private JTree tree;
	private JScrollPane treeView;
	private DefaultMutableTreeNode top;
	private RetrieveAllRequirementsRequestObserver retrieveAllRequirementsController;

	private MainTabController tabController;

	/** List of all the iterations currently being displayed */
	List<Requirement> requirements;

	private boolean firstPaint;

	public SubRequirementTreeView(MainTabController tabController) {
		super(new BorderLayout());
		this.tabController = tabController;

		retrieveAllRequirementsController = new RetrieveAllRequirementsRequestObserver(
				this);

		firstPaint = true;

		this.top = new DefaultMutableTreeNode("<html><b>Requirements</b></html>");
		this.tree = new JTree(top);
		this.tree.setEditable(false);
		this.tree.setDragEnabled(true);
		this.tree.setDropMode(DropMode.ON);

		this.tree.setTransferHandler(new SubReqTreeTransferHandler(
				tabController));
		this.tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		treeView = new JScrollPane(tree);
		this.add(treeView, BorderLayout.CENTER);

		RequirementDatabase.getInstance().registerListener(this);

		requirements = RequirementDatabase.getInstance().getAll();
		updateTreeView();
		MouseListener ml = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				if (e.getButton() == MouseEvent.BUTTON1) {
					int selRow = tree.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = tree.getPathForLocation(e.getX(),
							e.getY());
					if (selRow != -1) {
						if (e.getClickCount() == 2) {
							onDoubleClick(selRow, selPath);
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// this was a right click

					int selRow = tree.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = tree.getPathForLocation(e.getX(),
							e.getY());
					onRightClick(e.getX(), e.getY(), selRow, selPath);
				}
			}
		};
		this.tree.addMouseListener(ml);
	}

	protected void onRightClick(int x, int y, int selRow, TreePath selPath) {
				if (!PermissionModel.getInstance().getUserPermissions().canEditRequirement())
					return;
				// add a menu offset
				x += 10;

				// we right clicked on something in particular
				JPopupMenu menuToShow;
				if (selRow != -1) {					
					int levelClickedOn = ((DefaultMutableTreeNode) selPath
							.getLastPathComponent()).getLevel();

					if (tree.getSelectionModel().getSelectionMode() == TreeSelectionModel.SINGLE_TREE_SELECTION) {
						// we are in single selection mode
						tree.setSelectionPath(selPath);

					} else {
						// multi selection mode
						tree.addSelectionPath(selPath);
					}
				if(levelClickedOn>0){
					List<Requirement> selectedRequirements = new ArrayList<Requirement>();
					TreePath path = tree.getPathForRow(selRow);
					DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path.getLastPathComponent();
					if(firstNode.getUserObject().equals("<html><b><i>Deleted</i></b></html>"))
						return;
					Requirement tempReq = (Requirement) firstNode.getUserObject();
					selectedRequirements.add(tempReq);
					menuToShow = new SubReqRequirementPopupMenu(tabController,selectedRequirements);
					menuToShow.show(this, x, y);
					}
				}
				else{
					menuToShow = new SubReqAnywherePopupMenu(tabController);
					menuToShow.show(this, x, y);
				}
		
	}

	protected void onDoubleClick(int selRow, TreePath selPath) {
		if (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getLevel() == 0) {
			return;
		}
		boolean requirementIsOpen = false;
		// TODO Auto-generated method stub
		Requirement requirement = RequirementDatabase.getInstance()
				.getRequirement(
						((DefaultMutableTreeNode) selPath
								.getLastPathComponent()).toString());

		// Check to make sure the requirement is not already being
		// displayed. This is assuming that the list view is displayed in
		// the left most tab, index 0
		for (int i = 0; i < this.tabController.getTabView().getTabCount(); i++) {
			if (this.tabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				if (((((DetailPanel) this.tabController.getTabView()
						.getComponentAt(i))).getModel().getrUID()) == (requirement
						.getrUID())) {
					this.tabController.switchToTab(i);
					requirementIsOpen = true;
				}
			}
		}
		if (!requirementIsOpen) {
			// create the controller for fetching the new requirement
			RequirementsController controller = new RequirementsController();
			RetrieveRequirementByIDRequestObserver observer = new RetrieveRequirementByIDRequestObserver(
					new OpenRequirementTabAction(tabController, requirement));

			// get the requirement from the server
			controller.get(requirement.getrUID(), observer);
		}
		
	}

	public void updateTreeView() {
		String eState = getExpansionState(this.tree, 0);
		DefaultMutableTreeNode requirementNode = null;
		DefaultMutableTreeNode subRequirementNode = null;
		Requirement tempReq = null;
		List<Requirement> topReqsWithChildren = new ArrayList<Requirement>();
		List<Requirement> topReqsWOChildren = new ArrayList<Requirement>();
		List<Requirement> deletedReqs = new ArrayList<Requirement>();
		this.top.removeAllChildren();

		if (requirements != null) {
			for (Requirement anReq : requirements) {
				if (anReq.getpUID().size() == 0){
					if(anReq.getSubRequirements().size()>0)
						topReqsWithChildren.add(anReq);
					else if(anReq.getStatus()!=Status.DELETED)
						topReqsWOChildren.add(anReq);
					else
						deletedReqs.add(anReq);
				}
			}
			topReqsWOChildren = Requirement.sortRequirements(topReqsWOChildren);
			topReqsWithChildren = Requirement.sortRequirements(topReqsWithChildren);
			deletedReqs = Requirement.sortRequirements(deletedReqs);
			
			for (Requirement anReq : topReqsWithChildren) {

					requirementNode = new DefaultMutableTreeNode(anReq);

					for (Integer aReq : anReq.getSubRequirements()) {
						try {
							tempReq = RequirementDatabase.getInstance().get(
									aReq);
							subRequirementNode = new DefaultMutableTreeNode(
									tempReq);
							requirementNode.add(subRequirementNode);
							updateTreeNodes(tempReq, subRequirementNode);
						} catch (RequirementNotFoundException e) {
							System.out
									.println("Requirement not found: SubRequirementTreeView:372");
						}
					}
					this.top.add(requirementNode);
			}
			
			for (Requirement anReq : topReqsWOChildren) {
					requirementNode = new DefaultMutableTreeNode(anReq);

					for (Integer aReq : anReq.getSubRequirements()) {
						try {
							tempReq = RequirementDatabase.getInstance().get(
									aReq);
							subRequirementNode = new DefaultMutableTreeNode(
									tempReq);
							requirementNode.add(subRequirementNode);
							updateTreeNodes(tempReq, subRequirementNode);
						} catch (RequirementNotFoundException e) {
							System.out
									.println("Requirement not found: SubRequirementTreeView:372");
						}
					}
					this.top.add(requirementNode);
			}
			
			DefaultMutableTreeNode deletedNode = new DefaultMutableTreeNode("<html><b><i>Deleted</i></b></html>");
			for (Requirement anReq : deletedReqs) {
				requirementNode = new DefaultMutableTreeNode(anReq);
				deletedNode.add(requirementNode);
				}
			this.top.add(deletedNode);
				
//			for (Requirement anReq : requirements) {
//
//				if (anReq.getpUID().size() == 0) {
//					requirementNode = new DefaultMutableTreeNode(anReq);
//
//					for (Integer aReq : anReq.getSubRequirements()) {
//						try {
//							tempReq = RequirementDatabase.getInstance().get(
//									aReq);
//							subRequirementNode = new DefaultMutableTreeNode(
//									tempReq);
//							requirementNode.add(subRequirementNode);
//							updateTreeNodes(tempReq, subRequirementNode);
//						} catch (RequirementNotFoundException e) {
//							System.out
//									.println("Requirement not found: SubRequirementTreeView:372");
//						}
//					}
//					this.top.add(requirementNode);
//				}
//			}

			((DefaultTreeModel) this.tree.getModel())
					.nodeStructureChanged(this.top);
			DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) this.tree
					.getCellRenderer();
			renderer.setLeafIcon(null);
			this.tree.setCellRenderer(renderer);
			restoreExpanstionState(this.tree, 0, eState);

		}
	}

	public static void restoreExpanstionState(JTree tree, int row,
			String expansionState) {
		StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	public static String getExpansionState(JTree tree, int row) {
		TreePath rowPath = tree.getPathForRow(row);
		StringBuffer buf = new StringBuffer();
		int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			TreePath path = tree.getPathForRow(i);
			if (i == row || isDescendant(path, rowPath)) {
				if (tree.isExpanded(path))
					buf.append("," + String.valueOf(i - row));
			} else
				break;
		}
		return buf.toString();
	}

	public static boolean isDescendant(TreePath path1, TreePath path2) {
		int count1 = path1.getPathCount();
		int count2 = path2.getPathCount();
		if (count1 <= count2)
			return false;
		while (count1 != count2) {
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
	}

	public void updateTreeNodes(Requirement anReq, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode subRequirementNode = null;
		Requirement tempReq = null;

		for (Integer aReq : anReq.getSubRequirements()) {
			try {
				tempReq = RequirementDatabase.getInstance().get(aReq);
				subRequirementNode = new DefaultMutableTreeNode(tempReq);
				node.add(subRequirementNode);
				updateTreeNodes(tempReq, subRequirementNode);
			} catch (RequirementNotFoundException e) {
				System.out
						.println("GRRR Requirement not found: SubRequirementTreeView:372");
			}
		}
	}

	@Override
	public void receivedData(Requirement[] requirements) {
		// this.requirements = Arrays.asList(requirements);
		refresh();
	}

	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {

	}

	@Override
	public void update() {
	}

	public void refresh() {
		this.requirements = RequirementDatabase.getInstance().getAll();
		updateTreeView();
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (firstPaint) {
			firstPaint = false;

			refresh();
			// getRequirementsFromServer();
		}
	}

}
