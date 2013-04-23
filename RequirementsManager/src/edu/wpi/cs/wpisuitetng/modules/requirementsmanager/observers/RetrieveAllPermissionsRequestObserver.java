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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class RetrieveAllPermissionsRequestObserver implements RequestObserver {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		
		// TODO Auto-generated method stub
		// Get the response and call the singleton initializer
		ResponseModel response = iReq.getResponse();
		if (response.getStatusCode() == 200) {
		// parse the response
		final PermissionModel[] permissions = PermissionModel.fromJSONArray(response
				.getBody());

		ToolbarView.getInstance().refreshPermissions();
		
		PermissionsDatabase.getInstance().set(
				Arrays.asList(permissions));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.out.println("Permission Request Error");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.out.println("Permission Request Failure");

	}

}
