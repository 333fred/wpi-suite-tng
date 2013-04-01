/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kyle Burns, Conor Geary, Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;

public class RequirementTest {
	
	Requirement r1;
	Requirement r1copy;
	Requirement r2;
	Project project;
	
	String name = "name";
	String name2 = "name2";
	int rUID; 
	String description;
	Type type;
	Status status;
	Priority priority;
	private int iteration;
	private int estimate;
	private int effort;
	private List<String> assignees;
	private List<Integer> subRequirements;
	private List<Integer> pUID;
	private List<Note> notes;
	private List<Task> tasks = new ArrayList<Task>();

	
	@Before
	public void setUp() {
		r1 = new Requirement(name, description, rUID, type, subRequirements, notes, iteration, estimate, effort, assignees, pUID, tasks);
		r1copy = new Requirement(name, description, rUID, type, subRequirements, notes, iteration, estimate, effort, assignees, pUID, tasks);
		r2 = new Requirement(name2, description, rUID, type, subRequirements, notes, iteration, estimate, effort, assignees, pUID, tasks);
		project = new Project("test", "1");
	}
	
	@Test
	public void testIdentify() {
		assertTrue(r1.identify(r1));
		assertTrue(r1.identify(r1copy));
		assertTrue(r1.identify(rUID));
		assertFalse(r1.identify(new Object()));
		assertFalse(r1.identify(null));
	}
	
	@Test
	public void testfromJSON() {
		String json = r1.toJSON();
		Requirement newRequirement = Requirement.fromJSON(json);
		assertEquals("name", newRequirement.getName());
		assertEquals("", newRequirement.getDescription());
		assertEquals(0, newRequirement.getrUID());
	}
	
	@Test
	public void testSetProject() {
		r1.setProject(project);
		assertSame(project, r1.getProject());
	}
	
	@Test
	public void testSetName() {
		r1.setName(name2);
		assertSame(name2,r1.getName());
	}
	
	@Test
	public void testToListString() {
		r1.setName(name);
		r1.setrUID(1);
		assertEquals("1 name", r1.toListString());
	}
	
	@Test
	public void testSetDescription() {
		r1.setDescription("desc");
		assertEquals("desc",r1.getDescription());
	}
	
	@Test
	public void testSetreleaseNum() {
		r1.setReleaseNum(1);
		assertEquals(1,r1.getReleaseNum());
	}
	
	@Test
	public void testSetType() {
		r1.setType(type);
		assertSame(type,r1.getType());
	}
	
	@Test
	public void testSetPriority() {
		r1.setPriority(priority);
		assertSame(priority ,r1.getPriority());
	}
	
	//Testing the task completeness checking for a list of complete tasks
	@Test
	public void testTaskCheckTrue() {
		Task task1 = new Task();
		task1.setCompleted(true);
		Task task2 = new Task();
		task2.setCompleted(true);
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.add(task1);
		taskList.add(task2);
		r1.setTasks(taskList);
		assertTrue(r1.tasksCompleted());
	}
	
	//Testing the task completeness checking for a list of complete and incomplete tasks
	@Test
	public void testTaskCheckFalse() {
		Task task1 = new Task();
		task1.setCompleted(true);
		Task task2 = new Task();
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.add(task1);
		taskList.add(task2);
		r1.setTasks(taskList);
		assertFalse(r1.tasksCompleted());
	}
	
	//Testing the task completeness for a an empty list
	@Test
	public void testTaskCheckEmpty() {
		assertTrue(r1.tasksCompleted());
	}
}
