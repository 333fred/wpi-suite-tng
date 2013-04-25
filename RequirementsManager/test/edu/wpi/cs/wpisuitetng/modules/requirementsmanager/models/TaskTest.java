/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Conor Geary
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class TaskTest {
	
	Task t1, t2, t3;
	User u1;
	
	@Before
	public void setup() {
		t1 = new Task("Task 1", "Desc1");
		t2 = new Task("Task 2", "Desc2");
		t3 = new Task("Task 3", "Desc3");
		u1 = new User("name", "uname", "pass", 1);
	}
	
	@Test
	public void testEstimateGetterandSetter() {
		t1.setEstimate(10);
		Assert.assertEquals(10, t1.getEstimate());
	}
	
	@Test
	public void testGetContent() {
		t1.setCompleted(true);
		Assert.assertEquals(
				"<i>Desc1<br><FONT COLOR=\"gray\">No User Assigned</FONT COLOR><br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR><br><FONT COLOR=\"red\">Estimate: 0</FONT COLOR></i>",
				t1.getContent());
		Assert.assertEquals(
				"<i>Desc2<br><FONT COLOR=\"gray\">No User Assigned</FONT COLOR><br><FONT COLOR=\"red\">In Progress</FONT COLOR><br><FONT COLOR=\"red\">Estimate: 0</FONT COLOR></i>",
				t2.getContent());
		t1.setDescription("Desc1\nDesc");
		Assert.assertEquals(
				"<i>Desc1<br>Desc<br><FONT COLOR=\"gray\">No User Assigned</FONT COLOR><br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR><br><FONT COLOR=\"red\">Estimate: 0</FONT COLOR></i>",
				t1.getContent());
		t1.setAssignedUser(u1.getName());
		Assert.assertEquals(
				"<i>Desc1<br>Desc<br><FONT COLOR=\"blue\">Assignee: name</FONT COLOR><br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR><br><FONT COLOR=\"red\">Estimate: 0</FONT COLOR></i>",
				t1.getContent());
	}
	
	@Test
	public void testGetSetAssignedUser() {
		t1.setAssignedUser(u1.getName());
		Assert.assertEquals(t1.getAssignedUser(), u1.getName());
		Assert.assertEquals(t2.getAssignedUser(), null);
	}
	
	@Test
	public void testGetTitle() {
		Assert.assertEquals("<html><font size=4><b>Task 1</b></html>",
				t1.getTitle());
		Assert.assertEquals("<html><font size=4><b>Task 2</b></html>",
				t2.getTitle());
	}
	
	@Test
	public void testIDGetterandSetter() {
		t1.setId(123);
		Assert.assertEquals(123, t1.getId());
	}
	
	@Test
	public void testParseNewLines() {
		Assert.assertEquals("<br>testtest", t1.parseNewLines("\ntesttest"));
		Assert.assertEquals("<br><br>testt<br>est<br>",
				t1.parseNewLines("\n\ntestt\nest\n"));
	}
	
	@Test
	public void testTaskGetDescription() {
		Assert.assertEquals("Desc1", t1.getDescription());
		Assert.assertEquals("Desc2", t2.getDescription());
	}
	
	@Test
	public void testTaskGetName() {
		Assert.assertEquals("Task 1", t1.getName());
		Assert.assertEquals("Task 2", t2.getName());
	}
	
	@Test
	public void testTaskIsCompleted() {
		Assert.assertFalse(t1.isCompleted());
		Assert.assertFalse(t2.isCompleted());
		Assert.assertFalse(t3.isCompleted());
	}
	
	@Test
	public void testTaskSetCompleted() {
		t1.setCompleted(true);
		t2.setCompleted(false);
		Assert.assertTrue(t1.isCompleted());
		Assert.assertFalse(t2.isCompleted());
		Assert.assertFalse(t3.isCompleted());
	}
	
	@Test
	public void testTaskSetDescription() {
		t1.setDescription("New Desc");
		t2.setDescription("Some Desc");
		Assert.assertEquals("New Desc", t1.getDescription());
		Assert.assertEquals("Some Desc", t2.getDescription());
	}
	
	@Test
	public void testTaskSetName() {
		t1.setName("New Name");
		t2.setName("Some Name");
		Assert.assertEquals("New Name", t1.getName());
		Assert.assertEquals("Some Name", t2.getName());
	}
}
