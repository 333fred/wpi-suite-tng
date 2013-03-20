/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * Controller used to add a requirement to the database
 */
public class AddRequirementController {

	//view here
	
	public AddRequirementController( /*view here?*/){
		//TODO: Implement connection with view
		//Pass your view here
	}
	
	public void AddRequirement(Requirement toAdd) {
		final RequestObserver requestObserver = new AddRequirementRequestObserver(this); //you will probably want to pass your view to the observer as well
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.PUT);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}
}
