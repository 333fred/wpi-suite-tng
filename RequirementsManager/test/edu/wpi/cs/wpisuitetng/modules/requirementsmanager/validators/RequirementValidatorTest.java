/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.RequirementActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.RequirementValidator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
/**
 * @author Jason
 *
 * Testing for RequirementValidator class
 * 	
 * for now, a new requirement must be
 * not null
 * have a non-null name at most 100 characters
 * have a non-null description at most 5000 characters
 * 
 * and when validated should be given a status of NEW and a type of BLANK if null
 */

public class RequirementValidatorTest {
	
	RequirementValidator validator;
	Requirement goodRequirement;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	User testUser;

	
	@Before
	public void init() throws Exception {
		testUser = new User(null, "testUser", null, -1);
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(testUser, testProject, mockSsid);
		db = new MockData(new HashSet<Object>());
		db.save(testUser);

		
		validator = new RequirementValidator(db); 
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");
	}
	
	@Test
	public void testNullCreate() {
		Requirement r = null;
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testGoodRequirementCreate() {
		checkNoIssues(goodRequirement, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testNullNameCreate() {
		Requirement r  = new Requirement();
		r.setName(null);
		r.setDescription("A quality description");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testLongNameCreate() {
		Requirement r  = new Requirement();
		r.setName(new String(new char[101]));
		r.setDescription("A quality description");
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testNoDescriptionCreate() {
		Requirement r  = new Requirement();
		r.setName("Name");
		r.setDescription(null);
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testLinkedListCreate() {
		Requirement r  = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testLongDescriptionCreate() {
		Requirement r  = new Requirement();
		r.setName("Name");
		r.setDescription(new String(new char[5001]));
		checkNumIssues(1, r, defaultSession, RequirementActionMode.CREATE);
	}
	
	@Test
	public void testTypeCreate() {
		Requirement r  = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		
		assertEquals(r.getType(),Type.BLANK);
	}
	
	@Test
	public void testStatusCreate() {
		Requirement r  = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		
		assertEquals(r.getStatus(),Status.NEW);
	}
	
	@Test
	public void testNullLinkedLists() {
		Requirement r  = new Requirement();
		r.setName("Name");
		r.setDescription("Description");
		checkNumIssues(0, r, defaultSession, RequirementActionMode.CREATE);
		
		assertEquals(r.getLog(), new LinkedList<String>());
		assertEquals(r.getNotes(), new LinkedList<String>());
		assertEquals(r.getpUID(), new LinkedList<Integer>());
		assertEquals(r.getSubRequirements(), new LinkedList<Integer>());
		assertEquals(r.gettID(), new LinkedList<Integer>());
	}
	
	public List<ValidationIssue> checkNumIssues(int num, Requirement requirement, Session session, RequirementActionMode mode) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, requirement, mode);
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	public void checkNoIssues(Requirement requirement, Session session, RequirementActionMode mode) {
		checkNumIssues(0, requirement, session, mode);
	}

}