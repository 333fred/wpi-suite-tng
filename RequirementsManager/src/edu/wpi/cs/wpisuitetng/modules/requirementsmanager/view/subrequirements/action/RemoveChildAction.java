/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Nick, Matt
 *    
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.contoller.RemoveChildController;

/**
 * Action that calls saveChild in the controller
 */
@SuppressWarnings ("serial")
public class RemoveChildAction extends AbstractAction {
	
	private final RemoveChildController controller;
	
	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 */
	public RemoveChildAction(final RemoveChildController controller) {
		super("Remove Child");
		this.controller = controller;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		controller.saveChild();
	}
	
}
