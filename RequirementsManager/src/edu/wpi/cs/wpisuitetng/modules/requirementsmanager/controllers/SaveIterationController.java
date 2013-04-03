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
	
	private ISaveNotifier notifier; 
	
	public SaveIterationController (ISaveNotifier notifier){
		this.notifier = notifier;
	}
	
	/**
	 * Saves a iteration
	 * @param toAdd iteration that will be saved
	 */
	public void saveIteration(Iteration toAdd) {
		final RequestObserver requestObserver = new UpdateIterationRequestObserver(notifier);
		Request request;
		request = Network.getInstance().makeRequest("requirementsmanager/iteration", HttpMethod.POST);
		request.setBody(toAdd.toJSON());
		request.addObserver(requestObserver);
		request.send();
	}


}
