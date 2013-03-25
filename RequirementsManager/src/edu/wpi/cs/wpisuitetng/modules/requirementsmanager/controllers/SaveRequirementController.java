/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse
 * Controller for updating an existing defect
 */
public class SaveRequirementController {
	
	DetailPanel detailPanel;
	
	public SaveRequirementController (DetailPanel detailPanel){
		this.detailPanel = detailPanel;
	}
	
	public void SaveRequirement(Requirement toAdd, boolean closeTab) {
		final RequestObserver requestObserver = new UpdateRequirementRequestObserver(detailPanel,closeTab);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/requirement", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}


}
