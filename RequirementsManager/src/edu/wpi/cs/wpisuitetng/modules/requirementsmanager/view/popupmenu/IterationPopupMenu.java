/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Mitchell Caisse
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Class for creating a right click menu in IterationTreeView, when a user right
 * clicks an iteration node
 * 
 * @author Mitchell
 * 
 */

public class IterationPopupMenu extends JPopupMenu implements ActionListener {

	/** Menu options for the PopupMenu */
	// private JMenuItem menuCreateRequirement;
	private JMenuItem menuEditIteration;
	private JMenuItem menuFilterIteration;

	/** The tab controller used to create new tabs */
	private MainTabController tabController;

	/** The iterations that were selected when this was pressed */
	private List<Iteration> selectedIterations;

	/**
	 * Creates an IterationPopupMenu with the given tab controller, and selected
	 * iterations
	 * 
	 * @param tabController
	 *            The tab controller to open tabs in
	 * @param selectedIterations
	 *            The iterations that were selected when right click was pressed
	 */

	public IterationPopupMenu(MainTabController tabController,
			List<Iteration> selectedIterations) {
		this.tabController = tabController;
		this.selectedIterations = selectedIterations;

		// we only have on selected iteration
		if (selectedIterations.size() == 1) {
			if (PermissionModel.getInstance().getUserPermissions().canEditRequirement()) {
				menuEditIteration = new JMenuItem("Edit Iteration");
			} else {
				menuEditIteration = new JMenuItem("View Iteration");
			}
			menuFilterIteration = new JMenuItem("Filter By Iteration");
			// menuCreateRequirement = new JMenuItem("New Requirement");

			menuEditIteration.addActionListener(this);
			menuFilterIteration.addActionListener(this);
			// menuCreateRequirement.addActionListener(this);

			add(menuEditIteration);
			add(menuFilterIteration);
			// add(menuCreateRequirement);

		} else {

			if (PermissionModel.getInstance().getUserPermissions().canEditRequirement()) {
				menuEditIteration = new JMenuItem("Edit Iterations");
			} else {
				menuEditIteration = new JMenuItem("View Iterations");
			}
			menuFilterIteration = new JMenuItem("Filter By Iteration");
			menuEditIteration.addActionListener(this);
			menuFilterIteration.addActionListener(this);
			add(menuEditIteration);
			add(menuFilterIteration);
		}

	}

	/**
	 * The action listener that is called when the user selects a menu option
	 * 
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(menuEditIteration)) {
			// edit iteration was selected
			// TODO: Add check that the iteration tab isn't already opened			
			for (Iteration i : selectedIterations) {
				tabController.addIterationTab(i);
			}
			
		} else if (e.getSource().equals(menuFilterIteration)) {
			Iteration iter = selectedIterations.get(0);
			RequirementTableView tableView = RequirementTableView.getInstance();
			tableView.IterationFilter(iter.getName());
			tableView.displayFilterInformation("Filtering by " + iter.getName());
		} 
	}
}
