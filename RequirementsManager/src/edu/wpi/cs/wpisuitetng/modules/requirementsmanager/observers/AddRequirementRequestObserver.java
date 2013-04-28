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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
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
	
	/**
	 * Creates a new AddRequirementRequestObserver with the given detail panel
	 * to call back to
	 * 
	 * @param detailPanel
	 *            the detail panel to callback on success
	 */
	public AddRequirementRequestObserver(final DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		detailPanel.displaySaveError("Unable to complete request: "
				+ exception.getMessage());
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("Error: " + iReq.getResponse().getBody());
		detailPanel.displaySaveError("Received "
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
		
		RequirementDatabase.getInstance().add(
				Requirement.fromJSON(response.getBody()));
		
		// notify the controller
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				detailPanel.getMainTabController().closeCurrentTab();
			}
		});
	}
	
}
