/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Gorowara
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.ATest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;

public class LoggerTest {
	
	Requirement requirement;
	User user;
	Session session;
	
	@Before
	public void setUp() {
		
		requirement = new Requirement();
		user = new User("John Doe", "JDoe", "Password", 7);
		session = new Session(user, "ssid");
		
	}
	
	/**
	 * Test to confirm that a creation message can be generated for a
	 * requirement.
	 */
	@Test
	public void testCreationLog() {
		
		requirement = new Requirement();
		requirement.logCreation(session);
		final List<RequirementChangeset> reqChangeList = requirement.getLogs();
		final Map<String, FieldChange<?>> map = reqChangeList.get(0)
				.getChanges();
		
		Assert.assertEquals(reqChangeList.size(), 1); // confirm that there is
														// only one
		// set of recorded changes
		Assert.assertEquals(map.keySet().size(), 1); // confirm that only one
														// change
		// has been made in that set
		Assert.assertTrue(map.containsKey("creation")); // confirm that that one
														// change
		// is a creation change
		
	}
	
	/**
	 * Test to confirm that a series of events can be added to a log as a single
	 * log.
	 */
	@Test
	public void testMultipleEvents() {
		
		requirement = new Requirement();
		final RequirementChangeset reqChange = new RequirementChangeset(user);
		reqChange.getChanges().put("TYPE_A",
				new FieldChange<String>("OLD_A", "NEW_A"));
		reqChange.getChanges().put(
				"TYPE_B",
				new FieldChange<Exception>(new Exception("OLD_B"),
						new Exception("NEW_B")));
		requirement.logEvents(reqChange);
		
		Assert.assertEquals(requirement.getLogs().size(), 1); // confirm that
																// only one
		// log has been added
		Assert.assertEquals(requirement.getLogs().get(0).getChanges().keySet()
				.size(), 2); // confirm that this one log has recorded two
								// changes
		Assert.assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_A")); // confirm that change A has been added
		Assert.assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B")); // confirm that change B has been added
		Assert.assertEquals(
				requirement.getLogs().get(0).getChanges().get("TYPE_B")
						.getOldValue().toString(),
				new Exception("OLD_B").toString()); // confirm
													// one
													// value
													// as
													// a
													// sample
		
	}
	
	/**
	 * Test to confirm that a series of individual changes can be added as
	 * multiple logs.
	 */
	@Test
	public void testMultipleLogs() {
		
		requirement = new Requirement();
		
		final RequirementChangeset changeA = new RequirementChangeset(user);
		changeA.getChanges().put("TYPE_A",
				new FieldChange<String>("OLD_A", "NEW_A"));
		requirement.logEvents(changeA);
		
		final RequirementChangeset changeB = new RequirementChangeset(user);
		changeB.getChanges().put("TYPE_B",
				new FieldChange<String>("OLD_B", "NEW_B"));
		requirement.logEvents(changeB);
		
		Assert.assertEquals(requirement.getLogs().size(), 2); // confirm that
																// two and
		// only two logs have
		// been added
		Assert.assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B")); // confirm that the most recently added
											// log is first
		Assert.assertTrue(requirement.getLogs().get(1).getChanges()
				.containsKey("TYPE_A")); // confirm that the other log still
											// exists
		
	}
	
	/**
	 * Test to confirm that a series of multiple-change events can be added as
	 * multiple logs.
	 */
	@Test
	public void testMultipleMultipleChangeLogs() {
		
		requirement = new Requirement();
		
		requirement.logCreation(session);
		
		final RequirementChangeset changeA = new RequirementChangeset(user);
		changeA.getChanges().put("TYPE_A1",
				new FieldChange<String>("OLD_A1", "NEW_A1"));
		changeA.getChanges().put("TYPE_A2",
				new FieldChange<Object>(new Object(), new Object()));
		requirement.logEvents(changeA);
		
		final RequirementChangeset changeB = new RequirementChangeset(user);
		changeB.getChanges().put(
				"TYPE_B1",
				new FieldChange<Exception>(new Exception("OLD_B1"),
						new Exception("NEW_B")));
		changeB.getChanges().put(
				"TYPE_B2",
				new FieldChange<Requirement>(new Requirement(),
						new Requirement()));
		requirement.logEvents(changeB);
		
		Assert.assertEquals(requirement.getLogs().size(), 3); // confirm that
																// three and
		// only three logs have
		// been added
		Assert.assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B1")); // confirm that the most recently
											// added log is first
		Assert.assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B2")); // confirm that all changes have been
											// added to the most recent log
		
	}
	
