package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;

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
		String name = module.getName();
		assertEquals(name, "Requirements Manager");
	}

	
	/**
	 * This tests the Tab module for errors. TODO: Actually make a test, once
	 * the tab model has been implemented.
	 */
	
	
}
