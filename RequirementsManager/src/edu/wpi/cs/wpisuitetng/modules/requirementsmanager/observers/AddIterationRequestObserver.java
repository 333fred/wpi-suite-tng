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
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * Request Observer responsible for handling success or failure of the
 * AddIterationController
 */

public class AddIterationRequestObserver implements RequestObserver {
	
	private final IterationView iterationView;
	
	/**
	 * Creates a new AddIterationRequestObserver with the given iteration view
	 * as the callback
	 * 
	 * @param iterationView
	 *            the view to callback upon success or failure
	 */
	public AddIterationRequestObserver(final IterationView iterationView) {
		this.iterationView = iterationView;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void fail(final IRequest iReq, final Exception exception) {
		iterationView.displaySaveError("Unable to complete request: "
				+ exception.getMessage());
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseError(final IRequest iReq) {
		System.out.println("Error: " + iReq.getResponse().getBody());
		iterationView.displaySaveError("Received "
				+ iReq.getResponse().getStatusCode() + " error from server: "
				+ iReq.getResponse().getStatusMessage());
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void responseSuccess(final IRequest iReq) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				iterationView.getMainTabController().closeCurrentTab();
			}
		});
	}
	
}
