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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Jason Whitehouse Controller for updating an existing requirement
 */
public class UpdateRequirementRequestObserver implements RequestObserver {

	/**
	 * The notifier this class calls when the requirement has been saved or
	 * errored
	 */
	private ISaveNotifier notifier;

	public UpdateRequirementRequestObserver(ISaveNotifier notifier) {
		this.notifier = notifier;
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

		Requirement req = Requirement.fromJSON(response.getBody());
		RequirementDatabase.getInstance().add(req);

		notifier.responseSuccess();
		/*
		 * 
		 * if (this.closeTab) {
		 * this.detailPanel.getMainTabController().closeCurrentTab(); }
		 * 
		 * if (this.detailPanel != null) { detailPanel.logView.refresh(req);
		 * 
		 * if (this.closeTab) {
		 * this.detailPanel.getMainTabController().closeCurrentTab(); } }
		 */

		/*
		 * if (response.getStatusCode() == 200) { // parse the requirement from
		 * the body final Requirement requirement =
		 * Requirement.fromJSON(response.getBody());
		 * 
		 * // make sure the requirement isn't null if (requirement != null) {
		 * //success here! } else { //Display error } } else { //Display Error }
		 */

		RequirementTableView.getInstance().refresh();
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
		/*
		 * if (this.detailPanel != null) {
		 * this.detailPanel.displaySaveError("Received " +
		 * iReq.getResponse().getStatusCode() + " error from server: " +
		 * iReq.getResponse().getStatusMessage()); }
		 */
		notifier.responseError(iReq.getResponse().getStatusCode(), iReq
				.getResponse().getStatusMessage());
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
		/*
		 * if (this.detailPanel != null) {
		 * this.detailPanel.displaySaveError("Unable to complete request: " +
		 * exception.getMessage()); }
		 */
		notifier.fail(exception);
	}

}
