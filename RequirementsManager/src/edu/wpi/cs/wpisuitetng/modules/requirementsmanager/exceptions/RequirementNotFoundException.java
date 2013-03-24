/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

/**
 * @author Fredric
 *
 */
public class RequirementNotFoundException extends Exception {

	private long id;
	
	public RequirementNotFoundException(long id2){
		this.id = id2;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
}
