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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class QueryUserController {

	private AssigneePanel parentView;

	public QueryUserController(AssigneePanel parentView) {
		this.parentView = parentView;
	}
	
	public void getUsers() {
		final RequestObserver requestObserver = new QueryUserRequestObserver(parentView);
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/stringlistmodel", HttpMethod.POST);
		request.setBody(new StringListModel().toJSON());
		request.addObserver(requestObserver);
		request.send();
	}

}
