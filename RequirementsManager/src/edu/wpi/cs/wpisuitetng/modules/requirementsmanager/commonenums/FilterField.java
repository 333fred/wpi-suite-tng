/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

/**
 * Enum for the field to filter
 * 
 */
public enum FilterField {
	NAME("Name"), TYPE("Type"), PRIORITY("Priority"), STATUS("Status"), ITERATION(
			"Iteration"), ESTIMATE("Estimate"), EFFORT("Effort"), RELEASE_NUMBER(
			"Release Number");
	
	/**
	 * Return the enum that the given string represents
	 * 
	 * @param str
	 *            String to parse
	 * @return The enum value, or null if it doesnt exist
	 */
	
	public static FilterField getFromString(final String str) {
		for (final FilterField field : FilterField.values()) {
			if (str.equals(field.toString())) {
				return field;
			}
		}
		
		return null;
	}
	
	private String name;
	
	private FilterField(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}