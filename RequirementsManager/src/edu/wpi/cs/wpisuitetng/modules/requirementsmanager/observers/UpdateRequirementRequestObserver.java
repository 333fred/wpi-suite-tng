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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Controller for updating an existing requirement
 */
public class UpdateRequirementRequestObserver implements RequestObserver {
	
	/**
	 * The notifier this class calls when the requirement has been saved or
	 * errored
	 */
	private final ISaveNotifier notifier;
	
	/**
	 * Creates a new UpdateRequirementRequestObserver with the given notifier to
	 * callback to
	 * 
	 * @param notifier
	 *            the notifier callback for success or failure
	 */
	public UpdateRequirementRequestObserver(final ISaveNotifier notifier) {
		this.notifier = notifier;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		notifier.fail(exception);
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		notifier.responseError(iReq.getResponse().getStatusCode(), iReq
				.getResponse().getStatusMessage());
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
		
		final Requirement req = Requirement.fromJSON(response.getBody());
		RequirementDatabase.getInstance().add(req);
		
		notifier.responseSuccess();
		
		RequirementTableView.getInstance().refresh();
	}
	
}
