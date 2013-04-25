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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreivedAllIterationsNotifier;

/**
 * Maintains a local database of iterations
 */
public class IterationDatabase extends AbstractDatabase<Iteration> {
	
	/**
	 * Gets the singleton iterations database instance
	 * 
	 * @return the database instance
	 */
	public static IterationDatabase getInstance() {
		if (IterationDatabase.db == null) {
			IterationDatabase.db = new IterationDatabase();
		}
		return IterationDatabase.db;
	}
	
	private Map<Integer, Iteration> iterations;
	private final IterationController controller;
	private final RetrieveAllIterationsRequestObserver observer;
	
	private static IterationDatabase db;
	
	private IterationDatabase() {
		super(300000);
		iterations = new HashMap<Integer, Iteration>();
		controller = new IterationController();
		observer = new RetrieveAllIterationsRequestObserver(
				new IRetreivedAllIterationsNotifier() {
					
					@Override
					public void errorReceivingData(
							final String RetrieveAllRequirementsRequestObserver) {
						// Nothing needs to happen
					}
					
					@Override
					public void receivedData(final Iteration[] iterations) {
						// Nothing needs to happen
					}
				});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void add(final Iteration i) {
		iterations.put(i.getId(), i);
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addAll(final List<Iteration> iterations) {
		for (final Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Iteration get(final int id)
			throws IterationNotFoundException {
		if (iterations.get(id) != null) {
			return iterations.get(id);
		} else {
			throw new IterationNotFoundException(id);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<Iteration> getAll() {
		List<Iteration> list = new ArrayList<Iteration>();
		list = new ArrayList<Iteration>(iterations.values());
		return list;
	}
	
	/**
	 * Gets the given iteration, returning null if the iteration isn't found
	 * 
	 * @param name
	 *            the name of the iteration to retrieve
	 * @return the iteration if it exists, null otherwise
	 */
	public synchronized Iteration getIteration(final String name) {
		for (final Iteration anIteration : iterations.values()) {
			if (anIteration.getName().equals(name)) {
				return anIteration;
			}
		}
		// TODO: This needs to be an exception, not null
		return null;
	}
	
	/**
	 * Runs every 5 minutes and updates the local iterations database, which
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
	public synchronized void set(final List<Iteration> iterations) {
		this.iterations = new HashMap<Integer, Iteration>();
		for (final Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
		updateListeners();
	}
}
