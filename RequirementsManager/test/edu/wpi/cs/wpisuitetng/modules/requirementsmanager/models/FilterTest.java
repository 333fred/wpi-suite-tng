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
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;

public class FilterTest {
	
	Filter f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23, f24, f25, f26, f27, f28, 
	f29, f30, f31, f32, f33;

	User u1;
	User u2;
	Requirement r1;
	Requirement r2;
	Requirement r3;
	Requirement r4;
	Iteration i1, i2, i3, i4;
	
	String name = "name";
	String name2 = "name2";
	String releaseNum = "v1";
	String description;
	Type type;
	Status status;
	Priority priority;
	private int iteration = 0;
	private int estimate  = 1;
	private int effort  = 2;
	private List<String> assignees = new ArrayList<String>();
	private List<Integer> subRequirements = new ArrayList<Integer>();
	private List<Integer> pUID = new ArrayList<Integer>();
	private List<Note> notes = new ArrayList<Note>();
	private List<Task> tasks = new ArrayList<Task>();
	
	@Before
	public void setUp() {
		i1 = new Iteration("Iteration 1", new Date(2013, 4, 18), new Date(2013, 4, 30), 1);
		i2 = new Iteration("Iteration 2", new Date(2013, 4, 11), new Date(2013, 4, 17), 2);
		i3 = new Iteration("Iteration 3", new Date(2013, 5, 1), new Date(2013, 5, 10), 3);
		i4 = new Iteration("Iteration 4", new Date(2013, 4, 21), new Date(2013, 4, 28), 4);
		IterationDatabase.getInstance().add(i1);
		IterationDatabase.getInstance().add(i2);
		IterationDatabase.getInstance().add(i3);
		IterationDatabase.getInstance().add(i4);
		f1 = new Filter(u1);
		f2 = new Filter(u2);	
		f2 = new Filter(u2);
		r1 = new Requirement(name, description, releaseNum, type.USER_STORY,
				subRequirements, notes, 1, 1, 1, assignees,
				pUID, tasks);
		r2 = new Requirement(name2, description, "v2", type.THEME,
				subRequirements, notes, 2, 3, 3, assignees,
				pUID, tasks);
		r3 = new Requirement("example", description, "3.0", type.EPIC,
				subRequirements, notes, 3, 8, 8, assignees,
				pUID, tasks);
		r4 = new Requirement("example", description, "3.0", type.EPIC,
				subRequirements, notes, 4, 8, 8, assignees,
				pUID, tasks);
		r1.setPriority(Priority.HIGH);
		r2.setPriority(Priority.MEDIUM);
		r3.setPriority(Priority.LOW);
		r1.setStatus(Status.NEW);
		r2.setStatus(Status.BLANK);
		r3.setStatus(Status.IN_PROGRESS);

	}
		
		
	
