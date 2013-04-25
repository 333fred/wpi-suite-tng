/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers.UserEntityManager;

public class UserEntityManagerTest {
	
	Data db;
	UserEntityManager manager;
	Project testProject;
	Project otherProject;
	Session defaultSession;
	String mockSsid;
	User testUser;
	User existingUser;
	Session adminSession;
	
	User user1, user2, user3;
	
	@Test
	public void makeEntityTest() throws BadRequestException, ConflictException,
			WPISuiteException {
		// dummy test: maybe figure this out in the future?
		Assert.assertEquals(1, 1);
		// StringListModel created = manager.makeEntity(adminSession,
		// user1.toJSON());
		// assertEquals(created.getPermission(user1), Permission.READ);
		// assertEquals(created.getPermission(user2), Permission.WRITE);
		// assertEquals(created.getPermission(user3), null);
	}
	
	@Before
	public void setup() {
		manager = new UserEntityManager(db);
		user1 = new User("Fred", "fred", "pass", 1);
		user1.setPermission(Permission.READ, user1);
		user2 = new User("Joe", "Joe", "pass", 2);
		user2.setPermission(Permission.WRITE, user2);
		user3 = new User("Rob", "rob", "pass", 3);
		
	}
}
