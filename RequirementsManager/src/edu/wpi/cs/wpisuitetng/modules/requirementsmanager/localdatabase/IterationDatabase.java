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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SimpleRetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

/**
 * Maintains a local database of iterations
 */
public class IterationDatabase extends Thread {

	private Map<Integer, Iteration> iterations;
	private List<IDatabaseListener> listeners;
	private SimpleRetrieveAllIterationsController controller;
	private static IterationDatabase db;

	private IterationDatabase() {
		this.iterations = new HashMap<Integer, Iteration>();
		this.listeners = new ArrayList<IDatabaseListener>();
		this.controller = new SimpleRetrieveAllIterationsController();
		setDaemon(true);
	}

	public static IterationDatabase getInstance() {
		if (db == null) {
			db = new IterationDatabase();
		}
		return db;
	}

	/**
	 * Sets the iteration to the given map
	 * 
	 * @param iterations
	 */
	public synchronized void setIterations(Map<Integer, Iteration> iterations) {
		this.iterations = iterations;
		updateListeners();
	}

	/**
	 * Sets the iterations to the given list. This removes everything in the map
	 * and adds only things in the list
	 * 
	 * @param iterations
	 *            the iterations to add
	 */
	public synchronized void setIterations(List<Iteration> iterations) {
		this.iterations = new HashMap<Integer, Iteration>();
		for (Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
		updateListeners();
	}

	/**
	 * Adds the given iterations to the map. The difference between this and set
	 * iterations is that this doesn't erase all iterations, only adds/updates
	 * the given list
	 * 
	 * @param iterations
	 *            the iterations to add/update
	 */
	public synchronized void addIterations(List<Iteration> iterations) {
		for (Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
		updateListeners();
	}

	/**
	 * Adds or updates a specific iteration
	 * 
	 * @param i
	 *            the iteration to add/update
	 */
	public synchronized void addIteration(Iteration i) {
		iterations.put(i.getId(), i);
		updateListeners();
	}

	/**
	 * Get a specific iteration from the local database
	 * 
	 * @param id
	 *            the id of iteration to get
	 * @return the iteration requested
	 * @throws IterationNotFoundException
	 *             couldn't find the iteration
	 */
	public synchronized Iteration getIteration(int id)
			throws IterationNotFoundException {
		if (iterations.get(id) != null) {
			return iterations.get(id);
		} else {
			throw new IterationNotFoundException(id);
		}
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
	 * Gets all the iterations in the local database
	 * 
	 * @return all the current arrays
	 */
	public synchronized List<Iteration> getAllIterations() {
		List<Iteration> list = new ArrayList<Iteration>();
		list = new ArrayList<Iteration>(iterations.values());
		return list;
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
	 * Runs every 5 minutes and updates the local iterations database, which
	 * will trigger an update of all listeners
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			// Trigger an update
			controller.getAll();
			try {
				// Sleep for five minutes
				Thread.sleep(300000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

}
