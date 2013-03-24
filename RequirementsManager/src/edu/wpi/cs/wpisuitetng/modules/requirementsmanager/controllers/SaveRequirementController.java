/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * Controller for updating an existing defect
 */
public class SaveRequirementController {
	//add view here
	
	public SaveRequirementController (/*view*/){
		//this.view = view
	}
	
	public void SaveRequirement(Requirement toAdd) {
		final RequestObserver requestObserver = new UpdateRequirementRequestObserver(/*,view*/);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}


}
