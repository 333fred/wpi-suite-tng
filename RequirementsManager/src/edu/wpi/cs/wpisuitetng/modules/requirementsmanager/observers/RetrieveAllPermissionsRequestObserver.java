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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer for the request to get all user permissions from the server.
 */
public class RetrieveAllPermissionsRequestObserver implements RequestObserver {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		System.out.println("Permission Request Failure");
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("Permission Request Error");
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		
		// TODO Auto-generated method stub
		// Get the response and call the singleton initializer
		final ResponseModel response = iReq.getResponse();
		if (response.getStatusCode() == 200) {
			// parse the response
			final PermissionModel[] permissions = PermissionModel
					.fromJSONArray(response.getBody());
			
			ToolbarView.getInstance().refreshPermissions();
			
			PermissionsDatabase.getInstance().set(Arrays.asList(permissions));
		}
		
	}
	
}
