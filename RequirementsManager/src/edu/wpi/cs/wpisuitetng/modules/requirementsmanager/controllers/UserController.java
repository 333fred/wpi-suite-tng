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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;

/**
 * Manages retrieving users from the server
 */

public class UserController extends AbstractController<StringListModel> {

	/**
	 * Creates a controller to retrieve users from the server
	 */
	public UserController() {
		super("stringlistmodel");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(StringListModel model, RequestObserver observer) {
		// Nothing is stored on the server, do nothing.
	}

}
