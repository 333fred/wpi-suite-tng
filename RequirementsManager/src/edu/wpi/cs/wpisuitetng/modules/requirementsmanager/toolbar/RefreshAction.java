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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Action invoked upon use of the Refresh key Heavily adapted from
 * CreateDefectAction in the DefectTracker module
 * 
 * Action that calls {@link MainTabController},
 * default mnemonic key is C.
 */
@SuppressWarnings ("serial")
public class RefreshAction extends AbstractAction {
	
	private final RequirementTableView requirementList;
	
	/**
	 * Create a CreateDefectAction
	 * 
	 * @param requirementList
	 *            When the action is performed, controller.addCreateDefectTab()
	 *            is called
	 */
	public RefreshAction(final RequirementTableView requirementList) {
		super("Refresh");
		this.requirementList = requirementList;
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F5);
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		requirementList.refresh();
	}
	
}
