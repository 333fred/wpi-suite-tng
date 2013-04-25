/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Interface used for RetreiveRequirementsByIDController, and possibly elsewhere
 * Will be notified when data has been returned, or an error was returned
 */
public interface IRetreiveRequirementByIDControllerNotifier {
	
	/**
	 * Called when a error was returned from the server, instead of data
	 * 
	 * @param errorMessage
	 */
	
	public void errorReceivingData(String errorMessage);
	
	/**
	 * Called to retrieve a specific requirement from the server by ID number
	 * 
	 * @param requirement
	 *            The requirement
	 */
	
	public void receivedData(Requirement requirement);
	
}
