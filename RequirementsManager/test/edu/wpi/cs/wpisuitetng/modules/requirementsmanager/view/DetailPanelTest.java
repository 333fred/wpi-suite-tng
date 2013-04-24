package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

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
	public void testCreationForEachMode(){
		Requirement r = new Requirement();
		DetailPanel detailPanel = null;
		for(Mode mode : Mode.values()){
			detailPanel = new DetailPanel(r, mode, this.mainTabController);
			assertNotNull(detailPanel);
		}

	}

}
