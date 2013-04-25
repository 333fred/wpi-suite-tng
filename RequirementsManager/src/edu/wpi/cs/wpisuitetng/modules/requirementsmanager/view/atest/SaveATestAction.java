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
 * Action that calls the saveTest method in the SaveATestController
 * 
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
@SuppressWarnings ("serial")
public class SaveATestAction extends AbstractAction {
	
	private final SaveATestController controller;
	private final int[] selectedRows;
	
	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 */
	public SaveATestAction(final SaveATestController controller) {
		super("Save");
		this.controller = controller;
		selectedRows = new int[0];
	}
	
	/**
	 * Construct the action
	 * 
	 * @param controller
	 *            the controller to trigger
	 * @param selectedRows
	 *            object array of all of the aTests
	 */
	public SaveATestAction(final SaveATestController controller,
			final int[] selectedRows) {
		super("Save");
		this.controller = controller;
		this.selectedRows = selectedRows;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void actionPerformed(final ActionEvent e) {
		controller.saveaTest(selectedRows);
	}
	
}
