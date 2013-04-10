/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

/**
 * @author Kyle
 *
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.InvalidDateException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

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
	private List<Integer> subRequirements = new ArrayList<Integer>();
	private List<Integer> pUID;
	private List<Note> notes;
	private List<Task> tasks = new ArrayList<Task>();

	@Before
	public void setUp() {
		i1 = new Iteration(name, startDate, endDate, id, Requirements);
		i2 = new Iteration(name, startDate, endDate, 2, Requirements);
		r1 = new Requirement(name, description, releaseNum, type,
				subRequirements, notes, iteration, 3, effort, assignees,
				pUID, tasks);
		r2 = new Requirement(name2, description, releaseNum, type,
				subRequirements, notes, iteration, 5, effort, assignees,
				pUID, tasks);
	}

	@Test
	public void testFromJSON() throws InvalidDateException {
		String json = i1.toJSON();
		Iteration newIteration = Iteration.fromJSON(json);
		assertEquals("name", newIteration.getName());
		assertEquals(startDate, newIteration.getStartDate());
		assertEquals(endDate, newIteration.getEndDate());
		newIteration.setStartDate(new Date());
		newIteration.setEndDate(new Date());
		assertEquals(new Date(), newIteration.getStartDate());
		assertEquals(new Date(), newIteration.getEndDate());
		assertEquals(1, newIteration.getId());
	}

	@Test
	public void testFromJSONArray() {
		Gson parser = new Gson();
		Iteration[] array = { i1 };
		String json = parser.toJson(array, Iteration[].class);
		Iteration[] newIterationArray = Iteration.fromJSONArray(json);
		assertEquals("name", newIterationArray[0].getName());
		assertEquals(startDate, newIterationArray[0].getStartDate());
		assertEquals(endDate, newIterationArray[0].getEndDate());
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
		assertEquals(Requirements, moreRequirements);
	}

	@Test
	public void testRequirementAlreadyAdded() {
		i1.addRequirement(1);
		i1.addRequirement(1);
		i1.save();
		i1.delete();
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
		assertEquals(Requirements, moreRequirements);
	}

	@Test
	public void testIdentify() {
		assertTrue(i1.identify(i1));
		assertFalse(i1.identify(new Object()));
		assertFalse(i1.identify(i2));
	}
	
	@Test
	public void getEstimate() {
		assertEquals(i1.getEstimate(), 0);
		i1.addRequirement(r1.getrUID());
		i1.addRequirement(r2.getrUID());
		assertEquals(i1.getEstimate(), 0);
	}
	
	@Test
	public void testSortIterations() {
		assertEquals(i1.sortIterations(iterations), iterations);
	}
}
