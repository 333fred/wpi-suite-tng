package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SimpleRetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class SimpleRetrieveAllRequirementsController {
	
	public SimpleRetrieveAllRequirementsController() {
	}
	
	/**
	 * Sends a request for all of the requirements
	 */
	public void getAll() {	
		final RequestObserver requestObserver = new SimpleRetrieveAllRequirementsRequestObserver();
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}

}
