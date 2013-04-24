package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel.Mode;

public class DetailPanelTest {
	
	MainTabController mainTabController = null;
	
	@Before
	public void setup(){
		if(this.mainTabController == null){
			this.mainTabController = new MainTabController();
		}
	}
	
	@Test
	public void testCreationForEachModeNew(){
		Requirement r = new Requirement();
		r.setStatus(Status.NEW);
		DetailPanel detailPanel = null;
		for(Mode mode : Mode.values()){
			detailPanel = new DetailPanel(r, mode, this.mainTabController);
			assertNotNull(detailPanel);
		}
	}
	
	@Test
	public void testCreationForComplete(){
		Requirement r = new Requirement();
		r.setStatus(Status.COMPLETE);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForDeleted(){
		Requirement r = new Requirement();
		r.setStatus(Status.DELETED);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForInProgress(){
		Requirement r = new Requirement();
		r.setStatus(Status.IN_PROGRESS);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}
	
	@Test
	public void testCreationForOpen(){
		Requirement r = new Requirement();
		r.setStatus(Status.OPEN);
		DetailPanel detailPanel = null;
		detailPanel = new DetailPanel(r, Mode.EDIT, this.mainTabController);
		assertNotNull(detailPanel);
	}

}
