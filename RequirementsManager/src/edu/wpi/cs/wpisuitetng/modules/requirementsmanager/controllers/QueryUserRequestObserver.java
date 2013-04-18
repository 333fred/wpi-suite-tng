/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class QueryUserRequestObserver implements RequestObserver {

	private final AssigneePanel parentView;

	public QueryUserRequestObserver(AssigneePanel parentView) {
		this.parentView = parentView;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		// If the network hasn't been initialized, then this will fail, so
		// return
		if (Network.getInstance().isInitialized()) {
			return;
		}
		ResponseModel response = iReq.getResponse();

		final StringListModel users = StringListModel.fromJson(response
				.getBody());

		// notify the controller
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				parentView.setUnassignedUsersList(users);
			}
		});

	}

	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub

	}

}
