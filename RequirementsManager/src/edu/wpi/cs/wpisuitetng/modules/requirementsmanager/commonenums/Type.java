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
 * Enum that specifies a requirement's type.
 */
public enum Type {
	/** No/Unassigned type for a requirement */
	BLANK("None"),
	/** Requirement is an epic */
	EPIC("Epic"),
	/** Requirement is a theme */
	THEME("Theme"),
	/** Requirement is a user */
	USER_STORY("User Story"),
	/** Requirement is a non-functional requirement */
	NON_FUNCTIONAL("Non Functional"),
	/** Requirement is a scenario */
	SCENARIO("Scenario");
	
	/**
	 * Return the enum that the given string represents
	 * 
	 * @param str
	 *            String to parse
	 * @return The enum value, or null if it doesnt exist
	 */
	
	public static Type getFromString(final String str) {
		for (final Type type : Type.values()) {
			if (str.equals(type.toString())) {
				return type;
			}
		}
		
		return null;
	}
	
	/** The nicely formatted name for toString */
	private final String name;
	
	private Type(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
