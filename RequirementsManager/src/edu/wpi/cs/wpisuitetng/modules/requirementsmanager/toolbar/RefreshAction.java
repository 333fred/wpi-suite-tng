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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Action invoked upon use of the Refresh key Heavily adapted from
 * CreateDefectAction in the DefectTracker module
 * 
 * Action that calls {@link MainTabController#addCreateDefectTab()},
 * default mnemonic key is C.
 */
@SuppressWarnings("serial")
public class RefreshAction extends AbstractAction {

	private final RequirementTableView requirementList;

	/**
	 * Create a CreateDefectAction
	 * 
	 * @param controller
	 *            When the action is performed, controller.addCreateDefectTab()
	 *            is called
	 */
	public RefreshAction(RequirementTableView requirementList) {
		super("Refresh");
		this.requirementList = requirementList;
		putValue(MNEMONIC_KEY, KeyEvent.VK_F5);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		requirementList.refresh();
	}

}
