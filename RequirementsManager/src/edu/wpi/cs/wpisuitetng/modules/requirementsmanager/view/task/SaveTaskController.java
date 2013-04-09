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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * This controller handles saving requirement tasks to the server
 */
public class SaveTaskController {

	private final MakeTaskPanel view;
	private final Requirement model;
	private final DetailPanel parentView;

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
			DetailPanel parentView) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
	}

	/**
	 * Save a task to the server
	 */
	public void saveTask(Object[] tasks) {
		final String taskText = view.gettaskField().getText();
		final String taskName = view.gettaskName().getText();
		if (tasks == null) { // Creating a task!
			System.out.println("TASKS WAS NULL, ISSUE");
		} else if (tasks.length < 1) {
			if (taskText.length() > 0 && taskName.length() > 0) { // Task must
																	// have a
																	// name and
																	// description
																	// of at
																	// least 1
																	// char!
				Task tempTask = new Task(taskName, taskText);
				if ((view.getuserAssigned().getSelectedItem() == ""))
					tempTask.setAssignedUser(null);
				else
					tempTask.setAssignedUser((String) view.getuserAssigned()
							.getSelectedItem());
				tempTask.setId(this.model.getTasks().size() + 1);
				this.model.addTask(tempTask);
				parentView.getTaskList().addElement(tempTask);
				view.gettaskName().setText("");
				view.gettaskField().setText("");
				view.gettaskField().requestFocusInWindow();
				// We want to save the task to the server immediately, but only
				// if the requirement hasn't been just created
				if (model.getName().length() > 0) { // Save to requirement!
					SaveRequirementController controller = new SaveRequirementController(
							this.parentView);
					controller.SaveRequirement(model, false);
				}
			}
		} else {

			for (Object aTask : tasks) { // Modifying tasks!
				if (tasks.length == 1) { // If only one is selected, edit the
											// fields!
					if (taskText.length() > 0 && taskName.length() > 0) {
						((Task) aTask).setName(view.gettaskName().getText());
						((Task) aTask).setDescription(view.gettaskField()
								.getText());
					}
					if ((view.getuserAssigned().getSelectedItem() == ""))
						((Task) aTask).setAssignedUser(null);
					else
						((Task) aTask).setAssignedUser((String) view
								.getuserAssigned().getSelectedItem());
				}// Check the completion status on the tasks!
				((Task) aTask)
						.setCompleted(view.gettaskComplete().isSelected());
			}

			if (model.getName().length() > 0) { // Save to requirement!
				SaveRequirementController controller = new SaveRequirementController(
						this.parentView);
				controller.SaveRequirement(model, false);
			}
			view.gettaskName().setText("");
			view.gettaskField().setText("");
			view.gettaskField().requestFocusInWindow();
		}

	}
}
