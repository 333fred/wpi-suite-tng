package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Simple class to hold a note and any associated metadata
 * 
 * @author Fredrica
 */

public class Note {

	private String note;
	private Date date;
	private User creator;

	/**
	 * Create a new Note with everything specified
	 * 
	 * @param note
	 *            The content of the note
	 * @param date
	 *            The date the note was created
	 * @param creator
	 *            The user that created the note
	 */
	public Note(String note, Date date, User creator) {
		this.note = note;
		this.date = date;
		this.creator = creator;
	}

	/**
	 * Create a new note. The date and time is set to the current system date
	 * and time.
	 * 
	 * @param note
	 *            The content of the note
	 * @param creator
	 *            The creator of the note
	 */
	public Note(String note, User creator) {
		this.note = note;
		this.creator = creator;
		this.date = new Date();
	}

	/**
	 * Create a blank note. The date will be recorded, but it can always be
	 * changed with setDate
	 */
	public Note() {
		this.date = new Date();
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
}
