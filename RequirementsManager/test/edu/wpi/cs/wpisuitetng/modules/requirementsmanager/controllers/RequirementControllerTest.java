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

import org.junit.Before;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Implementation of the abstract controller
 */

public class RequirementControllerTest extends
		AbstractControllerTest<Requirement> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setup() {
		setupNetwork();
		controller = new RequirementsController();
		model = new Requirement();
		model.setName("Test");
		model.setrUID(1);
		id = model.getrUID();
		modelPath = "/requirementsmanager/requirement";
	}
}
