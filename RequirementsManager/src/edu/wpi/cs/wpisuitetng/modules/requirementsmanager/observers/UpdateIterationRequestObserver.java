/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Steven Kordell
 *    @author Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.StatView;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Controller for updating an existing Iteration
 */
public class UpdateIterationRequestObserver implements RequestObserver {
	
	private final ISaveNotifier notifier;
	
	public UpdateIterationRequestObserver(final ISaveNotifier notifier) {
		this.notifier = notifier;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng
	 * .network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		notifier.fail(exception);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.
	 * cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(final IRequest iReq) {
		notifier.responseError(iReq.getResponse().getStatusCode(), iReq
				.getResponse().getStatusMessage());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi
	 * .cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		
		// cast observable to a Request
		final Request request = (Request) iReq;
		
		// get the response from the request
		final ResponseModel response = request.getResponse();
		
		final Iteration req = Iteration.fromJSON(response.getBody());
		IterationDatabase.getInstance().add(req);
		
		notifier.responseSuccess();
		
		StatView.getInstance().updateChart();
		
		// detailPanel.logView.refresh(req);
		
		/*
		 * if (response.getStatusCgiode() == 200) { // parse the Iteration from
		 * the body final Iteration Iteration =
		 * Iteration.fromJSON(response.getBody());
		 * // make sure the Iteration isn't null if (Iteration != null) {
		 * //success here! } else { //Display error } } else { //Display Error }
		 */
	}
	
}
