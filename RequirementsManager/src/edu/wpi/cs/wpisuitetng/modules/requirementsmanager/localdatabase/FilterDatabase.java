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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.FilterController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.FilterNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllFiltersRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetrieveAllFiltersNotifier;

/**
 * Local cache that holds all filters from the server
 */

public class FilterDatabase extends AbstractDatabase<Filter> implements
		IRetrieveAllFiltersNotifier {

	private Map<Integer, Filter> filters;
	private FilterController controller;
	private static FilterDatabase database;

	/**
	 * Private constructor for creating the database singleton
	 */
	private FilterDatabase() {
		super(0); // The run will be overridden, so give it 0 seconds
		this.filters = new HashMap<Integer, Filter>();
		this.controller = new FilterController();
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
	 * {@inheritDoc}
	 */
	public synchronized void set(List<Filter> filters) {
		this.filters = new HashMap<Integer, Filter>();
		for (Filter f : filters) {
			this.filters.put(f.getId(), f);
		}
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void addAll(List<Filter> filters) {
		for (Filter f : filters) {
			this.filters.put(f.getId(), f);
		}
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void add(Filter f) {
		this.filters.put(f.getId(), f);
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Filter get(int id) throws FilterNotFoundException {
		if (filters.get(id) != null) {
			return filters.get(id);
		} else {
			throw new FilterNotFoundException(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized List<Filter> getAll() {
		return new ArrayList<Filter>(filters.values());
	}

	/**
	 * Returns all of the currently active filters
	 * 
	 * @return the list of currently active filters
	 */
	public synchronized List<Filter> getActiveFilters() {
		List<Filter> activeFilters = new ArrayList<Filter>();
		for (Filter f : getAll()) {
			if (f.isActive()) {
				activeFilters.add(f);
			}
		}
		return activeFilters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		// This database should not run continuously, so just update
		RetrieveAllFiltersRequestObserver observer = new RetrieveAllFiltersRequestObserver(
				this);
		controller.getAll(observer);
	}

	/**
	 * Sets the received filters to be in the database
	 */
	@Override
	public void receivedData(Filter[] filters) {
		set(Arrays.asList(filters));
	}

}
