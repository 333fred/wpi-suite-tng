/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.RequirementActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;

/**
 * Testing for RequirementValidator class
 * 
 * for now, a new requirement must be not null have a non-null name at most 100
 * characters have a non-null description at most 5000 characters
 * 
 * and when validated should be given a status of NEW and a type of BLANK if
 * null
 * 
 * CheckNumIssues and CheckNoIssues methods adapted from Defect Tracker
 */

public class RequirementValidatorTest {
	
	RequirementValidator validator;
	Requirement goodRequirement;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	User testUser;
	
	public void checkNoIssues(final Requirement requirement,
			final Session session, final RequirementActionMode mode) {
		checkNumIssues(0, requirement, session, mode);
	}
	
	/**
	 * 
	 * @param num
	 *            number of issues expected, if any other number are found will
	 *            cause test failure
	 * @param requirement
	 *            the requirement to test
	 * @param session
	 *            the current session
	 * @param mode
	 *            the action mode of the validation (EDIT or CREATE)
	 * @return a list of all validation issues found
	 */
	public List<ValidationIssue> checkNumIssues(final int num,
			final Requirement requirement, final Session session,
			final RequirementActionMode mode) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, requirement, mode);
			Assert.assertEquals(num, issues.size());
		} catch (final WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	@Before
	public void init() throws Exception {
		// initialize database and session info
		testUser = new User(null, "testUser", null, -1);
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(testUser, testProject, mockSsid);
		db = new MockData(new HashSet<Object>());
		db.save(testUser);
		
		// initialize good requirement and validator
		validator = new RequirementValidator(db);
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");
	}
	
	@Test
	public void testDeletedAssignment() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setStatus(Status.DELETED);
		r.setIteration(1);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		Assert.assertEquals(r.getIteration(), -2);
	}
	
	@Test
	// Test that an empty description will be rejected
	public void testEmptyDescription() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// Ensure that an empty name will cause rejection
	public void testEmptyName() {
		final Requirement r = new Requirement();
		r.setName("");
		r.setDescription("A quality description");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	public void testGetData() {
		Assert.assertEquals(validator.getData(), db);
	}
	
	@Test
	// Test that the most basic requirement (name and description only) will be
	// valid
	public void testGoodRequirementCreate() {
		checkNoIssues(goodRequirement, defaultSession,
				RequirementActionMode.CREATE);
		checkNoIssues(goodRequirement, defaultSession,
				RequirementActionMode.EDIT);
	}
	
	@Test
	// ensure that when a new requirement is assigned to an iteration
	// it will be changed to in progress
	// dependent on further implementation of setIteration
	public void testInProgressRemoveFromIteration() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setStatus(Status.IN_PROGRESS);
		r.setIteration(-1);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		Assert.assertEquals(r.getStatus(), Status.OPEN);
	}
	
	@Test
	// Test that a requirement with an invalid user will be rejected
	public void testInvalidUser() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.addUser("NotAUser");
		r.setDescription("Description");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	// Ensure that a name longer than 100 characters is rejected
	public void testLongName() {
		final Requirement r = new Requirement();
		r.setName(new String(new char[101]));
		r.setDescription("A quality description");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	public void testNegativeEffort() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setEffort(-1);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	public void testNegativeEstimate() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setEstimate(-1);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// ensure that when a new requirement is assigned to an iteration
	// it will be changed to in progress
	public void testNewAssignIteration() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setStatus(Status.NEW);
		r.setIteration(1);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		Assert.assertEquals(r.getStatus(), Status.IN_PROGRESS);
	}
	
	@Test
	// Test that a tab whitespace description will be rejected
	public void testNewlineDescription() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("\n\n\n");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// Test that a null description will be rejected
	public void testNoDescription() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription(null);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// Make sure that a null requirement will be rejected
	public void testNullCreate() {
		final Requirement r = null;
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// ensure that null linkedlists are properly initialized on creation
	public void testNullLinkedLists() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setNotes(null);
		r.setpUID(null);
		r.setSubRequirements(null);
		r.setUsers(null);
		r.setTasks(null);
		r.setLog(null);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		
		Assert.assertEquals(r.getLogs(), new LinkedList<RequirementChangeset>());
		Assert.assertEquals(r.getNotes(), new LinkedList<String>());
		Assert.assertEquals(r.getpUID(), new LinkedList<Integer>());
		Assert.assertEquals(r.getSubRequirements(), new LinkedList<Integer>());
		Assert.assertEquals(r.getUsers(), new LinkedList<String>());
		Assert.assertEquals(r.getTasks(), new LinkedList<Task>());
	}
	
	@Test
	// ensure that null linkedlists are properly initialized on edit
	// This is a separate test because we are checking the modifications done by
	// the validator
	// instead of just the result
	public void testNullLinkedListsEdit() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		
		Assert.assertEquals(r.getNotes(), new LinkedList<String>());
		Assert.assertEquals(r.getpUID(), new LinkedList<Integer>());
		Assert.assertEquals(r.getSubRequirements(), new LinkedList<Integer>());
		Assert.assertEquals(r.getUsers(), new LinkedList<String>());
	}
	
	@Test
	// Ensure that a null name will cause rejection
	public void testNullName() {
		final Requirement r = new Requirement();
		r.setName(null);
		r.setDescription("A quality description");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	public void testNullType() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setType(null);
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		Assert.assertEquals(r.getStatus(), Status.NEW);
	}
	
	@Test
	// ensure that when an open requirement is assigned to an iteration
	// it will be changed to in progress
	public void testOpenAssignIteration() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setStatus(Status.OPEN);
		r.setIteration(1);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		Assert.assertEquals(r.getStatus(), Status.IN_PROGRESS);
	}
	
	@Test
	public void testSetData() {
		final Data db2 = new MockData(new HashSet<Object>());
		validator.setData(db2);
		Assert.assertEquals(validator.getData(), db2);
	}
	
	@Test
	// Ensure that the status of any created requirement is 'NEW'
	public void testStatusCreate() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		
		Assert.assertEquals(r.getStatus(), Status.NEW);
	}
	
	@Test
	// Ensure that the status of any edited requirement is not set to 'NEW' in
	// validation
	public void testStatusEdit() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		r.setStatus(Status.OPEN);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		
		Assert.assertEquals(r.getStatus(), Status.OPEN);
	}
	
	@Test
	// Test that a tab whitespace description will be rejected
	public void testTabDescription() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("			");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// Test that a requirement created with no type will have its type set to
	// BLANK
	public void testTypeCreate() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		
		Assert.assertEquals(r.getType(), Type.BLANK);
	}
	
	@Test
	// Test that a requirement with an invalid user will be rejected
	public void testValidUser() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.addUser("testUser");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	// Test that a whitespace description will be rejected
	public void testWhitespaceDescription() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("      ");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.EDIT);
	}
	
	@Test
	// Test that a tab whitespace description will be rejected
	public void testWhitespaceTrim() {
		final Requirement r = new Requirement();
		r.setName("Name");
		r.setDescription("   testing   ");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		checkNumIssues(0, r, defaultSession, RequirementActionMode.EDIT);
		Assert.assertEquals("testing", r.getDescription());
	}
	
}
