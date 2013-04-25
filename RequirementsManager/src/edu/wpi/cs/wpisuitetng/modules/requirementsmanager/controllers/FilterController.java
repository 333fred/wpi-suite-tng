/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle all server communication with Filter objects.
 */

public class FilterController extends AbstractController<Filter> {
	
	/**
	 * Creates a controller to send Iteration requests to the server
	 */
	public FilterController() {
		super("filter");
	}
	
	@Override
	public void delete(final Filter f, final RequestObserver observer) {
		if (AbstractController.isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type + "/" + f.getId(),
				HttpMethod.DELETE);
		request.addObserver(observer);
		request.send();
	}
	
}
