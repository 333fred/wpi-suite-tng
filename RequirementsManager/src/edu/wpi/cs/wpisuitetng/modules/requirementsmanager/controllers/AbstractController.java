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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller to handle all server communication with Model objects. Each model
 * must implement an concrete class of this type that provides the correct
 * fields to the server methods
 */

public abstract class AbstractController<T extends AbstractModel> implements
		IModelController<T> {

	String type;

	/**
	 * Creates an Abstract controller which gives requests of a given type
	 * 
	 * @param type
	 *            the model specific string of the controller
	 */
	protected AbstractController(String type) {
		this.type = type;
	}

	/**
	 * Creates a get request for a given model type and id
	 * 
	 * @param id
	 *            the id of the model to retrieve
	 * @param observer
	 *            the observer that will handle server response
	 */
	@Override
	public void get(int id, RequestObserver observer) {
		if (isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type + "/" + id, HttpMethod.GET);
		request.addObserver(observer);
		request.send();
	}

	/**
	 * Creates a getAll server request for models of the given type
	 * 
	 * @param observer
	 *            the observer that will handle server response
	 */
	@Override
	public void getAll(RequestObserver observer) {
		if (isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type, HttpMethod.GET);
		request.addObserver(observer);
		request.send();
	}

	/**
	 * Creates a create server request for models of the given type
	 * 
	 * @param model
	 *            the model to create
	 * @param observer
	 *            the observer that will handle the server response
	 */
	@Override
	public void create(T model, RequestObserver observer) {
		if (isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type, HttpMethod.PUT);
		request.setBody(model.toJSON());
		request.addObserver(observer);
		request.send();

	}

	/**
	 * Creates a server save request for a model of the given type
	 * 
	 * @param model
	 *            the model to save
	 * @param observer
	 *            the observer that will handle the server response
	 */
	@Override
	public void save(T model, RequestObserver observer) {
		if (isSafeToSend()) {
			return;
		}
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/" + type, HttpMethod.POST);
		request.setBody(model.toJSON());
		request.addObserver(observer);
		request.send();
	}

	/**
	 * Returns whether it is safe to send a request. This is an abstraction in
	 * case checking become more complicated than calling a Network method
	 * 
	 * @return true if it is safe to send, false otherwise
	 */
	protected static boolean isSafeToSend() {
		return Network.getInstance().isInitialized();
	}
}
