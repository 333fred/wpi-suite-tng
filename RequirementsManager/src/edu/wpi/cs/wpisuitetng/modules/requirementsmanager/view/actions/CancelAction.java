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
@SuppressWarnings ("serial")
public class CancelAction extends AbstractAction {
	
	private final DetailPanel parentView;
	
	/**
	 * Creates a new cancel action with the given parent DetailPanel
	 * 
	 * @param parentView
	 *            the detailpanel parent for this cancelaction
	 */
	public CancelAction(final DetailPanel parentView) {
		super("Cancel");
		this.parentView = parentView;
	}
	
	/**
	 * Method to cancel any edits made but not yet saved or unsaved requirement
	 * creation
	 * 
	 * @param e any ActionEvent object
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		parentView.getMainTabController().closeCurrentTab();
	}
	
}
