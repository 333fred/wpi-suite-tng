/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Jason Whitehouse
 * Controller for updating an existing requirement
 */
public class UpdateRequirementRequestObserver implements RequestObserver {
	
	DetailPanel detailPanel;
	boolean closeTab;
	
	public UpdateRequirementRequestObserver (DetailPanel detailPanel, boolean closeTab) {
		this.detailPanel = detailPanel;	
		this.closeTab = closeTab;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();
		
		if (this.closeTab) {
			this.detailPanel.getMainTabController().closeCurrentTab();
		}
		
		Requirement req = Requirement.fromJSON(response.getBody());
		
		RequirementDatabase.getInstance().addRequirement(req);
		
		detailPanel.logView.refresh(req);
				
		/*if (response.getStatusCode() == 200) {
			// parse the requirement from the body
			final Requirement requirement = Requirement.fromJSON(response.getBody());

			// make sure the requirement isn't null
			if (requirement != null) {
				//success here!
			} else {
				//Display error
			}
		} else {
			//Display Error
		}*/
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		this.detailPanel.displaySaveError("Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		this.detailPanel.displaySaveError("Unable to complete request: " + exception.getMessage());
	}

}
