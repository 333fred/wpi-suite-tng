/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * Simple class to hold a note and any associated metadata
 */
public class Note implements Event {
	
	private String note;
	private Date date;
	private String creator;
	
	/**
	 * Create a blank note. The date will be recorded, but it can always be
	 * changed with setDate
	 */
	public Note() {
		date = new Date();
	}
	
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
	public Note(final String note, final Date date, final String creator) {
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
	public Note(final String note, final String creator) {
		this.note = note;
		this.creator = creator;
		date = new Date();
	}
	
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		Note compNote;
		if (o instanceof Note) {
			compNote = (Note) o;
		} else {
			return false;
		}
		if (!compNote.creator.equals(creator)) {
			return false;
		}
		if (!compNote.date.equals(date)) {
			return false;
		}
		if (!compNote.note.equals(note)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the content of this note to be displayed in the GUI, as specified
	 * by Event interface
	 * 
	 * @return The content
	 */
	
	@Override
	public String getContent() {
		return "<i>" + parseNewLines(getNote()) + "</i>";
	}
	
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * Returns the title of this note to be displayed in the GUI, as specified
	 * by Event interface
	 * 
	 * @return The title
	 */
	
	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + getCreator()
				+ "<font size=.25></b> added on "
				+ new SimpleDateFormat("MM/dd/yy hh:mm a").format(getDate())
				+ "</html>";
		
	}
	
	@Override
	public int hashCode() {
		int retVal = 0;
		
		retVal += 17 * creator.hashCode();
		retVal += 31 * date.hashCode();
		retVal += 13 * note.hashCode();
		
		return retVal;
	}
	
	/**
	 * Changes the new line characters (\n) in the given string to html new line
	 * tags (<br>
	 * )
	 * 
	 * @param text
	 *            The
	 *            string to parse
	 * @return The new string with <br>
	 *         's
	 */
	
	public String parseNewLines(String text) {
		text = text.replaceAll("\n", "<br>");
		return text;
		
	}
	
	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(final String creator) {
		this.creator = creator;
	}
	
	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(final Date date) {
		this.date = date;
	}
	
	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(final String note) {
		this.note = note;
	}
}
