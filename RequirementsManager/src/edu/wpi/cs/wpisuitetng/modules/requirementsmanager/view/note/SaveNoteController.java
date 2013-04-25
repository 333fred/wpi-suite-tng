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

import java.util.List;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * This controller handles saving requirement notes to the server
 */
public class SaveNoteController {
	
	private final MakeNotePanel view;
	private final Requirement model;
	private final DetailPanel parentView;
	
	/**
	 * Construct the controller
	 * 
	 * @param view
	 *            the MakeNotePanel containing the note field
	 * @param model
	 *            the requirement to which notes are being added
	 * @param parentView
	 *            the DetailPanel displaying the current requirement
	 */
	public SaveNoteController(final MakeNotePanel view,
			final Requirement model, final DetailPanel parentView) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
	}
	
	/**
	 * Save a note to the server
	 */
	public void savenote() {
		final String noteText = view.getnoteField().getText();
		if (noteText.length() > 0) {
			model.addNote(new Note(noteText, ConfigManager.getConfig()
					.getUserName()));
			final List<Note> notes = parentView.getNoteList();
			notes.add(model.getNotes().get(model.getNotes().size() - 1));
			parentView.setNoteList(notes);
			view.getnoteField().setText("");
			view.getnoteField().requestFocusInWindow();
			
			// We want to save the note to the server immediately, but only if
			// the requirement hasn't been just created
			if (model.getName().length() > 0) {
				final RequirementsController controller = new RequirementsController();
				final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
						parentView);
				controller.save(model, observer);
			}
			
		}
	}
}
