package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * @author Jason Whitehouse
 *
 * Testing for IterationValidator class
 * 	
 * An iteration must have a name
 * it must have a start date before its end date
 * if it has requirements, they must all exist in the iteration's project
 * its date range cannot overlap any other iteration in the project
 * a null requirements list will be initialized to empty
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

	
	@Before
	public void init() {
		//initialize database and session info
		testUser = new User(null, "testUser", null, -1);
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(testUser, testProject, mockSsid);
		db = new MockData(new HashSet<Object>());
		db.save(testUser);

		//set up validator, good & existing iterations
		validator = new IterationValidator();
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");

		existingIteration = new Iteration("Iteration 1", new Date(0),new Date(60000), 0);
		db.save(existingIteration, testProject);
		db.save(goodRequirement, testProject);

		goodIteration = new Iteration("Iteration 1", new Date(120000),new Date(180000), 1);
	}
	
	@Test
	//Ensure that good iterations are not rejected
	public void testGoodIteration() {
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.CREATE);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that null iterations are rejected
	public void testNullIteration() {
		Iteration it = null;
		checkNumIssues(1, it, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, it, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that empty name is rejected
	public void testNoName() {
		goodIteration.setName("");
		checkNumIssues(1, goodIteration, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, goodIteration, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that null name is rejected
	public void testNullName() {
		goodIteration.setName(null);
		checkNumIssues(1, goodIteration, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, goodIteration, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that null requirements list is initialized
	public void testNullRequirements() {
		goodIteration.setRequirements(null);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.CREATE);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.EDIT);
		assertEquals(goodIteration.getRequirements(), new LinkedList<Requirement>());
	}
	
	@Test
	//Ensure that incorrect dates (start after they end) cause rejection
	public void testIncorrectDate() {
		Iteration incorrect = new Iteration("Iteration 2", new Date(90000),new Date(80000), 2);
		checkNumIssues(1, incorrect, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, incorrect, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that null requirements cause rejection on edit
	public void testBadRequirement() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(-1);
		goodIteration.setRequirements(req);
		checkNumIssues(1, goodIteration, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that good requirements don't rejection on edit
	public void testGoodRequirement() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(0);
		goodIteration.setRequirements(req);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that an iteration with a good and a bad requirement is rejected
	public void testGoodAndBadRequirement() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(0);
		req.add(-1);
		goodIteration.setRequirements(req);
		checkNumIssues(1, goodIteration, defaultSession, IterationActionMode.EDIT);
	}
	
	@Test
	//Ensure that a created iteration has no requirements, even if it had them before
	public void testCreateRequirementList() {
		LinkedList<Integer> req = new LinkedList<Integer>();
		req.add(0);
		req.add(-1);
		goodIteration.setRequirements(req);
		checkNoIssues(goodIteration, defaultSession, IterationActionMode.CREATE);
		assertEquals(goodIteration.getRequirements(), new LinkedList<Requirement>());
	}
	
	@Test
	//Ensure that overlapping iteration will be rejected
	public void testOverlap() {
		Iteration overlapping = new Iteration("Iteration 2", new Date(8000),new Date(9000), 2);
		checkNumIssues(1, overlapping, defaultSession, IterationActionMode.CREATE);
		checkNumIssues(1, overlapping, defaultSession, IterationActionMode.EDIT);
	}
	
	/**
	 * Test to confirm that overlapping iterations are recognized as such
	 */
	@Test
	public void testOverlapDetection(){
		Iteration base = new Iteration("base", new Date(1000), new Date(1500));
		Iteration strictlyBeforeBase = new Iteration("strictlyBeforeBase", new Date(0), new Date(500));
		Iteration strictlyAfterBase = new Iteration("strictlyAfterBase", new Date(2000), new Date(2500));
		Iteration includesBaseStart = new Iteration("includesBaseStart", new Date(750), new Date(1250));
		Iteration includesBaseEnd = new Iteration("includesBaseEnd", new Date(1250), new Date(1750));
		Iteration includesBase = new Iteration("includesBase", new Date(750), new Date(1750));
		Iteration insideBase = new Iteration("insideBase", new Date(1125), new Date(1375));
		
		assertFalse(IterationValidator.overlapExists(base, strictlyBeforeBase));
		assertFalse(IterationValidator.overlapExists(base, strictlyAfterBase));
		assertTrue(IterationValidator.overlapExists(base, includesBaseStart));
		assertTrue(IterationValidator.overlapExists(base, includesBaseEnd));
		assertTrue(IterationValidator.overlapExists(base, includesBase));
		assertTrue(IterationValidator.overlapExists(base, insideBase));
		
	}
	
	/**
	 * 
	 * @param num number of issues expected, if any other number are found will cause test failure
	 * @param requirement the requirement to test
	 * @param session the current session
	 * @param mode the action mode of the validation (EDIT or CREATE)
	 * @return a list of all validation issues found
	 */
	public List<ValidationIssue> checkNumIssues(int num, Iteration it, Session session, IterationActionMode mode) {
		List<ValidationIssue> issues;
		issues = validator.validate(session, it, mode, db);
		assertEquals(num, issues.size());
		return issues;
	}
	
	public void checkNoIssues(Iteration it, Session session, IterationActionMode mode) {
		checkNumIssues(0, it, session, mode);
	}

}
