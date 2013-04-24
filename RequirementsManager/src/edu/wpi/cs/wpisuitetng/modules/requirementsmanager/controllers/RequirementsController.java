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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle all server communication with Requirement objects.
 */

public class RequirementsController extends AbstractController<Requirement> {

	/**
	 * Creates a controller to send Requirement requests to the server
	 */
	public RequirementsController() {
		super("requirement");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Requirement model, RequestObserver observer) {
		if (isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type + "/" + model.getrUID(),
				HttpMethod.DELETE);
		request.addObserver(observer);
		request.send();
	}

}
