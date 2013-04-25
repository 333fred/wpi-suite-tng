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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IReceivedAllRequirementNotifier;

/**
 * Maintains a local database of requirements
 */
@SuppressWarnings ("hiding")
public class RequirementDatabase extends AbstractDatabase<Requirement> {
	
	/**
	 * Gets the current static database
	 * 
	 * @return the current databse instance
	 */
	public static RequirementDatabase getInstance() {
		if (RequirementDatabase.db == null) {
			RequirementDatabase.db = new RequirementDatabase();
		}
		return RequirementDatabase.db;
	}
	
	private Map<Integer, Requirement> requirements;
	private final RequirementsController controller;
	private final RetrieveAllRequirementsRequestObserver observer;
	
	private static RequirementDatabase db;
	
	private RequirementDatabase() {
		super(300000);
		requirements = new HashMap<Integer, Requirement>();
		controller = new RequirementsController();
		observer = new RetrieveAllRequirementsRequestObserver(
				new IReceivedAllRequirementNotifier() {
					
					@Override
					public void errorReceivingData(
							final String RetrieveAllRequirementsRequestObserver) {
						// Nothing to do here
					}
					
					@Override
					public void receivedData(final Requirement[] requirements) {
						// Nothing to do here
					}
				});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void add(final Requirement model) {
		requirements.put(model.getrUID(), model);
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addAll(final List<Requirement> models) {
		for (final Requirement i : models) {
			requirements.put(i.getrUID(), i);
		}
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Requirement get(final int id)
			throws RequirementNotFoundException {
		if (requirements.get(id) != null) {
			return requirements.get(id);
		} else {
			throw new RequirementNotFoundException(id);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<Requirement> getAll() {
		return Arrays.asList(requirements.values().toArray(new Requirement[0]));
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
		final List<Requirement> filteredReqs = new ArrayList<Requirement>(
				getAll());
		final List<Requirement> allReqs = getAll();
		final List<Filter> filters = FilterDatabase.getInstance()
				.getActiveFilters();
		
		// Loop through the filters and requirements and remove anything that
		// should be filtered
		for (final Filter f : filters) {
			for (final Requirement r : allReqs) {
				if (!f.shouldFilter(r)) {
					filteredReqs.remove(r);
				}
			}
		}
		
		/*
		 * for (Requirement r : allReqs) {
		 * }
		 */
		return filteredReqs;
	}
	
	/**
	 * Gets a requirement by name. This will not detect duplicates, but return
	 * the first in the list
	 * 
	 * @param name
	 *            the name to match
	 * @return the first requirement with that name
	 */
	public Requirement getRequirement(final String name) {
		for (final Requirement aReq : requirements.values()) {
			if (aReq.getName().equals(name)) {
				return aReq;
			}
		}
		return null;
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
				Thread.sleep(secs);
			} catch (final InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void set(final List<Requirement> models) {
		requirements = new HashMap<Integer, Requirement>();
		for (final Requirement i : models) {
			requirements.put(i.getrUID(), i);
		}
		updateListeners();
	}
	
}
