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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SimpleRetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class SimpleRetrieveAllRequirementsController {

	public SimpleRetrieveAllRequirementsController() {
	}

	/**
	 * Sends a request for all of the requirements
	 */
	public void getAll() {
		final RequestObserver requestObserver = new SimpleRetrieveAllRequirementsRequestObserver();
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/requirement", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

}
