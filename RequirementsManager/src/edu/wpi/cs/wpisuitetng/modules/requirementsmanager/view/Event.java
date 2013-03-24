package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

/** Interface for rendering event panels (Notes and Logs) 
 * 
 * 
 * @author Mitchell
 *
 */

public interface Event {

	/** Returns this events title
	 * 
	 * @return The title
	 */
	
	public String getTitle();
	
	/** Returns this events content
	 * 
	 * @return The content
	 */
	
	public String getContent();
	
}
