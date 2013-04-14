/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import javax.swing.JTextArea;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

//TODO: Learn how to better test GUI panels
public class ViewTest {

	// Create a dummy test so that JUnit doesn't break
	Requirement RequirmentTest1 = new Requirement();
	MainTabController TabControllerTest = new MainTabController();
	MainTabView TabViewTest = TabControllerTest.getTabView();
	Iteration IterationTest = new Iteration();
	DetailPanel DetailTest1;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	User testUser;
	
	
	@Before
	public void setUp() throws Exception {

		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(
				new NetworkConfiguration("http://wpisuitetng"));
		// initialize database and session info
		testUser = new User(null, "testUser", null, -1);
		testProject = new Project("test", "1");
		mockSsid = "abc123";
		defaultSession = new Session(testUser, testProject, mockSsid);
		db = new MockData(new HashSet<Object>());
		db.save(testUser);
		DetailTest1 = new DetailPanel(IterationTest ,  TabControllerTest); 
	}
	
	@Test
	public void testGetMainTabControl() throws Exception {
		assertEquals(TabControllerTest, this.DetailTest1.getMainTabController());
	}
	
	@Test
	public void testGetTextName() throws Exception {
		assertEquals("", this.DetailTest1.getTextName().getText());
	}
}
