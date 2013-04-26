package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

public class RefreshActionTest {

	@Before
	public void setup() {
		
	}
	
	@Test
	public void testConstructor() {
		RequirementTableView tableView = RequirementTableView.getInstance();
		RefreshAction refreshAction = new RefreshAction(tableView);
		
		//check to make sure that refresh action is not null
		assertNotNull(refreshAction);
		//check that it properly added the table view
		assertEquals(refreshAction.getRequirementTableView(), tableView);
	}
	
	/*
	 *  Singletons are null in JUNIT tests, hence this does not seem to work properly
	 * 
	@Test
	public void testActionPerformed() {
		RequirementTableView tableView = RequirementTableView.getInstance();
		RefreshAction refreshAction = new RefreshAction(tableView);
		
		//add some requirements to the database
		List<Requirement> requirements = new ArrayList<Requirement>();
		for (int i=0;i<5;i++) {
			requirements.add(new Requirement());
			requirements.get(i).setName("Requirement " + i);
		}
		System.out.println("RequirementTableView: " + tableView);
		
		//call the refresh action
		refreshAction.actionPerformed(new ActionEvent(new Object(), 5, "Refresh Pressed"));

		
		assertEquals(tableView.getBtnEdit().getText(), "Enable Editing");
		assertFalse(tableView.getBtnSave().isEnabled());
		
		
	}
*/
}
