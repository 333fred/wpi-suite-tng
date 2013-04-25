/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Alex Gorowara
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Heavily adapted from CreateDefectAction in the DefectTracker module
 * 
 * @author Alex Gorowara
 * 
 *         Action that calls {@link MainTabController#addCreateRequirementTab()}
 *         , default mnemonic key is C.
 */
@SuppressWarnings ("serial")
public class RefreshAction extends AbstractAction {
	
	private final RequirementTableView requirementList;
	
	/**
	 * Create a RefreshAction object which operates on a specific
	 * RequirementListView
	 * 
	 * @param requirementList
	 *            the current view, which is to be refreshed
	 * 
	 */
	public RefreshAction(final RequirementTableView requirementList) {
		super("Refresh");
		this.requirementList = requirementList;
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F5);
	}
	
	/**
	 * Method to refresh the view upon receiving any ActionEvent
	 * 
	 * @e any ActionEvent
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		requirementList.refresh();
	}
	
}
