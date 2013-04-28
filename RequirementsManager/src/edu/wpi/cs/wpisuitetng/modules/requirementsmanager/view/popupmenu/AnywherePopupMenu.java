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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Popup menu when a user doesnt click on a list item, and only one lsit item is
 * selected
 * 
 * @author Mitchell
 * 
 */
@SuppressWarnings ("serial")
public class AnywherePopupMenu extends JPopupMenu implements ActionListener {
	
	/** The Tabcontroller used to open tabs */
	private final MainTabController tabController;
	
	private final JMenuItem itemCreateIteration;
	private final JMenuItem itemCreateRequirement;
	
	/**
	 * Creates a new instance of anywhere popup menu
	 * 
	 * @param tabController
	 *            tab controller ot open tabs in
	 */
	
	public AnywherePopupMenu(final MainTabController tabController) {
		this.tabController = tabController;
		
		itemCreateIteration = new JMenuItem("New Iteration");
		itemCreateRequirement = new JMenuItem("New Requirement");
		
		itemCreateIteration.addActionListener(this);
		itemCreateRequirement.addActionListener(this);
		
		if (PermissionModel.getInstance().getUserPermissions()
				.canCreateRequirement()) {
			add(itemCreateRequirement);
		}
		if (PermissionModel.getInstance().getUserPermissions()
				.canCreateIteration()) {
			add(itemCreateIteration);
		}
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource().equals(itemCreateIteration)) {
			tabController.addCreateIterationTab();
		} else {
			tabController.addCreateRequirementTab();
		}
		
	}
}
