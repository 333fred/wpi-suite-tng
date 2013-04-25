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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

/**
 * Enum for the status of a requirement
 */

public enum Status {
	/** No/Unassigned status */
	BLANK("None"),
	/** A new requirement */
	NEW("New"),
	/** Requirement has been assigned to an iteration */
	IN_PROGRESS("In Progress"),
	/** The requirement is on the backlog */
	OPEN("Open"),
	/** The requirement is complete */
	COMPLETE("Complete"),
	/** The requirement has been deleted */
	DELETED("Deleted");
	
	/**
	 * Return the enum that the given string represents
	 * 
	 * @param str
	 *            String to parse
	 * @return The enum value, or null if it doesnt exist
	 */
	
	public static Status getFromString(final String str) {
		for (final Status status : Status.values()) {
			if (str.equals(status.toString())) {
				return status;
			}
		}
		
		return null;
	}
	
	/** The nicely formated string represeting the name for toSring */
	private final String name;
	
	private Status(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
