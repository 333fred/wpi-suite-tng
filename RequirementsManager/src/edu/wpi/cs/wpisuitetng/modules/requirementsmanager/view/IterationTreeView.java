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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EditIterationAction;

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
	
	@SuppressWarnings("serial")
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
		final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		
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
	}

	public void refresh() {				
		DefaultMutableTreeNode iterationNode = null;
		this.top.removeAllChildren();

		iterations = IterationDatabase.getInstance().getAllIterations();

		for (Iteration anIteration : iterations) {
			
			//DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
			//renderer.setLeafIcon(null);
			//this.tree.setCellRenderer(renderer);
			
			iterationNode = new DefaultMutableTreeNode(anIteration.getName());

			for (Integer aReq : anIteration.getRequirements()) {
				try {
					iterationNode.add(new DefaultMutableTreeNode(RequirementDatabase.getInstance().getRequirement(aReq).getName()));
				} catch (RequirementNotFoundException e) {
					System.out.print("Requirement Not Found");
					e.printStackTrace();
				}
			}

			this.top.add(iterationNode);
		}
		this.tree.expandRow(0);
		this.tree.updateUI();
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
		refresh();
		
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
		System.out.println("Wat?" + selectedIterations.length);
		return selectedIterations;
		
	}
}