	@Test
	public void shouldFilterNameEqual() {
		Filter f3;
		f3 = new Filter(u1, FilterField.NAME, FilterOperation.EQUAL, "name");
		assertTrue(f3.shouldFilter(r1));
		assertFalse(f3.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterNameNotEqual() {
		f4 = new Filter(u1, FilterField.NAME, FilterOperation.NOT_EQUAL, "name2");
		assertTrue(f4.shouldFilter(r1));
		assertFalse(f4.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterNameContains() {
		f5 = new Filter(u1, FilterField.NAME, FilterOperation.CONTAINS, "e2");
		assertTrue(f5.shouldFilter(r2));
		assertFalse(f5.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterNameStartsWith() {
		f6 = new Filter(u1, FilterField.NAME, FilterOperation.STARTS_WITH, "e");
		assertTrue(f6.shouldFilter(r3));
		assertFalse(f6.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterReleaseEqual() {
		f7 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.EQUAL, "v1");
		assertTrue(f7.shouldFilter(r1));
		assertFalse(f7.shouldFilter(r2));
	}
	
	@Test
	public void shouldFilterReleaseNotEqual() {
		f8 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.NOT_EQUAL, "v1");
		assertTrue(f8.shouldFilter(r2));
		assertFalse(f8.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterReleaseContains() {
		f9 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.CONTAINS, "v");
		assertTrue(f9.shouldFilter(r1));
		assertFalse(f9.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterReleaseStartsWith() {
		f10 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		assertTrue(f10.shouldFilter(r1));
		assertFalse(f10.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterTypeEqual() {
		f11 = new Filter(u1, FilterField.TYPE, FilterOperation.EQUAL, Type.USER_STORY);
		assertTrue(f11.shouldFilter(r1));
		assertFalse(f11.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterTypeNotEqual() {
		f12 = new Filter(u1, FilterField.TYPE, FilterOperation.NOT_EQUAL, Type.USER_STORY);
		assertTrue(f12.shouldFilter(r3));
		assertFalse(f12.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterPriorityEqual() {
		f13 = new Filter(u1, FilterField.PRIORITY, FilterOperation.EQUAL, Priority.HIGH);
		assertTrue(f13.shouldFilter(r1));
		assertFalse(f13.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterPriorityNotEqual() {
		f14 = new Filter(u1, FilterField.PRIORITY, FilterOperation.NOT_EQUAL, Priority.HIGH);
		assertTrue(f14.shouldFilter(r3));
		assertFalse(f14.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterStatusEqual() {
		f15 = new Filter(u1, FilterField.STATUS, FilterOperation.EQUAL, Status.NEW);
		assertTrue(f15.shouldFilter(r1));
		assertFalse(f15.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterStatusNotEqual() {
		f16 = new Filter(u1, FilterField.STATUS, FilterOperation.NOT_EQUAL, Status.IN_PROGRESS);
		assertTrue(f16.shouldFilter(r1));
		assertFalse(f16.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateEqual() {
		f17 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.EQUAL, 1);
		assertTrue(f17.shouldFilter(r1));
		assertFalse(f17.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateNotEqual() {
		f18 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.NOT_EQUAL, 1);
		assertTrue(f18.shouldFilter(r3));
		assertFalse(f18.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateGreater() {
		f19 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.GREATER_THAN, 3);
		assertTrue(f19.shouldFilter(r3));
		assertFalse(f19.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateLess() {
		f20 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.LESS_THAN, 3);
		assertTrue(f20.shouldFilter(r1));
		assertFalse(f20.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateGreaterEqual() {
		f21 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.GREATER_THAN_EQUAL, 3);
		assertTrue(f21.shouldFilter(r3));
		assertFalse(f21.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateLessEqual() {
		f22 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.LESS_THAN_EQUAL, 3);
		assertTrue(f22.shouldFilter(r1));
		assertFalse(f22.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortEqual() {
		f23 = new Filter(u1, FilterField.EFFORT, FilterOperation.EQUAL, 1);
		assertTrue(f23.shouldFilter(r1));
		assertFalse(f23.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortNotEqual() {
		f24 = new Filter(u1, FilterField.EFFORT, FilterOperation.NOT_EQUAL, 1);
		assertTrue(f24.shouldFilter(r3));
		assertFalse(f24.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortGreater() {
		f25 = new Filter(u1, FilterField.EFFORT, FilterOperation.GREATER_THAN, 3);
		assertTrue(f25.shouldFilter(r3));
		assertFalse(f25.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortLess() {
		f26 = new Filter(u1, FilterField.EFFORT, FilterOperation.LESS_THAN, 3);
		assertTrue(f26.shouldFilter(r1));
		assertFalse(f26.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortGreaterEqual() {
		f27 = new Filter(u1, FilterField.EFFORT, FilterOperation.GREATER_THAN_EQUAL, 3);
		assertTrue(f27.shouldFilter(r3));
		assertFalse(f27.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortLessEqual() {
		f28 = new Filter(u1, FilterField.EFFORT, FilterOperation.LESS_THAN_EQUAL, 3);
		assertTrue(f28.shouldFilter(r1));
		assertFalse(f28.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterIterationEqual() {
		f29 = new Filter(u1, FilterField.ITERATION, FilterOperation.EQUAL, 1);
		assertTrue(f29.shouldFilter(r1));
		assertFalse(f29.shouldFilter(r3));
	}
	
	
	@Test
	public void shouldFilterIterationNotEqual() {
		f30 = new Filter(u1, FilterField.ITERATION, FilterOperation.NOT_EQUAL, 1);
		assertTrue(f30.shouldFilter(r3));
		assertFalse(f30.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterIterationOccursBefore() {
		Date d1 = new Date(2013, 4, 18);
		f31 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_BEFORE, d1);
		assertTrue(f31.shouldFilter(r2));
		assertFalse(f31.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterIterationOccursAfter() {
		Date d1 = new Date(2013, 4, 18);
		f32 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_AFTER, d1);
		assertTrue(f32.shouldFilter(r3));
		assertFalse(f32.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterIterationOccursBetween() {
		f33 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_BETWEEN, 1);
		assertTrue(f33.shouldFilter(r4));
		assertFalse(f33.shouldFilter(r1));
	}
	
	
	@Test
	public void testShouldFilterActive() {
		f14 = new Filter(u1, FilterField.PRIORITY, FilterOperation.NOT_EQUAL, Priority.HIGH);
		f15 = new Filter(u1, FilterField.STATUS, FilterOperation.EQUAL, Status.NEW);
		f18 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.NOT_EQUAL, 1);
		assertFalse(f14.shouldFilter(r1));		
		assertTrue(f15.shouldFilter(r1));
		//assertEquals(f16.shouldFilter(r1), 0);   Maddie is working on these
		//assertEquals(f17.shouldFilter(r1), 1);
		assertFalse(f18.shouldFilter(r1));
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
