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
	
	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng
	 * .network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		iterationView.displaySaveError("Unable to complete request: "
				+ exception.getMessage());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.
	 * cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("Error: " + iReq.getResponse().getBody());
		iterationView.displaySaveError("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
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
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				iterationView.getMainTabController().closeCurrentTab();
			}
		});
		/*
		 * if (response.getStatusCode() == 201) { // parse the Requirement from
		 * the body final Requirement requirement =
		 * Requirement.fromJSON(response .getBody());
		 * // make sure the requirement isn't null if (requirement != null) {
		 * /omething with the requirement if wanted
		 * singUtilities.invokeLater(new Runnable() {
		 * @Override public void run() { ((DefectPanel) view.getDefectPanel())
		 * .updateModel(defect); view.setEditModeDescriptors(defect); } });
		 * // JOptionPane.showMessageDialog(detailPanel, "SUCCESS","SUCCESS",
		 * JOptionPane.OK_OPTION); } else {
		 * //Display error in view... here's how defecttracker does it:
		 * JOptionPane.showMessageDialog(detailPanel,
		 * "Unable to parse defect received from server.", "Save Defect Error",
		 * JOptionPane.ERROR_MESSAGE);
		 * } } else { /* Display error in view... here's how defecttracker does
		 * it: JOptionPane.showMessageDialog(view, "Received " +
		 * iReq.getResponse().getStatusCode() + " status from server: " +
		 * iReq.getResponse().getStatusMessage(), "Save Defect Error",
		 * JOptionPane.ERROR_MESSAGE);
		 * }
		 */
		
	}
	
}
