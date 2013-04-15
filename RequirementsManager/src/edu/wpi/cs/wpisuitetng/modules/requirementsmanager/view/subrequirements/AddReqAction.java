/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls
 * {@link edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.RequirementsManager.subrequirements.AssignParent()}
 */
@SuppressWarnings("serial")
public class AddReqAction extends AbstractAction {
	public final AddReqController controller;

	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 */
	public AddReqAction(AddReqController controller) {
		super("Add");
		this.controller = controller;
	}

	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.saveParent();
	}

}
