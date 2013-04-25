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

import java.util.Arrays;

import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetrieveAllFiltersNotifier;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer callback responsible for handling the response success or failure of
 * RetrieveAllFiltersController
 */

public class RetrieveAllFiltersRequestObserver implements RequestObserver {
	
	private final IRetrieveAllFiltersNotifier notifier;
	
	/**
	 * Creates a Request Observer with the given controller to call back to
	 * 
	 * @param notifier
	 *            the controller to callback to
	 */
	public RetrieveAllFiltersRequestObserver(
			final IRetrieveAllFiltersNotifier notifier) {
		this.notifier = notifier;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		// TODO Auto-generated method stub
		System.out.println("Failed to get filters from server");
		exception.printStackTrace();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		// TODO Auto-generated method stub
		System.out.println("Error receiving filters from server: "
				+ iReq.getResponse().getStatusCode() + " "
				+ iReq.getResponse().getStatusMessage());
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		
		if (response.getStatusCode() == 200) {
			final Filter[] filters = Filter.fromJSONArray(response.getBody());
			
			// notify the controller
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					FilterDatabase.getInstance().set(Arrays.asList(filters));
					notifier.receivedData(filters);
				}
			});
		}
		
	}
	
}
