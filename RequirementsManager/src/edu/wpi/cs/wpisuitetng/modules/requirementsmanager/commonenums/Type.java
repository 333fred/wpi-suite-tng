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
	BLANK("None"), EPIC("Epic"), THEME("Theme"), USER_STORY("User Story"), NON_FUNCTIONAL(
			"Non Functional"), SCENARIO("Scenario");

	private String name;

	private Type(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Return the enum that the given string represents
	 * 
	 * @param str
	 *            String to parse
	 * @return The enum value, or null if it doesnt exist
	 */

	public static Type getFromString(String str) {
		for (Type type : values()) {
			if (str.equals(type.toString())) {
				return type;
			}
		}

		return null;
	}
}
