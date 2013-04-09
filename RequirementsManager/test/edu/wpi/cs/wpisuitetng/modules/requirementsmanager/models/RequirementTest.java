/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

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
		r1 = new Requirement(name, description, releaseNum, type,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		r1copy = new Requirement(name, description, releaseNum, type,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		r2 = new Requirement(name2, description, releaseNum, type,
				subRequirements, notes, iteration, estimate, effort, assignees,
				pUID, tasks);
		project = new Project("test", "1");
	}

	@Test
	public void testIdentify() {
		assertTrue(r1.identify(r1));
		assertTrue(r1.identify(r1copy));
		assertFalse(r1.identify(new Object()));
		assertFalse(r1.identify(null));
		assertTrue(r1.identify(0));
		r2.setrUID(1);
		assertFalse(r1.identify(r2));
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
	public void testfromJSONArray() {
		Gson parser = new Gson();
		Requirement[] array = { r1 };
		String json = parser.toJson(array, Requirement[].class);
		Requirement[] newRequirementArray = Requirement.fromJSONArray(json);
		assertEquals("name", newRequirementArray[0].getName());
		assertEquals("", newRequirementArray[0].getReleaseNum());
		assertEquals(0, newRequirementArray[0].getEffort());
	}

	@Test
	public void testSetProject() {
		r1.setProject(project);
		assertSame(project, r1.getProject());
	}

	@Test
	public void testSetName() {
		r1.setName(name2);
		assertSame(name2, r1.getName());
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
		assertEquals("desc", r1.getDescription());
	}

	@Test
	public void testSetreleaseNum() {
		r1.setReleaseNum("1");
		assertEquals("1", r1.getReleaseNum());
	}

	@Test
	public void testSetType() {
		r1.setType(type);
		assertSame(type, r1.getType());
	}

	@Test
	public void testSetPriority() {
		r1.setPriority(priority);
		assertSame(priority, r1.getPriority());
	}

	// Testing the task completeness checking for a list of complete tasks
	@Test
	public void testTaskCheckTrue() {
		Task task1 = new Task("Task 1", "This is a task!");
		task1.setCompleted(true);
		Task task2 = new Task("Task 2", "This is another task");
		task2.setCompleted(true);
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.add(task1);
		taskList.add(task2);
		r1.setTasks(taskList);
		assertTrue(r1.tasksCompleted());
	}

	// Testing the task completeness checking for a list of complete and
	// incomplete tasks
	@Test
	public void testTaskCheckFalse() {
		Task task1 = new Task("Task 1", "This is a task!");
		task1.setCompleted(true);
		Task task2 = new Task("Task 2", "This is another task");
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.add(task1);
		taskList.add(task2);
		r1.setTasks(taskList);
		assertFalse(r1.tasksCompleted());
	}

	// Testing the task completeness for a an empty list
	@Test
	public void testTaskCheckEmpty() {
		assertTrue(r1.tasksCompleted());
	}

	@Test
	public void testAddSubRequirement() {
		r1.addSubRequirement(0);
		assertTrue(r1.removeSubRequirement(0));
	}
}
