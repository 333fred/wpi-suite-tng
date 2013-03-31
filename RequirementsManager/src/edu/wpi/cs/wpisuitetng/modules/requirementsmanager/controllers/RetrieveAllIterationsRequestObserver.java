/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import java.util.Date;
import java.util.LinkedList;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * @author Jason Whitehouse
 *
 * Observer for the request to get all iterations from the server. For now,
 * it just returns a standard dummy list of iterations until we are assigned
 * the story for iteration management
 * 
 */
public class RetrieveAllIterationsRequestObserver implements RequestObserver {
	
	private Iteration[] testIterations; //List of iterations retrieved
	private RetrieveIterationsController controller; //Controller for callback

	public RetrieveAllIterationsRequestObserver (RetrieveIterationsController controller){
		this.controller = controller;
		//In a normal observer we would stop here
		//But for now we will just instantly callback with our dummy list of 4
		
		//create dummy list
		testIterations = new Iteration[4];
		testIterations[0] = new Iteration("",new Date(), new Date(), 1);
		testIterations[1] = new Iteration("",new Date(), new Date(), 2);
		testIterations[2] = new Iteration("",new Date(), new Date(), 3);
		testIterations[3] = new Iteration("",new Date(), new Date(), 4);
		
		//Now callback
		controller.receivedData(testIterations);
	}
	
	
	//TODO: implement server connection once required

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub

	}

}
