/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
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
	public RequirementsEntityManager(Data data) {
		db = data;
		validator = new RequirementValidator(db);
		updateMapper = new ModelMapper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {

		final Requirement newRequirement = Requirement.fromJSON(content);

		newRequirement.setrUID(getId(s)); // we have to set the UID

		List<ValidationIssue> issues = validator.validate(s, newRequirement,
				RequirementActionMode.CREATE);
		if (issues.size() > 0) {
			// TODO: pass errors to client through exception
			for (ValidationIssue issue : issues) {
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
	public Requirement[] getEntity(Session s, String id)
			throws NotFoundException {

		final int intId = Integer.parseInt(id);
		if (intId < 1) {
			throw new NotFoundException();
		}

		Requirement[] requirements = null;
		try {
			requirements = db.retrieve(Requirement.class, "rUID", intId,
					s.getProject()).toArray(new Requirement[0]);
		} catch (WPISuiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (requirements.length < 1 || requirements[0] == null) {
			throw new NotFoundException();
		}
		return requirements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Requirement[] getAll(Session s) {
		return db.retrieveAll(new Requirement(), s.getProject()).toArray(
				new Requirement[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, Requirement model) {
		db.save(model, s.getProject());
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
	private void ensureRole(Session session, Role role)
			throws WPISuiteException {
		User user = (User) db.retrieve(User.class, "username",
				session.getUsername()).get(0);
		if (!user.getRole().equals(role)) {
			throw new UnauthorizedException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO should user need to be Admin?
		ensureRole(s, Role.ADMIN);
		return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Requirement(), s.getProject());
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
	public Requirement update(Session s, String content)
			throws WPISuiteException {

		// Get updated requirements
		Requirement updatedRequirement = Requirement.fromJSON(content);
		Requirement oldReq;

		// Validate the requirement
		List<ValidationIssue> issues = validator.validate(s,
				updatedRequirement, RequirementActionMode.EDIT);
		if (issues.size() > 0) {
			throw new BadRequestException();
		}

		// Attempt to get the old version of the requirement
		try {
			oldReq = getRequirement(updatedRequirement.getrUID(), s);
		} catch (RequirementNotFoundException ex) {
			System.out.println("Error: Requirement "
					+ updatedRequirement.getrUID() + " not found.");
			throw new WPISuiteException();
		}

		// Set up the changeset callback
		RequirementChangeset changeset = new RequirementChangeset(s.getUser());
		ChangesetCallback callback = new ChangesetCallback(changeset);

		// Save the iteration values
		int oldIteration = oldReq.getIteration();
		int newIteration = updatedRequirement.getIteration();
		
//		if (updatedRequirement.getStatus() == Status.DELETED){
//			Integer parentID = updatedRequirement.getpUID().get(0);
//			Integer reqID = updatedRequirement.getrUID();
//			try {
//				Requirement parent;
//				parent = getRequirement(parentID,s);
//				parent.removeSubRequirement(reqID);
//				update(s,parent.toJSON());
//				updatedRequirement.removePUID(parentID);
//			} catch (RequirementNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
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
	public Requirement getRequirement(int id, Session s)
			throws RequirementNotFoundException {
		try {
			// Attempt to get the requirement
			List<Model> requirement = db.retrieve(Requirement.class, "rUID",
					id, s.getProject());

			// Check that the requirement was actually found
			if (requirement.size() < 1 || requirement.get(0) == null) {
				throw new RequirementNotFoundException(id);
			}

			// Make sure we actually got a requirement, and return if we have
			// Otherwise, throw an exception
			if (requirement.get(0) instanceof Requirement) {
				return ((Requirement) requirement.get(0));
			} else {
				throw new WPISuiteException();
			}
		} catch (WPISuiteException e) {
			// If we thew an exception, we didn't find the requirement, so throw
			// a requirement not found exception
			throw new RequirementNotFoundException(id);
		}
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
	private int getId(Session s) throws WPISuiteException {
		try {
			IdManager idManager;
			if (db.retrieve(IdManager.class, "type", "requirement",
					s.getProject()).size() != 0) {
				idManager = db.retrieve(IdManager.class, "type", "requirement",
						s.getProject()).toArray(new IdManager[0])[0];
			} else {
				idManager = new IdManager("requirement");
			}
			int id = idManager.getNextId();
			if (!db.save(idManager, s.getProject())) {
				throw new WPISuiteException();
			}
			return id;
		} catch (WPISuiteException ex) {
			throw ex;
		}
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

}