	/**
	 * Test to confirm that a single event can be added to a log.
	 */
	@Test
	public void testSingleEvent() {
		
		requirement = new Requirement();
		final RequirementChangeset reqChange = new RequirementChangeset(user);
		reqChange.getChanges().put("TYPE",
				new FieldChange<String>("OLD", "NEW"));
		requirement.logEvents(reqChange);
		
		Assert.assertEquals(requirement.getLogs().size(), 1); // confirm that
																// one and
		// only one changeset
		// has been added
		Assert.assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE")); // confirm that a change of type TYPE was
										// logged
										// to the zeroth and only changeset
		Assert.assertTrue(requirement.getLogs().get(0).getChanges().get("TYPE")
				.getOldValue().equals("OLD")); // confirm that the old value was
												// saved
		Assert.assertTrue(requirement.getLogs().get(0).getChanges().get("TYPE")
				.getNewValue().equals("NEW")); // conrfim that the new value was
												// saved
		
	}
	
	
	@Test
	public void testAllChanges(){
		final String[] changeTypes = {"creation", "name", "description", "type", "priority", "status", 
		                            "releaseNum", "iteration", "estimate", "effort", "users", "subRequirements", 
		                            "pUID", "notes", "tasks", "aTests"};
		HashMap<String, FieldChange<?>> map = new HashMap<String, FieldChange<?>>();
		for(String type : changeTypes){
			FieldChange change;
			if(type.equals("creation")){
				continue;	// skip this to avoid making a creation-only changeset
			}
			else if(type.equals("type")){
				change = new FieldChange<Type>(Type.USER_STORY, Type.NON_FUNCTIONAL);
			}
			else if(type.equals("priority")){
				change = new FieldChange<Priority>(Priority.HIGH, Priority.LOW);
			}
			else if(type.equals("status")){
				change = new FieldChange<Status>(Status.IN_PROGRESS, Status.OPEN);
			}
			else if(type.equals("iteration")){
				Iteration oldItr = new Iteration();
				oldItr.setId(0);
				IterationDatabase.getInstance().add(oldItr);
				Iteration newItr = new Iteration();
				newItr.setId(1);
				IterationDatabase.getInstance().add(newItr);
				change = new FieldChange<Iteration>(oldItr, newItr);
			}
			else if(type.equals("users")){
				User addedOne = new User("Add Name", "addname", "plus", 0);
				User addedTwo = new User("Plus Name", "pluname", "add", 1);
				User removed = new User("Remove Name", "remname", "minus", 2);
				List<User> addedUsers = new ArrayList<User>();
				List<User> removedUsers = new ArrayList<User>();
				addedUsers.add(addedOne);
				addedUsers.add(addedTwo);
				removedUsers.add(removed);
				change = new FieldChange<List<User>>(addedUsers, removedUsers);
			}
			else if(type.equals("subRequirements")){
				change = new FieldChange<List<Requirement>>(new ArrayList<Requirement>(), new ArrayList<Requirement>());
				// there won't be RequirementDatabase support for the test requirements anyway
			}
			else if(type.equals("pUID")){
				change = new FieldChange<List<Requirement>>(new ArrayList<Requirement>(), new ArrayList<Requirement>());	// or for these
			}
			else if(type.equals("notes")){
				List<String> addedNotes = new ArrayList<String>();
				List<String> removedNotes = new ArrayList<String>();
				addedNotes.add("new");
				addedNotes.add("notes");
				removedNotes.add("nothing");
				change = new FieldChange<List<String>>(addedNotes, removedNotes);
			}
			else if(type.equals("tasks")){
				List<Task> addedTasks = new ArrayList<Task>();
				List<Task> removedTasks = new ArrayList<Task>();
				addedTasks.add(new Task("name", "desc"));
				change = new FieldChange<List<Task>>(addedTasks, removedTasks);
			}
			else if(type.equals("aTests")){
				List<ATest> addedTests = new ArrayList<ATest>();
				List<ATest> removedTests = new ArrayList<ATest>();
				removedTests.add(new ATest("name", "desc"));
				change = new FieldChange<List<ATest>>(addedTests, removedTests);
			}
			else{
				change = new FieldChange<String>("old", "new");
			}
			map.put(type, change);
		}
		RequirementChangeset rc = new RequirementChangeset();
		rc.setChanges(map);
		assertNotNull(rc.getContent());
	}
		
	/**
	 * Trivial test to ensure that JUnit is playing nicely; should always pass.
	 */
	@Test
	public void trivialTest() {
		
		Assert.assertEquals(1, 1);
		
	}
	
}
