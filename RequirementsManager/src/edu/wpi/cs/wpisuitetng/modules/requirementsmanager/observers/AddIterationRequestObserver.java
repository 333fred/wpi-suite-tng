/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author Jason Whitehouse Request Observer for [@link
 *         AddRequirementController]
 */

public class AddIterationRequestObserver implements RequestObserver {

	private AddIterationController controller;

	DetailPanel detailPanel;

	public AddIterationRequestObserver(AddIterationController controller, DetailPanel detailPanel) {
		this.controller = controller;
		this.detailPanel = detailPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi
	 * .cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// cast observable to a Request
		Request request = (Request) iReq;

		// get the response from the request
		ResponseModel response = request.getResponse();

		// print the body
		System.out.println("Received response: " + response.getBody());

		this.detailPanel.getMainTabController().closeCurrentTab();
		
/*		if (response.getStatusCode() == 201) {
			// parse the Requirement from the body
			final Requirement requirement = Requirement.fromJSON(response
					.getBody());

			// make sure the requirement isn't null
			if (requirement != null) {
			/omething with the requirement if wanted
				 singUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						((DefectPanel) view.getDefectPanel())
								.updateModel(defect);
						view.setEditModeDescriptors(defect);
					}
				});
				
				 //  JOptionPane.showMessageDialog(detailPanel, "SUCCESS","SUCCESS", JOptionPane.OK_OPTION);
			} else {
				
				 //Display error in view... here's how defecttracker does it:
				  JOptionPane.showMessageDialog(detailPanel, "Unable to parse defect received from server.",
				  "Save Defect Error", JOptionPane.ERROR_MESSAGE);
				 
			}
		} else {
			/*
			 * Display error in view... here's how defecttracker does it:
			 * JOptionPane.showMessageDialog(view, "Received " +
			 * iReq.getResponse().getStatusCode() + " status from server: " +
			 * iReq.getResponse().getStatusMessage(), "Save Defect Error",
			 * JOptionPane.ERROR_MESSAGE);
			 
		}*/

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.
	 * cs.wpisuitetng.network.models.IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.out.println("Error: " + iReq.getResponse().getBody());
		this.detailPanel.displaySaveError("Received " + iReq.getResponse().getStatusCode() + " error from server: " + iReq.getResponse().getStatusMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng
	 * .network.models.IRequest, java.lang.Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		this.detailPanel.displaySaveError("Unable to complete request: " + exception.getMessage());
	}

}
