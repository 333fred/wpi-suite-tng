/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steve Kordell
 *    Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveRequirementByIDController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EditIterationAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;

@SuppressWarnings("serial")
public class IterationTreeView extends JPanel implements IDatabaseListener, IReceivedAllRequirementNotifier, IRetreivedAllIterationsNotifier {
	
	private JTree tree;
	private DefaultMutableTreeNode top;
	private RetrieveAllIterationsController retrieveAllIterationsController;
	private RetrieveAllRequirementsController retrieveAllRequirementsController;
	
	/** Temporary button used to view/edit an iteration */
	private JButton editIterationButton;
	
	private MainTabController tabController;
	
	/** List of all the iterations currently being displayed */
	List<Iteration> iterations;
	
	private boolean firstPaint;

	public IterationTreeView(MainTabController tabController) {
		//super(new BorderLayout());
		SpringLayout layout = new SpringLayout();
		this.tabController = tabController;
		iterations = new ArrayList<Iteration>();

		retrieveAllIterationsController = new RetrieveAllIterationsController(this);
		retrieveAllRequirementsController = new RetrieveAllRequirementsController(this);
		
		firstPaint = true;
		
		this.top = new DefaultMutableTreeNode("Iterations");
		this.tree = new JTree(top);
		this.tree.setEditable(false);
		this.tree.setDragEnabled(true);
		this.tree.setDropMode(DropMode.ON);
		//final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		
	    this.tree.setTransferHandler(new TreeTransferHandler());
	    this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	   
	    editIterationButton = new JButton("Edit Iteration");
	    editIterationButton.setAction(new EditIterationAction(this,tabController));
	    editIterationButton.setText("Edit Iteration");
		
		JScrollPane treeView = new JScrollPane(tree);
		
		
		layout.putConstraint(SpringLayout.WEST, editIterationButton, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, editIterationButton, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, editIterationButton, 0, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.NORTH, treeView, 0, SpringLayout.SOUTH, editIterationButton);
		layout.putConstraint(SpringLayout.WEST, treeView, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, treeView, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, treeView, 0, SpringLayout.SOUTH, this);
		
		setLayout(layout);
		
		add(editIterationButton);
		add(treeView);
		
		//register ourselves as a listener
		IterationDatabase.getInstance().registerListener(this);
		RequirementDatabase.getInstance().registerListener(this);
	
		//fetch the requirements and iterations from the server
		getRequirementsFromServer();
		getIterationsFromServer();
		
		MouseListener ml = new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		        int selRow = tree.getRowForLocation(e.getX(), e.getY());
		        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		        if(selRow != -1) {
		            if(e.getClickCount() == 2) {
		                onDoubleClick(selRow, selPath);
		            }
		        }
		    }
		};
		this.tree.addMouseListener(ml);
	}

	protected void onDoubleClick(int selRow, TreePath selPath) {	
		if (((DefaultMutableTreeNode)selPath.getLastPathComponent()).getLevel() != 2) {
			return;
		}
		boolean requirementIsOpen = false;
		// TODO Auto-generated method stub
		Requirement requirement = RequirementDatabase.getInstance().getRequirement(((DefaultMutableTreeNode)selPath.getLastPathComponent()).toString());
		
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
			RetrieveRequirementByIDController retreiveRequirementController = new RetrieveRequirementByIDController(
					new OpenRequirementTabAction(tabController,
							requirement));

			// get the requirement from the server
			retreiveRequirementController.get(requirement.getrUID());
		}
	}
	
	public void refresh() {
		getIterationsFromServer();
	}
	
	public void updateTreeView() {
		System.out.println("[" + new Date().toString() + "] We are refreshing the tree view");
		
		String eState = getExpansionState(this.tree, 0);
		DefaultMutableTreeNode iterationNode = null;
		this.top.removeAllChildren();
		//iterations = IterationDatabase.getInstance().getAllIterations();
		
		//sort the iterations
		iterations = Iteration.sortIterations(iterations);
		
		for (Iteration anIteration : iterations) {
			iterationNode = new DefaultMutableTreeNode(anIteration.getName());

			for (Integer aReq : anIteration.getRequirements()) {
				try {
					iterationNode.add(new DefaultMutableTreeNode(RequirementDatabase.getInstance().getRequirement(aReq).getName()));
				} catch (RequirementNotFoundException e) {
					//System.out.println("Requirement Not Found");
					//Unnecessary to do anything here...I think
				}
			}
			this.top.add(iterationNode);
		}
		((DefaultTreeModel)this.tree.getModel()).nodeStructureChanged(this.top);
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
		renderer.setLeafIcon(null);
		this.tree.setCellRenderer(renderer);
		restoreExpanstionState(this.tree,0,eState);
	}
	
    // is path1 descendant of path2
    public static boolean isDescendant(TreePath path1, TreePath path2){
        int count1 = path1.getPathCount();
        int count2 = path2.getPathCount();
        if(count1<=count2)
            return false;
        while(count1!=count2){
            path1 = path1.getParentPath();
            count1--;
        }
        return path1.equals(path2);
    }
    public static String getExpansionState(JTree tree, int row){
        TreePath rowPath = tree.getPathForRow(row);
        StringBuffer buf = new StringBuffer();
        int rowCount = tree.getRowCount();
        for(int i=row; i<rowCount; i++){
            TreePath path = tree.getPathForRow(i);
            if(i==row || isDescendant(path, rowPath)){
                if(tree.isExpanded(path))
                    buf.append(","+String.valueOf(i-row));
            }else
                break;
        }
        return buf.toString();
    }
    public static void restoreExpanstionState(JTree tree, int row, String expansionState){
        StringTokenizer stok = new StringTokenizer(expansionState, ",");
        while(stok.hasMoreTokens()){
            int token = row + Integer.parseInt(stok.nextToken());
            tree.expandRow(token);
        }
    }
	
		
	/** Retreives the iterations from the server
	 * 
	 */
	private void getRequirementsFromServer() {
		retrieveAllRequirementsController.getAll();
	}
	
	private void getIterationsFromServer() {
		retrieveAllIterationsController.getAll();;
	}

	/** Called when there was a change in iterations in the local database 
	 * TODO: Unimplement this for now
	 */
	
	public void update() {
		//refresh();		
	}
	

	/** This listener should persist
	 * 
	 */
	
	public boolean shouldRemove() {
		return false;
	}
	
	/** Overriding paint function to update this on first paint
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
		System.out.println("IterationTeamView: Error receiving requirements from server");
		
	}

	@Override
	public void receivedData(Iteration[] iterations) {
		this.iterations = Arrays.asList(iterations);
		updateTreeView();
		
	}
	
	/** Returns an array of the currently selected iterations
	 * 
	 * @return The currently selected iterations
	 */
	
	public Iteration[] getSelectedIterations() {
		int[] selectedIndexes = tree.getSelectionRows();
		
		if (selectedIndexes == null) {
			return new Iteration[0];
		}
		
		Iteration[] selectedIterations = new  Iteration[selectedIndexes.length];
		
		for (int i=0;i<selectedIterations.length;i++) {
			if (selectedIndexes[i] == 0) {
				selectedIterations[i] = null;
				continue;
			}
			selectedIterations[i] = iterations.get(selectedIndexes[i] - 1);
		}
		return selectedIterations;
		
	}
}