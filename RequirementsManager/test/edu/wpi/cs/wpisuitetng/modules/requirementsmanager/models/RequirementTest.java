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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.ATestStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;

public class RequirementTest {
	
	Requirement r1;
	Requirement r1copy;
	Requirement r2;
	Project project;
	Note n1;
	Note n2;
	ATest a1, a2, a3;
	
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
	private final List<String> assignees = new ArrayList<String>();
	private final List<Integer> subRequirements = new ArrayList<Integer>();
	private final List<Integer> pUID = new ArrayList<Integer>();
	private final List<Note> notes = new ArrayList<Note>();
	private final List<Task> tasks = new ArrayList<Task>();
	private final List<ATest> tests = new ArrayList<ATest>();
	
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
		n1 = new Note("note1", new Date(), "creator1");
		n2 = new Note("note2", new Date(), "creator2");
		a1 = new ATest("test1", "test1");
		a2 = new ATest("test2", "test2");
		a3 = new ATest("test3", "test3");
	}
	
	@Test
	public void testAddAndRemoveNotes() {
		r1.addNote(n1);
		r1.removeNote("note1");
	}
	
	@Test
	public void testAddAndRemovePUID() {
		r1.addPUID(0);
		r1.removePUID(0);
		Assert.assertTrue(r1.getpUID().isEmpty());
	}
	
	@Test
	public void testAddAndRemoveUsers() {
		r1.addUser("user1");
		Assert.assertTrue(r1.removeUser("user1"));
	}
	
	@Test
	public void testAddSubRequirement() {
		r1.addSubRequirement(0);
		Assert.assertTrue(r1.removeSubRequirement(0));
	}
	
	@Test
	public void testAddTask() {
		r1.addTask(new Task("Task 1", "Desc 1"));
	}
	
	@Test
	public void testfromJSON() {
		final String json = r1.toJSON();
		final Requirement newRequirement = Requirement.fromJSON(json);
		Assert.assertEquals("name", newRequirement.getName());
		Assert.assertEquals("", newRequirement.getDescription());
		Assert.assertEquals(0, newRequirement.getrUID());
	}
	
	@Test
	public void testfromJSONArray() {
		final Gson parser = new Gson();
		final Requirement[] array = { r1 };
		final String json = parser.toJson(array, Requirement[].class);
		final Requirement[] newRequirementArray = Requirement
				.fromJSONArray(json);
		Assert.assertEquals("name", newRequirementArray[0].getName());
		Assert.assertEquals("", newRequirementArray[0].getReleaseNum());
		Assert.assertEquals(0, newRequirementArray[0].getEffort());
	}
	
	@Test
	public void testIdentify() {
		Assert.assertTrue(r1.identify(r1));
		Assert.assertTrue(r1.identify(r1copy));
		Assert.assertFalse(r1.identify(new Object()));
		Assert.assertFalse(r1.identify(null));
		Assert.assertTrue(r1.identify(0));
		r2.setrUID(1);
		Assert.assertFalse(r1.identify(r2));
	}
	
	@Test
	public void testSetDescription() {
		r1.setDescription("desc");
		Assert.assertEquals("desc", r1.getDescription());
	}
	
	@Test
	public void testSetName() {
		r1.setName(name2);
		Assert.assertSame(name2, r1.getName());
	}
	
	@Test
	public void testSetPriority() {
		r1.setPriority(priority);
		Assert.assertSame(priority, r1.getPriority());
	}
	
	@Test
	public void testSetProject() {
		r1.setProject(project);
		Assert.assertSame(project, r1.getProject());
	}
	
	@Test
	public void testSetreleaseNum() {
		r1.setReleaseNum("1");
		Assert.assertEquals("1", r1.getReleaseNum());
	}
	
	@Test
	public void testSetType() {
		r1.setType(type);
		Assert.assertSame(type, r1.getType());
	}
	
	// Testing the task completeness for a an empty list
	@Test
	public void testTaskCheckEmpty() {
		Assert.assertTrue(r1.tasksCompleted());
	}
	
	// Testing the task completeness checking for a list of complete and
	// incomplete tasks
	@Test
	public void testTaskCheckFalse() {
		final Task task1 = new Task("Task 1", "This is a task!");
		task1.setCompleted(true);
		final Task task2 = new Task("Task 2", "This is another task");
		final List<Task> taskList = new ArrayList<Task>();
		taskList.add(task1);
		taskList.add(task2);
		r1.setTasks(taskList);
		Assert.assertFalse(r1.tasksCompleted());
	}
	
	// Testing the task completeness checking for a list of complete tasks
	@Test
	public void testTaskCheckTrue() {
		final Task task1 = new Task("Task 1", "This is a task!");
		task1.setCompleted(true);
		final Task task2 = new Task("Task 2", "This is another task");
		task2.setCompleted(true);
		final List<Task> taskList = new ArrayList<Task>();
		taskList.add(task1);
		taskList.add(task2);
		r1.setTasks(taskList);
		Assert.assertTrue(r1.tasksCompleted());
	}
	
	@Test
	public void testTestsPassed() {
		r1.setTests(tests);
		r1.addTest(a1);
		a1.setStatus(ATestStatus.PASSED);
		Assert.assertTrue(r1.testsPassed());
		r1.addTest(a2);
		a2.setStatus(ATestStatus.FAILED);
		Assert.assertFalse(r1.testsPassed());
	}
	
	@Test
	public void testTestsPassed2() {
		r1.setTests(tests);
		r1.addTest(a1);
		a1.setStatus(ATestStatus.PASSED);
		Assert.assertTrue(r1.testsPassed());
		r1.addTest(a3);
		a2.setStatus(ATestStatus.BLANK);
		Assert.assertFalse(r1.testsPassed());
	}
	
	@Test
	public void testToListString() {
		r1.setName(name);
		r1.setrUID(1);
		Assert.assertEquals("1 name", r1.toListString());
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals(r1.toString(), "name");
	}
}
