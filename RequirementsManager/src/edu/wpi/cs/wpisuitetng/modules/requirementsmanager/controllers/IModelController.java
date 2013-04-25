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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;

/**
 * Interface for a controller for a given model. It provides methods for
 * retrieving one, retrieving all, creating, and saving a model
 */

public interface IModelController<T extends AbstractModel> {
	
	/**
	 * Creates a given model on the server for the first time
	 * 
	 * @param model
	 *            the new model to be saved
	 * @param observer
	 *            the observer to handle server response
	 */
	public void create(T model, RequestObserver observer);
	
	/**
	 * Deletes the given model on server.
	 * 
	 * @param model
	 *            the model to update
	 * @param observer
	 *            the observer to handle server response
	 */
	public void delete(T model, RequestObserver observer);
	
	/**
	 * Gets the model with the given id from the server
	 * 
	 * @param id
	 *            the id of the model to get
	 * @param observer
	 *            the observer to handle server response
	 */
	public void get(int id, RequestObserver observer);
	
	/**
	 * Gets all of this type from the server
	 * 
	 * @param observer
	 *            the observer to handle server response
	 */
	public void getAll(RequestObserver observer);
	
	/**
	 * Updates the given model on server. It must be created once before it is
	 * saved.
	 * 
	 * @param model
	 *            the model to update
	 * @param observer
	 *            the observer to handle server response
	 */
	public void save(T model, RequestObserver observer);
}
