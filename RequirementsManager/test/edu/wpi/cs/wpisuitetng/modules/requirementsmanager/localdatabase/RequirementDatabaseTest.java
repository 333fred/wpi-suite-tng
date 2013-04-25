/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *	Alex Gorowara
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class RequirementDatabaseTest {
	
	RequirementDatabase instance;
	List<Requirement> requirements;
	
	@After
	public void cleanup() {
		instance.set(new ArrayList<Requirement>());
		requirements = null;
		instance = null;
	}
	
	@Before
	public void setup() {
		instance = RequirementDatabase.getInstance();
		requirements = new ArrayList<Requirement>();
		requirements.add(new Requirement());
		requirements.get(0).setrUID(1);
		requirements.add(new Requirement());
		requirements.get(1).setrUID(2);
	}
	
	@Test
	public void testAdd() {
		final Requirement r = new Requirement();
		r.setrUID(3);
		instance.set(requirements);
		instance.add(r);
		try {
			Assert.assertEquals(r, instance.get(3));
		} catch (final RequirementNotFoundException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testAddAll() {
		final List<Requirement> additionalRequirements = new ArrayList<Requirement>();
		additionalRequirements.add(new Requirement());
		additionalRequirements.get(0).setrUID(3);
		additionalRequirements.add(new Requirement());
		additionalRequirements.get(1).setrUID(4);
		instance.set(requirements);
		instance.addAll(additionalRequirements);
		try {
			Assert.assertEquals(instance.get(1), requirements.get(0));
			Assert.assertEquals(instance.get(4), additionalRequirements.get(1));
		} catch (final RequirementNotFoundException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testGetByName() {
		final Requirement r = new Requirement();
		r.setrUID(10);
		r.setName("Ten");
		instance.add(r);
		Assert.assertNotNull(instance.getRequirement("Ten"));
		Assert.assertNull(instance.getRequirement("Eleven"));
	}
	
	@Test
	public void testRequirementNotFound() {
		instance.set(requirements);
		try {
			instance.get(4);
			Assert.fail();
		} catch (final RequirementNotFoundException e) {
			Assert.assertNotNull(e);
		}
	}
	
	@Test
	public void testSet() {
		instance.set(requirements);
		try {
			Assert.assertEquals(instance.get(2), requirements.get(1));
		} catch (final RequirementNotFoundException e) {
			Assert.fail();
		}
	}
	
}
