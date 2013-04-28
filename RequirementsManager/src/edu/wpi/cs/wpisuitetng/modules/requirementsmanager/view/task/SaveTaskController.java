/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTableModel;

/**
 * This controller handles saving requirement tasks to the server
 * 
 * @author Nick Massa, Matt Costi
 */
public class SaveTaskController {
	
	private final MakeTaskPanel view;
	private final Requirement model;
	private final DetailPanel parentView;
	private final EventTableModel taskModel;
	private final EventTable taskTable;
	
	/**
	 * Construct the controller
	 * 
	 * @param view
	 *            the MakeTaskPanel containing the task field
	 * @param model
	 *            the requirement to which tasks are being added
	 * @param parentView
	 *            the DetailPanel displaying the current requirement
	 * @param taskTable
	 *            The tasktable to control
	 */
	public SaveTaskController(final MakeTaskPanel view,
			final Requirement model, final DetailPanel parentView,
			final EventTable taskTable) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
		this.taskTable = taskTable;
		taskModel = (EventTableModel) taskTable.getModel();
	}
	
	/**
	 * Save a task to the server
	 * 
	 * @param selectedRows
	 *            The rows to save
	 */
	@SuppressWarnings ("unchecked")
	public void saveTask(final int[] selectedRows) {
		final String taskText = view.getTaskField().getText();
		final String taskName = view.getTaskName().getText();
		int taskEstimate;
		boolean allTasksComplete = true;
		
		if (!view.getEstimate().getText().equals("")) {
			taskEstimate = Integer.parseInt(view.getEstimate().getText());
		} else {
			taskEstimate = -1;
		}
		
		int estimateSum = 0;
		
		for (final Task altTask : model.getTasks()) {
			if (altTask.getId() != altTask.getId()) {
				estimateSum = estimateSum + altTask.getEstimate();
			}
		}
		
		if (selectedRows == null) { // Creating a task!
			System.out.println("TASKS WAS NULL, ISSUE");
		} else if (selectedRows.length < 1) {
			// Task must have a name and description of at least one character
			if ((taskText.length() > 0) && (taskName.length() > 0)) {
				final Task tempTask = new Task(taskName, taskText);
				if ((view.getUserAssigned().getSelectedItem() == "")) {
					tempTask.setAssignedUser(null);
				} else {
					tempTask.setAssignedUser((String) view.getUserAssigned()
							.getSelectedItem());
				}
				
				if (taskEstimate != -1) {
					if ((taskEstimate + estimateSum) <= model.getEstimate()) {
						tempTask.setEstimate(taskEstimate);
					}
				}
				
				tempTask.setId(model.getTasks().size() + 1);
				model.addTask(tempTask);
				taskModel.addEvent(tempTask);
				view.getTaskName().setText("");
				view.getTaskField().setText("");
				view.getTaskField().requestFocusInWindow();
				// We want to save the task to the server immediately, but only
				// if the requirement hasn't been just created
				if (model.getName().length() > 0) {
					// Save to requirement!
					final RequirementsController controller = new RequirementsController();
					final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
							parentView);
					controller.save(model, observer);
				}
				parentView.getComboBoxStatus().removeItem("Complete");
			}
		} else {
			
			// Modifying tasks
			final List<Task> selectedTasks = new ArrayList<Task>();
			
			for (final int i : selectedRows) {
				selectedTasks.add((Task) taskModel.getValueAt(i, 0));
			}
			
			for (final Task task : selectedTasks) {
				if (selectedTasks.size() == 1) {
					// If only one is selected, edit the fields
					if ((taskText.length() > 0) && (taskName.length() > 0)) {
						task.setName(view.getTaskName().getText());
						task.setDescription(view.getTaskField().getText());
					}
					
					if ((view.getUserAssigned().getSelectedItem() == "")) {
						task.setAssignedUser(null);
					} else {
						task.setAssignedUser((String) view.getUserAssigned()
								.getSelectedItem());
					}
					
					if (taskEstimate != -1) {
						if (((taskEstimate + estimateSum) - task.getEstimate()) <= model
								.getEstimate()) {
							task.setEstimate(taskEstimate);
						}
					}
					
				}
				// Check the completion status on the tasks
				task.setCompleted(view.getTaskComplete().isSelected());
			}
			
			// Save to requirement!
			if (model.getName().length() > 0) {
				final RequirementsController controller = new RequirementsController();
				final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
						parentView);
				controller.save(model, observer);
			}
			view.getTaskName().setText("");
			view.getTaskField().setText("");
			view.getTaskField().requestFocusInWindow();
		}
		
		final List<Task> selectedTasks = new ArrayList<Task>();
		
		for (final int i : selectedRows) {
			selectedTasks.add((Task) taskModel.getValueAt(i, 0));
		}
		
		for (final Task task : selectedTasks) {
			if (!task.isCompleted()) {
				allTasksComplete = false;
				parentView.getComboBoxStatus().removeItem("Complete");
			}
		}
		
		if (allTasksComplete) {
			parentView.getComboBoxStatus().removeItem("Complete");
			parentView.getComboBoxStatus().addItem("Complete");
		}
		
		taskTable.clearSelection();
		view.getTaskStatus()
				.setText(
						"No tasks selected. Fill name and description to create a new one.");
		view.getTaskComplete().setEnabled(false);
		view.getTaskComplete().setSelected(false);
		view.getUserAssigned().setEnabled(true);
		view.getTaskField().setEnabled(true);
		view.getTaskName().setEnabled(true);
		view.getEstimate().setEnabled(true);
		view.getTaskField().setText("");
		view.getTaskName().setText("");
		view.getEstimate().setText("");
		view.getUserAssigned().setSelectedItem("");
		view.getTaskField().setBackground(Color.white);
		view.getTaskName().setBackground(Color.white);
		view.getAddTask().setEnabled(false);
		
	}
}
