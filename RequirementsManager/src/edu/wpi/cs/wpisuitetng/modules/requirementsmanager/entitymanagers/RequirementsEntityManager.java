/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Frederic Silberberg
 *    @author Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.ChangesetCallback;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.RequirementActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.IdManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.RequirementValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.ValidationIssue;

/**
 * Entity manager for requirements
 */
public class RequirementsEntityManager implements EntityManager<Requirement> {
	
	Data db;
	RequirementValidator validator;
	ModelMapper updateMapper;
	
	/**
	 * Create a RequirementsEntityManager
	 * 
	 * @param data
	 *            The Data instance to use
	 */
	public RequirementsEntityManager(final Data data) {
		db = data;
		validator = new RequirementValidator(db);
		updateMapper = new ModelMapper();
	}
	
	@Override
	public String advancedGet(final Session s, final String[] args)
			throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	@Override
	public String advancedPost(final Session s, final String string,
			final String content) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	@Override
	public String advancedPut(final Session s, final String[] args,
			final String content) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int Count() {
		// Retrieve all from this project
		return db.retrieveAll(new Requirement()).size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll(final Session s) throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Requirement(), s.getProject());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEntity(final Session s, final String id)
			throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}
	
	/**
	 * Ensures that a given user has given permissions
	 * 
	 * @param session
	 *            the session of the current user
	 * @param role
	 *            the role we are checking
	 * @throws WPISuiteException
	 *             the unauthorized exception
	 */
	private void ensureRole(final Session session, final Role role)
			throws WPISuiteException {
		final User user = (User) db.retrieve(User.class, "username",
				session.getUsername()).get(0);
		if (!user.getRole().equals(role)) {
			throw new UnauthorizedException();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement[] getAll(final Session s) {
		return db.retrieveAll(new Requirement(), s.getProject()).toArray(
				new Requirement[0]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement[] getEntity(final Session s, final String id)
			throws NotFoundException {
		
		final int intId = Integer.parseInt(id);
		if (intId < 1) {
			throw new NotFoundException();
		}
		
		Requirement[] requirements = null;
		try {
			requirements = db.retrieve(Requirement.class, "rUID", intId,
					s.getProject()).toArray(new Requirement[0]);
		} catch (final WPISuiteException e) {
			System.out.println("Requirements Entity Manager, getEntity, Line 151");
			System.out.println("Status: " + e.getStatus());
			System.out.println("Message: " + e.getMessage());
		}
		
		if ((requirements.length < 1) || (requirements[0] == null)) {
			throw new NotFoundException();
		}
		return requirements;
	}
	
	/**
	 * Gets the next valid id for this class
	 * 
	 * @param s
	 *            the current session
	 * @return the new id
	 * @throws WPISuiteException
	 *             if there was a lookup error
	 */
	private int getId(final Session s) throws WPISuiteException {
		IdManager idManager;
		if (db.retrieve(IdManager.class, "type", "requirement", s.getProject())
				.size() != 0) {
			idManager = db.retrieve(IdManager.class, "type", "requirement",
					s.getProject()).toArray(new IdManager[0])[0];
		} else {
			idManager = new IdManager("requirement");
		}
		final int id = idManager.getNextId();
		if (!db.save(idManager, s.getProject())) {
			throw new WPISuiteException();
		}
		return id;
	}
	
	/**
	 * Gets a requirement given an id
	 * 
	 * @param id
	 *            The ID of the requirement to find
	 * @param s
	 *            The session the requirement is in
	 * @return The requested requirement
	 * @throws RequirementNotFoundException
	 *             If the requirement cannot be found, or is invalid, we throw
	 *             this
	 */
	public Requirement getRequirement(final int id, final Session s)
			throws RequirementNotFoundException {
		try {
			// Attempt to get the requirement
			final List<Model> requirement = db.retrieve(Requirement.class,
					"rUID", id, s.getProject());
			
			// Check that the requirement was actually found
			if ((requirement.size() < 1) || (requirement.get(0) == null)) {
				throw new RequirementNotFoundException(id);
			}
			
			// Make sure we actually got a requirement, and return if we have
			// Otherwise, throw an exception
			if (requirement.get(0) instanceof Requirement) {
				return ((Requirement) requirement.get(0));
			} else {
				throw new WPISuiteException();
			}
		} catch (final WPISuiteException e) {
			// If we thew an exception, we didn't find the requirement, so throw
			// a requirement not found exception
			throw new RequirementNotFoundException(id);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement makeEntity(final Session s, final String content)
			throws BadRequestException, WPISuiteException {
		
		final Requirement newRequirement = Requirement.fromJSON(content);
		
		newRequirement.setrUID(getId(s)); // we have to set the UID
		
		final List<ValidationIssue> issues = validator.validate(s,
				newRequirement, RequirementActionMode.CREATE);
		if (issues.size() > 0) {
			for (final ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}
		
		newRequirement.logCreation(s);
		
		if (!db.save(newRequirement, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return newRequirement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(final Session s, final Requirement model) {
		db.save(model, s.getProject());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement update(final Session s, final String content)
			throws WPISuiteException {
		
		// Get updated requirements
		final Requirement updatedRequirement = Requirement.fromJSON(content);
		Requirement oldReq;
		
		// Validate the requirement
		final List<ValidationIssue> issues = validator.validate(s,
				updatedRequirement, RequirementActionMode.EDIT);
		if (issues.size() > 0) {
			throw new BadRequestException();
		}
		
		// Attempt to get the old version of the requirement
		try {
			oldReq = getRequirement(updatedRequirement.getrUID(), s);
		} catch (final RequirementNotFoundException ex) {
			System.out.println("Error: Requirement "
					+ updatedRequirement.getrUID() + " not found.");
			throw new WPISuiteException();
		}
		
		// Set up the changeset callback
		final RequirementChangeset changeset = new RequirementChangeset(
				s.getUser());
		final ChangesetCallback callback = new ChangesetCallback(changeset);
		
		// Copy values from the new requirement to the old requirement
		updateMapper.map(updatedRequirement, oldReq, callback);
		
		// If the user actually changed something, add the changeset to the
		// requirement and save the requirement. Otherwise, do nothing
		if (changeset.getChanges().size() > 0) {
			oldReq.logEvents(changeset);
			// Save the requirement, and throw an exception if if fails
			if (!db.save(oldReq, s.getProject()) || !db.save(oldReq.getLogs())) {
				throw new WPISuiteException();
			}
		}
		
		return oldReq;
	}
	
}
