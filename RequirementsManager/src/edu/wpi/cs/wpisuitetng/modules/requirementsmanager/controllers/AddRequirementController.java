/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *******************************************************************************//**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller used to add a requirement to the database
 * 
 * @author Jason Whitehouse 
 */
public class AddRequirementController {

	DetailPanel detailPanel;

	public AddRequirementController(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	/**
	 * Adds a requirement to the database
	 * 
	 * @param toAdd
	 *            requirement that will be added
	 */
	public void AddRequirement(Requirement toAdd) {
		final RequestObserver requestObserver = new AddRequirementRequestObserver(
				this, detailPanel); // you will probably want to pass your view
									// to the observer as well
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/requirement", HttpMethod.PUT);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}
}
