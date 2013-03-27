/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllUsersController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Jason Whitehouse
 *
 * Request observer for retrieving all users for the active project from the server
 */
public class RetrieveAllUsersRequestObserver implements RequestObserver {

	private RetrieveAllUsersController controller;
	
	/**
	 * Constructor for the Request Observer
	 * @param controller the controller we wil callback
	 */
	public RetrieveAllUsersRequestObserver (RetrieveAllUsersController controller) {
		this.controller = controller;
	}
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		if (response.getStatusCode() == 200) {
			// parse the response
			//User[] users = User.fromJSONArray(response.getBody());

			// notify the controller
			//controller.receivedData(users);
		} else {
			controller.errorReceivingData("Received " + iReq.getResponse().getStatusCode() + " error from server: "+ iReq.getResponse().getStatusMessage());
		}

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub
		controller.errorReceivingData("Received " + iReq.getResponse().getStatusCode() + " error from server: "+ iReq.getResponse().getStatusMessage());

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub
		controller.errorReceivingData("Received " + iReq.getResponse().getStatusCode() + " error from server: "+ iReq.getResponse().getStatusMessage());

	}

}
