/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public interface IRetreiveIterationByIDControllerNotifier {

	/**
	 * Called when the requirement has been received by the server
	 * 
	 * @param requirement
	 *            The received data
	 */

	public void receivedData(Iteration iteration);

	/**
	 * Called when a error was returned from the server, instead of data
	 * 
	 * @param errorMessage
	 */

	public void errorReceivingData(String errorMessage);

}
