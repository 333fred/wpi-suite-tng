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

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
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
	 * @param view the MakeTaskPanel containing the task field
	 * @param model the requirement to which tasks are being added
	 * @param parentView the DetailPanel displaying the current requirement
	 */
	public SaveTaskController(MakeTaskPanel view, Requirement model, DetailPanel parentView) {
		this.view = view; 
		this.model = model;
		this.parentView = parentView;
	}
	
	/**
	 * Save a task to the server
	 */
	public void savetask() {
		final String taskText = view.gettaskField().getText();
		if (taskText.length() > 0) {
			this.model.addTask(new Task(taskText, ConfigManager.getConfig().getUserName()));
			parentView.getTaskList().addElement(this.model.getTasks().get(this.model.getTasks().size()-1));
			view.gettaskField().setText("");
			view.gettaskField().requestFocusInWindow();
			
			//We want to save the task to the server immediately, but only if the requirement hasn't been just created
			if (model.getName().length() > 0) {
				SaveRequirementController controller = new SaveRequirementController(this.parentView);
				controller.SaveRequirement(model,false);
			}
			
			
		}
	}
}
