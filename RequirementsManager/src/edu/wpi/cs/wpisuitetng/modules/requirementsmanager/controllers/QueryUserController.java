package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class QueryUserController {

	public void getUsers() {
		System.out.println("Getting Called");
		final RequestObserver requestObserver = new QueryUserRequestObserver();
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/stringlistmodel", HttpMethod.POST);
		request.setBody(new StringListModel().toJSON());
		request.addObserver(requestObserver);
		request.send();
		System.out.println("Made Request");
	}

}
