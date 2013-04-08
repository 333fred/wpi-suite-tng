/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

/**
 * @author Kyle
 *
 */

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public class IterationTest {
	
	Iteration i1;
	Iteration i2;
	
	String name = "name";
	Date startDate;
	Date endDate;
	int id = 1;
	List<Integer> Requirements = new ArrayList<Integer>();
	List<Integer> moreRequirements = new ArrayList<Integer>();

	
	@Before
	public void setUp() {
		i1 = new Iteration(name, startDate, endDate, id, Requirements);		
	}
	
	@Test
	public void testFromJSON() {
		String json = i1.toJSON();
		Iteration newIteration = Iteration.fromJSON(json);
		assertEquals("name", newIteration.getName());
		assertEquals(startDate, newIteration.getStartDate());
		assertEquals(endDate, newIteration.getEndDate());
		assertEquals(1, newIteration.getId());
	}
	
	@Test
	public void testFromJSONArray() {
		Gson parser = new Gson();
		Iteration[] array = {i1};
		String json = parser.toJson(array, Iteration[].class);
		Iteration[] newIterationArray = Iteration.fromJSONArray(json);
		assertEquals("name", newIterationArray[0].getName());
		assertEquals(startDate, newIterationArray[0].getStartDate());
		assertEquals(endDate, newIterationArray[0].getEndDate());
	}
	
	@Test
	public void testAddRequirement(){
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
	public void testRequirementAlreadyAdded(){
		i1.addRequirement(1);
		i1.addRequirement(1);
	}
	
	@Test
	public void testRemoveRequirement(){
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

}
