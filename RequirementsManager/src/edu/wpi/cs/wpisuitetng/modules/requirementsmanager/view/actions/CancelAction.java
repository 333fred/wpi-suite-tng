/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Chris Keane
 ********************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * Action which concerns canceling the creation/editing of a requirement
 */
public class CancelAction extends AbstractAction {

	private DetailPanel parentView;

	public CancelAction(DetailPanel parentView) {
		super("Cancel");
		this.parentView = parentView;
	}

	/**
	 * Method to cancel any edits made but not yet saved or unsaved requirement
	 * creation
	 * 
	 * @e any ActionEvent object
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.parentView.getMainTabController().closeCurrentTab();
	}

}
