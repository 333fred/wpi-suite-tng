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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.MockRequest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.AssigneePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class RequirementControllerTest {

	//TODO: Test the requirement controllers, and separate into other files

	AddRequirementController controller;
	SaveRequirementController controller2;
	
	RetrieveAllUsersController controller3;
	SaveRequirementController controller4;
	
	RetrieveAllRequirementsController controller5; //Cannot test these controllers yet
	RetrieveAllIterationsController controller6;
	
	
	DetailPanel view;
	AssigneePanel view2;
	IReceivedAllRequirementNotifier requirementNotifier; //Needs to be replaced by an object that extends this
	Requirement r1;
	Requirement r2;
	
	String name;
	int rUID; 
	String description;
	Type type;
	Status status;
	Priority priority;
	
	@Before
	public void setUp() throws Exception {		
		r1 = new Requirement();		
		Network.initNetwork(new MockNetwork());
		Network.getInstance().setDefaultNetworkConfiguration(new NetworkConfiguration("http://wpisuitetng"));
		view = new DetailPanel(r1, new MainTabController(new MainTabView()));
		view2 = new AssigneePanel(r1, view);
		controller = new AddRequirementController(view);
		controller2 = new SaveRequirementController(view);
		controller3 = new RetrieveAllUsersController(view2);
		controller4 = new SaveRequirementController(view);
		//controller5 = new RetrieveAllRequirementsController(null);		
	}

	@Test
	public void contructorSetsViewFieldCorrectly() {
		assertEquals(view, controller.detailPanel);
		assertEquals(view, controller2.detailPanel);
		//assertEquals(view2, controller3.view);  This field is not private and can't be seen
		assertEquals(view, controller4.detailPanel);
	}
	
	/*@Test
	public void requestBuiltCorrectly() {
		// See if the request was sent
			MockRequest request = ((MockNetwork)Network.getInstance()).getLastRequestMade();
			assertEquals(request,null); //No request should be sent
			
			r2 = new Requirement();
			controller.AddRequirement(r2);
			request = ((MockNetwork)Network.getInstance()).getLastRequestMade();

			// Validate the request
			assertEquals("/requirementsmanager/requirement", request.getUrl().getPath());
			assertEquals(HttpMethod.PUT, request.getHttpMethod());
			
			r2 = new Requirement("name", "description", 1, type.BLANK, new ArrayList(), new ArrayList(), 1, 1, new ArrayList(), new ArrayList());
			controller.AddRequirement(r2);
			MockRequest request2 = ((MockNetwork)Network.getInstance()).getLastRequestMade();

			//Show that the requests are not equal
			assertTrue(!request.equals(request2));
	}*/

	/*@Test
	public void ensureMockJTableIsValid() {
		setupMockJTable();
		assertEquals(3, view.getModel().getRowCount());
		assertEquals(1, view.getResultsTable().rowAtPoint(new Point(1, 0)));
		assertEquals("2", view.getModel().getValueAt(1, 0));
		assertEquals("2", view.getResultsTable().getModel().getValueAt(1, 0));
		assertEquals("2", (String) view.getResultsTable().getValueAt(1, 0));
	}

	@Test
	public void requestBuiltCorrectly() {
		// row to "click"
		int row = 1;

		// Replace results table with a mock table so we can fake the rowAtPoint() method
		setupMockJTable();
		assertEquals(3, view.getModel().getRowCount());

		// the id of the defect we are selecting
		int defectId = Integer.valueOf((String) view.getResultsTable().getValueAt(row, 0));

		// Construct dummy mouse click
		MouseEvent me = new MouseEvent(view.getResultsTable(), 0, 0, 0, row, 0, 2, false);

		// Click the mouse on the second row
		controller.mouseClicked(me);

		// See if the request was sent
		MockRequest request = ((MockNetwork)Network.getInstance()).getLastRequestMade();
		if (request == null) {
			fail("request not sent");
		}
		assertTrue(request.isSent());

		// Validate the request
		assertEquals("/defecttracker/defect/" + defectId, request.getUrl().getPath());
		assertEquals(HttpMethod.GET, request.getHttpMethod());
	}

	@Test
	public void invalidMouseClicksIgnoredProperly() {
		// invalid row click
		int row = -1;

		// Replace results table with a mock table so we can fake the rowAtPoint() method
		setupMockJTable();
		assertEquals(3, view.getModel().getRowCount());

		// Construct dummy mouse click
		MouseEvent me = new MouseEvent(view.getResultsTable(), 0, 0, 0, row, 0, 2, false);

		// Click the mouse on the second row
		controller.mouseClicked(me);

		// Request should not have been sent
		assertNull(((MockNetwork)Network.getInstance()).getLastRequestMade());
	}

	@Test
	public void singleClicksIgnoredProperly() {
		// Construct dummy mouse click
		MouseEvent me = new MouseEvent(view.getResultsTable(), 0, 0, 0, 0, 0, 1, false);

		// Click the mouse on the second row
		controller.mouseClicked(me);

		// Request should not have been sent
		assertNull(((MockNetwork)Network.getInstance()).getLastRequestMade());
	}

	private void setupMockJTable() {
		MockJTable mockResultsTable = new MockJTable(view.getModel());
		mockResultsTable.setDefaultRenderer(Date.class, new DateTableCellRenderer());
		mockResultsTable.addMouseListener(controller);
		view.setResultsTable(mockResultsTable);
		insertTestData();
	}

	private void insertTestData() {
		String[] columnNames = {"ID", "Title", "Description", "Creator", "Created", "Last Modified"};

		Object[][] newData = {
				{"1", "Defect 1", "Description", "ccasola", new Date(), new Date()},
				{"2", "Defect 2", "Description", "ccasola", new Date(), new Date()},
				{"3", "Defect 3", "Description", "ccasola", new Date(), new Date()},
		};

		view.getModel().setColumnNames(columnNames);
		view.getModel().setData(newData);
	}*/
}
