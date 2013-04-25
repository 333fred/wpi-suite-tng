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
	
	public Task(final String name, final String description) {
		completed = false;
		this.name = name;
		this.description = description;
		id = -1;
		estimate = 0;
	}
	
	/**
	 * @return the assignedUser
	 */
	public String getAssignedUser() {
		return assignedUser;
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
		if (completed) {
			completeMessage = "<br><FONT COLOR=\"blue\">Currently Completed</FONT COLOR>";
		} else {
			completeMessage = "<br><FONT COLOR=\"red\">In Progress</FONT COLOR>";
		}
		estimateMessage = "<br><FONT COLOR=\"red\">Estimate: " + estimate
				+ "</FONT COLOR>";
		// return assembled content string;
		return temp + userMessage + completeMessage + estimateMessage + "</i>";
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @return the estimate of the task
	 */
	public int getEstimate() {
		return estimate;
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
	
	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + getName() + "</b></html>";
	}
	
	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		
		return completed;
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
	 * @param assignedUser
	 *            the assignedUser to set
	 */
	public void setAssignedUser(final String assignedUser) {
		this.assignedUser = assignedUser;
	}
	
	/**
	 * @param completed
	 *            the completed to set
	 */
	public void setCompleted(final boolean completed) {
		this.completed = completed;
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * @param est
	 *            the estimate to set
	 */
	public void setEstimate(final int est) {
		estimate = est;
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
	
}
