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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class TaskTest {
	Task t1, t2, t3;
	User u1;
	
	@Before
	public void setup(){
		t1  = new Task("Task 1", "Desc1");
		t2  = new Task("Task 2", "Desc2");
		t3  = new Task("Task 3", "Desc3");
		u1 = new User("name", "uname", "pass", 1);
	}
	
	@Test
	public void testTaskIsCompleted(){
		assertFalse(t1.isCompleted());
		assertFalse(t2.isCompleted());
		assertFalse(t3.isCompleted());
	}
	
	@Test
	public void testTaskSetCompleted(){
		t1.setCompleted(true);
		t2.setCompleted(false);
		assertTrue(t1.isCompleted());
		assertFalse(t2.isCompleted());
		assertFalse(t3.isCompleted());
	}
	
	@Test
	public void testTaskGetName(){
		assertEquals("Task 1", t1.getName());
		assertEquals("Task 2", t2.getName());
	}
	
	@Test
	public void testTaskSetName(){
		t1.setName("New Name");
		t2.setName("Some Name");
		assertEquals("New Name", t1.getName());
		assertEquals("Some Name", t2.getName());
	}
	
	@Test
	public void testTaskGetDescription(){
		assertEquals("Desc1", t1.getDescription());
		assertEquals("Desc2", t2.getDescription());
	}
	
	@Test
	public void testTaskSetDescription(){
		t1.setDescription("New Desc");
		t2.setDescription("Some Desc");
		assertEquals("New Desc", t1.getDescription());
		assertEquals("Some Desc", t2.getDescription());
	}
	
	@Test
	public void testGetSetAssignedUser(){
		t1.setAssignedUser(u1.getName());
		assertEquals(t1.getAssignedUser(), u1.getName());
		assertEquals(t2.getAssignedUser(), null);
	}
	
	@Test
	public void testGetTitle(){
		assertEquals("<html><font size=4><b>Task 1</b></html>", t1.getTitle());
		assertEquals("<html><font size=4><b>Task 2</b></html>", t2.getTitle());
	}
	
	@Test
	public void testParseNewLines(){
		assertEquals("<br>testtest",t1.parseNewLines("\ntesttest"));
		assertEquals("<br><br>testt<br>est<br>",t1.parseNewLines("\n\ntestt\nest\n"));
	}
	
	@Test
	public void testGetContent(){
		t1.setCompleted(true);
		assertEquals("<html><i>Desc1<br><FONT COLOR=\"gray\">No User Assigned</FONT COLOR><br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR></i></html>", t1.getContent());
		assertEquals("<html><i>Desc2<br><FONT COLOR=\"gray\">No User Assigned</FONT COLOR><br><FONT COLOR=\"red\">In Progress</FONT COLOR></i></html>", t2.getContent());
		t1.setDescription("Desc1\nDesc");
		assertEquals("<html><i>Desc1<br>Desc<br><FONT COLOR=\"gray\">No User Assigned</FONT COLOR><br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR></i></html>", t1.getContent());
		t1.setAssignedUser(u1.getName());
		assertEquals("<html><i>Desc1<br>Desc<br><FONT COLOR=\"blue\">Assignee: name</FONT COLOR><br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR></i></html>", t1.getContent());
	}
}
