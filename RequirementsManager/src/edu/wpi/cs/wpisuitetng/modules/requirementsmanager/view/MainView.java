/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Shell class to call detail views from main Requirement GUI
 */
public class MainView extends JPanel {

	Requirement requirement;
	
	/** The panel containing the detail view */
	private DetailPanel detailView;
	
	/**
	 * Construct the panel.
	 * @param boardModel 
	 */
	public MainView(DetailPanel detailView,Requirement requirement) {
		this.requirement = requirement;
		// Add the board panel to this view
		//detailView = new DetailPanel(requirement);
		//add(detailView);
	}
}
