/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Fredric Silberberg
 * 
 *         Observer for the request to get all iterations from the server.
 * 
 */
public class RetrieveAllIterationsRequestObserver implements RequestObserver {

	private RetrieveAllIterationsController controller; // Controller for
														// callback

	public RetrieveAllIterationsRequestObserver(
			RetrieveAllIterationsController controller) {
		this.controller = controller;
	}

	// TODO: implement server connection once required

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi
	 * .cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		if (response.getStatusCode() == 200) {
			// parse the response
			final Iteration[] iterations = Iteration
					.fromJSONArray(response.getBody());

			IterationDatabase.getInstance().setIterations(
					Arrays.asList(iterations));

			// notify the controller
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					controller.receivedData(iterations);
				}
			});
		} else {
			controller.errorReceivingData("Received "
					+ iReq.getResponse().getStatusCode()
					+ " error from server: "
					+ iReq.getResponse().getStatusMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.
	 * cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub
		controller.errorReceivingData("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng
	 * .network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub
		controller.errorReceivingData("Unable to complete request: "
				+ exception.getMessage());
	}

}
