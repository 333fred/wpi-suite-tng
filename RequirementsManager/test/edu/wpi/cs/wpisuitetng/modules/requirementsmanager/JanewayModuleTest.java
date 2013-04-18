package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JanewayModuleTest {

	JanewayModule module;

	/**
	 * Sets up all necessary objects for testing, in this case the
	 * JanewayModule.
	 */
	@Before
	public void setUp() {
		module = new JanewayModule();
	}

	/**
	 * Tests the name return function of the Janeway Module interface.
	 */
	@Test
	public void testGetName() {
		String name = module.getName();
		assertEquals(name, "Requirements Manager");
	}

	
	/**
	 * This tests the Tab module for errors. TODO: Actually make a test, once
	 * the tab model has been implemented.
	 */
	@Test
	@Ignore
	public void testTabModel(){
		//TODO
	}

}
