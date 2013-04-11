/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Nick Massa, Matt Costi
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls
 * {@link edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task.RequirementsManager.tasks.SaveTaskController#savetask()}
 * 
 * @author Nick Massa, Matt Costi
 */
@SuppressWarnings("serial")
public class SaveTaskAction extends AbstractAction {
	public final SaveTaskController controller;
	public Object[] tasks;

	/**
	 * Construct the action
	 * 
	 * @param controller the controller to trigger
	 */
	public SaveTaskAction(SaveTaskController controller) {
		super("Save");
		this.controller = controller;
		this.tasks = new Object[0]; // Placeholder list
	}

	/**
	 * Construct the action
	 * 
	 * @param controller the controller to trigger
	 * @param objects object array of all of the tasks
	 */
	public SaveTaskAction(SaveTaskController controller, Object[] objects) {
		super("Save");
		this.controller = controller;
		this.tasks = objects;
	}

	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.saveTask(tasks);
	}

}
