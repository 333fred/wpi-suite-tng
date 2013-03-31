/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author Jason Whitehouse Controller used to add a requirement to the database
 */
public class AddIterationController {

	DetailPanel detailPanel;

	public AddIterationController(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	/**
	 * Adds a requirement to the database
	 * 
	 * @param toAdd
	 *            requirement that will be added
	 */
	public void AddRequirement(Iteration toAdd) {
		final RequestObserver requestObserver = new AddIterationRequestObserver(
				this, detailPanel); // you will probably want to pass your view
									// to the observer as well
		Request request;
		request = Network.getInstance().makeRequest(
				"requirementsmanager/requirement", HttpMethod.PUT);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}
}