/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.DetailNoteView;

/**
 * Class for displaying the note view and the log view on the right side of the
 * Detail View
 * 
 * @author Mitchell
 * 
 */

public class DetailEventPane extends JTabbedPane {

	JPanel notesListPane;
	JPanel logListPane;
	JPanel userListPane;
	JPanel taskListPane;

	/**
	 * Creates a new DetailEvent pane, that displays the given NotesListPane,
	 * and LogListPane
	 * 
	 * @param notesListPane
	 *            The JPanel that represents the list of notes to represent
	 * @param logListPane
	 *            TheJPanel that represents the list of logs to represent
	 */

	public DetailEventPane(JPanel notesListPane, JPanel logListPane,
			JPanel userListPane) {
		this.notesListPane = notesListPane;
		this.logListPane = logListPane;
		this.userListPane = userListPane;

		// add the given tabs to the pane
		addTab("Notes", new ImageIcon(), notesListPane,
				"The notes for this requirement");
		addTab("Log", new ImageIcon(), logListPane,
				"The log for this requirement");
		addTab("Users", new ImageIcon(), userListPane,
				"The users assigned to this requirement");

	}

	public DetailEventPane(JPanel notesListPane, JPanel logListPane,
			JPanel userListPane, JPanel taskListPane) {
		this.notesListPane = notesListPane;
		this.logListPane = logListPane;
		this.userListPane = userListPane;
		this.taskListPane = taskListPane;

		// add the given tabs to the pane
		addTab("Notes", new ImageIcon(), notesListPane,
				"The notes for this requirement");
		addTab("Log", new ImageIcon(), logListPane,
				"The log for this requirement");
		addTab("Users", new ImageIcon(), userListPane,
				"The users assigned to this requirement");
		addTab("Tasks", new ImageIcon(), taskListPane,
				"The tasks assigned to this requirement");
	}

	public void disableUserButtons() {
		((AssigneePanel) userListPane).disableUserButtons();
		((DetailNoteView) notesListPane).disableUserButtons();
	}
}
