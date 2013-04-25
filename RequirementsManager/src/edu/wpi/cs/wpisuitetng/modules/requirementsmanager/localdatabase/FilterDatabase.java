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
	private final FilterController controller;
	private static FilterDatabase database;
	
	/**
	 * Gets the singleton filter database instance
	 * 
	 * @return the database singleton
	 */
	public static FilterDatabase getInstance() {
		if (FilterDatabase.database == null) {
			FilterDatabase.database = new FilterDatabase();
		}
		return FilterDatabase.database;
	}
	
	/**
	 * Private constructor for creating the database singleton
	 */
	private FilterDatabase() {
		super(0); // The run will be overridden, so give it 0 seconds
		filters = new HashMap<Integer, Filter>();
		controller = new FilterController();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void add(final Filter f) {
		filters.put(f.getId(), f);
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addAll(final List<Filter> filters) {
		for (final Filter f : filters) {
			this.filters.put(f.getId(), f);
		}
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Filter get(final int id) throws FilterNotFoundException {
		if (filters.get(id) != null) {
			return filters.get(id);
		} else {
			throw new FilterNotFoundException(id);
		}
	}
	
	/**
	 * Returns all of the currently active filters
	 * 
	 * @return the list of currently active filters
	 */
	public synchronized List<Filter> getActiveFilters() {
		final List<Filter> activeFilters = new ArrayList<Filter>();
		for (final Filter f : getAll()) {
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
	public synchronized List<Filter> getAll() {
		return new ArrayList<Filter>(filters.values());
	}
	
	/**
	 * Sets the received filters to be in the database
	 */
	@Override
	public void receivedData(final Filter[] filters) {
		set(Arrays.asList(filters));
	}
	
	/**
	 * Removes the given filter from the database
	 * 
	 * @param toRemove
	 */
	
	public synchronized void remove(final Filter toRemove) {
		filters.remove(toRemove.getId());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		// This database should not run continuously, so just update
		final RetrieveAllFiltersRequestObserver observer = new RetrieveAllFiltersRequestObserver(
				this);
		controller.getAll(observer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void set(final List<Filter> filters) {
		this.filters = new HashMap<Integer, Filter>();
		for (final Filter f : filters) {
			this.filters.put(f.getId(), f);
		}
		updateListeners();
	}
	
}
