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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

import java.util.List;
import java.util.ArrayList;

public class RequirementDatabaseTest {
	
	RequirementDatabase instance;
	List<Requirement> requirements;
	
	@Before
	public void setup(){
		this.instance = RequirementDatabase.getInstance();
		this.requirements = new ArrayList<Requirement>();
		this.requirements.add(new Requirement());
		this.requirements.get(0).setrUID(1);
		this.requirements.add(new Requirement());
		this.requirements.get(1).setrUID(2);
	}
	
	@Test
	public void testSet(){
		this.instance.set(this.requirements);
		try {
			assertEquals(this.instance.get(2), this.requirements.get(1));
		}
		catch (RequirementNotFoundException e) {
			fail();
		}
	}
	
	@Test
	public void testAddAll(){
		List<Requirement> additionalRequirements = new ArrayList<Requirement>();
		additionalRequirements.add(new Requirement());
		additionalRequirements.get(0).setrUID(3);
		additionalRequirements.add(new Requirement());
		additionalRequirements.get(1).setrUID(4);
		this.instance.set(this.requirements);
		this.instance.addAll(additionalRequirements);
		try {
			assertEquals(this.instance.get(1), this.requirements.get(0));
			assertEquals(this.instance.get(4), additionalRequirements.get(1));
		}
		catch (RequirementNotFoundException e) {
			fail();
		}
	}
	
	@Test
	public void testAdd(){
		Requirement r = new Requirement();
		r.setrUID(3);
		this.instance.set(this.requirements);
		this.instance.add(r);
		try {
			assertEquals(r, this.instance.get(3));
		}
		catch (RequirementNotFoundException e) {
			fail();
		}
	}
	
	@Test
	public void testRequirementNotFound(){
		this.instance.set(this.requirements);
		try {
			this.instance.get(4);
			fail();
		}
		catch (RequirementNotFoundException e) {
			assertNotNull(e);
		}
	}
	
	@Test
	public void testGetByName(){
		Requirement r = new Requirement();
		r.setrUID(10);
		r.setName("Ten");
		this.instance.add(r);
		assertNotNull(this.instance.getRequirement("Ten"));
		assertNull(this.instance.getRequirement("Eleven"));
	}
	
	@After
	public void cleanup(){
		this.instance.set(new ArrayList<Requirement>());
		this.requirements = null;
		this.instance = null;
	}	

}
