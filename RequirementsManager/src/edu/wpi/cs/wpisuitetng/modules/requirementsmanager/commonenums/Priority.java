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
 * Enum to set the requirement's priority
 */
public enum Priority {
	/** No/unassigned priority */
	BLANK("None"),
	/** Low priority */
	LOW("Low"),
	/** Medium priority */
	MEDIUM("Medium"),
	/** High priority */
	HIGH("High");
	
	/**
	 * Return the enum that the given string represents
	 * 
	 * @param str
	 *            String to parse
	 * @return The enum value, or null if it doesnt exist
	 */
	public static Priority getFromString(final String str) {
		for (final Priority priority : Priority.values()) {
			if (str.equals(priority.toString())) {
				return priority;
			}
		}
		return null;
	}
	
	/** Nicely formatted string for toString */
	private final String name;
	
	private Priority(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
