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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Log;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class RequirementControllerTest {

	//TODO: Test the requirement controllers

	AddRequirementController controller;
	SaveRequirementController controller2;
	DetailPanel view;
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
		controller = new AddRequirementController(view);
		controller2 = new SaveRequirementController(view);
	}
	
	// This is here so that JUnit won't say that this failed
	// TODO: Replace with an actual test
	@Test
	public void notATest(){
		assertEquals(1, 1);
		
	}

	@Test
	public void contructorSetsViewFieldCorrectly() {
		assertEquals(view, controller.detailPanel);
		assertEquals(view, controller2.detailPanel);
	}
	
	@Test
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
	}

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
