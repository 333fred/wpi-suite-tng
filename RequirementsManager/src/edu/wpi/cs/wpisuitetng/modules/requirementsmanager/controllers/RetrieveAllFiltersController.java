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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllFiltersRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responsible for creating and sending a request for all filters
 * from the server
 */

public class RetrieveAllFiltersController {

	/**
	 * Retrieves all filters for the current user from the server. The correct
	 * callback in RetrieveAllFiltersRequestObserver is called upon success or
	 * failure
	 */
	public void getAll() {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final RequestObserver requestObserver = new RetrieveAllFiltersRequestObserver(
				this);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/filter", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

}
