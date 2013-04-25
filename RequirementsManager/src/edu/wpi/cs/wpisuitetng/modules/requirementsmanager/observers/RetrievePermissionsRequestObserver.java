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
 *    @author Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.PermissionToolbarPane;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer for the request to get a single user's permissions from the server
 */
public class RetrievePermissionsRequestObserver implements RequestObserver {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		// Get the response and call the singleton initializer
		final ResponseModel response = iReq.getResponse();
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					PermissionModel.fromJSONSingleton(response.getBody());
					PermissionToolbarPane.getInstance().refreshPermission();
				} catch (final IllegalThreadStateException ex) {
					ex.printStackTrace();
				}
			}
		});
		
	}
	
}
