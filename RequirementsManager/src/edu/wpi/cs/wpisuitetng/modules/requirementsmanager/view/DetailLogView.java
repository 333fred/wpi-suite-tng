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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTableModel;

/**
 * A panel containing a form for adding a new log to a requirement
 */
public class DetailLogView extends JPanel {
	/** For Notes */

	private EventTableModel logModel;
	private EventTable logTable;
	private Requirement requirement;
	private DetailPanel parentView;
	private EventCellRenderer cellRenderer;
	private JScrollPane logScrollPane;
	
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
		logModel = new EventTableModel();
		logTable = new EventTable(logModel);
		cellRenderer = new EventCellRenderer();
		
		
		logTable.getTableHeader().setVisible(false);
		// Add the list to the scroll pane
		logScrollPane = new JScrollPane();
		logScrollPane.getViewport().add(logTable);

		// Set up the frame
		JPanel logPane = new JPanel();
		logPane.setLayout(new BorderLayout());
		logPane.add(logScrollPane, BorderLayout.CENTER);
		add(logPane, BorderLayout.CENTER);
		

		this.refresh(this.requirement);
	}

	public void refresh(Requirement requirement) {
		List<Event> logs = new ArrayList<Event>();
		this.requirement = requirement;
		for (RequirementChangeset aLog : requirement.getLogs()) {
			logs.add(aLog);
		}		
		logModel.setRowData(logs);
	}
	
}
