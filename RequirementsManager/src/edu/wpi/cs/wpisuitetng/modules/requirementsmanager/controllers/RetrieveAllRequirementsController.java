/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *    Mitchell Caisse
 *******************************************************************************/package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * Controller to retrieve all requirements from server and return them to the central table view
 */
public class RetrieveAllRequirementsController {
	
	protected Requirement[] data;
	private IReceivedAllRequirementNotifier notifier;
	
	public RetrieveAllRequirementsController(IReceivedAllRequirementNotifier notifier){
		this.notifier = notifier;
	}
	
	/**
	 * Sends a request for all of the requirements
	 */
	public void getAll() {	
		final RequestObserver requestObserver = new RetrieveAllRequirementsRequestObserver(this);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}
	
	/**
	 * This method is called by the {@link RetrieveAllRequirementsRequestObserver} when the
	 * response is received
	 * 
	 * @param requirements an array of requirements returned by the server
	 */
	public void receivedData(Requirement[] requirements){
		notifier.receivedData(requirements);
	}
	
	/**
	 * This method is called by the {@link RetrieveAllRequirementsRequestObserver} when an
	 * error occurs retrieving the requirements from the server.
	 */
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		notifier.errorReceivingData(RetrieveAllRequirementsRequestObserver);
	}
}
