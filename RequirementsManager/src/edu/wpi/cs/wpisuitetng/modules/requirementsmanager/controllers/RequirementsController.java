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

/**
 * Controller to handle all server communication with Requirement objects.
 */

public class RequirementsController extends AbstractController {

	/**
	 * Creates a controller to send Requirement requests to the server
	 */
	public RequirementsController() {
		super("requirement");
	}

}
