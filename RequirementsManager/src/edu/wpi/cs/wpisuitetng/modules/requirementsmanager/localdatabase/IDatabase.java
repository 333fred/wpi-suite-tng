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

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.NotFoundException;

/**
 * Interface to support the local database
 * 
 * @param <T>
 *            The model that this database will support
 */

public interface IDatabase<T extends Model> extends Runnable {
	
	/**
	 * Adds or updates the given model
	 * 
	 * @param model
	 *            the model to add or update
	 */
	public void add(T model);
	
	/**
	 * Adds or updates the given models
	 * 
	 * @param models
	 *            the models to add or updates
	 */
	public void addAll(List<T> models);
	
	/**
	 * Gets the object from the database with the specified id
	 * 
	 * @param id
	 *            id of the model you want
	 * @return the model you want
	 * @throws NotFoundException
	 *             If the model wasn't found
	 */
	public T get(int id) throws NotFoundException;
	
	/**
	 * @return all the data in this database
	 */
	public List<T> getAll();
	
	/**
	 * Registers a database listener to this database for receiving database
	 * updates
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public void registerListener(IDatabaseListener listener);
	
	/**
	 * Removes a given database listener from this database
	 * 
	 * @param listener
	 *            the listener to remove
	 * @return true if successful, false otherwise
	 */
	public boolean removeListener(IDatabaseListener listener);
	
	/**
	 * Sets the database to the given list of models
	 * 
	 * @param models
	 *            the models to be in the database
	 */
	public void set(List<T> models);
	
	/**
	 * Starts the database background thread
	 */
	public void start();
	
	/**
	 * Updates all listeners in the class
	 */
	public void updateListeners();
	
}
