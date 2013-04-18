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

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;

public class FilterTest {
	
	Filter f1;
	Filter f2;
	User u1;
	User u2;
	
	@Before
	public void setUp() {
		f1 = new Filter(u1);
		f2 = new Filter(u2);
	}
	
	@Test
	public void testfromJSON() {
		String json = f1.toJSON();
		Filter newFilter = Filter.fromJSON(json);
		newFilter.setCreator(u2);
		assertEquals(u2, newFilter.getCreator());
		newFilter.setField(FilterField.ITERATION);
		assertEquals(FilterField.ITERATION, newFilter.getField());
		newFilter.setOperation(FilterOperation.OCCURS_AFTER);
		assertEquals(FilterOperation.OCCURS_AFTER, newFilter.getOperation());
		newFilter.setValue(new String("test"));
		assertEquals("test", newFilter.getValue());
	}
	
	@Test
	public void testfromJSONArray() {
		Gson parser = new Gson();
		Filter[] array = { f1 };
		String json = parser.toJson(array, Filter[].class);
		Filter[] newFilterArray = Filter.fromJSONArray(json);
		assertEquals(u1, newFilterArray[0].getCreator());
		assertEquals(FilterField.NAME, newFilterArray[0].getField());
		assertEquals(FilterOperation.EQUAL, newFilterArray[0].getOperation());
	}
	
	@Test
	public void testIdentify() {
		assertTrue(f1.identify(f1));
		f2.setId(1);
		assertFalse(f1.identify(f2));
		assertFalse(f1.identify(new String()));
	}
	
	@Test
	public void testToString() {
		assertEquals(f1.toString(), "[Filter ID:" + -1 + " Field:" + FilterField.NAME + " Operation:"
				+ FilterOperation.EQUAL + " Value: " + new String() + " Active: " + true + "]");
	}
	
	@Test
	public void testActive() {
		assertTrue(f1.isActive());
		f1.setActive(false);
		assertFalse(f1.isActive());
		assertFalse(f1.getActive());
	}
}
