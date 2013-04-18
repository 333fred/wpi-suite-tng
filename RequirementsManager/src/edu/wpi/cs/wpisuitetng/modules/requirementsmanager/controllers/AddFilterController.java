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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddFilterRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * A controller responsible for adding filters to the server
 */

public class AddFilterController {

	/**
	 * Sends a filter to the server to be stored. The correct callback in
	 * AddFilterRequestObserver is called upon success or failure
	 * 
	 * @param toAdd
	 *            the filter to be stored
	 */
	public void addFilter(Filter toAdd) {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final RequestObserver requestObserver = new AddFilterRequestObserver(
				this);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementmanager/filter", HttpMethod.PUT);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}
