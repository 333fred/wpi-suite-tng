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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.ToggleSelectionModel;

/**
 * A panel containing a form for adding a new log to a requirement
 */
public class DetailLogView extends JPanel {
	/** For Notes */
	protected DefaultListModel logList;
	protected JList log;
	private Requirement requirement;
	private DetailPanel parentView;

	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailLogView(Requirement requirement, DetailPanel parentView) {
		this.requirement = requirement;
		this.parentView = parentView;

		setLayout(new BorderLayout());

		// Create the log list
		logList = new DefaultListModel();
		log = new JList(logList);
		log.setCellRenderer(new EventCellRenderer());
		log.setSelectionModel(new ToggleSelectionModel());

		// Add the list to the scroll pane
		JScrollPane noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(log);

		// Set up the frame
		JPanel logPane = new JPanel();
		logPane.setLayout(new BorderLayout());
		logPane.add(noteScrollPane, BorderLayout.CENTER);

		add(logPane, BorderLayout.CENTER);

		this.refresh(this.requirement);
	}

	public void refresh(Requirement requirement) {
		this.logList.clear();
		this.requirement = requirement;
		for (RequirementChangeset aLog : requirement.getLogs()) {
			this.logList.addElement(aLog);
		}
	}
}
