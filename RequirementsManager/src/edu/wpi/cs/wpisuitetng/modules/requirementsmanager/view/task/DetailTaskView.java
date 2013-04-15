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
 * @author Nick Massa, Matt Costi
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

		List<String> assignedUsers = requirement.getUsers();
		// iterate through and add them to the list
		makeTaskPanel.getUserAssigned().addItem("");
		for (String user : assignedUsers) {
			makeTaskPanel.getUserAssigned().addItem(user);
		}

		if(requirement.getStatus() != Status.DELETED && requirement.getStatus() != Status.COMPLETE) {
			//Set the action of the save button to the default (create new task)
			makeTaskPanel.getAddTask().setAction(new SaveTaskAction(new SaveTaskController(makeTaskPanel, requirement, parentView, tasks)));


			//Listen for user clicking on tasks
			tasks.addMouseListener(new MouseAdapter() { 
				@Override
				public void mouseClicked(MouseEvent evt) {
					updateTaskView(); //Update the task view, will change based on number of tasks clicked (0,1,multiple)
				}
			});

			//		Timer ensures right fields are enabled/disabled, but is sort of sketchy
			//		int delay = 1000; // Setting up timer, delay for 1 sec
			//		int period = 1000; // repeat every 1 sec
			//		Timer timer = new Timer();
			//		timer.scheduleAtFixedRate(new TimerTask()
			//		{
			//			public void run()
			//			{
			//				if(requirement.getStatus() != Status.DELETED)
			//					updateTaskViewTime(); //Update the view periodically. Used due to swing clicking buggy
			//			}
			//		}, delay, period);

			makeTaskPanel.getAddTask().setEnabled(false);
			//Make sure save button is unavailable if name field is empty
			makeTaskPanel.getTaskField().addKeyListener(new KeyAdapter() { 
				//For creating a new task
				@Override
				public void keyReleased(KeyEvent e) {
					if (makeTaskPanel.getTaskField().getText().trim().equals("")
							&& tasks.getSelectedValues().length == 0)
						makeTaskPanel.getAddTask().setEnabled(false);
					else if (!makeTaskPanel.getTaskName().getText().trim()
							.equals(""))
						makeTaskPanel.getAddTask().setEnabled(true);
				}
			});
			
			//Make sure save button is unavailable if desc field is empty
			//for creating a new task
			makeTaskPanel.getTaskName().addKeyListener(new KeyAdapter() { 
				@Override                                                 
				public void keyReleased(KeyEvent e) {
					if (makeTaskPanel.getTaskName().getText().trim().equals("")
							&& tasks.getSelectedValues().length == 0)
						makeTaskPanel.getAddTask().setEnabled(false);
					else if (!makeTaskPanel.getTaskField().getText().trim()
							.equals(""))
						makeTaskPanel.getAddTask().setEnabled(true);
				}
			});

		}else{
			//Requirement is set to deleted, so disable all of the fields
			makeTaskPanel.getTaskFieldPane().setEnabled(false);
			makeTaskPanel.getTaskField().setEnabled(false);
			makeTaskPanel.getTaskName().setEnabled(false);
			makeTaskPanel.getUserAssigned().setEnabled(false);
			makeTaskPanel.getAddTask().setEnabled(false);
			makeTaskPanel.getEstimate().setEnabled(false);
			makeTaskPanel.getTaskStatus().setText("");
			makeTaskPanel.getEstimate().setBackground(makeTaskPanel.getBackground());
			makeTaskPanel.getTaskField().setBackground(makeTaskPanel.getBackground());
			makeTaskPanel.getTaskName().setBackground(makeTaskPanel.getBackground());
		}

	}

	/**
	 * updateTaskView
	 * 
	 * currently not used. Would be called by the timer to update the view task
	 * 
	 */
	private void updateTaskView(){
		if(requirement.getStatus() != Status.DELETED && requirement.getStatus() != Status.COMPLETE){
			makeTaskPanel.getAddTask().setAction(
					new SaveTaskAction(new SaveTaskController(
							makeTaskPanel, requirement, parentView, tasks), tasks
							.getSelectedValues()));

			if (tasks.getSelectedValues().length == 0) {
				makeTaskPanel
				.getTaskStatus()
				.setText(
						"No tasks selected. Fill name and description to create a new one.");
				makeTaskPanel.getTaskComplete().setEnabled(false);
				makeTaskPanel.getTaskComplete().setSelected(false);
				makeTaskPanel.getUserAssigned().setEnabled(true);
				makeTaskPanel.getTaskField().setText("");
				makeTaskPanel.getTaskName().setText("");
				makeTaskPanel.getEstimate().setText("0");
				makeTaskPanel.getTaskField().setBackground(Color.white);
				makeTaskPanel.getTaskName().setBackground(Color.white);
				if (makeTaskPanel.getTaskName().getText().trim().equals("")
						|| makeTaskPanel.getTaskField().getText().trim()
						.equals(""))
					makeTaskPanel.getAddTask().setEnabled(false);
			} else {
				makeTaskPanel.getTaskComplete().setEnabled(true);
				if (tasks.getSelectedValues().length > 1) {
					makeTaskPanel
					.getTaskStatus()
					.setText(
							"Multiple tasks selected. Can only change status.");
					makeTaskPanel.getTaskFieldPane().setEnabled(false);
					makeTaskPanel.getTaskField().setEnabled(false);
					makeTaskPanel.getTaskName().setEnabled(false);
					makeTaskPanel.getTaskComplete().setSelected(false);
					makeTaskPanel.getUserAssigned().setEnabled(false);
					makeTaskPanel.getEstimate().setEnabled(false);
					makeTaskPanel.getTaskField().setText("");
					makeTaskPanel.getTaskName().setText("");
					makeTaskPanel.getEstimate().setText("0");
					makeTaskPanel.getTaskField().setBackground(
							makeTaskPanel.getBackground());
					makeTaskPanel.getTaskName().setBackground(
							makeTaskPanel.getBackground());
					makeTaskPanel.getEstimate().setBackground(
							makeTaskPanel.getBackground());
				} else {
					makeTaskPanel
					.getTaskStatus()
					.setText(
							"One task selected. Fill name AND description to edit. Leave blank to just change status/user.");
					makeTaskPanel.getTaskFieldPane().setEnabled(true);
					makeTaskPanel.getTaskField().setEnabled(true);
					makeTaskPanel.getTaskName().setEnabled(true);
					makeTaskPanel.getUserAssigned().setEnabled(true);
					makeTaskPanel.getEstimate().setEnabled(true);
					makeTaskPanel.getTaskField().setText(
							getSingleSelectedTask().getDescription());
					makeTaskPanel.getTaskName().setText(
							getSingleSelectedTask().getName());
					makeTaskPanel.getTaskComplete().setSelected(
							getSingleSelectedTask().isCompleted());					
					makeTaskPanel.getEstimate().setText(Integer.toString(
							getSingleSelectedTask().getEstimate()));
					makeTaskPanel.getTaskField().setBackground(Color.white);
					makeTaskPanel.getTaskName().setBackground(Color.white);
					makeTaskPanel.getEstimate().setBackground(Color.white);	
				}
			}
		}
	}

	/**
	 * updateTaskViewTime
	 * 
	 * currently not used. Would be called by the timer to update the view task 
	 * and populate the fields
	 * 
	 */
	private void updateTaskViewTime(){
		if(requirement.getStatus() != Status.DELETED && requirement.getStatus() != Status.COMPLETE){
			makeTaskPanel.getAddTask().setAction(
					new SaveTaskAction(new SaveTaskController(
							makeTaskPanel, requirement, parentView, tasks), tasks
							.getSelectedValues()));

			if (tasks.getSelectedValues().length == 0) {
				makeTaskPanel
				.getTaskStatus()
				.setText(
						"No tasks selected. Fill name and description to create a new one.");
				makeTaskPanel.getTaskComplete().setEnabled(false);
				makeTaskPanel.getUserAssigned().setEnabled(true);
				makeTaskPanel.getTaskField().setBackground(Color.white);
				makeTaskPanel.getTaskName().setBackground(Color.white);
				if (makeTaskPanel.getTaskName().getText().trim().equals("")
						|| makeTaskPanel.getTaskField().getText().trim()
						.equals(""))
					makeTaskPanel.getAddTask().setEnabled(false);
			} else {
				makeTaskPanel.getTaskComplete().setEnabled(true);
				if (tasks.getSelectedValues().length > 1) {
					makeTaskPanel
					.getTaskStatus()
					.setText(
							"Multiple tasks selected. Can only change status.");
					makeTaskPanel.getTaskFieldPane().setEnabled(false);
					makeTaskPanel.getTaskField().setEnabled(false);
					makeTaskPanel.getTaskName().setEnabled(false);
					makeTaskPanel.getUserAssigned().setEnabled(false);
					makeTaskPanel.getTaskField().setBackground(
							makeTaskPanel.getBackground());
					makeTaskPanel.getTaskName().setBackground(
							makeTaskPanel.getBackground());
				} else {
					makeTaskPanel
					.getTaskStatus()
					.setText(
							"One task selected. Fill name AND description to edit. Leave blank to just change status/user.");
					makeTaskPanel.getTaskFieldPane().setEnabled(true);
					makeTaskPanel.getTaskField().setEnabled(true);
					makeTaskPanel.getTaskName().setEnabled(true);
					makeTaskPanel.getUserAssigned().setEnabled(true);
					makeTaskPanel.getTaskField().setBackground(Color.white);
					makeTaskPanel.getTaskName().setBackground(Color.white);
				}
			}
		}
	}
	
	/**
	 * 
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

	public MakeTaskPanel getTaskPanel() {
		return makeTaskPanel;
	}
}
