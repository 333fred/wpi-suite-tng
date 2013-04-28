/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.IdManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.FilterValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.ValidationIssue;

/**
 * Entity manager for filters
 */
public class FilterEntityManager implements EntityManager<Filter> {

	private final Data db;
	private final ModelMapper updateMapper;
	private final FilterValidator validator;

	/**
	 * Creates a new entity manager for filters, with the given database
	 * 
	 * @param db
	 *            the database to store filters in
	 */
	public FilterEntityManager(final Data db) {
		this.db = db;
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("stringValue");
		validator = new FilterValidator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedGet(final Session s, final String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPost(final Session s, final String string,
			final String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPut(final Session s, final String[] args,
			final String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int Count() {
		return db.retrieveAll(new Filter()).size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll(final Session s) throws WPISuiteException {
		ensureRole(s, Role.ADMIN);
		db.deleteAll(new Filter(), s.getProject());
	}

	/**
	 * Deletes the filter from the database.
	 * 
	 * Currently, the delete method seems to be bugged and throws illegal state
	 * exceptions when users are added to the project. As such, we are using the
	 * deleted flag to determine if the filter should be returned or not. The
	 * database will only return filters that don't have this flag set. If it
	 * does have this flag set, then the entity manager will ignore the filter.
	 */
	@Override
	public boolean deleteEntity(final Session s, final String id)
			throws WPISuiteException {
		final Filter del = getEntity(s, id)[0];
		del.setDeleted(true);
		if (!db.save(del, s.getProject())) {
			throw new WPISuiteException();
		}
		return true;
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
	public Filter[] getAll(final Session s) {
		final List<Model> models = db.retrieveAll(new Filter(), s.getProject());
		final List<Filter> filters = new ArrayList<Filter>();

		// Make sure that we only return non-deleted filters that belong to
		// current user
		for (final Model m : models) {
			final Filter f = (Filter) m;
			System.out.println("Filter Get all, user: " + s.getUsername());
			System.out.println("Filter Get All, Current filter: " + f);
			if ((s.getUsername() != null) && !f.isDeleted()
					&& f.getCreator().equals(s.getUsername())) {
				filters.add(f);
			}
		}

		return filters.toArray(new Filter[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter[] getEntity(final Session s, final String id)
			throws NotFoundException {

		// Attempt to get the filter id
		final int filterId = Integer.parseInt(id);
		if (filterId < 1) {
			throw new NotFoundException();
		}

		final Filter[] filter = { getFilter(filterId, s) };

		return filter;
	}

	/**
	 * Finds a specific filter given an id and a session
	 * 
	 * @param id
	 *            the id of the filter to find
	 * @param s
	 *            the session to look in
	 * @return the filter
	 * @throws NotFoundException
	 *             if the filter
	 */
	public Filter getFilter(final int id, final Session s)
			throws NotFoundException {

		// Attempt to find the filter
		Filter[] filter = null;
		try {
			filter = db.retrieve(Filter.class, "id", id, s.getProject())
					.toArray(new Filter[0]);
		} catch (final WPISuiteException e) {
			e.printStackTrace();
		}

		// Check for filter existence
		if ((filter.length < 1) || (filter[0] == null)) {
			throw new NotFoundException();
		}

		// Make sure the filter belongs to this user and is not deleted
		final Filter f = filter[0];
		if (!f.isDeleted() && !f.getCreator().equals(s.getUsername())) {
			throw new NotFoundException();
		}

		return f;
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
		if (db.retrieve(IdManager.class, "type", "filter", s.getProject())
				.size() != 0) {
			idManager = db.retrieve(IdManager.class, "type", "filter",
					s.getProject()).toArray(new IdManager[0])[0];
		} else {
			idManager = new IdManager("filter");
		}
		final int id = idManager.getNextId();
		if (!db.save(idManager, s.getProject())) {
			throw new WPISuiteException();
		}
		return id;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter makeEntity(final Session s, final String content)
			throws BadRequestException, WPISuiteException {

		final Filter newFilter = Filter.fromJSON(content);

		newFilter.setId(getId(s));
		newFilter.setCreator(s.getUsername());

		// Validate the filter, and error if failure
		List<ValidationIssue> issues;

		issues = validator.validate(s, newFilter);

		if (issues.size() > 0) {
			for (final ValidationIssue issue : issues) {
				System.out.println(issue.getMessage());
			}
			throw new BadRequestException();
		}

		// Save the filter
		if (!db.save(newFilter, s.getProject())) {
			throw new WPISuiteException();
		}

		return newFilter;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(final Session s, final Filter model) {
		db.save(model, s.getProject());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter update(final Session s, final String content)
			throws WPISuiteException {

		// Get updated filter
		final Filter updatedFilter = Filter.fromJSON(content);
		Filter oldFilter;

		// Validate the filter, and error if failure
		List<ValidationIssue> issues;
		issues = validator.validate(s, updatedFilter);

		if (issues.size() > 0) {
			for (final ValidationIssue issue : issues) {
				System.out.println(issue.getMessage());
			}
			throw new BadRequestException();
		}

		// Get the old iteration
		try {
			oldFilter = getFilter(updatedFilter.getId(), s);
		} catch (final NotFoundException e) {
			System.out.println("update, getfilter ");
			throw new WPISuiteException();
		}

		// Coppy values from the old filter to the new filter
		updateMapper.map(updatedFilter, oldFilter);

		// Save the filter and return
		if (!db.save(oldFilter, s.getProject())) {
			throw new WPISuiteException();
		}

		return oldFilter;
	}
}
