package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public interface IRetreiveRequirementByIDControllerNotifier {

	/** Called when the requirement has been received by the server
	 *  
	 * @param requirement The received data
	 */
	
	public void receivedData(Requirement requirement);
	
	/** Called when a error was returned from the server, instead of data
	 * 
	 * @param errorMessage
	 */
	
	public void errorReceivingData(String errorMessage);
	
}
