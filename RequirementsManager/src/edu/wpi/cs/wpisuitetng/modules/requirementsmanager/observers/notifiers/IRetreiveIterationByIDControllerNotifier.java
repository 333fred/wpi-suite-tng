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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public interface IRetreiveIterationByIDControllerNotifier {

	/**
	 * Called to retrieve a specific iteration from the server by its ID number
	 * 
	 * @param iteration
	 *            The iteration
	 */

	public void receivedData(Iteration iteration);

	/**
	 * Called when a error was returned from the server, instead of data
	 * 
	 * @param errorMessage
	 */

	public void errorReceivingData(String errorMessage);

}
