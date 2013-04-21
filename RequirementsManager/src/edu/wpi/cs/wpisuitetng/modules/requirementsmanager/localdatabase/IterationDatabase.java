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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SimpleRetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreivedAllIterationsNotifier;

/**
 * Maintains a local database of iterations
 */
public class IterationDatabase extends AbstractDatabase<Iteration> {

	private Map<Integer, Iteration> iterations;
	private IterationController controller;
	private RetrieveAllIterationsRequestObserver observer;
	private static IterationDatabase db;

	private IterationDatabase() {
		super(300000);
		this.iterations = new HashMap<Integer, Iteration>();
		this.controller = new IterationController();
		this.observer = new RetrieveAllIterationsRequestObserver(
				new IRetreivedAllIterationsNotifier() {

					@Override
					public void receivedData(Iteration[] iterations) {
						// Nothing needs to happen
					}

					@Override
					public void errorReceivingData(
							String RetrieveAllRequirementsRequestObserver) {
						// Nothing needs to happen
					}
				});
	}

	/**
	 * Gets the singleton iterations database instance
	 * 
	 * @return the database instance
	 */
	public static IterationDatabase getInstance() {
		if (db == null) {
			db = new IterationDatabase();
		}
		return db;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void set(List<Iteration> iterations) {
		this.iterations = new HashMap<Integer, Iteration>();
		for (Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void addAll(List<Iteration> iterations) {
		for (Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void add(Iteration i) {
		iterations.put(i.getId(), i);
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Iteration get(int id) throws IterationNotFoundException {
		if (iterations.get(id) != null) {
			return iterations.get(id);
		} else {
			throw new IterationNotFoundException(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
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
	public synchronized Iteration getIteration(String name) {
		for (Iteration anIteration : iterations.values()) {
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
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}
}
