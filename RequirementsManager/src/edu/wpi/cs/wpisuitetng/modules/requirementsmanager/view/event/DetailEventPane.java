/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailLogView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest.DetailATestView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.DetailNoteView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.SubRequirementPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task.DetailTaskView;

/**
 * Class for displaying the note view and the log view on the right side of the
 * Detail View
 */
@SuppressWarnings("serial")
public class DetailEventPane extends JTabbedPane {

	private DetailNoteView notesListPane;
	private DetailLogView logListPane;
	private AssigneePanel userListPane;
	private DetailTaskView taskListPane;
	private DetailATestView atestListPane;
	private SubRequirementPanel subreqListPane;

	/**
	 * Creates a new DetailEvent pane, that displays the given NotesListPane,
	 * and LogListPane
	 * 
	 * @param notesListPane
	 *            The JPanel that represents the list of notes to represent
	 * @param logListPane
	 *            TheJPanel that represents the list of logs to represent
	 * @param userListPane
	 *            the ListPane that has all the users
	 * @param taskListPane
	 *            the ListPane that has all the tasks
	 * @param atestListPane
	 *            the ListPane that has all the tests
	 * @param subreqListPane
	 *            the ListPane that all all the subrequirement
	 */
	public DetailEventPane(final DetailNoteView notesListPane,
			final DetailLogView logListPane, final AssigneePanel userListPane,
			final DetailTaskView taskListPane,
			final DetailATestView atestListPane,
			final SubRequirementPanel subreqListPane) {
		this.notesListPane = notesListPane;
		this.logListPane = logListPane;
		this.userListPane = userListPane;
		this.taskListPane = taskListPane;
		this.atestListPane = atestListPane;
		this.subreqListPane = subreqListPane;

		// add the given tabs to the pane
		addTab("Notes", new ImageIcon(), notesListPane,
				"The notes for this requirement");
		addTab("Log", new ImageIcon(), logListPane,
				"The log for this requirement");
		addTab("Users", new ImageIcon(), userListPane,
				"The users assigned to this requirement");
		addTab("Tasks", new ImageIcon(), taskListPane,
				"The tasks assigned to this requirement");
		addTab("Tests", new ImageIcon(), atestListPane,
				"The acceptance tests assigned to this requirement");
		addTab("Subrequirements", new ImageIcon(), subreqListPane,
				"The subrequirements for this requirement");

	}

	/**
	 * Disables the subrequirement's fields
	 */
	public void disableSubReqs() {
		subreqListPane.disableUserButtons();
	}

	/**
	 * Disables the user buttons
	 */
	public void disableUserButtons() {
		userListPane.disableUserButtons();
		notesListPane.disableUserButtons();
		subreqListPane.disableUserButtons();
		atestListPane.disableUserButtons();
		taskListPane.disableUserButtons();
	}

	/**
	 * Disables users and subrequirements
	 */
	public void disableUsersAndSubReqs() {
		userListPane.disableUserButtons();
		subreqListPane.disableUserButtons();
	}

	public void updateRequirement(Requirement newRequirement) {
		notesListPane.updateRequirement(newRequirement);
	}
}
