/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse, Alex Gorowara
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import EDU.purdue.cs.bloat.tree.SRStmt;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Testing for IterationValidator class
 * 
 * An iteration must have a name it must have a start date before its end date
 * if it has requirements, they must all exist in the iteration's project its
 * date range cannot overlap any other iteration in the project a null
 * requirements list will be initialized to empty
 * 
 * CheckNumIssues and CheckNoIssues methods adapted from Defect Tracker
 */

public class IterationValidatorTest {

	IterationValidator validator;
	Iteration goodIteration;
	Requirement goodRequirement;
	Iteration existingIteration;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	User testUser;
	Calendar startCalendar;
	Calendar endCalendar;

	@Before
	public void init() {
		// initialize database and session info
		testUser = new User(null, "testUser", null, -1);
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(testUser, testProject, mockSsid);
		db = new MockData(new HashSet<Object>());
		db.save(testUser);

		// set up validator, good & existing iterations
		startCalendar = Calendar.getInstance();
		endCalendar = Calendar.getInstance();
		startCalendar.set(0, 0, 6);
		endCalendar.set(0, 0, 10);
		validator = new IterationValidator();
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");

		existingIteration = new Iteration("Iteration 1",
				startCalendar.getTime(), endCalendar.getTime(), 0);
		db.save(existingIteration, testProject);
		db.save(goodRequirement, testProject);

		startCalendar.set(0, 0, 10);
		endCalendar.set(0, 0, 12);
		goodIteration = new Iteration("Good Iteration",
				startCalendar.getTime(), endCalendar.getTime(), 1);
	}

	@Test
	// Ensure that good iterations are not rejected
	public void testGoodIteration() {
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.CREATE);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.EDIT);
	}

	@Test
	// Ensure that null iterations are rejected
	public void testNullIteration() {
		Iteration it = null;
		checkNumIssues(1, it, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, it, defaultSession, IterationActionMode.EDIT);
	}

	@Test
	// Ensure that empty name is rejected
	public void testNoName() {
		goodIteration.setName("");
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.CREATE);
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.EDIT);
	}

	@Test
	// Ensure that null name is rejected
	public void testNullName() {
		goodIteration.setName(null);
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.CREATE);
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.EDIT);
	}

	@Test
	// Ensure that whitespace name is rejected
	public void testWhitespaceOnlyName() {
		goodIteration.setName("  	  ");
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.CREATE);
		goodIteration.setName("  	  ");
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.EDIT);
	}

	@Test
	// Ensure that whitespace name is rejected
	public void testWhitespaceTrimName() {
		goodIteration.setName("  s	  ");
		checkNumIssues(0, goodIteration, defaultSession,
				IterationActionMode.CREATE);
		assertEquals("s", goodIteration.getName());
		goodIteration.setName("  s	  ");
		checkNumIssues(0, goodIteration, defaultSession,
				IterationActionMode.EDIT);
		assertEquals("s", goodIteration.getName());
	}

	@Test
	// Ensure that null requirements list is initialized
	public void testNullRequirements() {
		goodIteration.setRequirements(null);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.CREATE);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.EDIT);
		assertEquals(goodIteration.getRequirements(),
				new LinkedList<Requirement>());
	}

	@Test
	// Ensure that incorrect dates (start after they end) cause rejection
	public void testIncorrectDate() {
		startCalendar.set(0, 0, 21);
		endCalendar.set(0, 0, 20);
		Iteration incorrect = new Iteration("Iteration 2",
				startCalendar.getTime(), endCalendar.getTime(), 2);
		checkNumIssues(1, incorrect, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, incorrect, defaultSession, IterationActionMode.EDIT);
	}

	@Test
	// Ensure that null requirements cause rejection on edit
	public void testBadRequirement() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(-1);
		goodIteration.setRequirements(req);
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.EDIT);
	}

	@Test
	// Ensure that good requirements don't rejection on edit
	public void testGoodRequirement() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(0);
		goodIteration.setRequirements(req);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.EDIT);
	}

	@Test
	// Ensure that an iteration with a good and a bad requirement is rejected
	public void testGoodAndBadRequirement() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(0);
		req.add(-1);
		goodIteration.setRequirements(req);
		checkNumIssues(1, goodIteration, defaultSession,
				IterationActionMode.EDIT);
	}

	@Test
	// Ensure that a created iteration has no requirements, even if it had them
	// before
	public void testCreateRequirementList() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(0);
		req.add(-1);
		goodIteration.setRequirements(req);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.CREATE);
		assertEquals(goodIteration.getRequirements(),
				new LinkedList<Requirement>());
	}

	@Test
	// Tests validation on all overlap cases
	public void testOverlapDetection() {
		startCalendar = Calendar.getInstance();
		endCalendar = Calendar.getInstance();

		startCalendar.set(0, 0, 3);
		endCalendar.set(0, 0, 5);
		Iteration strictlyBefore = new Iteration("strictlyBeforeBase",
				startCalendar.getTime(), endCalendar.getTime());

		startCalendar.set(0, 0, 10);
		endCalendar.set(0, 0, 12);
		Iteration strictlyAfter = new Iteration("strictlyAfterBase",
				startCalendar.getTime(), endCalendar.getTime());

		startCalendar.set(0, 0, 3);
		endCalendar.set(0, 0, 7);
		Iteration includesStart = new Iteration("includesBaseStart",
				startCalendar.getTime(), endCalendar.getTime());

		startCalendar.set(0, 0, 12);
		endCalendar.set(0, 0, 9);
		Iteration includesEnd = new Iteration("includesBaseEnd",
				startCalendar.getTime(), endCalendar.getTime());

		startCalendar.set(0, 0, 3);
		endCalendar.set(0, 0, 12);
		Iteration includes = new Iteration("includesBase",
				startCalendar.getTime(), endCalendar.getTime());

		startCalendar.set(0, 0, 6);
		endCalendar.set(0, 0, 9);
		Iteration inside = new Iteration("insideBase", startCalendar.getTime(),
				endCalendar.getTime());

		checkNoIssues(strictlyBefore, defaultSession,
				IterationActionMode.CREATE);
		checkNoIssues(strictlyBefore, defaultSession, IterationActionMode.EDIT);

		checkNoIssues(strictlyAfter, defaultSession, IterationActionMode.CREATE);
		checkNoIssues(strictlyAfter, defaultSession, IterationActionMode.EDIT);

		checkNumIssues(1, includesStart, defaultSession,
				IterationActionMode.CREATE);
		checkNumIssues(1, includesStart, defaultSession,
				IterationActionMode.EDIT);

		checkNumIssues(1, includesEnd, defaultSession,
				IterationActionMode.CREATE);
		checkNumIssues(1, includesEnd, defaultSession, IterationActionMode.EDIT);

		checkNumIssues(1, includes, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, includes, defaultSession, IterationActionMode.EDIT);

		checkNumIssues(1, inside, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, inside, defaultSession, IterationActionMode.EDIT);

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
	public List<ValidationIssue> checkNumIssues(int num, Iteration it,
			Session session, IterationActionMode mode) {
		List<ValidationIssue> issues;
		issues = validator.validate(session, it, mode, db);
		assertEquals(num, issues.size());
		return issues;
	}

	public void checkNoIssues(Iteration it, Session session,
			IterationActionMode mode) {
		checkNumIssues(0, it, session, mode);
	}

}
