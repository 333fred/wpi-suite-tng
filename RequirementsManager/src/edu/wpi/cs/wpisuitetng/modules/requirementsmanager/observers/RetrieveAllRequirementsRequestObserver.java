/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Jason Whitehouse
 *
 */
public class RetrieveAllRequirementsRequestObserver implements RequestObserver {
	
	/** The controller managing the request */
	private RetrieveAllRequirementsController controller;
	
	/**
	 * Construct the observer
	 * @param controller
	 */
	public RetrieveAllRequirementsRequestObserver (RetrieveAllRequirementsController controller){
		this.controller = controller;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		if (response.getStatusCode() == 200) {
			// parse the response				
			Requirement[] requirements = Requirement.fromJSONArray(response.getBody());

			RequirementDatabase.getInstance().setRequirements(Arrays.asList(requirements));
			
			// notify the controller
			controller.receivedData(requirements);
		}
		else {
			controller.errorReceivingData("Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
		}		
	}

	@Override
	public void responseError(IRequest iReq) {
		// an error occurred
		controller.errorReceivingData("Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		// an error occurred
		controller.errorReceivingData("Unable to complete request: " + exception.getMessage());
	}

}
