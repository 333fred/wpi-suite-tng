/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller for updating an existing defect
 */
public class SaveRequirementController {

	/** The notifier this class uses */
	private ISaveNotifier notifier;

	public SaveRequirementController(ISaveNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * Saves a requirement
	 * 
	 * @param toAdd
	 *            requirement that will be saved
	 * @param closeTab
	 *            tab that will be closed
	 */
	public void SaveRequirement(Requirement toAdd, boolean closeTab) {
		final RequestObserver requestObserver = new UpdateRequirementRequestObserver(
				notifier);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/requirement", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}
