/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author 
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SimpleRetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;



public class SimpleRetrieveAllIterationsController {
	
	public SimpleRetrieveAllIterationsController() {
	}
	
	/**
	 * Sends a request for all of the iterations
	 */
	public void getAll() {	
		final RequestObserver requestObserver = new SimpleRetrieveAllIterationsRequestObserver();
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/iteration", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}
	
	/**
	 * This method is called by the {@link RetrieveAllIterationRequestObserver} when the
	 * response is received
	 * 
	 * @param iterations an array of iterations returned by the server
	 */
	public void receivedData(Iteration[] iterations){
	}
	
	/**
	 * This method is called by the {@link RetrieveAllIterationRequestObserver} when an
	 * error occurs retrieving the iterations from the server.
	 */
	public void errorReceivingData(String error) {
		//now update your view to show the error however you prefer
	}
}
