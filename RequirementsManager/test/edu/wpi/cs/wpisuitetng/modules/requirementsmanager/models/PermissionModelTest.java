/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

public class PermissionModelTest {
	
	PermissionModel model;
	User u;
	static boolean run = false;
	
	@Before
	public void setup() {
		u = new User("u1", "u1", "p1", 1);
		model = new PermissionModel();
		if (!run) {
			run = true;
			// This needs to run first, as it is a singleton
			assertEquals(PermissionModel.getInstance().getUser(), null);
			assertEquals(PermissionModel.getPermissionStatic(), UserPermissionLevel.OBSERVE);
			assertEquals(PermissionModel.getInstance().getId(), -1);
		}
	}
	
	@Test
	public void testUserMethods() {
		PermissionModel.setUserStatic(u);
		assertEquals(PermissionModel.getUserStatic(), u);
		assertEquals(PermissionModel.getInstance().getUser(), u);
	}
	
	@Test
	public void testPermissionLevelMethods() {
		UserPermissionLevel l = UserPermissionLevel.UPDATE;
		PermissionModel.setUserPermissionLevelStatic(l);
		assertEquals(PermissionModel.getPermissionStatic(), l);
	}
	
}