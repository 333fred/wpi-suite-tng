package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;


public interface IRetreivedAllIterationsNotifier {
	/** Called when the requirements data has been received by the server
	 *  
	 * @param requirements The received data
	 */
	
	public void receivedData(Iteration[] iterations);
	
	/** Called when a error was returned from the server, instead of data
	 * 
	 * @param RetrieveAllRequirementsRequestObserver 
	 */
	
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver);
}
