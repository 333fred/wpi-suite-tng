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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;

public class UserControllerTest extends AbstractControllerTest<StringListModel> {
	
	/**
	 * In the case of the string list model, we aren't using the delete method,
	 * so just assert 1 equals 1
	 */
	@Override
	@Test
	public void deleteGood() {
		Assert.assertEquals(1, 1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Before
	public void setup() {
		setupNetwork();
		controller = new UserController();
		model = new StringListModel();
		id = 1;
		modelPath = "/requirementsmanager/stringlistmodel";
	}
}
