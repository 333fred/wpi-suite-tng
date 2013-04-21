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

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;

public class FilterTest {
	
	Filter f1;
	Filter f2;
	Filter f3;
	Filter f4;
	Filter f5;
	Filter f6;
	Filter f7;
	Filter f8;
	Filter f9;
	Filter f10;
	Filter f11;
	Filter f12;
	Filter f13;
	Filter f14;
	Filter f15;
	Filter f16;
	Filter f17;
	Filter f18;
	Filter f19;
	Filter f20;
	Filter f21;
	Filter f22;
	User u1;
	User u2;
	Requirement r1;
	Requirement r2;
	Requirement r3;
	
	String name = "name";
	String name2 = "name2";
	String releaseNum = "v1";
	String description;
	Type type;
	Status status;
	Priority priority;
	private int iteration = 0;
	private int estimate;
	private int effort;
	private List<String> assignees = new ArrayList<String>();
	private List<Integer> subRequirements = new ArrayList<Integer>();
	private List<Integer> pUID = new ArrayList<Integer>();
	private List<Note> notes = new ArrayList<Note>();
	private List<Task> tasks = new ArrayList<Task>();
	
	@Before
	public void setUp() {
		f1 = new Filter(u1);
		f2 = new Filter(u2);
		f3 = new Filter(u1, FilterField.NAME, FilterOperation.EQUAL, "name");
		f4 = new Filter(u1, FilterField.NAME, FilterOperation.NOT_EQUAL, "name2");
		f5 = new Filter(u1, FilterField.NAME, FilterOperation.CONTAINS, "e2");
		f6 = new Filter(u1, FilterField.NAME, FilterOperation.STARTS_WITH, "e");
		f7 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.EQUAL, "v1");
		f8 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.NOT_EQUAL, "v1");
		f9 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.CONTAINS, "v");
		f10 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f11 = new Filter(u1, FilterField.TYPE, FilterOperation.EQUAL, Type.USER_STORY);
		f12 = new Filter(u1, FilterField.TYPE, FilterOperation.NOT_EQUAL, Type.USER_STORY);
		f13 = new Filter(u1, FilterField.PRIORITY, FilterOperation.EQUAL, Priority.HIGH);
		f14 = new Filter(u1, FilterField.PRIORITY, FilterOperation.NOT_EQUAL, Priority.HIGH);
		f15 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f16 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f17 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f18 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f19 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f20 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f21 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		f22 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		r1 = new Requirement(name, description, releaseNum, type.USER_STORY,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		r2 = new Requirement(name2, description, "v2", type.THEME,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		r3 = new Requirement("example", description, "3.0", type.EPIC,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		r1.setPriority(Priority.HIGH);
		r2.setPriority(Priority.MEDIUM);
		r3.setPriority(Priority.LOW);
	}
	
	@Test
	public void testShouldFilter() {
		assertTrue(f3.shouldFilter(r1));
		assertFalse(f3.shouldFilter(r2));
		assertTrue(f4.shouldFilter(r1));
		assertFalse(f4.shouldFilter(r2));
		assertTrue(f5.shouldFilter(r2));
		assertFalse(f5.shouldFilter(r1));
		assertTrue(f6.shouldFilter(r3));
		assertFalse(f6.shouldFilter(r1));
		assertTrue(f7.shouldFilter(r1));
		assertFalse(f7.shouldFilter(r2));
		assertTrue(f8.shouldFilter(r2));
		assertFalse(f8.shouldFilter(r1));
		assertTrue(f9.shouldFilter(r1));
		assertFalse(f9.shouldFilter(r3));
		assertTrue(f10.shouldFilter(r1));
		assertFalse(f10.shouldFilter(r3));
		assertTrue(f11.shouldFilter(r1));
		assertFalse(f11.shouldFilter(r3));
		assertTrue(f12.shouldFilter(r3));
		assertFalse(f12.shouldFilter(r1));
		assertTrue(f13.shouldFilter(r1));
		assertFalse(f13.shouldFilter(r3));
		assertTrue(f14.shouldFilter(r3));
		assertFalse(f14.shouldFilter(r1));
		f1.setActive(false);
		assertFalse(f1.shouldFilter(r1));
		
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
	public void testToStringsetJsonValueandgetJsonValue() {
		f1.setJsonValue("string");
		String JsonVal = f1.getJsonValue();
		assertEquals(f1.toString(), "[Filter ID:" + -1 + " Field:" + FilterField.NAME + " Operation:"
				+ FilterOperation.EQUAL + " Value: " + JsonVal + " Active: " + true + "]");
	}
	@Test
	public void testActive() {
		assertTrue(f1.isActive());
		f1.setActive(false);
		assertFalse(f1.isActive());
		assertFalse(f1.getActive());
	}
	

	// TODO: Change this test once getStringValue() is finalized
	/*@Test
	public void testStringValue() {
		f1.setStringValue("test");
		assertEquals(f1.getStringValue(), "test");
	}
	*/

}
