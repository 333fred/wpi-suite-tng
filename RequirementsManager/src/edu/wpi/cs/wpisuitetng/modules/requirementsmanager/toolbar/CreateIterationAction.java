/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Alex Gorowara
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Action invoked upon use of the Create Iteration key
 * Heavily adapted from CreateDefectAction in the DefectTracker module
 * 
 * @author Alex Gorowara 
 
 * Action that creates a new Iteration 
 */
@SuppressWarnings("serial")
public class CreateIterationAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a CreateIterationAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public CreateIterationAction(MainTabController controller) {
		super("Create Iteration");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_I);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addCreateIterationTab();
	}

}
