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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTableModel;

/**
 * A panel containing a form for adding a new log to a requirement
 */
@SuppressWarnings ("serial")
public class DetailLogView extends JPanel {
	
	/** For Notes */
	
	private final EventTableModel logModel;
	private final EventTable logTable;
	private Requirement requirement;
	private final JScrollPane logScrollPane;
	
	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailLogView(final Requirement requirement,
			final DetailPanel parentView) {
		this.requirement = requirement;
		
		setLayout(new BorderLayout());
		
		// Create the log list
		logModel = new EventTableModel();
		logTable = new EventTable(logModel);
		
		logTable.getTableHeader().setVisible(false);
		logTable.setEnabled(false); // Disable selection
		// Add the list to the scroll pane
		logScrollPane = new JScrollPane();
		logScrollPane.getViewport().add(logTable);
		
		// Set up the frame
		final JPanel logPane = new JPanel();
		logPane.setLayout(new BorderLayout());
		logPane.add(logScrollPane, BorderLayout.CENTER);
		add(logPane, BorderLayout.CENTER);
		
		refresh(this.requirement);
	}
	
	/**
	 * Refreshes the log view with logs from the given requirement
	 * 
	 * @param newRequirement
	 *            the requirement to pull logs form
	 */
	public void refresh(final Requirement newRequirement) {
		final List<Event> logs = new ArrayList<Event>();
		this.requirement = newRequirement;
		for (final RequirementChangeset aLog : newRequirement.getLogs()) {
			logs.add(aLog);
		}
		logModel.setRowData(logs);
	}
	
}
