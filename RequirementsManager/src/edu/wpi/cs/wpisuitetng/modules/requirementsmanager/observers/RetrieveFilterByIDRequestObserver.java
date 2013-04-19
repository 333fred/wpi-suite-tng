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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreiveRequirementByIDControllerNotifier;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

public class RetrieveFilterByIDRequestObserver implements RequestObserver {

	private IRetreiveRequirementByIDControllerNotifier notifier;

	/**
	 * Creates a Request Observer with the given notifier to call back to
	 * 
	 * @param notifier
	 *            the notifier to callback to
	 */
	public RetrieveFilterByIDRequestObserver(
			IRetreiveRequirementByIDControllerNotifier notifier) {
		this.notifier = notifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// TODO Auto-generated method stub

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
