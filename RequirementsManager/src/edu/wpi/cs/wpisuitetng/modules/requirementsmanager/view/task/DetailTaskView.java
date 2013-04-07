/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick, Conor, Matt
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;


/**
 * Panel containing a task for a requirement
 * @author Zac Chupka, Maddie Burris
 */
public class DetailTaskView extends JPanel{
	/** For Tasks */
	protected DefaultListModel taskList;
	protected JList tasks;
	private Requirement requirement;
	private DetailPanel parentView;
	private MakeTaskPanel makeTaskPanel;

	
	/**
	 * Construct the panel and add layout components
	 * @param requirement the requirement 
	 * @param parentView the parent view
	 */
	public DetailTaskView(Requirement requirement, DetailPanel parentView){
		this.requirement = requirement;
		this.parentView = parentView;
		
		setLayout(new BorderLayout());
		// Set up the task panel
		makeTaskPanel = new MakeTaskPanel(requirement, parentView);

		// Create the task list
		taskList = new DefaultListModel();
		tasks = new JList(taskList);
		tasks.setCellRenderer(new EventCellRenderer());

		// Add the list to the scroll pane
		JScrollPane taskScrollPane = new JScrollPane();
		taskScrollPane.getViewport().add(tasks);
		
		// Set up the frame
		JPanel taskPane = new JPanel();
		taskPane.setLayout(new BorderLayout());
		taskPane.add(taskScrollPane, BorderLayout.CENTER);
		taskPane.add(makeTaskPanel, BorderLayout.SOUTH);
		
		add(taskPane, BorderLayout.CENTER);
		
		//adds the tasks to the list model
		addTasksToList();
	}
	
	/**
	 * Method to populate this object's list of tasks
	 * from the current requirement's list of tasks
	 */
	private void addTasksToList() {
		taskList.clear();
		
		//add the tasks to the list model.
		for (Task aTask : requirement.getTasks()) {
			this.taskList.addElement(aTask);
		}
	}
	
	/**
	 * simple getter for the list of tasks of which this view is currently aware
	 * @return the list of tasks
	 */
	public DefaultListModel getTaskList() {
		return taskList;
	}
	
	/** Updates the local display of the current requirement's tasks
	 * 
	 * @param newRequirement the most recent version of the current requirement
	 */
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;
		
		//updates the tasks list
		addTasksToList();
	}
	
	/**
	 * This function disables interaction with the tasks panel
	 */
	public void disableUserButtons(){
		makeTaskPanel.setInputEnabled(false);
	}	
}
