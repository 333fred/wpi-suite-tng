/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Steve Kordell
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.ATestStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * An acceptance test
 */
public class ATest implements Event {
	
	private String name;
	private String description;
	private ATestStatus status;
	
	private int id;
	
	/**
	 * Creates an new acceptance test with the given name and description. The
	 * status is set to blank, and the id is set to -1 until it is properly
	 * assigned
	 * 
	 * @param name
	 *            the name of the ATest
	 * @param description
	 *            the description of the ATest
	 */
	public ATest(final String name, final String description) {
		status = ATestStatus.BLANK;
		this.name = name;
		this.description = description;
		id = -1;
	}
	
	/**
	 * Returns the content of this note to be displayed in the GUI, as specified
	 * by Event interface
	 * 
	 * @return The content
	 */
	
	@Override
	public String getContent() {
		final String temp = "<i>" + parseNewLines(getDescription());
		String completeMessage;
		if (status == ATestStatus.PASSED) {
			completeMessage = "<br><FONT COLOR=\"blue\">PASSED</FONT COLOR>";
		} else if (status == ATestStatus.FAILED) {
			completeMessage = "<br><FONT COLOR=\"red\">FAILED</FONT COLOR>";
		} else {
			completeMessage = "<br><FONT COLOR=\"green\">OPEN</FONT COLOR>";
		}
		// return assembled content string;
		return temp + completeMessage + "</i>";
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the id of the task
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the completed
	 */
	public ATestStatus getStatus() {
		return status;
	}
	
	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + getName() + "</b></html>";
	}
	
	/**
	 * @return Whether the ATest is has passed
	 */
	public boolean isPassed() {
		return getStatus() == ATestStatus.PASSED;
	}
	
	/**
	 * Changes the new line characters (\n) in the given string to html new line
	 * tags (<br>
	 * )
	 * 
	 * @param text
	 *            string to parse
	 * @return The new string with <br>
	 *         's
	 */
	
	public String parseNewLines(String text) {
		text = text.replaceAll("\n", "<br>");
		return text;
		
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * @param status
	 *            the new status
	 */
	public void setStatus(final ATestStatus status) {
		this.status = status;
	}
}
