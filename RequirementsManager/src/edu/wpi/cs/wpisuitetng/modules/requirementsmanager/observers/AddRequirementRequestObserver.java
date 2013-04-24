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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.DefaultSaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request Observer responsible for handling success or failure of the
 * AddRequirementController
 */

public class AddRequirementRequestObserver implements RequestObserver {

	DetailPanel detailPanel;

	public AddRequirementRequestObserver(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi
	 * .cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		RequirementDatabase.getInstance().add(
				Requirement.fromJSON(response.getBody()));

		IterationController iterationController = new IterationController();

		if (response.getStatusCode() == 201) {
			// parse the Requirement from the body
			final Requirement requirement = Requirement.fromJSON(response
					.getBody());

			// make sure the requirement isn't null
			if (requirement != null) {
				Iteration anIteration;
				try {
					anIteration = IterationDatabase.getInstance().get(
							-1);
					anIteration.addRequirement(requirement.getrUID());
					UpdateIterationRequestObserver observer = new UpdateIterationRequestObserver(
							new DefaultSaveNotifier());
					iterationController.save(anIteration, observer);
				} catch (IterationNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		// notify the controller
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				detailPanel.getMainTabController().closeCurrentTab();
			}
		});
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
		System.out.println("Error: " + iReq.getResponse().getBody());
		this.detailPanel.displaySaveError("Received "
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
		this.detailPanel.displaySaveError("Unable to complete request: "
				+ exception.getMessage());
	}

}
