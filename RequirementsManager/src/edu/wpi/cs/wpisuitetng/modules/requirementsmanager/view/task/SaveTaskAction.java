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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;

/**
 * Action that calls {@link edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task.RequirementsManager.tasks.SaveTaskController#savetask()}
 */
@SuppressWarnings("serial")
public class SaveTaskAction extends AbstractAction {
	public final SaveTaskController controller;
	public List tasks;

	/**
	 * Construct the action
	 * @param controller the controller to trigger
	 */
	public SaveTaskAction(SaveTaskController controller) {
		super("Save");
		this.controller = controller;
		this.tasks = new ArrayList<Task>(); //Placeholder list
	}
	
	public SaveTaskAction(SaveTaskController controller, List selectedValuesList) {
		super("Save");
		this.controller = controller;
		this.tasks = selectedValuesList;
	}

	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.saveTask(tasks);
	}

}
