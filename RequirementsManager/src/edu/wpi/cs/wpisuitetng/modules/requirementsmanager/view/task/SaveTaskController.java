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

import javax.swing.JList;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * This controller handles saving requirement tasks to the server
 * 
 * @author Nick Massa, Matt Costi
 */
public class SaveTaskController {

	private final MakeTaskPanel view;
	private final Requirement model;
	private final DetailPanel parentView;
	private final JList tasks;

	/**
	 * Construct the controller
	 * 
	 * @param view
	 *            the MakeTaskPanel containing the task field
	 * @param model
	 *            the requirement to which tasks are being added
	 * @param parentView
	 *            the DetailPanel displaying the current requirement
	 */
	public SaveTaskController(MakeTaskPanel view, Requirement model,
			DetailPanel parentView, JList tasks) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
		this.tasks = tasks;
	}

	/**
	 * Save a task to the server
	 */
	public void saveTask(Object[] tasks) {
		final String taskText = view.getTaskField().getText();
		final String taskName = view.getTaskName().getText();
		int taskEstimate;
		
		if(!view.getEstimate().getText().equals(""))
			taskEstimate = Integer.parseInt(view.getEstimate().getText());
		else
			taskEstimate = -1;
		
		int estimateSum = 0;
		
		for(Task altTask : model.getTasks())
			estimateSum = estimateSum + altTask.getEstimate();
		
		if (tasks == null) { // Creating a task!
			System.out.println("TASKS WAS NULL, ISSUE");
		} else if (tasks.length < 1) {
			//Task must have a name and description of at least one character
			if (taskText.length() > 0 && taskName.length() > 0) { 
				Task tempTask = new Task(taskName, taskText);
				if ((view.getUserAssigned().getSelectedItem() == ""))
					tempTask.setAssignedUser(null);
				else
					tempTask.setAssignedUser((String) view.getUserAssigned()
							.getSelectedItem());
				
				if(taskEstimate!=-1){
					if(taskEstimate+estimateSum <= model.getEstimate())
						tempTask.setEstimate(taskEstimate);
				}

				tempTask.setId(this.model.getTasks().size() + 1);
				this.model.addTask(tempTask);
				parentView.getTaskList().addElement(tempTask);
				view.getTaskName().setText("");
				view.getTaskField().setText("");
				view.getTaskField().requestFocusInWindow();
				// We want to save the task to the server immediately, but only
				// if the requirement hasn't been just created
				if (model.getName().length() > 0) { 
					// Save to requirement!
					SaveRequirementController controller = new SaveRequirementController(
							this.parentView);
					controller.SaveRequirement(model, false);
				}
			}
		} else {
			
			// Modifying tasks
			for (Object aTask : tasks) { 
				if (tasks.length == 1) { 
					// If only one is selected, edit the fields
					if (taskText.length() > 0 && taskName.length() > 0) {
						((Task) aTask).setName(view.getTaskName().getText());
						((Task) aTask).setDescription(view.getTaskField()
								.getText());
					}
					
					if ((view.getUserAssigned().getSelectedItem() == ""))
						((Task) aTask).setAssignedUser(null);
					else
						((Task) aTask).setAssignedUser((String) view
								.getUserAssigned().getSelectedItem());
					
					if(taskEstimate!=-1){
						if(taskEstimate+estimateSum-((Task) aTask).getEstimate() <= model.getEstimate())
							((Task) aTask).setEstimate(taskEstimate);
					}
					
				}
				// Check the completion status on the tasks
				((Task) aTask)
						.setCompleted(view.getTaskComplete().isSelected());
			}
			
			// Save to requirement!
			if (model.getName().length() > 0) { 
				SaveRequirementController controller = new SaveRequirementController(
						this.parentView);
				controller.SaveRequirement(model, false);
			}
			view.getTaskName().setText("");
			view.getTaskField().setText("");
			view.getTaskField().requestFocusInWindow();
		}
		
		this.tasks.clearSelection();
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
		view.getTaskField().setBackground(Color.white);
		view.getTaskName().setBackground(Color.white);
		view.getAddTask().setEnabled(false);

	}
}
