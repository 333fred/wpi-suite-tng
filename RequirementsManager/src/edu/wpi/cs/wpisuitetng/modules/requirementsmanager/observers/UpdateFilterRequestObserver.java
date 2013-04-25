/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Request Observer responsible for handling success or failure of the
 * SaveFilterController
 */

public class UpdateFilterRequestObserver implements RequestObserver {
	
	private final ISaveNotifier notifier;
	
	/**
	 * Creates a request observer with the given controller as a callback
	 * 
	 * @param controller
	 *            the controller to callback
	 */
	public UpdateFilterRequestObserver(final ISaveNotifier notifier) {
		this.notifier = notifier;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		notifier.fail(exception);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		notifier.responseError(iReq.getResponse().getStatusCode(), iReq
				.getResponse().getStatusMessage());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		
		// put the saved filter into the database
		final ResponseModel response = iReq.getResponse();
		
		// if (response.getStatusCode() == 200) {
		final Filter filter = Filter.fromJSON(response.getBody());
		FilterDatabase.getInstance().add(filter);
		// }
		
		notifier.responseSuccess();
		
	}
	
}
