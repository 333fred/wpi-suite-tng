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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SimpleRetrieveAllFiltersController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.FilterNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

/**
 * Local cache that holds all filters from the server
 */

public class FilterDatabase extends Thread {

	private Map<Integer, Filter> filters;
	private SimpleRetrieveAllFiltersController controller;
	private static FilterDatabase database;

	/**
	 * Private constructor for creating the database singleton
	 */
	private FilterDatabase() {
		this.filters = new HashMap<Integer, Filter>();
		this.controller = new SimpleRetrieveAllFiltersController();
		setDaemon(true);
	}

	/**
	 * Gets the singleton filter database instance
	 * 
	 * @return the database singleton
	 */
	public static FilterDatabase getInstance() {
		if (database == null) {
			database = new FilterDatabase();
		}
		return database;
	}

	/**
	 * Sets the current filter database to the given map
	 * 
	 * @param filters
	 *            the map of filters
	 */
	public synchronized void setFilters(Map<Integer, Filter> filters) {
		this.filters = filters;
	}

	/**
	 * Sets the map of filters to be the given list
	 * 
	 * @param filters
	 *            the filters to be in the database
	 */
	public synchronized void setFilters(List<Filter> filters) {
		this.filters = new HashMap<Integer, Filter>();
		for (Filter f : filters) {
			this.filters.put(f.getId(), f);
		}
	}

	/**
	 * Adds/updates the given list of filters
	 * 
	 * @param filters
	 *            the filters to add/update
	 */
	public synchronized void addFilters(List<Filter> filters) {
		for (Filter f : filters) {
			this.filters.put(f.getId(), f);
		}
	}

	/**
	 * Adds the given filter to the list of filters
	 * 
	 * @param f
	 *            the filter to add/update
	 */
	public synchronized void addFilter(Filter f) {
		this.filters.put(f.getId(), f);
	}

	/**
	 * Gets the filter with the given id from the database
	 * 
	 * @param id
	 *            the id of the requested filter
	 * @return the requested filter
	 * @throws FilterNotFoundException
	 *             if we can't find the filter
	 */
	public synchronized Filter getId(int id) throws FilterNotFoundException {
		if (filters.get(id) != null) {
			return filters.get(id);
		} else {
			throw new FilterNotFoundException(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		// This database should not run continuously, so just update
		controller.getAll();
	}

}
