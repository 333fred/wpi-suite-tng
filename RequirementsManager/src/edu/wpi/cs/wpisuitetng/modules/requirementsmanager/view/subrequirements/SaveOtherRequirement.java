package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class SaveOtherRequirement implements ISaveNotifier {
	
	public SaveOtherRequirement() {

	}

	@Override
	public void responseSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MainTabController getTabController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Requirement getDraggedRequirement() {
		// TODO Auto-generated method stub
		return null;
	}

}
