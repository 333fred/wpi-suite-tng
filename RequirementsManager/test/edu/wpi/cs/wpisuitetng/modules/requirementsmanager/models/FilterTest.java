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
		r1 = new Requirement(name, description, releaseNum, type,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		r2 = new Requirement(name2, description, releaseNum, type,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
	}
	
	@Test
	public void testShouldFilter() {
		assertTrue(f3.shouldFilter(r1));
		assertFalse(f3.shouldFilter(r2));
		assertTrue(f4.shouldFilter(r1));
		assertFalse(f4.shouldFilter(r2));
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
	
	@Test
	public void testStringValue() {
		f1.setStringValue("test");
		assertEquals(f1.getStringValue(), "test");
	}
}
