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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * A mock request observer simply stores the request and an enum indicating
 * success, error, or failure
 */

public class MockRequestObserver implements RequestObserver {

	/**
	 * Enum to show the status of the response
	 */
	public enum Response {
		SUCCESS, ERROR, FAILURE, NONE;
	}

	private Response response;
	private IRequest request;

	/**
	 * Creates a new mock request observer, with the enum set to none
	 */
	public MockRequestObserver() {
		response = Response.NONE;
		request = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		response = Response.SUCCESS;
		request = iReq;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseError(IRequest iReq) {
		response = Response.ERROR;
		request = iReq;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		response = Response.FAILURE;
		request = iReq;
	}

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @return the request
	 */
	public IRequest getRequest() {
		return request;
	}
}
