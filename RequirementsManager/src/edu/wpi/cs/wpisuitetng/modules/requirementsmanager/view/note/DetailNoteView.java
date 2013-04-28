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
@SuppressWarnings ("serial")
public class DetailNoteView extends JPanel {
	
	private final JScrollPane noteScrollPane;
	
	protected EventTable notesTable;
	private final EventTableModel tableModel;
	
	private Requirement requirement;
	@SuppressWarnings ("unused")
	private final DetailPanel parentView;
	private final MakeNotePanel makeNotePanel;
	
	@SuppressWarnings ("unused")
	private final EventCellRenderer cellRenderer;
	
	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailNoteView(final Requirement requirement,
			final DetailPanel parentView) {
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
		notesTable.setEnabled(false); // Disable selection
		
		// Add the list to the scroll pane
		noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(notesTable);
		
		// Set up the frame
		final JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		notePane.add(noteScrollPane, BorderLayout.CENTER);
		notePane.add(makeNotePanel, BorderLayout.SOUTH);
		notePane.remove(notesTable.getTableHeader());
		
		add(notePane, BorderLayout.CENTER);
		
		// add key listener
		final NoteViewListener noteFieldListener = new NoteViewListener(this,
				makeNotePanel.getnoteField());
		makeNotePanel.getnoteField().addKeyListener(noteFieldListener);
		
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
		final List<Note> notes = requirement.getNotes();
		final List<Event> events = new ArrayList<Event>();
		for (final Note note : notes) {
			events.add(note);
		}
		tableModel.setRowData(events);
	}
	
	/**
	 * Disables the addNote button
	 */
	public void disableAddNote() {
		makeNotePanel.getAddnote().setEnabled(false);
	}
	
	/**
	 * This function disables interaction with the notes panel
	 */
	public void disableUserButtons() {
		makeNotePanel.setInputEnabled(false);
	}
	
	/**
	 * Enables the addNote button
	 */
	public void enableAddNote() {
		makeNotePanel.getAddnote().setEnabled(true);
	}
	
	/**
	 * Gets the panel for making notes
	 * 
	 * @return the note panel
	 */
	public MakeNotePanel getNotePanel() {
		return makeNotePanel;
	}
	
	/**
	 * Gets the current full list of notes
	 * 
	 * @return the list of notes
	 */
	public List<Note> getNotesList() {
		final List<Note> notes = new ArrayList<Note>();
		for (final Event e : tableModel.getRowData()) {
			notes.add((Note) e);
		}
		return notes;
	}
	
	/**
	 * Scrolls the panel to the bottom
	 */
	public void scrollToBottom() {
		noteScrollPane.getVerticalScrollBar().setValue(
				noteScrollPane.getVerticalScrollBar().getMaximum());
	}
	
	/**
	 * Sets the list of notes to the given list
	 * 
	 * @param notes
	 *            the notes to set
	 */
	public void setNotesList(final List<Note> notes) {
		final List<Event> events = new ArrayList<Event>();
		for (final Note note : notes) {
			events.add(note);
		}
		tableModel.setRowData(events);
	}
	
	/**
	 * Updates the local display of the current requirement's notes
	 * 
	 * @param newRequirement
	 *            the most recent version of the current requirement
	 */
	public void updateRequirement(final Requirement newRequirement) {
		requirement = newRequirement;
		
		// updates the notes list
		addNotesToList();
	}
}
