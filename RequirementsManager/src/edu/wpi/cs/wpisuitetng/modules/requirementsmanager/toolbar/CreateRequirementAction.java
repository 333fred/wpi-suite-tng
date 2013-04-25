/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Alex Gorowara
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Action invoked upon use of the Create Requirement key Heavily adapted from
 * CreateDefectAction in the DefectTracker module
 * Action that calls {@link MainTabController#addCreateDefectTab()},
 * default mnemonic key is C.
 */
@SuppressWarnings ("serial")
public class CreateRequirementAction extends AbstractAction {
	
	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * 
	 * @param controller
	 *            When the action is performed, controller.addCreateDefectTab()
	 *            is called
	 */
	public CreateRequirementAction(final MainTabController controller) {
		super("Create Requirement");
		this.controller = controller;
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		if (PermissionModel.getInstance().getUserPermissions()
				.canCreateRequirement()) {
			controller.addCreateRequirementTab();
		} else {
			JOptionPane.showMessageDialog(controller.getTabView(),
					"You do not have permission to create a requirement");
		}
	}
	
}
