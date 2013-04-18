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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateFilterRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to save a given Filter on the server
 */

public class SaveFilterController {

	private ISaveNotifier notifier;

	public SaveFilterController(ISaveNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * Saves a filter
	 * 
	 * @param toAdd
	 *            the filter to save
	 */
	public void saveFilter(Filter toAdd) {
		final RequestObserver requestObserver = new UpdateFilterRequestObserver(
				notifier);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/filter", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}
