/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.text.SimpleDateFormat;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

public class Task implements Event {
	private String name;
	private String description;
	private boolean completed;
	private User assignedUser;
	
	public Task(String name, String description){
		completed  = false;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		
		return completed;
	}

	/**
	 * @param completed the completed to set
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
	 * @param description the description to set
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the assignedUser
	 */
	public User getAssignedUser() {
		return assignedUser;
	}

	/**
	 * @param assignedUser the assignedUser to set
	 */
	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public String getTitle() {
		return "<html><font size=4><b>" + getName() + "</b></html>";		
	}
	
	/** Returns the content of this note to be displayed in the GUI, as specified by Event interface 
	 * 
	 * @return The content
	 */
	
	public String getContent() {
		String temp = "<html><i>" + parseNewLines(getDescription());
		String userMessage;
		String completeMessage;
		if(assignedUser == null) {
			userMessage = "<br><FONT COLOR=\"blue\"> No User Assigned";
		}
		else {
			userMessage = "<br><FONT COLOR=\"blue\">Assignee: " + assignedUser.getName();
		}
		if(this.completed) {
			completeMessage = "<br>Currently Completed";
		}
		else {
			completeMessage = "<br>In Progress";
		}
		//return assembled content string;
		return temp + userMessage + completeMessage + "</FONT COLOR></i></html>";
	}
	
	/** Changes the new line characters (\n) in the given string to html new line tags (<br>)
	 * 
	 * @param The string to parse
	 * @return The new string with <br>'s
	 */
	
	public String parseNewLines(String text) {
		text = text.replaceAll("\n", "<br>");
		return text;

	}
	
	

}
