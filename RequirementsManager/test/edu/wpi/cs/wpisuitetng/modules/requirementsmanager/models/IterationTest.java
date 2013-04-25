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
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.InvalidDateException;

public class IterationTest {
	
	Iteration i1;
	Iteration i2;
	Requirement r1;
	Requirement r2;
	
	String name = "name";
	Date startDate;
	Date endDate;
	int id = 1;
	List<Integer> Requirements = new ArrayList<Integer>();
	List<Integer> moreRequirements = new ArrayList<Integer>();
	List<Iteration> iterations = new ArrayList<Iteration>();
	
	String name2 = "name2";
	String releaseNum;
	String description;
	Type type;
	Status status;
	Priority priority;
	private int iteration;
	private int estimate;
	private int effort;
	private List<String> assignees;
	private final List<Integer> subRequirements = new ArrayList<Integer>();
	private List<Integer> pUID;
	private List<Note> notes;
	private final List<Task> tasks = new ArrayList<Task>();
	
	@Test
	public void getEstimate() {
		Assert.assertEquals(i1.getEstimate(), 0);
		i1.addRequirement(r1.getrUID());
		i1.addRequirement(r2.getrUID());
		Assert.assertEquals(i1.getEstimate(), 0);
	}
	
	@Before
	public void setUp() {
		i1 = new Iteration(name, startDate, endDate, id, Requirements);
		i2 = new Iteration(name, startDate, endDate, 2, Requirements);
		r1 = new Requirement(name, description, releaseNum, type,
				subRequirements, notes, iteration, 3, effort, assignees, pUID,
				tasks);
		r2 = new Requirement(name2, description, releaseNum, type,
				subRequirements, notes, iteration, 5, effort, assignees, pUID,
				tasks);
	}
	
	@Test
	public void testAddRequirement() {
		i1.addRequirement(1);
		i1.addRequirement(2);
		i1.addRequirement(3);
		i1.addRequirement(4);
		moreRequirements.add(1);
		moreRequirements.add(2);
		moreRequirements.add(3);
		moreRequirements.add(4);
		Assert.assertEquals(Requirements, moreRequirements);
	}
	
	@Test
	public void testFromJSON() throws InvalidDateException {
		final String json = i1.toJSON();
		final Iteration newIteration = Iteration.fromJSON(json);
		Assert.assertEquals("name", newIteration.getName());
		Assert.assertEquals(startDate, newIteration.getStartDate());
		Assert.assertEquals(endDate, newIteration.getEndDate());
		newIteration.setStartDate(new Date());
		newIteration.setEndDate(new Date());
		Assert.assertEquals(new Date(), newIteration.getStartDate());
		Assert.assertEquals(new Date(), newIteration.getEndDate());
		Assert.assertEquals(1, newIteration.getId());
	}
	
	@Test
	public void testFromJSONArray() {
		final Gson parser = new Gson();
		final Iteration[] array = { i1 };
		final String json = parser.toJson(array, Iteration[].class);
		final Iteration[] newIterationArray = Iteration.fromJSONArray(json);
		Assert.assertEquals("name", newIterationArray[0].getName());
		Assert.assertEquals(startDate, newIterationArray[0].getStartDate());
		Assert.assertEquals(endDate, newIterationArray[0].getEndDate());
	}
	
	@Test
	public void testIdentify() {
		Assert.assertTrue(i1.identify(i1));
		Assert.assertFalse(i1.identify(new Object()));
		Assert.assertFalse(i1.identify(i2));
	}
	
	@Test
	public void testRemoveRequirement() {
		i1.addRequirement(1);
		i1.addRequirement(2);
		i1.addRequirement(3);
		i1.addRequirement(4);
		i1.addRequirement(5);
		i1.removeRequirement(5);
		moreRequirements.add(1);
		moreRequirements.add(2);
		moreRequirements.add(3);
		moreRequirements.add(4);
		Assert.assertEquals(Requirements, moreRequirements);
	}
	
	@Test
	public void testRequirementAlreadyAdded() {
		i1.addRequirement(1);
		i1.addRequirement(1);
		i1.save();
		i1.delete();
	}
	
	@Test
	public void testSortIterations() {
		Assert.assertEquals(Iteration.sortIterations(iterations), iterations);
	}
}
