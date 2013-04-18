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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrievePermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to retrieve the Permissions for the current user
 */

public class RetrievePermissionsController {

	/**
	 * Gets the permissions for the current user
	 */
	public void get() {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final RetrievePermissionsRequestObserver observer = new RetrievePermissionsRequestObserver();
		Request request = Network.getInstance().makeRequest(
				"requirementsmanager/permissionmodel/user", HttpMethod.GET);
		request.addObserver(observer);
		request.send();
	}

}
