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
	/** If this filter is a name field */
	NAME("Name"),
	/** If this filter is a type field */
	TYPE("Type"),
	/** If this filter is a priority field */
	PRIORITY("Priority"),
	/** If this filter is a status field */
	STATUS("Status"),
	/** If this filter is an iteration field */
	ITERATION("Iteration"),
	/** If this filter is an estimate field */
	ESTIMATE("Estimate"),
	/** If this filter is an effort field */
	EFFORT("Effort"),
	/** If this filter is a release number field */
	RELEASE_NUMBER("Release Number");
	
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
	
	/** The nicely formatted field of this enum for toString */
	private final String name;
	
	private FilterField(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}