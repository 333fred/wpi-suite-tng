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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllPermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Retieves all permissions from the database
 */

public class RetrieveAllPermissionsController {

	/**
	 * Gets all current permissions from the server
	 */
	public void getAll() {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final RetrieveAllPermissionsRequestObserver observer = new RetrieveAllPermissionsRequestObserver();
		Request request = Network.getInstance().makeRequest(
				"requirementsmanager/permissionmodel", HttpMethod.GET);
		request.addObserver(observer);
		request.send();
	}

}
