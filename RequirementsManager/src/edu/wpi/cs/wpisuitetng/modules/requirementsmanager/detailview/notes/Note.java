/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes;

/**
 * @author Zac
 * Model that represents a Note on a defect
 */
public class Note {
	private int dID;
	private String body;
	
	public Note(String body) {
		this.dID = 0;
		this.body = body;
	}
}


