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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevels;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SavePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller that saves permissions to the server
 */

public class SavePermissionController {

	/**
	 * Saves the given permissions to the server
	 * 
	 * @param toAdd
	 *            the permission to save/update
	 */
	public void save(PermissionModel toAdd) throws UnauthorizedException {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		// Throw an exception if the user can't save permissions
		if (PermissionModel.getInstance().getPermission() != UserPermissionLevels.ADMIN) {
			throw new UnauthorizedException(UserPermissionLevels.ADMIN,
					PermissionModel.getInstance().getPermission());
		}
		final RequestObserver requestObserver = new SavePermissionRequestObserver();
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/permissionmodel", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}
