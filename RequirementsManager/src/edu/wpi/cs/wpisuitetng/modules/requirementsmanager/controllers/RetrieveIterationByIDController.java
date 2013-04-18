/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveIterationByIDRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to retrieve a single requirement from server by id and return them
 * to the central table view
 */
public class RetrieveIterationByIDController {

	IRetreiveIterationByIDControllerNotifier notifier;

	public RetrieveIterationByIDController(
			IRetreiveIterationByIDControllerNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * Sends a request for all of the requirements
	 */
	public void get(int id) {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final RequestObserver requestObserver = new RetrieveIterationByIDRequestObserver(
				this);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/iteration/" + id, HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

	/**
	 * This method is called by the
	 * {@link RetrieveAllRequirementsRequestObserver} when the response is
	 * received
	 * 
	 * @param requirements
	 *            an array of requirements returned by the server
	 */
	public void receivedData(Iteration iteration) {
		notifier.receivedData(iteration);
	}

	/**
	 * This method is called by the
	 * {@link RetrieveAllRequirementsRequestObserver} when an error occurs
	 * retrieving the requirements from the server.
	 */
	public void errorReceivingData(String errMsg) {
		notifier.errorReceivingData(errMsg);
	}
}
