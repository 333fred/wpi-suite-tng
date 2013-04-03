/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Steven Kordell
 * Controller for updating an existing defect
 */
public class SaveIterationController {
	
	DetailPanel detailPanel;
	
	public SaveIterationController (DetailPanel detailPanel){
		this.detailPanel = detailPanel;
	}
	
	/**
	 * Saves a iteration
	 * @param toAdd iteration that will be saved
	 */
	public void Saveiteration(Iteration toAdd) {
		final RequestObserver requestObserver = new UpdateIterationRequestObserver(detailPanel);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/iteration", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}


}
