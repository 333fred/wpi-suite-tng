/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllFiltersController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observer callback responsible for handling the response success or failure of
 * RetrieveAllFiltersController
 */

public class RetrieveAllFiltersRequestObserver implements RequestObserver {

	private RetrieveAllFiltersController controller;

	/**
	 * Creates a Request Observer with the given controller to call back to
	 * 
	 * @param controller
	 *            the controller to callback to
	 */
	public RetrieveAllFiltersRequestObserver(
			RetrieveAllFiltersController controller) {
		this.controller = controller;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		ResponseModel response = iReq.getResponse();

		if (response.getStatusCode() == 200) {
			Filter[] filters = Filter.fromJSONArray(iReq.getBody());

			FilterDatabase.getInstance().setFilters(Arrays.asList(filters));
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(IRequest iReq) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		// TODO Auto-generated method stub

	}

}
