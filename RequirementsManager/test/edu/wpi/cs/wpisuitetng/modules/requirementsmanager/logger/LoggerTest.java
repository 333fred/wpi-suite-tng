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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
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
	 * Trivial test to ensure that JUnit is playing nicely; should always pass.
	 */
	@Test
	public void trivialTest() {

		assertEquals(1, 1);

	}

	/**
	 * Test to confirm that a creation message can be generated for a
	 * requirement.
	 */
	@Test
	public void testCreationLog() {

		requirement = new Requirement();
		requirement.logCreation(session);
		List<RequirementChangeset> reqChangeList = requirement.getLogs();
		Map<String, FieldChange<?>> map = reqChangeList.get(0).getChanges();

		assertEquals(reqChangeList.size(), 1); // confirm that there is only one
												// set of recorded changes
		assertEquals(map.keySet().size(), 1); // confirm that only one change
												// has been made in that set
		assertTrue(map.containsKey("creation")); // confirm that that one change
													// is a creation change

	}

	/**
	 * Test to confirm that a single event can be added to a log.
	 */
	@Test
	public void testSingleEvent() {

		requirement = new Requirement();
		RequirementChangeset reqChange = new RequirementChangeset(user);
		reqChange.getChanges().put("TYPE",
				new FieldChange<String>("OLD", "NEW"));
		requirement.logEvents(reqChange);

		assertEquals(requirement.getLogs().size(), 1); // confirm that one and
														// only one changeset
														// has been added
		assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE")); // confirm that a change of type TYPE was
										// logged
										// to the zeroth and only changeset
		assertTrue(requirement.getLogs().get(0).getChanges().get("TYPE")
				.getOldValue().equals("OLD")); // confirm that the old value was
												// saved
		assertTrue(requirement.getLogs().get(0).getChanges().get("TYPE")
				.getNewValue().equals("NEW")); // conrfim that the new value was
												// saved

	}

	/**
	 * Test to confirm that a series of events can be added to a log as a single
	 * log.
	 */
	@Test
	public void testMultipleEvents() {

		requirement = new Requirement();
		RequirementChangeset reqChange = new RequirementChangeset(user);
		reqChange.getChanges().put("TYPE_A",
				new FieldChange<String>("OLD_A", "NEW_A"));
		reqChange.getChanges().put(
				"TYPE_B",
				new FieldChange<Exception>(new Exception("OLD_B"),
						new Exception("NEW_B")));
		requirement.logEvents(reqChange);

		assertEquals(requirement.getLogs().size(), 1); // confirm that only one
														// log has been added
		assertEquals(requirement.getLogs().get(0).getChanges().keySet().size(),
				2); // confirm that this one log has recorded two changes
		assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_A")); // confirm that change A has been added
		assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B")); // confirm that change B has been added
		assertEquals(requirement.getLogs().get(0).getChanges().get("TYPE_B")
				.getOldValue().toString(), new Exception("OLD_B").toString()); // confirm
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

		RequirementChangeset changeA = new RequirementChangeset(user);
		changeA.getChanges().put("TYPE_A",
				new FieldChange<String>("OLD_A", "NEW_A"));
		requirement.logEvents(changeA);

		RequirementChangeset changeB = new RequirementChangeset(user);
		changeB.getChanges().put("TYPE_B",
				new FieldChange<String>("OLD_B", "NEW_B"));
		requirement.logEvents(changeB);

		assertEquals(requirement.getLogs().size(), 2); // confirm that two and
														// only two logs have
														// been added
		assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B")); // confirm that the most recently added
											// log is first
		assertTrue(requirement.getLogs().get(1).getChanges()
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

		RequirementChangeset changeA = new RequirementChangeset(user);
		changeA.getChanges().put("TYPE_A1",
				new FieldChange<String>("OLD_A1", "NEW_A1"));
		changeA.getChanges().put("TYPE_A2",
				new FieldChange<Object>(new Object(), new Object()));
		requirement.logEvents(changeA);

		RequirementChangeset changeB = new RequirementChangeset(user);
		changeB.getChanges().put(
				"TYPE_B1",
				new FieldChange<Exception>(new Exception("OLD_B1"),
						new Exception("NEW_B")));
		changeB.getChanges().put(
				"TYPE_B2",
				new FieldChange<Requirement>(new Requirement(),
						new Requirement()));
		requirement.logEvents(changeB);

		assertEquals(requirement.getLogs().size(), 3); // confirm that three and
														// only three logs have
														// been added
		assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B1")); // confirm that the most recently
											// added log is first
		assertTrue(requirement.getLogs().get(0).getChanges()
				.containsKey("TYPE_B2")); // confirm that all changes have been
											// added to the most recent log

	}

}
