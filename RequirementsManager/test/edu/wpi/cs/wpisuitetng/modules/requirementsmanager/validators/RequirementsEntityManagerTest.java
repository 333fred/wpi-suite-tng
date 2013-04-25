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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers.RequirementsEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.IdManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class RequirementsEntityManagerTest {
	
	Data db;
	RequirementsEntityManager manager;
	Project testProject;
	Project otherProject;
	Session defaultSession;
	String mockSsid;
	User testUser;
	User existingUser;
	Session adminSession;
	
	Requirement goodRequirement;
	Requirement oldRequirement;
	Requirement otherRequirement;
	
	@Before
	public void init() throws Exception {
		final User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "abc123";
		adminSession = new Session(admin, testProject, mockSsid);
		
		existingUser = new User("joe", "joe", "1234", 2);
		oldRequirement = new Requirement();
		oldRequirement.setrUID(1);
		oldRequirement.setName("Old Requirement");
		oldRequirement.setDescription("So Old...");
		
		otherRequirement = new Requirement();
		otherRequirement.setrUID(2);
		otherRequirement.setName("In another project");
		otherRequirement.setDescription("in another project, still");
		
		goodRequirement = new Requirement();
		goodRequirement.setName("Name");
		goodRequirement.setDescription("A quality description");
		
		defaultSession = new Session(existingUser, testProject, mockSsid);
		
		db = new MockData(new HashSet<Object>());
		db.save(oldRequirement, testProject);
		db.save(existingUser);
		db.save(otherRequirement, otherProject);
		db.save(admin);
		manager = new RequirementsEntityManager(db);
	}
	
	@Test (expected = NotImplementedException.class)
	public void testAdvancedGet() throws WPISuiteException {
		manager.advancedGet(defaultSession, new String[0]);
	}
	
	@Test (expected = NotImplementedException.class)
	public void testAdvancedPost() throws WPISuiteException {
		manager.advancedPost(defaultSession, "", "");
	}
	
	@Test (expected = NotImplementedException.class)
	public void testAdvancedPut() throws WPISuiteException {
		manager.advancedPut(defaultSession, new String[0], "");
	}
	
	@Test
	public void testCount() {
		Assert.assertEquals(2, manager.Count());
	}
	
	@Test
	public void testDelete() throws WPISuiteException {
		Assert.assertSame(oldRequirement,
				db.retrieve(Requirement.class, "rUID", 1).get(0));
		Assert.assertTrue(manager.deleteEntity(adminSession, "1"));
		Assert.assertEquals(0, db.retrieve(Requirement.class, "id", 1).size());
	}
	
	@Test
	public void testDeleteAll() throws WPISuiteException {
		final Requirement anotherRequirement = new Requirement();
		anotherRequirement.setrUID(4);
		anotherRequirement.setName("aName");
		anotherRequirement.setDescription("Description");
		manager.makeEntity(defaultSession, anotherRequirement.toJSON());
		Assert.assertEquals(2, db.retrieveAll(new Requirement(), testProject)
				.size());
		manager.deleteAll(adminSession);
		Assert.assertEquals(0, db.retrieveAll(new Requirement(), testProject)
				.size());
		// otherDefect should still be around
		Assert.assertEquals(1, db.retrieveAll(new Requirement(), otherProject)
				.size());
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testDeleteAllNotAllowed() throws WPISuiteException {
		manager.deleteAll(defaultSession);
	}
	
	@Test
	public void testDeleteAllWhenEmpty() throws WPISuiteException {
		manager.deleteAll(adminSession);
		manager.deleteAll(adminSession);
		// no exceptions
	}
	
	@Test (expected = NotFoundException.class)
	public void testDeleteFromOtherProject() throws WPISuiteException {
		manager.deleteEntity(adminSession,
				Integer.toString(otherRequirement.getrUID()));
	}
	
	@Test (expected = NotFoundException.class)
	public void testDeleteMissing() throws WPISuiteException {
		manager.deleteEntity(adminSession, "4534");
	}
	
	@Test (expected = UnauthorizedException.class)
	public void testDeleteNotAllowed() throws WPISuiteException {
		manager.deleteEntity(defaultSession,
				Integer.toString(oldRequirement.getrUID()));
	}
	
	@Test
	public void testGetAll() {
		final Requirement[] gotten = manager.getAll(defaultSession);
		Assert.assertEquals(1, gotten.length);
		Assert.assertSame(oldRequirement, gotten[0]);
	}
	
	@Test (expected = NotFoundException.class)
	public void testGetBadId() throws NotFoundException {
		manager.getEntity(defaultSession, "-1");
	}
	
	@Test
	public void testGetEntity() throws NotFoundException {
		final Requirement[] gotten = manager.getEntity(defaultSession, "1");
		Assert.assertSame(oldRequirement, gotten[0]);
	}
	
	@Test (expected = NotFoundException.class)
	public void testGetMissingEntity() throws NotFoundException {
		manager.getEntity(defaultSession, "9");
	}
	
	@Test (expected = BadRequestException.class)
	public void testMakeBadEntity() throws WPISuiteException {
		final Requirement r = new Requirement();
		// make sure it's being passed through the validator
		manager.makeEntity(defaultSession, r.toJSON());
	}
	
	@Test
	public void testMakeEntity() throws WPISuiteException {
		final IdManager idManager = new IdManager("requirement");
		idManager.setCurId(2);
		db.save(idManager, defaultSession.getProject());
		final Requirement created = manager.makeEntity(defaultSession,
				goodRequirement.toJSON());
		Assert.assertEquals("Name", created.getName());
		Assert.assertSame(
				db.retrieve(Requirement.class, "rUID", created.getrUID(),
						defaultSession.getProject()).get(0), created);
	}
	
	/*
	 * @Test(expected=BadRequestException.class) public void testBadUpdate()
	 * throws WPISuiteException { goodUpdatedDefect.setTitle("");
	 * manager.update(defaultSession, goodUpdatedDefect.toJSON()); }
	 * @Test public void testNoUpdate() throws WPISuiteException { Date
	 * origLastModified = existingDefect.getLastModifiedDate(); Defect updated =
	 * manager.update(defaultSession, existingDefect.toJSON());
	 * assertSame(existingDefect, updated); // there were no changes - make sure
	 * lastModifiedDate is same, no new events assertEquals(origLastModified,
	 * updated.getLastModifiedDate()); assertEquals(0,
	 * updated.getEvents().size()); }
	 * @Test public void testProjectChangeIgnored() throws WPISuiteException {
	 * Defect existingDefectCopy = new Defect(1, "An existing defect", "",
	 * existingUser); existingDefectCopy.setProject(otherProject); Defect
	 * updated = manager.update(defaultSession, existingDefectCopy.toJSON());
	 * assertEquals(0, updated.getEvents().size()); assertSame(testProject,
	 * updated.getProject()); }
	 */
	
	@Test
	// Tests updating a requirement without changes
	public void testNoUpdate() throws WPISuiteException {
		final Requirement updated = manager.update(defaultSession,
				oldRequirement.toJSON());
		Assert.assertSame(oldRequirement, updated);
		Assert.assertEquals(oldRequirement.getName(), updated.getName());
		Assert.assertEquals(oldRequirement.getDescription(),
				updated.getDescription());
		Assert.assertEquals(oldRequirement.getEstimate(), updated.getEstimate());
		Assert.assertEquals(oldRequirement.getIteration(),
				updated.getIteration());
		Assert.assertEquals(oldRequirement.getNotes(), updated.getNotes());
		Assert.assertEquals(oldRequirement.getPriority(), updated.getPriority());
		Assert.assertEquals(oldRequirement.getpUID(), updated.getpUID());
		Assert.assertEquals(oldRequirement.getReleaseNum(),
				updated.getReleaseNum());
		Assert.assertEquals(oldRequirement.getrUID(), updated.getrUID());
		Assert.assertEquals(oldRequirement.getStatus(), updated.getStatus());
		Assert.assertEquals(oldRequirement.getSubRequirements(),
				updated.getSubRequirements());
		Assert.assertEquals(oldRequirement.getTasks(), updated.getTasks());
		Assert.assertEquals(oldRequirement.getType(), updated.getType());
		Assert.assertEquals(oldRequirement.getUsers(), updated.getUsers());
	}
	
	@Test
	public void testSave() throws WPISuiteException {
		final Requirement newRequirement = new Requirement();
		newRequirement.setrUID(3);
		manager.save(defaultSession, newRequirement);
		Assert.assertSame(newRequirement,
				db.retrieve(Requirement.class, "rUID", 3).get(0));
		Assert.assertSame(testProject, newRequirement.getProject());
	}
	
	@Test
	// Tests updating a requirement with name change
	public void testUpdate() throws WPISuiteException {
		Requirement updated = new Requirement();
		oldRequirement.setrUID(1);
		oldRequirement.setName("Update Old Requirement");
		oldRequirement.setDescription("UPDATED");
		oldRequirement.setEstimate(2);
		oldRequirement.setIteration(2);
		oldRequirement.setPriority(Priority.HIGH);
		oldRequirement.setReleaseNum("5");
		oldRequirement.setStatus(Status.BLANK);
		oldRequirement.setType(Type.EPIC);
		
		updated = manager.update(defaultSession, oldRequirement.toJSON());
		Assert.assertEquals("Update Old Requirement", updated.getName());
		Assert.assertEquals("UPDATED", updated.getDescription());
		Assert.assertEquals(2, updated.getEstimate());
		Assert.assertEquals(2, updated.getIteration());
		Assert.assertEquals(oldRequirement.getNotes(), updated.getNotes());
		Assert.assertEquals(Priority.HIGH, updated.getPriority());
		Assert.assertEquals(oldRequirement.getpUID(), updated.getpUID());
		Assert.assertEquals("5", updated.getReleaseNum());
		Assert.assertEquals(oldRequirement.getrUID(), updated.getrUID());
		Assert.assertEquals(Status.BLANK, updated.getStatus());
		Assert.assertEquals(oldRequirement.getSubRequirements(),
				updated.getSubRequirements());
		Assert.assertEquals(oldRequirement.getTasks(), updated.getTasks());
		Assert.assertEquals(Type.EPIC, updated.getType());
		Assert.assertEquals(oldRequirement.getUsers(), updated.getUsers());
	}
	
}
