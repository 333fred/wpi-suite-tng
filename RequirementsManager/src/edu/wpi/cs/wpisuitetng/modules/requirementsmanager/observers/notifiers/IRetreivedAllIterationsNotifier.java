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

/**
 * Interface used for RetreiveAllIterations, and possibly elsewhere
 * Will be notified when data has been returned, or an error was returned
 */
public interface IRetreivedAllIterationsNotifier {
	/**
	 * Called when the iteration data has been retrieved from the server
	 * 
	 * @param iterations
	 *            The retrieved data
	 */

	public void receivedData(Iteration[] iterations);

	/**
	 * Called when a error was returned from the server, instead of data
	 * 
	 * @param RetrieveAllRequirementsRequestObserver
	 */

	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver);
}
