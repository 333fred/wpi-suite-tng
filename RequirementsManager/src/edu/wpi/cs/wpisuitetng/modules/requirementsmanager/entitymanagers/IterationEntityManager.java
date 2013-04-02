/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
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
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
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
			throws BadRequestException, ConflictException, WPISuiteException {
		
		// Get the iteration from JSON
		final Iteration newIteration = Iteration.fromJSON(content);

		// Get the id for the new iteration
		newIteration.setId(Count() + 1);
		System.out.println("Count " + Count());

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

		// TODO: Determine if we want to do logging here

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
	public Iteration[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {

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
		Iteration[] is = db.retrieveAll(new Iteration(), s.getProject()).toArray(
				new Iteration[0]);
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
			List<Model> iteration = db.retrieve(Iteration.class, "rUID", id,
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
			// a iteration not found exception
			throw new IterationNotFoundException(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, Iteration model) throws WPISuiteException {
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
	public int Count() throws WPISuiteException {
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

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
