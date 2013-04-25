/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Opens panel to edit permissions
 */
public class CreatePermissionPanelAction extends AbstractAction {

	private final MainTabController controller;

	/**
	 * Constructor that creates a new permission panel
	 * 
	 * @param controller The main tab controller
	 */
	public CreatePermissionPanelAction(MainTabController controller) {
		super("Edit Permissions");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
	}

	@Override
	/**
	 * Method to check if a permissions tab is already open, or create a new tab if there is no tab already open.
	 */
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < controller.getNumberOfOpenTabs(); i++) {
			if (controller.getTabView().getTitleAt(i).equals("Permissions")) {
				controller.switchToTab(i);
				return;
			}
		}
		controller.addPermissionTab();
	}
}
