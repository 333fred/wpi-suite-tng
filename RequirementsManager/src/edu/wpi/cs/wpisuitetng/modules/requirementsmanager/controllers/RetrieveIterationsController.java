/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * 
 * This is the controller for retrieving all iterations for the current project on the server
 * 
 * TODO implement server back-end for this. Right now it is just a dummy that returns a premade list of iterations
 */
public class RetrieveIterationsController {
	//put view here
	
	public RetrieveIterationsController(/* view here*/) {
		//view
	}
	
	/**
	 * Sends a request for all of the iterations
	 */
	public void getAll() {	
		final RequestObserver requestObserver = new RetrieveAllIterationsRequestObserver(this);
		/* TODO: Implement actual network request
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.GET);
		request.addObserver(requestObserver);
		request.send();*/
	}
	
	/**
	 * This method is called by the {@link RetrieveAllIterationRequestObserver} when the
	 * response is received
	 * 
	 * @param requirements an array of requirements returned by the server
	 */
	public void receivedData(Iteration[] requirements){
		//You have the iterations, so here somehow update your view to display them
	}
	
	/**
	 * This method is called by the {@link RetrieveAllIterationRequestObserver} when an
	 * error occurs retrieving the requirements from the server.
	 */
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		//now update your view to show the error however you prefer
	}
}
