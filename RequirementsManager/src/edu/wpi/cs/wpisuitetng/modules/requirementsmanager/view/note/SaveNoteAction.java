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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls
 * {@link edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.RequirementsManager.notes.SaveNoteController#savenote()}
 */
@SuppressWarnings ("serial")
public class SaveNoteAction extends AbstractAction {
	
	private final SaveNoteController controller;
	
	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 */
	public SaveNoteAction(final SaveNoteController controller) {
		super("Save");
		this.controller = controller;
	}
	
	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		controller.savenote();
	}
	
}
