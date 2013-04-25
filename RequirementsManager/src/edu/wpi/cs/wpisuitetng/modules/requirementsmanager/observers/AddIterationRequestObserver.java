/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request Observer responsible for handling success or failure of the
 * AddIterationController
 */

public class AddIterationRequestObserver implements RequestObserver {
	
	private final IterationView iterationView;
	
	public AddIterationRequestObserver(final IterationView iterationView) {
		this.iterationView = iterationView;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		iterationView.displaySaveError("Unable to complete request: "
				+ exception.getMessage());
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("Error: " + iReq.getResponse().getBody());
		iterationView.displaySaveError("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		// cast observable to a Request
		final Request request = (Request) iReq;
		
		// get the response from the request
		final ResponseModel response = request.getResponse();
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				iterationView.getMainTabController().closeCurrentTab();
			}
		});		
	}
	
}
