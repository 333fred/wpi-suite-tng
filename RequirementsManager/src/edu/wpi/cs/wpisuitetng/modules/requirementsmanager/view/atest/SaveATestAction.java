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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls
 * {@link edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.aTest.SaveATestController.aTests.SaveaTestController#saveaTest()}
 * 
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
@SuppressWarnings("serial")
public class SaveATestAction extends AbstractAction {
	public final SaveATestController controller;
	public Object[] aTests;

	/**
	 * Construct the action
	 * 
	 * @param controller the controller to trigger
	 */
	public SaveATestAction(SaveATestController controller) {
		super("Save");
		this.controller = controller;
		this.aTests = new Object[0]; // Placeholder list
	}

	/**
	 * Construct the action
	 * 
	 * @param controller the controller to trigger
	 * @param objects object array of all of the aTests
	 */
	public SaveATestAction(SaveATestController controller, Object[] objects) {
		super("Save");
		this.controller = controller;
		this.aTests = objects;
	}

	/*
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.saveaTest(aTests);
	}

}
