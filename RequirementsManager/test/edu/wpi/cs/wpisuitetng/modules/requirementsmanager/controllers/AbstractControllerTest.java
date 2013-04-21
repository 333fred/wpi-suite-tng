/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	@author Fredric
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockRequest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Abstract class to test all of the controllers
 * 
 * @param <T>
 *            The type of model this will be testing
 */

public abstract class AbstractControllerTest<T extends AbstractModel> {

	protected AbstractController<T> controller;
	protected MockRequestObserver observer;
	protected T model;
	protected MockNetwork network;
	protected String netConfig;
	protected String modelPath;
	protected int id;

	/**
	 * Pre-Test setup
	 */
	@Before
	public abstract void setup();

	/**
	 * Sets up a new network. The configuration is null to test that particular
	 * branch in the controller
	 */
	protected void setupNetwork() {
		netConfig = "http://localhost:8080/WPISuite/API";
		network = new MockNetwork();
		Network.initNetwork(network);
	}

	/**
	 * Sets up the network configuration
	 */
	protected void setupNetworkConfig() {
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration(netConfig));
	}

	/**
	 * Tests creating a good create request
	 */
	@Test
	public void createGood() {
		setupNetworkConfig();
		controller.create(model, observer);
		MockRequest request = network.getLastRequestMade();
		assertEquals(request.getUrl().toString(), netConfig + modelPath);
		assertEquals(request.getBody(), model.toJSON());
		assertEquals(request.getHttpMethod(), HttpMethod.PUT);
	}

	/**
	 * Tests creating a good save request
	 */
	@Test
	public void saveGood() {
		setupNetworkConfig();
		controller.save(model, observer);
		MockRequest request = network.getLastRequestMade();
		assertEquals(request.getUrl().toString(), netConfig + modelPath);
		assertEquals(request.getBody(), model.toJSON());
		assertEquals(request.getHttpMethod(), HttpMethod.POST);
	}

	/**
	 * Gets creating a good get by id request
	 */
	@Test
	public void getIdGood() {
		setupNetworkConfig();
		controller.get(id, observer);
		MockRequest request = network.getLastRequestMade();
		assertEquals(request.getUrl().toString(), netConfig + modelPath + "/"
				+ id);
		assertEquals(request.getBody(), null);
		assertEquals(request.getHttpMethod(), HttpMethod.GET);
	}

	/**
	 * Gets creating a good get by id request
	 */
	@Test
	public void getAllGood() {
		setupNetworkConfig();
		controller.getAll(observer);
		MockRequest request = network.getLastRequestMade();
		assertEquals(request.getUrl().toString(), netConfig + modelPath);
		assertEquals(request.getBody(), null);
		assertEquals(request.getHttpMethod(), HttpMethod.GET);
	}

	/**
	 * Test creating a good delete request
	 */
	@Test
	public void deleteGood() {
		setupNetworkConfig();
		controller.delete(model, observer);
		MockRequest request = network.getLastRequestMade();
		assertEquals(request.getUrl().toString(), netConfig + modelPath + "/"
				+ id);
		assertEquals(request.getBody(), null);
		assertEquals(request.getHttpMethod(), HttpMethod.DELETE);
	}

	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badCreate() {
		controller.create(model, observer);
		assertEquals(network.getLastRequestMade(), null);
	}

	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badSave() {
		controller.save(model, observer);
		assertEquals(network.getLastRequestMade(), null);
	}

	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badGetId() {
		controller.get(id, observer);
		assertEquals(network.getLastRequestMade(), null);
	}

	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badGetAll() {
		controller.getAll(observer);
		assertEquals(network.getLastRequestMade(), null);
	}

	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badDelete() {
		controller.delete(model, observer);
		assertEquals(network.getLastRequestMade(), null);
	}
}
