/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Jason Whitehouse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreiveIterationByIDControllerNotifier;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request observer for retrieving a single requirement from the server
 * by id
 */
public class RetrieveIterationByIDRequestObserver implements RequestObserver {
	
	/** The notifier managing the request */
	private final IRetreiveIterationByIDControllerNotifier notifier;
	
	/**
	 * Construct the observer
	 * 
	 * @param notifier
	 */
	public RetrieveIterationByIDRequestObserver(
			final IRetreiveIterationByIDControllerNotifier notifier) {
		this.notifier = notifier;
	}
	
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		// an error occurred
		notifier.errorReceivingData("Unable to complete request: "
				+ exception.getMessage());
	}
	
	@Override
	public void responseError(final IRequest iReq) {
		// an error occurred
		notifier.errorReceivingData("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
	}
	
	@Override
	public void responseSuccess(final IRequest iReq) {
		// cast observable to request
		final Request request = (Request) iReq;
		
		// get the response from the request
		final ResponseModel response = request.getResponse();
		
		if (response.getStatusCode() == 200) {
			// parse the response
			final Iteration[] iteration = Iteration.fromJSONArray(response
					.getBody());
			
			IterationDatabase.getInstance().add(iteration[0]);
			
			// notify the notifier
			// notify the notifier
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					notifier.receivedData(iteration[0]);
				}
			});
		} else {
			notifier.errorReceivingData("Received "
					+ iReq.getResponse().getStatusCode()
					+ " error from server: "
					+ iReq.getResponse().getStatusMessage());
		}
	}
	
}
