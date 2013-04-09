/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Jason Whitehouse, Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * This is the controller for retrieving all iterations for the current project
 * on the server
 */
public class RetrieveAllIterationsController {

	private IRetreivedAllIterationsNotifier notifier;

	public RetrieveAllIterationsController(
			IRetreivedAllIterationsNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * Sends a request for all of the iterations
	 */
	public void getAll() {
		final RequestObserver requestObserver = new RetrieveAllIterationsRequestObserver(
				this);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/iteration", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

	/**
	 * This method is called by the {@link RetrieveAllIterationRequestObserver}
	 * when the response is received
	 * 
	 * @param iterations
	 *            an array of iterations returned by the server
	 */
	public void receivedData(Iteration[] iterations) {
		notifier.receivedData(iterations);
	}

	/**
	 * This method is called by the {@link RetrieveAllIterationRequestObserver}
	 * when an error occurs retrieving the iterations from the server.
	 */
	public void errorReceivingData(String error) {
		notifier.errorReceivingData(error);
	}
}
