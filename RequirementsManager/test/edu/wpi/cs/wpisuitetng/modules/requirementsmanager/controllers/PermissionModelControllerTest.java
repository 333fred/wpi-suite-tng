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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockRequest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Implementation of the abstract controller test for permission models
 */

public class PermissionModelControllerTest extends
		AbstractControllerTest<PermissionModel> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setup() {
		setupNetwork();
		controller = new PermissionModelController();
		model = new PermissionModel();
		User u = new User("Fred", "fred", "test", 1);
		model.setUser(u);
		id = 1; // Permission Models have no id
		modelPath = "/requirementsmanager/permissionmodel";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Test
	public void deleteGood() {
		setupNetworkConfig();
		controller.delete(model, observer);
		MockRequest request = network.getLastRequestMade();
		assertEquals(request.getUrl().toString(), netConfig + modelPath + "/"
				+ model.getUser().getName());
		assertEquals(request.getBody(), null);
		assertEquals(request.getHttpMethod(), HttpMethod.DELETE);
	}

}
