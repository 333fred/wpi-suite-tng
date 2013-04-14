/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Zac Chupka, Maddie Burris
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.ToggleSelectionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.NoteViewListener;

/**
 * Panel containing a note for a requirement
 * 
 * @author Zac Chupka, Maddie Burris
 */
public class DetailNoteView extends JPanel {
	/** For Notes */
	protected DefaultListModel noteList;
	protected JList notes;
	private Requirement requirement;
	private DetailPanel parentView;
	private MakeNotePanel makeNotePanel;
	public boolean hasChanges;

	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailNoteView(Requirement requirement, DetailPanel parentView) {
		this.requirement = requirement;
		this.parentView = parentView;
		hasChanges = false;

		setLayout(new BorderLayout());
		// Set up the note panel
		makeNotePanel = new MakeNotePanel(requirement, parentView);

		// Create the note list
		noteList = new DefaultListModel();
		notes = new JList(noteList);
		notes.setCellRenderer(new EventCellRenderer());
		notes.setSelectionModel(new ToggleSelectionModel());

		// Add the list to the scroll pane
		JScrollPane noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(notes);

		// Set up the frame
		JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		notePane.add(noteScrollPane, BorderLayout.CENTER);
		notePane.add(makeNotePanel, BorderLayout.SOUTH);

		add(notePane, BorderLayout.CENTER);

		// add key listener
		NoteViewListener noteFieldListener = new NoteViewListener(this,
				this.makeNotePanel.getnoteField());
		this.makeNotePanel.getnoteField().addKeyListener(noteFieldListener);

		// note will be disabled on start, nothing to add
		disableAddNote();

		// adds the notes to the list model
		addNotesToList();
	}

	/**
	 * Method to populate this object's list of notes from the current
	 * requirement's list of notes
	 */
	private void addNotesToList() {
		noteList.clear();

		// add the notes to the list model.
		for (Note aNote : requirement.getNotes()) {
			this.noteList.addElement(aNote);
		}
	}

	/**
	 * simple getter for the list of notes of which this view is currently aware
	 * 
	 * @return the list of notes
	 */
	public DefaultListModel getNoteList() {
		return noteList;
	}

	/**
	 * Updates the local display of the current requirement's notes
	 * 
	 * @param newRequirement
	 *            the most recent version of the current requirement
	 */
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;

		// updates the notes list
		addNotesToList();
	}

	/**
	 * This function disables interaction with the notes panel
	 */
	public void disableUserButtons() {
		makeNotePanel.setInputEnabled(false);
	}

	public void disableAddNote() {
		this.makeNotePanel.getAddnote().setEnabled(false);
	}

	public void enableAddNote() {
		this.makeNotePanel.getAddnote().setEnabled(true);
	}
}
