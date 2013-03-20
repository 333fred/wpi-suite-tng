/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * Controller to retrieve all requirements from server and return them to the central table view
 */
public class RetrieveAllRequirementsController {

	
	protected Requirement[] data;
	
	public RetrieveAllRequirementsController( /*view here?*/){
		//TODO: Implement connection with view
		//You will want to pass your view here
	}
	
	/**
	 * Sends a request for all of the requirements
	 */
	public void getAll() {	
		final RequestObserver requestObserver = new RetrieveAllRequirementsRequestObserver(this);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();
	}
	
	/**
	 * This method is called by the {@link RetrieveAllRequirementsRequestObserver} when the
	 * response is received
	 * 
	 * @param requirements an array of requirements returned by the server
	 */
	public void receivedData(Requirement[] requirements){
		this.data = requirements;
		//TODO: implement connection with view
		//You have the requirements, so here somehow update your view to display them
	}
	
	/**
	 * This method is called by the {@link RetrieveAllRequirementsRequestObserver} when an
	 * error occurs retrieving the requirements from the server.
	 */
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		//TODO: Implement the error display once a view is connected
	}
}
