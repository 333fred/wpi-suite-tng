/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Team Swagasaurus
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request Observer responsible for handling success or failure of the
 * QueryUserRequestController
 */
public class QueryUserRequestObserver implements RequestObserver {
	
	private final AssigneePanel parentView;
	
	/**
	 * Creates a new QueryUserRequestObserver with the given AssigneePanel to
	 * callback to
	 * 
	 * @param parentView
	 *            the AssigneePanel to callback to on success
	 */
	public QueryUserRequestObserver(final AssigneePanel parentView) {
		this.parentView = parentView;
	}
	
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		System.out.println("User Query response Failure");
	}
	
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("User Query Response Error");
		
	}
	
	@Override
	public void responseSuccess(final IRequest iReq) {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		final ResponseModel response = iReq.getResponse();
		
		final StringListModel users = StringListModel.fromJSONArray(response
				.getBody())[0];
		
		// notify the controller
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				parentView.setUnassignedUsersList(users);
			}
		});
		
	}
	
}
