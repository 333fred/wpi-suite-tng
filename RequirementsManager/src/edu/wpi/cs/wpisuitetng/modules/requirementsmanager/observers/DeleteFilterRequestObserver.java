/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

public class DeleteFilterRequestObserver implements RequestObserver {

	/** the notifier to notify of the response received */
	private ISaveNotifier notifier;
	
	/** The filter to delete if this responds sucessfully */
	private Filter toDelete;

	/**
	 * Creates a request observer with the given controller as a callback
	 * 
	 * @param controller
	 *            the controller to callback
	 */
	public DeleteFilterRequestObserver(ISaveNotifier notifier, Filter toDelete) {
		this.notifier = notifier;
		this.toDelete = toDelete;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		//we cannot remove filter from the DB, it returns success. but makes sense		
		
		FilterDatabase.getInstance().remove(toDelete);
		
		notifier.responseSuccess();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(IRequest iReq) {
		notifier.responseError(iReq.getResponse().getStatusCode(), iReq
				.getResponse().getStatusMessage());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		notifier.fail(exception);

	}

}
