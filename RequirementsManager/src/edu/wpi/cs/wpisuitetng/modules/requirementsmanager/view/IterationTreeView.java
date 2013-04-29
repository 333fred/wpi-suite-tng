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

/**
 * Creates the view with iterations holding all requirements assigned to them
 */

@SuppressWarnings ("serial")
public class IterationTreeView extends JPanel implements IDatabaseListener,
		IReceivedAllRequirementNotifier, IRetreivedAllIterationsNotifier {
	
	protected static final int ROOT_LEVEL = 0;
	protected static final int ITERATION_LEVEL = 1;
	protected static final int REQUIREMENT_LEVEL = 2;
	
	private TreePath lastSelPath;
	
	/**
	 * Gets how expanded a given row in a given tree is
	 * 
	 * @param tree
	 *            the tree to look in
	 * @param row
	 *            the row to look at
	 * @return the expansion state
	 */
	public static String getExpansionState(final JTree tree, final int row) {
		final TreePath rowPath = tree.getPathForRow(row);
		final StringBuffer buf = new StringBuffer();
		final int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			final TreePath path = tree.getPathForRow(i);
			if ((i == row) || IterationTreeView.isDescendant(path, rowPath)) {
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
	 * Determines if path 1 is a descendent of path 2
	 * 
	 * @param path1
	 *            the possible child
	 * @param path2
	 *            the possible parent
	 * @return true if path 1 is a descendent
	 */
	// is path1 descendant of path2
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
	 * Restores expansion state for a given tree and row to a given state
	 * 
	 * @param tree
	 *            the tree to restore
	 * @param row
	 *            the row to restor
	 * @param expansionState
	 *            the expansion state to restore
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
	
	private final DefaultMutableTreeNode top;
	
	private final IterationController iterationsController;
	
	private final RequirementsController requirementsController;
	
	private final MainTabController tabController;
	
	/** List of all the iterations currently being displayed */
	List<Iteration> iterations;
	
	private boolean firstPaint;
	
	private MouseListener ml;
	
	/**
	 * Creates a new tree view in the given controller
	 * 
	 * @param tabController
	 *            the controller to create a view for
	 */
	public IterationTreeView(final MainTabController tabController) {
		super(new BorderLayout());
		this.tabController = tabController;
		iterations = new ArrayList<Iteration>();
		
		iterationsController = new IterationController();
		requirementsController = new RequirementsController();
		
		firstPaint = true;
		
		top = new DefaultMutableTreeNode("<HTML><B>Iterations</B></HTML>");
		tree = new JTree(top);
		tree.setEditable(false);
		tree.setDragEnabled(true);
		tree.setDropMode(DropMode.ON);
		
		tree.setTransferHandler(new TreeTransferHandler(tabController));
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		final JScrollPane treeView = new JScrollPane(tree);
		
		add(treeView, BorderLayout.CENTER);
		
		// register ourselves as a listener
		IterationDatabase.getInstance().registerListener(this);
		RequirementDatabase.getInstance().registerListener(this);
		
		// fetch the requirements and iterations from the server
		getRequirementsFromServer();
		getIterationsFromServer();
		
		this.ml = new MouseAdapter() {
			
			@Override
			public void mousePressed(final MouseEvent e) {
				final int selRow = tree.getRowForLocation(e.getX(), e.getY());
				final TreePath selPath = tree.getPathForLocation(e.getX(),
						e.getY());
				
				if (e.getButton() == MouseEvent.BUTTON1) {
					// this was a left click
					if (selRow != -1) {
						if (e.getClickCount() == 2) {
							// it was a double click
							onDoubleClick(selRow, selPath);
						}
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// this was a right click
					onRightClick(e.getX(), e.getY(), selRow, selPath);
				}
				tree.setSelectionPath(selPath); // Prevent null pointers on
												// Mouse Release when focus
												// changes
				lastSelPath = selPath;
			}
			
			@Override
			public void mouseReleased(final MouseEvent e) {
				tree.setSelectionPath(lastSelPath);
			}
			
		};
		tree.addMouseListener(ml);
	}
	
	@Override
	public void errorReceivingData(
			final String RetrieveAllRequirementsRequestObserver) {
		System.out
				.println("IterationTeamView: Error receiving requirements from server");
		
	}
	
	private void getIterationsFromServer() {
		final RetrieveAllIterationsRequestObserver observer = new RetrieveAllIterationsRequestObserver(
				this);
		iterationsController.getAll(observer);
	}
	
	/**
	 * Gets the requirement from a name
	 * 
	 * @param name
	 *            the name to look for
	 * @return the requirement, or null if there is nones
	 */
	public Requirement getRequirementFromName(final String name) {
		return RequirementDatabase.getInstance().getRequirement(name);
	}
	
	/**
	 * Retreives the iterations from the server
	 * 
	 */
	private void getRequirementsFromServer() {
		final RetrieveAllRequirementsRequestObserver observer = new RetrieveAllRequirementsRequestObserver(
				this);
		requirementsController.getAll(observer);
	}
	
	/**
	 * Returns an array of the currently selected iterations
	 * 
	 * @return The currently selected iterations
	 */
	
	public List<Iteration> getSelectedIterations() {
		// TODO: Handle selecting closed iteration
		final int[] selectedIndexes = tree.getSelectionRows();
		final TreePath[] paths = tree.getSelectionPaths();
		
		if ((selectedIndexes == null) || (paths == null)) {
			return new ArrayList<Iteration>();
		}
		
		final List<Iteration> selectedIterations = new ArrayList<Iteration>();
		
		for (final TreePath path : paths) {
			if (((DefaultMutableTreeNode) path.getLastPathComponent())
					.getLevel() != IterationTreeView.ITERATION_LEVEL) {
				continue; // thing selected was not an iteration
			}
			
			final Iteration toAdd = (Iteration) ((DefaultMutableTreeNode) tree
					.getSelectionPaths()[0].getLastPathComponent())
					.getUserObject();
			final String iterationName = toAdd.getName();
			if (iterationName.equals("Backlog")
					|| iterationName.equals("Deleted") || (toAdd == null)) {
				continue; // either iteration was not found, or user tried to
				// open backlog
			}
			selectedIterations.add(toAdd);
		}
		return selectedIterations;
		
	}
	
	/**
	 * Gets all selected requirements from the tree
	 * 
	 * @return all selected requirements
	 */
	public List<Requirement> getSelectedRequirements() {
		final int[] selectedIndexes = tree.getSelectionRows();
		final TreePath[] paths = tree.getSelectionPaths();
		
		if ((selectedIndexes == null) || (paths == null)) {
			return new ArrayList<Requirement>();
		}
		
		final List<Requirement> selectedRequirements = new ArrayList<Requirement>();
		
		for (final TreePath path : paths) {
			if (((DefaultMutableTreeNode) path.getLastPathComponent())
					.getLevel() != IterationTreeView.REQUIREMENT_LEVEL) {
				continue; // thing selected was not an iteration
			}
			final Requirement toAdd = (Requirement) ((DefaultMutableTreeNode) (tree
					.getSelectionPaths()[0].getLastPathComponent()))
					.getUserObject();
			
			selectedRequirements.add(toAdd);
		}
		return selectedRequirements;
	}
	
	protected void onDoubleClick(final int selRow, final TreePath selPath) {
		if (((DefaultMutableTreeNode) selPath.getLastPathComponent())
				.getLevel() != 2) {
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
					this.tree.setSelectionPath(selPath); // Prevent null
															// pointers on Mouse
															// Release when
															// focus changes
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
		
		/*
		 * // create the controller for fetching the new requirement
		 * final RequirementsController controller = new
		 * RequirementsController();
		 * final RetrieveRequirementByIDRequestObserver observer = new
		 * RetrieveRequirementByIDRequestObserver(
		 * new OpenRequirementTabAction(tabController, requirement));
		 * // get the requirement from the server
		 * controller.get(requirement.getrUID(), observer);
		 * //}
		 */
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
	
	protected void onRightClick(int x, final int y, final int selRow,
			final TreePath selPath) {
		if (!isEnabled()) {
			return;
		}
		System.out.println("Right click iteration tree view");
		
		// add a menu offset
		x += 10;
		
		// we right clicked on something in particular
		if (selRow != -1) {
			System.out.println("We right clicked on something");
			JPopupMenu menuToShow;
			final int levelClickedOn = ((DefaultMutableTreeNode) selPath
					.getLastPathComponent()).getLevel();
			
			if (tree.getSelectionModel().getSelectionMode() == TreeSelectionModel.SINGLE_TREE_SELECTION) {
				// we are in single selection mode
				tree.setSelectionPath(selPath);
				
			} else {
				// multi selection mode
				tree.addSelectionPath(selPath);
			}
			
			boolean backLogSingleSel = false;
			// Does the current user have admin permissions?
			
			// check if the user has selected only the backlog
			if ((tree.getSelectionPaths().length == 1)
					&& (levelClickedOn == IterationTreeView.ITERATION_LEVEL)) {
				
				System.out.println("We right clicked on one thing");
				
				// one thing selected, check for backlog
				// final Iteration selectedIteration = (Iteration)
				// tree.getSelectionPaths()[0].getLastPathComponent();
				final Iteration selectedIteration = (Iteration) ((DefaultMutableTreeNode) tree
						.getSelectionPaths()[0].getLastPathComponent())
						.getUserObject();
				final String iterationName = selectedIteration.getName();
				
				System.out.println("IterationName: " + iterationName);
				
				if (iterationName.equals("Backlog")) {
					backLogSingleSel = true;
					// user has selected backlog
					final BacklogPopupMenu menu = new BacklogPopupMenu(
							tabController);
					menu.show(this, x, y);
				} else if (iterationName.equals("Deleted")) {
					// set flag, perhaps rename later
					backLogSingleSel = true;
					// user has selected deleted
					final DeletedPopupMenu delMenu = new DeletedPopupMenu(
							tabController);
					delMenu.show(this, x, y);
				}
			}
			
			// the backlog was not selected, or not only thing selected
			if (!backLogSingleSel) {
				switch (levelClickedOn) {
				
					case ROOT_LEVEL:
						menuToShow = new RootPopupMenu(tabController);
						menuToShow.show(this, x, y);
						break;
					
					case ITERATION_LEVEL:
						System.out.println("IteraitonLevel");
						final List<Iteration> selectedIterations = getSelectedIterations();
						if (selectedIterations.size() == 0) {
							System.out.println("Selected Iterations size is 0");
							// there were no selected iterations, WUT ARE WE
							// DOIN
							// HERE
							break;
						}
						menuToShow = new IterationPopupMenu(tabController,
								selectedIterations);
						menuToShow.show(this, x, y);
						break;
					
					case REQUIREMENT_LEVEL:
						final List<Requirement> selectedRequirements = getSelectedRequirements();
						if (selectedRequirements.size() == 0) {
							// there were no selected requirements,
							break;
						}
						menuToShow = new RequirementPopupMenu(tabController,
								selectedRequirements);
						menuToShow.show(this, x, y);
						break;
					default:
						break;
				}
			}
			
		} else {
			// we right clicked in the tree.
			// TODO: We might want to check if multiple selected, and then open
			// according menu?
			
			// do this only if more than one thing was selected
			if ((tree.getSelectionPaths() != null)
					&& (tree.getSelectionPaths().length > 1)) {
				// something was selected, lets open its stuff
				// need to check if only one level is selected
				int curSelectionLevel = -1;
				boolean sameLevel = true;
				for (final TreePath path : tree.getSelectionPaths()) {
					final int pathLevel = ((DefaultMutableTreeNode) path
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
							menuToShow = new RootPopupMenu(tabController);
							menuToShow.show(this, x, y);
							break;
						
						case ITERATION_LEVEL:
							final List<Iteration> selectedIterations = getSelectedIterations();
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
							final List<Requirement> selectedRequirements = getSelectedRequirements();
							if (selectedRequirements.size() == 0) {
								// there were no selected requirements,
								break;
							}
							menuToShow = new RequirementPopupMenu(
									tabController, selectedRequirements);
							menuToShow.show(this, x, y);
							break;
						default:
							break;
					}
				}
				
			} else {
				// if nothing selected, we create the anywhere menu, to create
				// req and iter.
				final JPopupMenu menuToShow = new AnywherePopupMenu(
						tabController);
				menuToShow.show(this, x, y);
			}
			
		}
		
	}
	
	/**
	 * Overriding paint function to update this on first paint
	 * 
	 */
	
	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		if (firstPaint) {
			firstPaint = false;
			getRequirementsFromServer();
			getIterationsFromServer();
		}
	}
	
	@Override
	public void receivedData(final Iteration[] newIterations) {
		this.iterations = Arrays.asList(newIterations);
		updateTreeView();
		
	}
	
	@Override
	public void receivedData(final Requirement[] requirements) {
		refresh();
	}
	
	/**
	 * Refreshes the iteration views
	 */
	public void refresh() {
		getIterationsFromServer();
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
	 * Called when there was a change in iterations in the local database TODO:
	 * Unimplement this for now
	 */
	
	@Override
	public void update() {
	}
	
	/**
	 * Updates the view with all iterations and requirements from the server
	 */
	public void updateTreeView() {
		
		final String eState = IterationTreeView.getExpansionState(tree, 0);
		DefaultMutableTreeNode iterationNode = null;
		DefaultMutableTreeNode requirementNode = null;
		Requirement requirement = null;
		top.removeAllChildren();
		
		// sort the iterations
		iterations = Iteration.sortIterations(iterations);
		
		for (final Iteration anIteration : iterations) {
			iterationNode = new DefaultMutableTreeNode(anIteration);
			for (final Integer aReq : anIteration.getRequirements()) {
				try {
					requirement = RequirementDatabase.getInstance().get(aReq);
					requirementNode = new DefaultMutableTreeNode(requirement);
					iterationNode.add(requirementNode);
				} catch (final RequirementNotFoundException e) {
					System.out
							.println("Requirement Not Found: IterationTreeView:369");
				}
			}
			top.add(iterationNode);
		}
		
		((DefaultTreeModel) tree.getModel()).nodeStructureChanged(top);
		final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree
				.getCellRenderer();
		renderer.setLeafIcon(null);
		tree.setCellRenderer(renderer);
		IterationTreeView.restoreExpanstionState(tree, 0, eState);
	}
	
	/**
	 * Will disable all of the fields in this View
	 * 
	 */
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tree.setEnabled(enabled);
	}
}