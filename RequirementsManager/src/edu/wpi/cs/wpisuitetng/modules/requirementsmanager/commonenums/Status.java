/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

/**
 * Enum for the status of a requirement
 */

public enum Status {
	BLANK("None"), NEW("New"), IN_PROGRESS("In Progress"), OPEN("Open"), COMPLETE("Complete"), DELETED("Deleted");
	
	private String name;
	
	private Status(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	/** Return the enum that the given string represents
	 * 
	 * @param str String to parse
	 * @return The enum value, or null if it doesnt exist 
	 */
	
	public static Status getFromString(String str) {
		for (Status status: values()) {
			if (str.equals(status.toString())) {
				return status;
			}
		}
		
		return null;
	}
	
}
