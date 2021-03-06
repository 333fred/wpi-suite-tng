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
 *    @author Frederic Silberberg
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer for the request to get all iterations from the server.
 */
public class RetrieveAllIterationsRequestObserver implements RequestObserver {
	
	private final IRetreivedAllIterationsNotifier notifier;
	
	/**
	 * Creates a new RetrieveAllIterationsRequestObserver with the given
	 * notifier as a callback
	 * 
	 * @param notifier
	 *            the notifier to notify upon success
	 */
	public RetrieveAllIterationsRequestObserver(
			final IRetreivedAllIterationsNotifier notifier) {
		this.notifier = notifier;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		// TODO Auto-generated method stub
		notifier.errorReceivingData("Unable to complete request: "
				+ exception.getMessage());
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		// TODO Auto-generated method stub
		notifier.errorReceivingData("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		// cast observable to request
		final Request request = (Request) iReq;
		
		// get the response from the request
		final ResponseModel response = request.getResponse();
		
		if (response.getStatusCode() == 200) {
			// parse the response
			final Iteration[] iterations = Iteration.fromJSONArray(response
					.getBody());
			
			IterationDatabase.getInstance().set(Arrays.asList(iterations));
			
			// notify the controller
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					notifier.receivedData(iterations);
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
