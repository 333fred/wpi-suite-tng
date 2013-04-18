/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Steven Kordell
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller for updating an existing defect
 */
public class SaveIterationController {

	private ISaveNotifier notifier;

	public SaveIterationController(ISaveNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * Saves a iteration
	 * 
	 * @param toAdd
	 *            iteration that will be saved
	 */
	public void saveIteration(Iteration toAdd) {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final RequestObserver requestObserver = new UpdateIterationRequestObserver(
				notifier);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/iteration", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}
