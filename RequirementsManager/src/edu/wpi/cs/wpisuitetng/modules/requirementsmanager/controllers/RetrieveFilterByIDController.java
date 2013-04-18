/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveFilterByIDRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class RetrieveFilterByIDController {

	/**
	 * Retrieves a given filter from the server. The correct callback in
	 * RetriveFilterByIDRequestObserver is called upon success or failure
	 * 
	 * @param id
	 *            the id of the filter to be retrieved
	 */
	public void get(int id) {
		final RequestObserver requestObserver = new RetrieveFilterByIDRequestObserver(
				this);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmamager/filter", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

}
