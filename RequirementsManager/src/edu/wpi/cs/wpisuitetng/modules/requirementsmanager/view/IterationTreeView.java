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
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

@SuppressWarnings("serial")
public class IterationTreeView extends JPanel {

	private JTree tree;
	private DefaultMutableTreeNode top;
	private RetrieveAllIterationsController retrieveAllIterationsController;

	@SuppressWarnings("serial")
	public IterationTreeView() {
		super(new BorderLayout());

		retrieveAllIterationsController = new RetrieveAllIterationsController(this);
		
		this.top = new DefaultMutableTreeNode("Iterations");
		this.tree = new JTree(top);
		this.tree.setEditable(false);
		this.tree.setDragEnabled(true);
		this.tree.setDropMode(DropMode.ON);
		final DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		
	     this.tree.setTransferHandler(new TreeTransferHandler());
	     this.tree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
	   
		
		JScrollPane treeView = new JScrollPane(tree);
		this.add(treeView, BorderLayout.CENTER);
	}

	public void refresh() {
		System.out.print("Refreshing tree\n");
		DefaultMutableTreeNode iterationNode = null;
		this.top.removeAllChildren();

		List<Iteration> iterations = IterationDatabase.getInstance().getAllIterations();

		for (Iteration anIteration : iterations) {
			
			//DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)this.tree.getCellRenderer();
			//renderer.setLeafIcon(null);
			//this.tree.setCellRenderer(renderer);
			
			iterationNode = new DefaultMutableTreeNode(anIteration.getName());
			System.out.println(anIteration.getName());

			for (Integer aReq : anIteration.getRequirements()) {
				try {
					iterationNode.add(new DefaultMutableTreeNode(
							RequirementDatabase.getInstance().getRequirement(
									aReq).getName()));
				} catch (RequirementNotFoundException e) {
					System.out.print("Requirement Not Found");
				}
			}

			this.top.add(iterationNode);
		}
		this.tree.expandRow(0);
		this.tree.updateUI();
	}

	public void getIterationsFromServer() {
		retrieveAllIterationsController.getAll();
	}
}