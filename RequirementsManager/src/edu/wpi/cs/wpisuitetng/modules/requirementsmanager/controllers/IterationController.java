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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle all server communication with Iteration objects.
 */

public class IterationController extends AbstractController<Iteration> {

	/**
	 * Creates a controller to send Iteration requests to the server
	 */
	public IterationController() {
		super("iteration");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Iteration model, RequestObserver observer) {
		if (isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type + "/" + model.getId(),
				HttpMethod.DELETE);
		request.addObserver(observer);
		request.send();
	}

}
