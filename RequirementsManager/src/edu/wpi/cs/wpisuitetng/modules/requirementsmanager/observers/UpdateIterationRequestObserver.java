/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Steven Kordell
 * Controller for updating an existing Iteration
 */
public class UpdateIterationRequestObserver implements RequestObserver {
	
	DetailPanel detailPanel;
	
	public UpdateIterationRequestObserver (DetailPanel detailPanel) {
		this.detailPanel = detailPanel;	
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

		Iteration req = Iteration.fromJSON(response.getBody());
		IterationDatabase.getInstance().addIteration(req);
		
		//detailPanel.logView.refresh(req);
				
		/*if (response.getStatusCode() == 200) {
			// parse the Iteration from the body
			final Iteration Iteration = Iteration.fromJSON(response.getBody());

			// make sure the Iteration isn't null
			if (Iteration != null) {
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
