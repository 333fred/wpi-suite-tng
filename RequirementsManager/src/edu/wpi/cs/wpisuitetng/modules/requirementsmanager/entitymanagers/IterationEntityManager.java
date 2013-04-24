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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.IdManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.ValidationIssue;

public class IterationEntityManager implements EntityManager<Iteration> {

	Data db;
	IterationValidator validator;
	ModelMapper updateMapper;

	public IterationEntityManager(Data db) {
		this.db = db;
		this.validator = new IterationValidator();
		this.updateMapper = new ModelMapper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration makeEntity(Session s, String content)
			throws BadRequestException, WPISuiteException {

		// Get the iteration from JSON
		final Iteration newIteration = Iteration.fromJSON(content);

		// Get the id for the new iteration
		newIteration.setId(getId(s));

		// Validate the new iteration
		List<ValidationIssue> issues = validator.validate(s, newIteration,
				IterationActionMode.CREATE, db);

		// If there were issues, then print the errors and notify the client
		if (issues.size() > 0) {
			for (ValidationIssue issue : issues) {
				System.out.println(issue.getMessage());
			}
			throw new BadRequestException();
		}

		// Save the iteration
		if (!db.save(newIteration, s.getProject())) {
			throw new WPISuiteException();
		}

		return newIteration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration[] getEntity(Session s, String id) throws NotFoundException {

		final int intId = Integer.parseInt(id);
		if (intId < 1) {
			throw new NotFoundException();
		}

		Iteration[] iterations = null;
		try {
			iterations = db.retrieve(Iteration.class, "id", intId,
					s.getProject()).toArray(new Iteration[0]);
		} catch (WPISuiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (iterations.length < 1 || iterations[0] == null) {
			throw new NotFoundException();
		}
		return iterations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration[] getAll(Session s) throws WPISuiteException {
		Iteration[] is = db.retrieveAll(new Iteration(), s.getProject())
				.toArray(new Iteration[0]);

		// there must always be a backlog iteration with id of -1
		// so if we don't find one already, we create it
		boolean backlogExists = false;

		// check if any of the iterations we retrieved are the backlog
		for (Iteration it : is) {
			if (it.getId() == -1) {
				backlogExists = true;
				break;
			}
		}

		// If we don't find one, then we have to create it and save it to the
		// database
		if (!backlogExists) {
			// Save the iteration
			System.out.println("No Backlog Iteration found! Creating one.");
			Iteration backlog = new Iteration("Backlog", new Date(0), new Date(
					60000));
			backlog.setId(-1);
			if (!db.save(backlog, s.getProject())) {
				throw new WPISuiteException();
			}

			// Now add it to our array to return
			is = Arrays.copyOf(is, is.length + 1);
			is[is.length - 1] = backlog;
		}

		boolean deletedExists = false;

		// check if any of the iterations we retrieved are the backlog
		for (Iteration it : is) {
			if (it.getId() == -2) {
				deletedExists = true;
				break;
			}
		}

		// If we don't find one, then we have to create it and save it to the
		// database
		if (!deletedExists) {
			// Save the iteration
			System.out.println("No Deleted Iteration found! Creating one.");
			Iteration deleted = new Iteration("Deleted", new Date(
					Long.MAX_VALUE - 60000), new Date(Long.MAX_VALUE));
			deleted.setId(-2);
			if (!db.save(deleted, s.getProject())) {
				throw new WPISuiteException();
			}

			// Now add it to our array to return
			is = Arrays.copyOf(is, is.length + 1);
			is[is.length - 1] = deleted;
		}

		return is;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration update(Session s, String content) throws WPISuiteException {

		// Get updated iterations
		Iteration updatedIteration = Iteration.fromJSON(content);
		Iteration oldIteration;

		// Validate the iteration
		List<ValidationIssue> issues = validator.validate(s, updatedIteration,
				IterationActionMode.EDIT, db);
		if (issues.size() > 0) {
			for (ValidationIssue i : issues) {
				System.out.println(i.getMessage());
			}
			throw new BadRequestException();
		}

		// Attempt to get the old version of the iteration
		try {
			oldIteration = getIteration(updatedIteration.getId(), s);
		} catch (IterationNotFoundException ex) {
			System.out.println("Error: Iteration " + updatedIteration.getId()
					+ " not found.");
			throw new WPISuiteException();
		}

		/*
		 * TODO: Determine if we want to actually log changes // Set up the
		 * changeset callback IterationChangeset changeset = new
		 * IterationChangeset(s.getUser()); ChangesetCallback callback = new
		 * ChangesetCallback(changeset);
		 */
		// Copy values from the new iteration to the old iteration
		updateMapper.map(updatedIteration, oldIteration);
		/*
		 * // If the user actually changed something, add the changeset to the
		 * // iteration and save the iteration. Otherwise, do nothing if
		 * (changeset.getChanges().size() > 0) {
		 * oldIteration.logEvents(changeset); // Save the iteration, and throw
		 * an exception if if fails if (!db.save(oldIteration, s.getProject()))
		 * { throw new WPISuiteException(); } }
		 */

		// Save the iteration, and throw an exception if if fails
		if (!db.save(oldIteration, s.getProject())) {
			throw new WPISuiteException();
		}

		return oldIteration;
	}

	/**
	 * Gets a iteration given an id
	 * 
	 * @param id
	 *            The ID of the iteration to find
	 * @param s
	 *            The session the iteration is in
	 * @return The requested iteration
	 * @throws IterationNotFoundException
	 *             If the iteration cannot be found, or is invalid, we throw
	 *             this
	 */
	public Iteration getIteration(int id, Session s)
			throws IterationNotFoundException {
		try {
			// Attempt to get the iteration
			List<Model> iteration = db.retrieve(Iteration.class, "id", id,
					s.getProject());

			// Check that the iteration was actually found
			if (iteration.size() < 1 || iteration.get(0) == null) {
				throw new IterationNotFoundException(id);
			}

			// Make sure we actually got a iteration, and return if we have
			// Otherwise, throw an exception
			if (iteration.get(0) instanceof Iteration) {
				return ((Iteration) iteration.get(0));
			} else {
				throw new WPISuiteException();
			}
		} catch (WPISuiteException e) {
			// If we thew an exception, we didn't find the iteration, so throw
			// an iteration not found exception
			throw new IterationNotFoundException(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, Iteration model) {
		db.save(model, s.getProject());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
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
		return db.retrieveAll(new Iteration()).size();
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
	 * Gets the next valid id for this class
	 * 
	 * @param s
	 *            the current session
	 * @return the new id
	 * @throws WPISuiteException
	 *             if there was a lookup error
	 */
	private int getId(Session s) throws WPISuiteException {

		IdManager idManager;
		if (db.retrieve(IdManager.class, "type", "iteration", s.getProject())
				.size() != 0) {
			idManager = db.retrieve(IdManager.class, "type", "iteration",
					s.getProject()).toArray(new IdManager[0])[0];
		} else {
			idManager = new IdManager("iteration");
		}
		int id = idManager.getNextId();
		if (!db.save(idManager, s.getProject())) {
			throw new WPISuiteException();
		}
		return id;

	}

	@Override
	public String advancedPut(Session s, String[] args, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedGet(Session s, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

}
