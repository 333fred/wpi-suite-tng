/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Kyle
 *  
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StringListModelTest {
	
	StringListModel s1;

	List<String> users = new ArrayList<String>();
	
	@Before
	public void setUp() {
		s1 = new StringListModel(users);
		users.add("user1");
		users.add("user2");		
	}
	
	@Test
	public void testUsers() {
		s1.setUsers(users);
		assertEquals(s1.getUsers(), users);
	}
	
	@Test
	public void testfromJSON() {
		String json = s1.toJSON();
		StringListModel newStringListModel = StringListModel.fromJson(json);
		assertEquals(users, newStringListModel.getUsers());
	}
	
	@Test
	public void testIdentify() {
		assertTrue(s1.identify(s1));
		assertFalse(s1.identify(new String()));
	}
	
}
