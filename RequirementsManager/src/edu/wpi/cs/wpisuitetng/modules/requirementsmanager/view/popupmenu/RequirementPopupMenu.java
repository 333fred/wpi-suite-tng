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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Class for creating a right click menu in IterationTreeView, when a user right
 * clicks on a requirement node
 * 
 * @author Mitchell
 * 
 */

public class RequirementPopupMenu extends JPopupMenu implements ActionListener {

	/** Menu options for the PopupMenu */
	private JMenuItem menuViewRequirement;

	/** The tab controller to open tabs in */
	private MainTabController tabController;

	/** List of the selected requirements to possibly open */
	private List<Requirement> selectedRequirements;

	public RequirementPopupMenu(MainTabController tabController,
			List<Requirement> selectedRequirements) {
		this.tabController = tabController;
		this.selectedRequirements = selectedRequirements;

		if (selectedRequirements.size() == 1) {
			menuViewRequirement = new JMenuItem("Edit Requirement");
		} else {
			menuViewRequirement = new JMenuItem("Edit Requirements");
		}

		menuViewRequirement.addActionListener(this);

		add(menuViewRequirement);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (Requirement r : selectedRequirements) {
			tabController.addViewRequirementTab(r);

		}
	}

}
