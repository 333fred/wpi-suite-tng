/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steve Kordell
 *    Mitchell Caisse
 *    Alex Gorowara
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveRequirementByIDRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu.AnywherePopupMenu;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu.BacklogPopupMenu;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu.DeletedPopupMenu;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu.IterationPopupMenu;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu.RequirementPopupMenu;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu.RootPopupMenu;

@SuppressWarnings("serial")
public class IterationTreeView extends JPanel implements IDatabaseListener,
		IReceivedAllRequirementNotifier, IRetreivedAllIterationsNotifier {

	protected static final int ROOT_LEVEL = 0;
	protected static final int ITERATION_LEVEL = 1;
	protected static final int REQUIREMENT_LEVEL = 2;

	private JTree tree;
	private DefaultMutableTreeNode top;
	private IterationController iterationsController;
	private RequirementsController requirementsController;

	private MainTabController tabController;

	/** List of all the iterations currently being displayed */
	List<Iteration> iterations;

	private boolean firstPaint;

	public IterationTreeView(MainTabController tabController) {
		super(new BorderLayout());
		this.tabController = tabController;
		iterations = new ArrayList<Iteration>();

		iterationsController = new IterationController();
		requirementsController = new RequirementsController();

		firstPaint = true;

		this.top = new DefaultMutableTreeNode("Iterations");
		this.tree = new JTree(top);
		this.tree.setEditable(false);
		this.tree.setDragEnabled(true);
		this.tree.setDropMode(DropMode.ON);
		// final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		this.tree.setTransferHandler(new TreeTransferHandler(tabController));
		this.tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		JScrollPane treeView = new JScrollPane(tree);

		add(treeView, BorderLayout.CENTER);

		// register ourselves as a listener
		IterationDatabase.getInstance().registerListener(this);
		RequirementDatabase.getInstance().registerListener(this);

		// fetch the requirements and iterations from the server
		getRequirementsFromServer();
		getIterationsFromServer();

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

	/**
	 * Called when the user right clicks, will determine where the user clicked
	 * on the tree, and open the correct menu
	 * 
	 * @param x
	 *            The x coordinate of where the user clicked
	 * @param y
	 *            The y coordinate of where the user clicked
	 * @param selRow
	 *            The selection row of where the user clicked
	 * @param selPath
	 *            The selection path of where the user clicked
	 */

	protected void onRightClick(int x, int y, int selRow, TreePath selPath) {
		// add a menu offset
		x += 10;

		// we right clicked on something in particular
		if (selRow != -1) {
			JPopupMenu menuToShow;
			int levelClickedOn = ((DefaultMutableTreeNode) selPath
					.getLastPathComponent()).getLevel();
			System.out.println("LevelClickedOn: " + levelClickedOn);

			if (tree.getSelectionModel().getSelectionMode() == TreeSelectionModel.SINGLE_TREE_SELECTION) {
				// we are in single selection mode
				tree.setSelectionPath(selPath);

			} else {
				// multi selection mode
				tree.addSelectionPath(selPath);
			}

			boolean backLogSingleSel = false;

			// check if the user has selected only the backlog
			if (tree.getSelectionPaths().length == 1
					&& levelClickedOn == ITERATION_LEVEL) {
				// one thing selected, check for backlog
				String iterationName = tree.getSelectionPaths()[0]
						.getLastPathComponent().toString();
				if (iterationName.equals("Backlog")) {
					backLogSingleSel = true;
					// user has selected backlog
					BacklogPopupMenu menu = new BacklogPopupMenu(tabController);
					menu.show(this, x, y);
				}
				if (iterationName.equals("Deleted")) {
					// set flag, perhaps rename later
					backLogSingleSel = true;
					// user has selected deleted
					DeletedPopupMenu delMenu = new DeletedPopupMenu(
							tabController);
					delMenu.show(this, x, y);
				}
			}

			// the backlog was not selected, or not only thing selected
			if (!backLogSingleSel) {
				switch (levelClickedOn) {

				case ROOT_LEVEL:
					System.out.println("Root Level");
					menuToShow = new RootPopupMenu(tabController);
					menuToShow.show(this, x, y);
					break;

				case ITERATION_LEVEL:
					System.out.println("Iteration Level");
					List<Iteration> selectedIterations = getSelectedIterations();
					if (selectedIterations.size() == 0) {
						// there were no selected iterations, WUT ARE WE DOIN
						// HERE
						break;
					}
					menuToShow = new IterationPopupMenu(tabController,
							selectedIterations);
					menuToShow.show(this, x, y);
					break;

				case REQUIREMENT_LEVEL:
					System.out.println("Requirement Level");
					List<Requirement> selectedRequirements = getSelectedRequirements();
					if (selectedRequirements.size() == 0) {
						// there were no selected requirements,
						break;
					}
					menuToShow = new RequirementPopupMenu(tabController,
							selectedRequirements);
					menuToShow.show(this, x, y);
					break;
				}
			}

		} else {
			// we right clicked in the tree.
			// TODO: We might want to check if multiple selected, and then open
			// according menu?

			// do this only if more than one thing was selected
			if (tree.getSelectionPaths() != null
					&& tree.getSelectionPaths().length > 1) {
				// something was selected, lets open its stuff
				// need to check if only one level is selected
				int curSelectionLevel = -1;
				boolean sameLevel = true;
				for (TreePath path : tree.getSelectionPaths()) {
					int pathLevel = ((DefaultMutableTreeNode) path
							.getLastPathComponent()).getLevel();
					if (curSelectionLevel == -1) {
						curSelectionLevel = pathLevel;
					} else if (curSelectionLevel != pathLevel) {
						sameLevel = false;
					}
				}

				if (sameLevel) {

					JPopupMenu menuToShow;

					switch (curSelectionLevel) {

					case ROOT_LEVEL:
						System.out.println("Root Level");
						menuToShow = new RootPopupMenu(tabController);
						menuToShow.show(this, x, y);
						break;

					case ITERATION_LEVEL:
						System.out.println("Iteration Level");
						List<Iteration> selectedIterations = getSelectedIterations();
						if (selectedIterations.size() == 0) {
							// there were no selected iterations, WUT ARE WE
							// DOIN HERE
							break;
						}
						menuToShow = new IterationPopupMenu(tabController,
								selectedIterations);
						menuToShow.show(this, x, y);
						break;

					case REQUIREMENT_LEVEL:
						System.out.println("Requirement Level");
						List<Requirement> selectedRequirements = getSelectedRequirements();
						if (selectedRequirements.size() == 0) {
							// there were no selected requirements,
							break;
						}
						menuToShow = new RequirementPopupMenu(tabController,
								selectedRequirements);
						menuToShow.show(this, x, y);
						break;
					}
				}

			} else {
				// if nothing selected, we create the anywhere menu, to create
				// req and iter.
				JPopupMenu menuToShow = new AnywherePopupMenu(tabController);
				menuToShow.show(this, x, y);
			}

		}

	}

	protected void onDoubleClick(int selRow, TreePath selPath) {
		if (((DefaultMutableTreeNode) selPath.getLastPathComponent())
				.getLevel() != 2) {
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

	public void refresh() {
		getIterationsFromServer();
	}

	public void updateTreeView() {
		System.out.println("[" + new Date().toString()
				+ "] We are refreshing the tree view");

		String eState = getExpansionState(this.tree, 0);
		DefaultMutableTreeNode iterationNode = null;
		DefaultMutableTreeNode requirementNode = null;
		Requirement requirement = null;
		this.top.removeAllChildren();
		// iterations = IterationDatabase.getInstance().getAllIterations();

		// sort the iterations
		iterations = Iteration.sortIterations(iterations);
		Date acurrentDate = new Date();

		for (Iteration anIteration : iterations) {
			// iterationNode = new
			// DefaultMutableTreeNode((acurrentDate.compareTo(anIteration.getEndDate())
			// > 0 && anIteration.getId() != -1) ? anIteration.getName()+
			// " (Closed)" : anIteration.getName());
			iterationNode = new DefaultMutableTreeNode(anIteration);
			// iterationNode.setUserObject(anIteration);
			for (Integer aReq : anIteration.getRequirements()) {
				try {
					requirement = RequirementDatabase.getInstance().get(aReq);
					requirementNode = new DefaultMutableTreeNode(requirement);
					// requirementNode.setUserObject(requirement);
					iterationNode.add(requirementNode);
				} catch (RequirementNotFoundException e) {
					System.out
							.println("Requirement Not Found: IterationTreeView:369");
				}
			}
			this.top.add(iterationNode);
		}

		((DefaultTreeModel) this.tree.getModel())
				.nodeStructureChanged(this.top);
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) this.tree
				.getCellRenderer();
		renderer.setLeafIcon(null);
		this.tree.setCellRenderer(renderer);
		restoreExpanstionState(this.tree, 0, eState);
	}

	// is path1 descendant of path2
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

	public static void restoreExpanstionState(JTree tree, int row,
			String expansionState) {
		StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	/**
	 * Retreives the iterations from the server
	 * 
	 */
	private void getRequirementsFromServer() {
		RetrieveAllRequirementsRequestObserver observer = new RetrieveAllRequirementsRequestObserver(
				this);
		requirementsController.getAll(observer);
	}

	private void getIterationsFromServer() {
		RetrieveAllIterationsRequestObserver observer = new RetrieveAllIterationsRequestObserver(
				this);
		iterationsController.getAll(observer);
	}

	/**
	 * Called when there was a change in iterations in the local database TODO:
	 * Unimplement this for now
	 */

	@Override
	public void update() {
		// refresh();
	}

	/**
	 * This listener should persist
	 * 
	 */

	@Override
	public boolean shouldRemove() {
		return false;
	}

	/**
	 * Overriding paint function to update this on first paint
	 * 
	 */

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (firstPaint) {
			firstPaint = false;
			getRequirementsFromServer();
			getIterationsFromServer();
		}
	}

	@Override
	public void receivedData(Requirement[] requirements) {
		refresh();
	}

	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		System.out
				.println("IterationTeamView: Error receiving requirements from server");

	}

	@Override
	public void receivedData(Iteration[] iterations) {
		this.iterations = Arrays.asList(iterations);
		updateTreeView();

	}

	/**
	 * Returns an array of the currently selected iterations
	 * 
	 * @return The currently selected iterations
	 */

	public List<Iteration> getSelectedIterations() {
		// TODO: Handle selecting closed iteration
		int[] selectedIndexes = tree.getSelectionRows();
		TreePath[] paths = tree.getSelectionPaths();

		if (selectedIndexes == null || paths == null) {
			return new ArrayList<Iteration>();
		}

		List<Iteration> selectedIterations = new ArrayList<Iteration>();

		for (TreePath path : paths) {
			if (((DefaultMutableTreeNode) path.getLastPathComponent())
					.getLevel() != ITERATION_LEVEL) {
				continue; // thing selected was not an iteration
			}
			String iterationName = path.getLastPathComponent().toString();
			iterationName = iterationName.replace(" (Closed)", "");
			System.out.println(iterationName);

			Iteration toAdd = getIterationFromName(iterationName);
			if (iterationName.equals("Backlog")
					|| iterationName.equals("Deleted") || toAdd == null) {
				continue; // either iteration was not found, or user tried to
							// open backlog
			}
			selectedIterations.add(toAdd);
		}
		return selectedIterations;

	}

	private Iteration getIterationFromName(String name) {
		for (Iteration i : iterations) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	public List<Requirement> getSelectedRequirements() {
		int[] selectedIndexes = tree.getSelectionRows();
		TreePath[] paths = tree.getSelectionPaths();

		if (selectedIndexes == null || paths == null) {
			return new ArrayList<Requirement>();
		}

		// Iteration[] selectedIterations = new
		// Iteration[selectedIndexes.length];
		List<Requirement> selectedRequirements = new ArrayList<Requirement>();

		for (TreePath path : paths) {
			if (((DefaultMutableTreeNode) path.getLastPathComponent())
					.getLevel() != REQUIREMENT_LEVEL) {
				continue; // thing selected was not an iteration
			}
			String requirementName = path.getLastPathComponent().toString();
			Requirement toAdd = getRequirementFromName(requirementName);
			if (toAdd == null) {
				continue; // requirement was not found whoops.
			}
			selectedRequirements.add(toAdd);
		}
		return selectedRequirements;
	}

	public Requirement getRequirementFromName(String name) {
		return RequirementDatabase.getInstance().getRequirement(name);
	}
}