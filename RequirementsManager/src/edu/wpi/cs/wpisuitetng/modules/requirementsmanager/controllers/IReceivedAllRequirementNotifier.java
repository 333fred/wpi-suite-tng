package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/** Interface used for RetreiveAllRequiremetnsController, and possibly elsewhere
 *  Will be notified when data has been returned, or an error was returned
 */

public interface IReceivedAllRequirementNotifier {
	
	/** Called when the requirements data has been received by the server
	 *  
	 * @param requirements The receied data
	 */
	
	public void receivedData(Requirement[] requirements);
	
	/** Called when a error was returned from the server, instead of data
	 * 
	 * @param RetrieveAllRequirementsRequestObserver 
	 */
	
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver);
}
