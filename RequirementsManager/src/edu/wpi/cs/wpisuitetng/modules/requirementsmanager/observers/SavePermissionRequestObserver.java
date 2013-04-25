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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request observer for saving permissions
 */

public class SavePermissionRequestObserver implements RequestObserver {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		System.out.println("Failure saving permissions");
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("Error saving permissions");
		System.out.println("Error: " + iReq.getResponse().getBody());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final PermissionModel perm = PermissionModel.fromJSON(response
				.getBody());
		PermissionsDatabase.getInstance().add(perm);
		
	}
	
}
