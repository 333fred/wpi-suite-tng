package edu.wpi.cs.wpisuitetng.modues.requirementsmanager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.JanewayModule;

public class JanewayModuleTest {
	
	JanewayModule module;
	
	/**
	 * Sets up all necessary objects for testing, in this 
	 * case the JanewayModule.
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
	 * This tests the Tab module for errors.  
	 * TODO: Actually make a test, once the tab model has been implemented.
	 */
	@Test
	public void testTabModel() {
		List<JanewayTabModel> model = module.getTabs();
	}

}
