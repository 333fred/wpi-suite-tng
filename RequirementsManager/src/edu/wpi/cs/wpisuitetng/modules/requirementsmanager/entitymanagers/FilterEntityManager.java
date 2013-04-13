/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public class FilterEntityManager implements EntityManager<Filter> {

	private Data db;
	private ModelMapper updateMapper;

	/**
	 * Creates a new entity manager for filters, with the given database
	 * 
	 * @param db
	 *            the database to store filters in
	 */
	public FilterEntityManager(Data db) {
		this.db = db;
		this.updateMapper = new ModelMapper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {

		Filter newFilter = Filter.fromJSON(content);

		newFilter.setId(Count() + 1);

		// Set the user of the filter
		newFilter.setCreator(s.getUser());

		// TODO: Write a validator for filters and validate them here

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
	public Filter[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {

		// Attempt to get the filter id
		final int filterId = Integer.parseInt(id);
		if (filterId < 1) {
			throw new NotFoundException();
		}

		Filter[] filter = { getFilter(filterId, s) };

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
	public Filter getFilter(int id, Session s) throws NotFoundException {

		// Attempt to find the filter
		Filter[] filter = null;
		try {
			filter = db.retrieve(Filter.class, "id", id, s.getProject())
					.toArray(new Filter[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}

		// Check for filter existence
		if (filter.length < 1 || filter[0] == null) {
			throw new NotFoundException();
		}
		return filter[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new Filter(), s.getProject()).toArray(
				new Filter[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Filter update(Session s, String content) throws WPISuiteException {

		// Get updated filter
		Filter updatedFilter = Filter.fromJSON(content);
		Filter oldFilter;

		// TODO: Validate the updated iteration

		// Get the old iteration
		try {
			oldFilter = getFilter(updatedFilter.getId(), s);
		} catch (NotFoundException e) {
			throw new WPISuiteException();
		}

		// Coppy values from the old filter to the new filter
		updateMapper.map(updatedFilter, oldFilter);

		// Save the filter and return
		if (!db.save(updatedFilter, s.getProject())) {
			throw new WPISuiteException();
		}

		return oldFilter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, Filter model) throws WPISuiteException {
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
		db.deleteAll(new Filter(), s.getProject());
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
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return db.retrieveAll(new Filter()).size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}
}
