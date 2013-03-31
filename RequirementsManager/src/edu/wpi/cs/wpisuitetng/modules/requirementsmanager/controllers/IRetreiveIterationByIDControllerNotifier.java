package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

public interface IRetreiveIterationByIDControllerNotifier {

	/** Called when the requirement has been received by the server
	 *  
	 * @param requirement The received data
	 */
	
	public void receivedData(Iteration iteration);
	
	/** Called when a error was returned from the server, instead of data
	 * 
	 * @param errorMessage
	 */
	
	public void errorReceivingData(String errorMessage);
	
}
