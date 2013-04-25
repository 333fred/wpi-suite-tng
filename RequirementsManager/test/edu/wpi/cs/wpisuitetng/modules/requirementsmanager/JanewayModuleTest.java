package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class JanewayModuleTest {
	
	JanewayModule module;
	List<JanewayTabModel> tabTest;
	
	/**
	 * Sets up all necessary objects for testing, in this case the
	 * JanewayModule.
	 */
	@Before
	public void setUp() {
		module = new JanewayModule();
		tabTest = module.getTabs();
	}
	
	/**
	 * Tests the name return function of the Janeway Module interface.
	 */
	@Test
	public void testGetName() {
		final String name = module.getName();
		Assert.assertEquals(name, "Requirements Manager");
	}
	
	/**
	 * This tests the Tab module for errors. TODO: Actually make a test, once
	 * the tab model has been implemented.
	 */
	
}
