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

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

public class Task extends AbstractModel implements Event {
	private String name;
	private String description;
	private boolean completed;
	private String assignedUser;
	private int id;

	public Task(String name, String description) {
		completed = false;
		this.name = name;
		this.description = description;
		this.id = -1;
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
		String temp = "<html><i>" + parseNewLines(getDescription());
		String userMessage;
		String completeMessage;
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
		// return assembled content string;
		return temp + userMessage + completeMessage + "</i></html>";
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
	 * Override of the equals method to allow for comparison of tasks using the
	 * getId method
	 */
	@Override
	public boolean equals(Object o) {
		System.out.println("Called!");
		if (o instanceof Task) {
			Task t = (Task) o;
			if (t.getId() == this.getId()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns whether this task has been modified from the given task. It will
	 * return false either if it hasn't been modified, or if the given task is
	 * does not have the same id as us
	 * 
	 * @param t
	 *            the task to compare
	 * @return whether the task has been modified
	 */
	public boolean modified(Task t) {
		if (this.equals(t)) {
			// Check to see if anything has been modified
			return super.equals(t);
		} else {
			// This is a different task than us, so return false
			return false;
		}
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

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJSON() {
		return new Gson().toJson(this);
	}
	
	public static Task fromJSON(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Task.class);
	}
	
	public static Task[] fromJSONArray(String content){
		final Gson parser = new Gson();
		return parser.fromJson(content, Task[].class);
	}

	@Override
	public Boolean identify(Object o) {
		if (o instanceof Task) {
			Task t = (Task) o;
			if (t.getId() == this.getId()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
