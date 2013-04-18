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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SimpleRetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SimpleRetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SimpleRetrieveAllRequirementsRequestObserver;

/**
 * Maintains a local database of requirements
 */
public class RequirementDatabase extends Thread {

	private Map<Integer, Requirement> requirements;
	private List<IDatabaseListener> listeners;
	private RequirementsController controller;
	private SimpleRetrieveAllRequirementsRequestObserver observer;
	private static RequirementDatabase db;

	private RequirementDatabase() {
		requirements = new HashMap<Integer, Requirement>();
		this.listeners = new ArrayList<IDatabaseListener>();
		this.controller = new RequirementsController();
		this.observer = new SimpleRetrieveAllRequirementsRequestObserver();
		setDaemon(true);
	}

	public static RequirementDatabase getInstance() {
		if (db == null) {
			db = new RequirementDatabase();
		}
		return db;
	}

	/**
	 * Sets the requirement to the given map
	 * 
	 * @param requirements
	 */
	public synchronized void setRequirements(
			Map<Integer, Requirement> requirements) {
		this.requirements = requirements;
		updateListeners();
	}

	/**
	 * Sets the requirements to the given list. This removes everything in the
	 * map and adds only things in the list
	 * 
	 * @param requirements
	 *            the requirements to add
	 */
	public synchronized void setRequirements(List<Requirement> requirements) {
		this.requirements = new HashMap<Integer, Requirement>();
		for (Requirement i : requirements) {
			this.requirements.put(i.getrUID(), i);
		}
		updateListeners();
	}

	/**
	 * Adds the given requirements to the map. The difference between this and
	 * set requirements is that this doesn't erase all requirements, only
	 * adds/updates the given list
	 * 
	 * @param requirements
	 *            the requirements to add/update
	 */
	public synchronized void addRequirements(List<Requirement> requirements) {
		for (Requirement i : requirements) {
			this.requirements.put(i.getrUID(), i);
		}
		updateListeners();
	}

	/**
	 * Adds or updates a specific requirement
	 * 
	 * @param i
	 *            the requirement to add/update
	 */
	public synchronized void addRequirement(Requirement i) {
		requirements.put(i.getrUID(), i);
		updateListeners();
	}

	/**
	 * Get a specific requirement from the local database
	 * 
	 * @param id
	 *            the id of requirement to get
	 * @return the requirement requested
	 * @throws RequirementNotFoundException
	 *             couldn't find the requirement
	 */
	public synchronized Requirement getRequirement(int id)
			throws RequirementNotFoundException {
		if (requirements.get(id) != null) {
			return requirements.get(id);
		} else {
			throw new RequirementNotFoundException(id);
		}
	}

	/**
	 * Gets all the requirements in the local database
	 * 
	 * @return all the current arrays
	 */
	public synchronized List<Requirement> getAllRequirements() {
		List<Requirement> list = new ArrayList<Requirement>();
		list = Arrays.asList(requirements.values().toArray(new Requirement[0]));
		return list;
	}

	/**
	 * Returns only the requirements that need to be filtered out of the view
	 * 
	 * 
	 * TODO: How to filter? If filter name == A and name == B, either or? so if
	 * one returns TRUE, keep it?
	 * 
	 * @return the list of filtered requirements
	 */
	public synchronized List<Requirement> getFilteredRequirements() {
		List<Requirement> filteredReqs = new ArrayList<Requirement>();
		List<Requirement> allReqs = getAllRequirements();
		List<Filter> filters = FilterDatabase.getInstance().getActiveFilters();

		// Loop through the filters and requirements and remove anything that
		// should be filtered
		/*
		 * for (Filter f : filters) { for (Requirement r : allReqs) { if
		 * (!f.shouldFilter(r)) { filteredReqs.remove(r); } } }
		 */

		for (Requirement r : allReqs) {

		}
		return filteredReqs;
	}

	/**
	 * Call the update method on all registered listeners
	 */
	public synchronized void updateListeners() {
		List<IDatabaseListener> removes = new ArrayList<IDatabaseListener>();
		for (IDatabaseListener l : listeners) {
			l.update();
			if (l.shouldRemove()) {
				removes.add(l);
			}
		}
		for (IDatabaseListener l : removes) {
			listeners.remove(l);
		}
	}

	/**
	 * Registers a database listener
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public synchronized void registerListener(IDatabaseListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a given listener from the list of listeners
	 * 
	 * @param listener
	 *            the listener to be removed
	 * @return True if the listener was removed or did not exits, false
	 *         otherwise
	 */
	public synchronized boolean removeListener(IDatabaseListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Runs every 5 minutes and updates the local requirements database, which
	 * will trigger an update of all listeners
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// Trigger an update
			controller.getAll(observer);
			try {
				// Sleep for five minutes
				Thread.sleep(300000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	// TODO documentation
	public Requirement getRequirement(String string) {
		for (Requirement aReq : requirements.values()) {
			if (aReq.getName().equals(string)) {
				return aReq;
			}
		}
		return null;
	}

}
