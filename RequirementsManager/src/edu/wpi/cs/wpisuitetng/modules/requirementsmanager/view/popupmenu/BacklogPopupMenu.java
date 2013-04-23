/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

public class BacklogPopupMenu extends JPopupMenu implements ActionListener {

	/** Menu options for the PopupMenu */
	private JMenuItem menuCreateRequirement;
	private JMenuItem menuFilterBacklog;

	/** The tab controller used to create new tabs */
	private MainTabController tabController;
	

	/**
	 * Creates an BacklogPopupMenu with the given tab controller
	 * 
	 * @param tabController
	 *            The tab controller to open tabs in
	 */

	public BacklogPopupMenu(MainTabController tabController) {
		this.tabController = tabController;

		menuCreateRequirement = new JMenuItem("New Requirement");
		menuFilterBacklog = new JMenuItem("Filter By Backlog");
		
		menuCreateRequirement.addActionListener(this);
		menuFilterBacklog.addActionListener(this);
	
		if(PermissionModel.getInstance().getUserPermissions().canCreateRequirement()) {
			add(menuCreateRequirement);
		}
		add(menuFilterBacklog);

	}

	/**
	 * The action listener that is called when the user selects a menu option
	 * 
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(menuFilterBacklog)) {
			Iteration iter;
			try {
				// backlog has ID -1
				iter = IterationDatabase.getInstance().get(-1);
				RequirementTableView tableView = RequirementTableView.getInstance();
				tableView.IterationFilter(iter.getName());
				tableView.displayFilterInformation("Filtering by " + iter.getName());
			} catch (IterationNotFoundException e1) {
				e1.printStackTrace();
			} 
		} else {
			tabController.addCreateRequirementTab();
		}
	}

}
