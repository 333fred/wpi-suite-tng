/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   @author Frederic Silberberg
 *   @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * Request Observer responsible for handling success or failure of the
 * DeleteFilterRequestController
 */
public class DeleteFilterRequestObserver implements RequestObserver {
	
	/** the notifier to notify of the response received */
	private final ISaveNotifier notifier;
	
	/** The filter to delete if this responds sucessfully */
	private final Filter toDelete;
	
	/**
	 * Creates a request observer with the given controller as a callback
	 * 
	 * @param controller
	 *            the controller to callback
	 */
	public DeleteFilterRequestObserver(final ISaveNotifier notifier,
			final Filter toDelete) {
		this.notifier = notifier;
		this.toDelete = toDelete;
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
		// we cannot remove filter from the DB, it returns success. but makes
		// sense
		
		FilterDatabase.getInstance().remove(toDelete);
		
		notifier.responseSuccess();
	}
	
}
