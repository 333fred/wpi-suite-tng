/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 				Kyle Burns and Maddie Burris
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Date;

import org.junit.*;
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
	Filter f23;
	Filter f24;
	Filter f25;
	Filter f26;
	Filter f27;
	Filter f28;
	Filter f29;
	Filter f30;
	Filter f31;
	Filter f32;
	Filter f33;
	Filter f34;
	Filter f35;
	Filter f36;
	Filter f37;
	Filter f38;
	Filter f39;
	Filter f40;
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
	Iteration i5;
	String Iname;
	Date startDate;
	Date endDate;
	int iterationID1 = 0;
	int iterationID2 = 1;
	int iterationID3 = 2;
	private int estimate1  = 1;
	private int estimate2  = 2;
	private int estimate3  = 3;
	private int effort1  = 1;
	private int effort2 = 2;
	private int effort3 = 3;
	private List<Integer> reqs = new ArrayList<Integer>();
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
		i5 = new Iteration(Iname, startDate, endDate, iterationID1,
				reqs);
		IterationDatabase.getInstance().add(i5);
		IterationDatabase.getInstance().add(i1);
		IterationDatabase.getInstance().add(i2);
		IterationDatabase.getInstance().add(i3);
		IterationDatabase.getInstance().add(i4);
		f1 = new Filter(u1); 
		f2 = new Filter(u2);
		/**Name Filters**/
		f3 = new Filter(u1, FilterField.NAME, FilterOperation.EQUAL, "name");
		f4 = new Filter(u1, FilterField.NAME, FilterOperation.NOT_EQUAL, "name2");
		f5 = new Filter(u1, FilterField.NAME, FilterOperation.CONTAINS, "2");
		f6 = new Filter(u1, FilterField.NAME, FilterOperation.STARTS_WITH, "e");
		/**Release Number Filters**/
		f7 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.EQUAL, "v1");
		f8 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.NOT_EQUAL, "v1");
		f9 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.CONTAINS, "v");
		f10 = new Filter(u1, FilterField.RELEASE_NUMBER, FilterOperation.STARTS_WITH, "v");
		/**Type Filters**/
		f11 = new Filter(u1, FilterField.TYPE, FilterOperation.EQUAL, Type.USER_STORY);
		f12 = new Filter(u1, FilterField.TYPE, FilterOperation.NOT_EQUAL, Type.USER_STORY);
		/**Priority Filters**/
		f13 = new Filter(u1, FilterField.PRIORITY, FilterOperation.EQUAL, Priority.HIGH);
		f14 = new Filter(u1, FilterField.PRIORITY, FilterOperation.NOT_EQUAL, Priority.HIGH);
		/**Status Filters**/
		f15 = new Filter(u1, FilterField.STATUS, FilterOperation.EQUAL, Status.NEW);
		f16 = new Filter(u1, FilterField.STATUS, FilterOperation.NOT_EQUAL, Status.IN_PROGRESS);
		/**Iteration Filters**/
		f17 = new Filter(u1, FilterField.ITERATION, FilterOperation.EQUAL, 2);
		//f18 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_BEFORE, startDate);
		//f19 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_BETWEEN, 2);
		//f20 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_AFTER, endDate);
		f21= new Filter(u1, FilterField.ITERATION, FilterOperation.NOT_EQUAL, 0);
		/**Filters free for use**/
		//f22 free
		//f23 free
		/**Estimate Filters**/
		f24 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.GREATER_THAN, 2);
		f25 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.EQUAL, 1);
		f26 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.NOT_EQUAL, 1);
		f27 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.LESS_THAN, 2);
		f28 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.LESS_THAN_EQUAL, 2);
		f29 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.GREATER_THAN_EQUAL, 2);
		/**Effort Filters**/
		f30 = new Filter(u1, FilterField.EFFORT, FilterOperation.GREATER_THAN, 1);
		f31 = new Filter(u1, FilterField.EFFORT, FilterOperation.GREATER_THAN_EQUAL, 2);
		f32 = new Filter(u1, FilterField.EFFORT, FilterOperation.LESS_THAN, 2);
		f33 = new Filter(u1, FilterField.EFFORT, FilterOperation.LESS_THAN_EQUAL, 2);
		f34 = new Filter(u1, FilterField.EFFORT, FilterOperation.EQUAL, 1);
		f35 = new Filter(u1, FilterField.EFFORT, FilterOperation.NOT_EQUAL, 1);
		/**Incorrect Filters for "Break" testing**/
		f36 = new Filter(u1, FilterField.NAME, FilterOperation.GREATER_THAN, 1);
		f37 = new Filter(u1, FilterField.TYPE, FilterOperation.CONTAINS, 1);
		f38 = new Filter(u1, FilterField.ITERATION, FilterOperation.CONTAINS, 1);
		f39 = new Filter(u1, FilterField.ESTIMATE, FilterOperation.CONTAINS, 1);
		
		r1 = new Requirement(name, description, releaseNum, type.USER_STORY,
				subRequirements, notes, iterationID1, estimate1, effort1, assignees,
				pUID, tasks);
		r2 = new Requirement(name2, description, "v2", type.THEME,
				subRequirements, notes, iterationID2, estimate2, effort2, assignees,
				pUID, tasks);
		r3 = new Requirement("example", description, "3.0", type.EPIC,
				subRequirements, notes, iterationID3, estimate3, effort3, assignees,	
				pUID, tasks);
		
		r1.setPriority(Priority.HIGH);
		r2.setPriority(Priority.MEDIUM);
		r3.setPriority(Priority.LOW);
		
		r1.setStatus(Status.NEW);
		r2.setStatus(Status.BLANK);
		r3.setStatus(Status.IN_PROGRESS);
	}
	
	@Test
	public void testShouldFilterActive() {
		assertFalse(f1.shouldFilter(r1));
	}
	
	@Test
	public void testShouldFilterNonActive() {
		f1.setActive(false);
		assertFalse(f1.shouldFilter(r1));
	}	

	@Test
	public void shouldFilterNameEqual(){
		assertTrue(f3.shouldFilter(r1));
		assertFalse(f3.shouldFilter(r2));		
	}
	
	@Test
	public void shouldFilterNameNotEqual(){
		assertTrue(f4.shouldFilter(r1));
		assertFalse(f4.shouldFilter(r2));		
	}

	@Test
	public void shouldFilterNameContains(){
		assertTrue(f5.shouldFilter(r2));
		assertFalse(f5.shouldFilter(r1));		
	}
	
	@Test
	public void shouldFilterNameStartsWith(){
		assertTrue(f6.shouldFilter(r3));
		assertFalse(f6.shouldFilter(r1));
	}
		
	@Test
	public void shouldFilterReleaseEqual(){
		assertTrue(f7.shouldFilter(r1));
		assertFalse(f7.shouldFilter(r2));		
	}
	
	@Test
	public void shouldFilterReleaseNotEqual(){
		assertTrue(f8.shouldFilter(r2));
		assertFalse(f8.shouldFilter(r1));		
	}
	
	@Test
	public void shouldFilterReleaseContains(){
		assertTrue(f9.shouldFilter(r1));
		assertFalse(f9.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterReleaseStartsWith() {		
		assertTrue(f10.shouldFilter(r1));
		assertFalse(f10.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterTypeEqual() {
		assertTrue(f11.shouldFilter(r1));
		assertFalse(f11.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterTypeNotEqual() {
		assertTrue(f12.shouldFilter(r3));
		assertFalse(f12.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterPriorityEqual() {		
		assertTrue(f13.shouldFilter(r1));
		assertFalse(f13.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterPriorityNotEqual() {		
		assertTrue(f14.shouldFilter(r3));
		assertFalse(f14.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterStatusEqual() {		
		assertTrue(f15.shouldFilter(r1));
		assertFalse(f15.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterStatusNotEqual() {		
		assertTrue(f16.shouldFilter(r1));
		assertFalse(f16.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateGreater() {
		assertTrue(f24.shouldFilter(r3));
		assertFalse(f24.shouldFilter(r1));	
	}
		
	@Test
	public void shouldFilterEstimateEqual() {
		assertTrue(f25.shouldFilter(r1));
		assertFalse(f25.shouldFilter(r2));	
	}
	
	@Test
	public void shouldFilterEstimateNotEqual() {
		assertTrue(f26.shouldFilter(r2));
		assertFalse(f26.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEstimateLess() {
		assertTrue(f27.shouldFilter(r1));
		assertFalse(f27.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateLessEqual() {
		assertTrue(f28.shouldFilter(r1));
		assertTrue(f28.shouldFilter(r2));
		assertFalse(f28.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEstimateGreaterEqual() {
		assertTrue(f29.shouldFilter(r2));
		assertTrue(f29.shouldFilter(r3));
		assertFalse(f29.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortEqual() {
		assertTrue(f34.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortGreater() {
		assertTrue(f30.shouldFilter(r2));
		assertFalse(f30.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortGreaterEqual() {
		assertTrue(f31.shouldFilter(r2));
		assertTrue(f31.shouldFilter(r3));
		assertFalse(f31.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterEffortLess() {
		assertTrue(f32.shouldFilter(r1));
		assertFalse(f32.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortLessEqual() {
		assertTrue(f33.shouldFilter(r1));
		assertTrue(f33.shouldFilter(r2));
		assertFalse(f33.shouldFilter(r3));
	}
	
	@Test
	public void shouldFilterEffortNotEqual() {
		assertTrue(f35.shouldFilter(r2));
		assertFalse(f35.shouldFilter(r1));
	}
	
/******* Iteration tests below .... Iteration testing is confusing and will be worked on later**********/
	
//	assertTrue(f16.shouldFilter(r1));		
//	assertTrue(f17.shouldFilter(r1));
//	assertTrue(f18.shouldFilter(r1));
//	assertTrue(f19.shouldFilter(r1));
	
	@Test
	public void shouldFilterIterationEqual() {		
		assertTrue(f17.shouldFilter(r3));
		assertFalse(f17.shouldFilter(r2));
	}
	
	
	@Test
	public void shouldFilterIterationNotEqual() {		
		assertTrue(f21.shouldFilter(r3));
		assertFalse(f21.shouldFilter(r1));
	}
	
	/*@Test
	public void shouldFilterIterationOccursBefore() {
		f31 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_BEFORE, 1);
		assertTrue(f31.shouldFilter(r2));
		assertFalse(f31.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterIterationOccursAfter() {
		f32 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_AFTER, 1);
		assertTrue(f32.shouldFilter(r3));
		assertFalse(f32.shouldFilter(r1));
	}
	
	@Test
	public void shouldFilterIterationOccursBetween() {
		f33 = new Filter(u1, FilterField.ITERATION, FilterOperation.OCCURS_BETWEEN, 1);
		assertTrue(f33.shouldFilter(r4));
		assertFalse(f33.shouldFilter(r1));
	}
	*/
/*****************************End Iteration Tests***************************************/	
		
	@Test
	public void breakCheckString(){
		//fieldOperation = something other than equal, not equal, contains, starts with
		assertFalse(f36.shouldFilter(r1));
	}
	
	@Test
	public void breakCheckEnum(){
		//fieldOperation = something other than equal or not equal
		assertFalse(f37.shouldFilter(r1));
	}
	
	@Test public void breakCheckIteration(){
		//use contains for field operation
		assertFalse(f38.shouldFilter(r1));
	}
	
	@Test
	public void breakCheckInteger(){
		assertFalse(f39.shouldFilter(r1));		
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
