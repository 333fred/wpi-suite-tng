/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllUsersRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 *
 * Controller for retrieving all users for the active project form the server
 */
public class RetrieveAllUsersController {
	//view here
	private AssigneePanel view;
	
	public RetrieveAllUsersController(AssigneePanel view){
		this.view = view;
	}
	
	/**
	 * Function to make asynchronous call to retrieve users from the database
	 */
	public void GetUsers(){
		final RequestObserver requestObserver = new RetrieveAllUsersRequestObserver(this);
		Request request;
		request = Network.getInstance().makeRequest("core/user", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

	/**
	 * Called by the [@link RetrieveAllUsersRequestObserver] when there is an error receiving the data
	 * @param errorMsg the message of the error
	 */
	public void errorReceivingData(String errorMsg) {		
		System.out.println(errorMsg);
	}

	/**
	 * Called by the [@link RetrieveAllUsersRequestObserver] when data successfully retrieved
	 * @param users the users retrieved
	 */
	public void receivedData(User[] users) {		
	//	view.changeUsers(users);
	}
}
