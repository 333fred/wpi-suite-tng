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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
 * This is ignored as it just provides a framework for testing, not actually
 * doing a test
 * 
 * @param <T>
 *            The type of model this will be testing
 */
@Ignore
public abstract class AbstractControllerTest<T extends AbstractModel> {
	
	protected AbstractController<T> controller;
	protected MockRequestObserver observer;
	protected T model;
	protected MockNetwork network;
	protected String netConfig;
	protected String modelPath;
	protected int id;
	
	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badCreate() {
		controller.create(model, observer);
		Assert.assertEquals(network.getLastRequestMade(), null);
	}
	
	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badDelete() {
		controller.delete(model, observer);
		Assert.assertEquals(network.getLastRequestMade(), null);
	}
	
	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badGetAll() {
		controller.getAll(observer);
		Assert.assertEquals(network.getLastRequestMade(), null);
	}
	
	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badGetId() {
		controller.get(id, observer);
		Assert.assertEquals(network.getLastRequestMade(), null);
	}
	
	/**
	 * Test creating a create request with null network
	 */
	@Test
	public void badSave() {
		controller.save(model, observer);
		Assert.assertEquals(network.getLastRequestMade(), null);
	}
	
	/**
	 * Tests creating a good create request
	 */
	@Test
	public void createGood() {
		setupNetworkConfig();
		controller.create(model, observer);
		final MockRequest request = network.getLastRequestMade();
		Assert.assertEquals(request.getUrl().toString(), netConfig + modelPath);
		Assert.assertEquals(request.getBody(), model.toJSON());
		Assert.assertEquals(request.getHttpMethod(), HttpMethod.PUT);
	}
	
	/**
	 * Test creating a good delete request
	 */
	@Test
	public void deleteGood() {
		setupNetworkConfig();
		controller.delete(model, observer);
		final MockRequest request = network.getLastRequestMade();
		Assert.assertEquals(request.getUrl().toString(), netConfig + modelPath
				+ "/" + id);
		Assert.assertEquals(request.getBody(), null);
		Assert.assertEquals(request.getHttpMethod(), HttpMethod.DELETE);
	}
	
	/**
	 * Gets creating a good get by id request
	 */
	@Test
	public void getAllGood() {
		setupNetworkConfig();
		controller.getAll(observer);
		final MockRequest request = network.getLastRequestMade();
		Assert.assertEquals(request.getUrl().toString(), netConfig + modelPath);
		Assert.assertEquals(request.getBody(), null);
		Assert.assertEquals(request.getHttpMethod(), HttpMethod.GET);
	}
	
	/**
	 * Gets creating a good get by id request
	 */
	@Test
	public void getIdGood() {
		setupNetworkConfig();
		controller.get(id, observer);
		final MockRequest request = network.getLastRequestMade();
		Assert.assertEquals(request.getUrl().toString(), netConfig + modelPath
				+ "/" + id);
		Assert.assertEquals(request.getBody(), null);
		Assert.assertEquals(request.getHttpMethod(), HttpMethod.GET);
	}
	
	/**
	 * Tests creating a good save request
	 */
	@Test
	public void saveGood() {
		setupNetwork();
		setupNetworkConfig();
		controller.save(model, observer);
		final MockRequest request = network.getLastRequestMade();
		Assert.assertEquals(request.getUrl().toString(), netConfig + modelPath);
		Assert.assertEquals(request.getBody(), model.toJSON());
		Assert.assertEquals(request.getHttpMethod(), HttpMethod.POST);
	}
	
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
}
