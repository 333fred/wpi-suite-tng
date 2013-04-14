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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

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
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
public class DetailATestView extends JPanel {
	/** For Tasks */
	protected DefaultListModel testList;
	protected JList tests;
	private Requirement requirement;
	private DetailPanel parentView;
	private MakeATestPanel makeATestPanel;

	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailATestView(final Requirement requirement,
			final DetailPanel parentView) {

		this.requirement = requirement;
		this.parentView = parentView;

		setLayout(new BorderLayout());
		// Set up the task panel
		makeATestPanel = new MakeATestPanel(requirement, parentView);



		// Create the task list TODO: CHANGE GETSELECTEDVALUES TO
		// GETSELECTEDVALUES
		testList = new DefaultListModel();
		tests = new JList(testList);
		tests.setCellRenderer(new EventCellRenderer());
		tests.setSelectionModel(new ToggleSelectionModel());

		// Add the list to the scroll pane
		JScrollPane taskScrollPane = new JScrollPane();
		taskScrollPane.getViewport().add(tests);

		// Set up the frame
		JPanel taskPane = new JPanel();
		taskPane.setLayout(new BorderLayout());
		taskPane.add(taskScrollPane, BorderLayout.CENTER);
		taskPane.add(makeATestPanel, BorderLayout.SOUTH);

		add(taskPane, BorderLayout.CENTER);

		// adds the tasks to the list model
		addTasksToList();
		// ,tasks.getSelectedValues()

		List<String> assignedUsers = requirement.getUsers();
		// iterate through and add them to the list
		makeATestPanel.getUserAssigned().addItem("");
		for (String user : assignedUsers) {
			makeATestPanel.getUserAssigned().addItem(user);
		}

		if(requirement.getStatus() != Status.DELETED) {
			//Set the action of the save button to the default (create new task)
			makeATestPanel.getAddTask().setAction(new SaveATestAction(new SaveATestController(makeATestPanel, requirement, parentView, tests)));


			//Listen for user clicking on tasks
			tests.addMouseListener(new MouseAdapter() { 
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

			
			//Make sure save button is unavailable if name field is empty
			makeATestPanel.getTaskField().addKeyListener(new KeyAdapter() { 
				//For creating a new task
				@Override
				public void keyReleased(KeyEvent e) {
					if (makeATestPanel.getTaskField().getText().trim().equals("")
							&& tests.getSelectedValues().length == 0)
						makeATestPanel.getAddTask().setEnabled(false);
					else if (!makeATestPanel.getTaskName().getText().trim()
							.equals(""))
						makeATestPanel.getAddTask().setEnabled(true);
				}
			});
			
			//Make sure save button is unavailable if desc field is empty
			//for creating a new task
			makeATestPanel.getTaskName().addKeyListener(new KeyAdapter() { 
				@Override                                                 
				public void keyReleased(KeyEvent e) {
					if (makeATestPanel.getTaskName().getText().trim().equals("")
							&& tests.getSelectedValues().length == 0)
						makeATestPanel.getAddTask().setEnabled(false);
					else if (!makeATestPanel.getTaskField().getText().trim()
							.equals(""))
						makeATestPanel.getAddTask().setEnabled(true);
				}
			});

		}else{
			//Requirement is set to delted, so disable all of the fields
			makeATestPanel.getTaskFieldPane().setEnabled(false);
			makeATestPanel.getTaskField().setEnabled(false);
			makeATestPanel.getTaskName().setEnabled(false);
			makeATestPanel.getUserAssigned().setEnabled(false);
			makeATestPanel.getAddTask().setEnabled(false);
			makeATestPanel.getTaskStatus().setText("");

			makeATestPanel.getTaskField().setBackground(makeATestPanel.getBackground());
			makeATestPanel.getTaskName().setBackground(makeATestPanel.getBackground());
		}

	}

	/**
	 * updateTaskView
	 * 
	 * currently not used. Would be called by the timer to update the view task
	 * 
	 */
	private void updateTaskView(){
		if(requirement.getStatus() != Status.DELETED){
			makeATestPanel.getAddTask().setAction(
					new SaveATestAction(new SaveATestController(
							makeATestPanel, requirement, parentView, tests), tests
							.getSelectedValues()));

			if (tests.getSelectedValues().length == 0) {
				makeATestPanel
				.getTaskStatus()
				.setText(
						"No tasks selected. Fill name and description to create a new one.");
				makeATestPanel.getTaskComplete().setEnabled(false);
				makeATestPanel.getTaskComplete().setSelected(false);
				makeATestPanel.getUserAssigned().setEnabled(true);
				makeATestPanel.getTaskField().setText("");
				makeATestPanel.getTaskName().setText("");
				makeATestPanel.getTaskField().setBackground(Color.white);
				makeATestPanel.getTaskName().setBackground(Color.white);
				if (makeATestPanel.getTaskName().getText().trim().equals("")
						|| makeATestPanel.getTaskField().getText().trim()
						.equals(""))
					makeATestPanel.getAddTask().setEnabled(false);
			} else {
				makeATestPanel.getTaskComplete().setEnabled(true);
				if (tests.getSelectedValues().length > 1) {
					makeATestPanel
					.getTaskStatus()
					.setText(
							"Multiple tasks selected. Can only change status.");
					makeATestPanel.getTaskFieldPane().setEnabled(false);
					makeATestPanel.getTaskField().setEnabled(false);
					makeATestPanel.getTaskName().setEnabled(false);
					makeATestPanel.getTaskComplete().setSelected(false);
					makeATestPanel.getUserAssigned().setEnabled(false);
					makeATestPanel.getTaskField().setText("");
					makeATestPanel.getTaskName().setText("");
					makeATestPanel.getTaskField().setBackground(
							makeATestPanel.getBackground());
					makeATestPanel.getTaskName().setBackground(
							makeATestPanel.getBackground());
				} else {
					makeATestPanel
					.getTaskStatus()
					.setText(
							"One task selected. Fill name AND description to edit. Leave blank to just change status/user.");
					makeATestPanel.getTaskFieldPane().setEnabled(true);
					makeATestPanel.getTaskField().setEnabled(true);
					makeATestPanel.getTaskName().setEnabled(true);
					makeATestPanel.getUserAssigned().setEnabled(true);
					makeATestPanel.getTaskField().setText(
							getSingleSelectedTask().getDescription());
					makeATestPanel.getTaskName().setText(
							getSingleSelectedTask().getName());
					makeATestPanel.getTaskComplete().setSelected(
							getSingleSelectedTask().isCompleted());
					makeATestPanel.getTaskField().setBackground(Color.white);
					makeATestPanel.getTaskName().setBackground(Color.white);
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
		if(requirement.getStatus() != Status.DELETED){
			makeATestPanel.getAddTask().setAction(
					new SaveATestAction(new SaveATestController(
							makeATestPanel, requirement, parentView, tests), tests
							.getSelectedValues()));

			if (tests.getSelectedValues().length == 0) {
				makeATestPanel
				.getTaskStatus()
				.setText(
						"No tasks selected. Fill name and description to create a new one.");
				makeATestPanel.getTaskComplete().setEnabled(false);
				makeATestPanel.getUserAssigned().setEnabled(true);
				makeATestPanel.getTaskField().setBackground(Color.white);
				makeATestPanel.getTaskName().setBackground(Color.white);
				if (makeATestPanel.getTaskName().getText().trim().equals("")
						|| makeATestPanel.getTaskField().getText().trim()
						.equals(""))
					makeATestPanel.getAddTask().setEnabled(false);
			} else {
				makeATestPanel.getTaskComplete().setEnabled(true);
				if (tests.getSelectedValues().length > 1) {
					makeATestPanel
					.getTaskStatus()
					.setText(
							"Multiple tasks selected. Can only change status.");
					makeATestPanel.getTaskFieldPane().setEnabled(false);
					makeATestPanel.getTaskField().setEnabled(false);
					makeATestPanel.getTaskName().setEnabled(false);
					makeATestPanel.getUserAssigned().setEnabled(false);
					makeATestPanel.getTaskField().setBackground(
							makeATestPanel.getBackground());
					makeATestPanel.getTaskName().setBackground(
							makeATestPanel.getBackground());
				} else {
					makeATestPanel
					.getTaskStatus()
					.setText(
							"One task selected. Fill name AND description to edit. Leave blank to just change status/user.");
					makeATestPanel.getTaskFieldPane().setEnabled(true);
					makeATestPanel.getTaskField().setEnabled(true);
					makeATestPanel.getTaskName().setEnabled(true);
					makeATestPanel.getUserAssigned().setEnabled(true);
					makeATestPanel.getTaskField().setBackground(Color.white);
					makeATestPanel.getTaskName().setBackground(Color.white);
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
		testList.clear();

		// add the tasks to the list model.
		for (Task aTask : requirement.getTasks()) {
			this.testList.addElement(aTask);
		}
	}

	/**
	 * simple getter for the list of tasks of which this view is currently aware
	 * 
	 * @return the list of tasks
	 */
	public DefaultListModel getTaskList() {
		return testList;
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
		return (Task) tests.getSelectedValue();
	}

	/**
	 * This function disables interaction with the tasks panel
	 */
	public void disableUserButtons() {
		makeATestPanel.setInputEnabled(false);
	}
}
