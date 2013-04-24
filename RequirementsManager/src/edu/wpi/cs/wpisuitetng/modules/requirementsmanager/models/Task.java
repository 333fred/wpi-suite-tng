/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * Model for a task
 */
public class Task implements Event {
	private String name;
	private String description;
	private boolean completed;
	private String assignedUser;
	private int estimate;
	private int id;

	public Task(String name, String description) {
		completed = false;
		this.name = name;
		this.description = description;
		this.id = -1;
		this.estimate = 0;		
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {

		return completed;
	}

	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the assignedUser
	 */
	public String getAssignedUser() {
		return assignedUser;
	}

	/**
	 * @param assignedUser
	 *            the assignedUser to set
	 */
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}

	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + getName() + "</b></html>";
	}

	/**
	 * Returns the content of this note to be displayed in the GUI, as specified
	 * by Event interface
	 * 
	 * @return The content
	 */

	@Override
	public String getContent() {
		String temp = "<i>" + parseNewLines(getDescription());
		String userMessage;
		String completeMessage;
		String estimateMessage;
		if (assignedUser == null) {
			userMessage = "<br><FONT COLOR=\"gray\">No User Assigned"
					+ "</FONT COLOR>";
		} else {
			userMessage = "<br><FONT COLOR=\"blue\">Assignee: " + assignedUser
					+ "</FONT COLOR>";
		}
		if (this.completed) {
			completeMessage = "<br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR>";
		} else {
			completeMessage = "<br><FONT COLOR=\"red\">In Progress</FONT COLOR>";
		}
		estimateMessage = "<br><FONT COLOR=\"red\">Estimate: " + this.estimate + "</FONT COLOR>";
		// return assembled content string;
		return temp + userMessage + completeMessage + estimateMessage + "</i>";
	}

	/**
	 * Changes the new line characters (\n) in the given string to html new line
	 * tags (<br>
	 * )
	 * 
	 * @param The
	 *            string to parse
	 * @return The new string with <br>
	 *         's
	 */

	public String parseNewLines(String text) {
		text = text.replaceAll("\n", "<br>");
		return text;

	}

	/**
	 * @return the id of the task
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the estimate of the task
	 */
	public int getEstimate() {
		return estimate;
	}

	/**
	 * @param est
	 *            the estimate to set
	 */
	public void setEstimate(int est) {
		this.estimate = est;
	}

}
