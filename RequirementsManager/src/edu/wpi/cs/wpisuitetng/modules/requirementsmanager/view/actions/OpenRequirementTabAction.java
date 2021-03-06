/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Mitchell Caisse
 ********************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreiveRequirementByIDControllerNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Action to open a new Requirement tab when the server returns with an updated
 * copy of the requirement
 */

public class OpenRequirementTabAction implements
		IRetreiveRequirementByIDControllerNotifier {
	
	/** The tab controller to open the requirement tab on */
	private final MainTabController tabController;
	
	/** The old requirement incase updating fails */
	private final Requirement oldRequirement;
	
	/**
	 * Create a new OpenRequirementTabAction with the given tabController
	 * 
	 * @param tabController
	 *            the controller for this tab action
	 * @param oldRequirement
	 *            the requirement to open a tab for
	 */
	
	public OpenRequirementTabAction(final MainTabController tabController,
			final Requirement oldRequirement) {
		this.tabController = tabController;
		this.oldRequirement = oldRequirement;
	}
	
	/**
	 * Called if there was an error fetching the requirement from the sever In
	 * this case we open the old, possibly outdated requirement
	 * 
	 * @param errorMessage
	 *            The error message the server returned
	 */
	
	@Override
	public void errorReceivingData(final String errorMessage) {
		// we couldn't fetch the requirement, open old requirement
		openRequirementTab(oldRequirement);
		
		// print the error message
		System.out.println("Error fetching requirement from server: "
				+ errorMessage);
		
	}
	
	/**
	 * Opens a new tab to view/edit the given requirement
	 * 
	 * @param requirement
	 *            The requirement to view / edit
	 */
	
	public void openRequirementTab(final Requirement requirement) {
		tabController.addViewRequirementTab(requirement);
	}
	
	/**
	 * Called when the server returns a requirement In this case we open a copy
	 * of the updated requirement
	 * 
	 * @param requirement
	 *            The updated requirement returned from the server
	 */
	
	@Override
	public void receivedData(final Requirement requirement) {
		// we received the new requirement open the tab
		openRequirementTab(requirement);
		
	}
}
