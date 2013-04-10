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
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.ToggleSelectionModel;

/**
 * Panel containing a task for a requirement
 * 
 * @author Nick Massa
 */
public class DetailTaskView extends JPanel {
	/** For Tasks */
	protected DefaultListModel taskList;
	protected JList tasks;
	private Requirement requirement;
	private DetailPanel parentView;
	private MakeTaskPanel makeTaskPanel;

	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailTaskView(final Requirement requirement,
			final DetailPanel parentView) {

		this.requirement = requirement;
		this.parentView = parentView;

		setLayout(new BorderLayout());
		// Set up the task panel
		makeTaskPanel = new MakeTaskPanel(requirement, parentView);

		// Create the task list TODO: CHANGE GETSELECTEDVALUES TO
		// GETSELECTEDVALUES
		taskList = new DefaultListModel();
		tasks = new JList(taskList);
		tasks.setCellRenderer(new EventCellRenderer());
		tasks.setSelectionModel(new ToggleSelectionModel());

		// Add the list to the scroll pane
		JScrollPane taskScrollPane = new JScrollPane();
		taskScrollPane.getViewport().add(tasks);

		// Set up the frame
		JPanel taskPane = new JPanel();
		taskPane.setLayout(new BorderLayout());
		taskPane.add(taskScrollPane, BorderLayout.CENTER);
		taskPane.add(makeTaskPanel, BorderLayout.SOUTH);

		add(taskPane, BorderLayout.CENTER);

		// adds the tasks to the list model
		addTasksToList();
		// ,tasks.getSelectedValues()

		List<String> assignedUsers = requirement.getUsers();
		// iterate through and add them to the list
		makeTaskPanel.getuserAssigned().addItem("");
		for (String user : assignedUsers) {
			makeTaskPanel.getuserAssigned().addItem(user);
		}

		//Set the action of the save button to the default (create new task)
		makeTaskPanel.getaddTask().setAction(new SaveTaskAction(new SaveTaskController(makeTaskPanel, requirement, parentView, tasks)));

		tasks.addMouseListener(new MouseAdapter() { //Listen for user clicking on tasks
			@Override
			public void mouseClicked(MouseEvent evt) {
				updateTaskView(); //Update the task view, will change based on number of tasks clicked (0,1,multiple)
			}
		});

		int delay = 1000; // Setting up timer, delay for 1 sec
		int period = 1000; // repeat every 1 sec
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{
				updateTaskView(); //Update the view periodically. Used due to swing clicking buggy
			}
		}, delay, period);

		makeTaskPanel.gettaskField().addKeyListener(new KeyAdapter() { //Make sure save button is unavailable if name field is empty
			//For creating a new task
			@Override
			public void keyReleased(KeyEvent e) {
				if (makeTaskPanel.gettaskField().getText().trim().equals("")
						&& tasks.getSelectedValues().length == 0)
					makeTaskPanel.getaddTask().setEnabled(false);
				else if (!makeTaskPanel.gettaskName().getText().trim()
						.equals(""))
					makeTaskPanel.getaddTask().setEnabled(true);
			}
		});

		makeTaskPanel.gettaskName().addKeyListener(new KeyAdapter() { //Make sure save button is unavailable if desc field is empty
			@Override                                                 //For creating a new task
			public void keyReleased(KeyEvent e) {
				if (makeTaskPanel.gettaskName().getText().trim().equals("")
						&& tasks.getSelectedValues().length == 0)
					makeTaskPanel.getaddTask().setEnabled(false);
				else if (!makeTaskPanel.gettaskField().getText().trim()
						.equals(""))
					makeTaskPanel.getaddTask().setEnabled(true);
			}
		});

	}

	private void updateTaskView(){
		if(requirement.getStatus() != Status.DELETED){
			makeTaskPanel.getaddTask().setAction(
					new SaveTaskAction(new SaveTaskController(
							makeTaskPanel, requirement, parentView, tasks), tasks
							.getSelectedValues()));

			if (tasks.getSelectedValues().length == 0) {
				makeTaskPanel
				.gettaskStatus()
				.setText(
						"No tasks selected. Fill name and description to create a new one.");
				makeTaskPanel.gettaskComplete().setEnabled(false);
				makeTaskPanel.gettaskComplete().setSelected(false);
				makeTaskPanel.getuserAssigned().setEnabled(true);
				makeTaskPanel.gettaskField().setText("");
				makeTaskPanel.gettaskName().setText("");
				makeTaskPanel.gettaskField().setBackground(Color.white);
				makeTaskPanel.gettaskName().setBackground(Color.white);
				if (makeTaskPanel.gettaskName().getText().trim().equals("")
						|| makeTaskPanel.gettaskField().getText().trim()
						.equals(""))
					makeTaskPanel.getaddTask().setEnabled(false);
			} else {
				makeTaskPanel.gettaskComplete().setEnabled(true);
				if (tasks.getSelectedValues().length > 1) {
					makeTaskPanel
					.gettaskStatus()
					.setText(
							"Multiple tasks selected. Can only change status.");
					makeTaskPanel.gettaskFieldPane().setEnabled(false);
					makeTaskPanel.gettaskField().setEnabled(false);
					makeTaskPanel.gettaskName().setEnabled(false);
					makeTaskPanel.gettaskComplete().setSelected(false);
					makeTaskPanel.getuserAssigned().setEnabled(false);
					makeTaskPanel.gettaskField().setText("");
					makeTaskPanel.gettaskName().setText("");
					makeTaskPanel.gettaskField().setBackground(
							makeTaskPanel.getBackground());
					makeTaskPanel.gettaskName().setBackground(
							makeTaskPanel.getBackground());
				} else {
					makeTaskPanel
					.gettaskStatus()
					.setText(
							"One task selected. Fill name AND description to edit. Leave blank to just change status/user.");
					makeTaskPanel.gettaskFieldPane().setEnabled(true);
					makeTaskPanel.gettaskField().setEnabled(true);
					makeTaskPanel.gettaskName().setEnabled(true);
					makeTaskPanel.getuserAssigned().setEnabled(true);
					makeTaskPanel.gettaskField().setText(
							getSingleSelectedTask().getDescription());
					makeTaskPanel.gettaskName().setText(
							getSingleSelectedTask().getName());
					makeTaskPanel.gettaskComplete().setSelected(
							getSingleSelectedTask().isCompleted());
					makeTaskPanel.gettaskField().setBackground(Color.white);
					makeTaskPanel.gettaskName().setBackground(Color.white);
				}
			}
		}
	}
	/**
	 * Method to populate this object's list of tasks from the current
	 * requirement's list of tasks
	 */
	private void addTasksToList() {
		taskList.clear();

		// add the tasks to the list model.
		for (Task aTask : requirement.getTasks()) {
			this.taskList.addElement(aTask);
		}
	}

	/**
	 * simple getter for the list of tasks of which this view is currently aware
	 * 
	 * @return the list of tasks
	 */
	public DefaultListModel getTaskList() {
		return taskList;
	}

	/**
	 * Updates the local display of the current requirement's tasks
	 * 
	 * @param newRequirement
	 *            the most recent version of the current requirement
	 */
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;

		// updates the tasks list
		addTasksToList();
	}

	/**
	 * simple getter for the single currently selected task
	 * 
	 * @return the selected task
	 */
	public Task getSingleSelectedTask() {
		return (Task) tasks.getSelectedValue();
	}

	/**
	 * This function disables interaction with the tasks panel
	 */
	public void disableUserButtons() {
		makeTaskPanel.setInputEnabled(false);
	}
}
