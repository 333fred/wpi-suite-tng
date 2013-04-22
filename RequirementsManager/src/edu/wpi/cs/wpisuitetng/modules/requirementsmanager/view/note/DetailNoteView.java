/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Zac Chupka, Maddie Burris, Steve Kordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTableModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.NoteViewListener;

/**
 * Panel containing a note for a requirement
 * 
 * @author Zac Chupka, Maddie Burris
 */
public class DetailNoteView extends JPanel {

	private JScrollPane noteScrollPane;

	protected EventTable notesTable;

	private EventTableModel tableModel;

	private Requirement requirement;
	private DetailPanel parentView;
	private MakeNotePanel makeNotePanel;

	private EventCellRenderer cellRenderer;

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

		setLayout(new BorderLayout());
		// Set up the note panel
		makeNotePanel = new MakeNotePanel(requirement, parentView);

		// Create the note list

		tableModel = new EventTableModel(new ArrayList<Event>());
		notesTable = new EventTable(tableModel);
		cellRenderer = new EventCellRenderer();

		notesTable.setShowGrid(false);
		notesTable.setIntercellSpacing(new Dimension(0, 0));
		notesTable.getTableHeader().setVisible(false);

		// Add the list to the scroll pane
		this.noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(notesTable);

		// Set up the frame
		JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		notePane.add(noteScrollPane, BorderLayout.CENTER);
		notePane.add(makeNotePanel, BorderLayout.SOUTH);
		notePane.remove(notesTable.getTableHeader());

		add(notePane, BorderLayout.CENTER);

		// add key listener
		NoteViewListener noteFieldListener = new NoteViewListener(this,
				this.makeNotePanel.getnoteField());
		this.makeNotePanel.getnoteField().addKeyListener(noteFieldListener);

		setMinimumSize(new Dimension(300, 50));
		
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
		List<Note> notes = requirement.getNotes();
		List<Event> events = new ArrayList<Event>();
		for (Note note : notes) {
			events.add(note);
		}
		tableModel.setRowData(events);

		notesTable.updateRowHeights();

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

	public MakeNotePanel getNotePanel() {
		return makeNotePanel;
	}

	public List<Note> getNotesList() {
		List<Note> notes = new ArrayList<Note>();
		for (Event e : tableModel.getRowData()) {
			notes.add((Note) e);
		}
		return notes;
	}

	public void setNotesList(List<Note> notes) {
		List<Event> events = new ArrayList<Event>();
		for (Note note : notes) {
			events.add(note);
		}
		tableModel.setRowData(events);

		notesTable.updateRowHeights();

		// scrollToBottom();
	}

	public void scrollToBottom() {
		noteScrollPane.getVerticalScrollBar().setValue(
				noteScrollPane.getVerticalScrollBar().getMaximum());
	}	
}
