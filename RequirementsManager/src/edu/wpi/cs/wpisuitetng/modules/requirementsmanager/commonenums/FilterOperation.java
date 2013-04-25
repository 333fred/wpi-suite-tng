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
 * Enum for the filters that can be applied
 * 
 */

public enum FilterOperation {
	/** If this filter is a less-than operation */
	LESS_THAN("<"),
	/** If this filter is a less-than-or-equal operation */
	LESS_THAN_EQUAL("<="),
	/** If this filter is an equal operation */
	EQUAL("Equal"),
	/** If this filter is a not-equal operation */
	NOT_EQUAL("Not equal"),
	/** If this filter is a greater-than-equal operation */
	GREATER_THAN_EQUAL(">="),
	/** If this filter is a greater-than operation */
	GREATER_THAN(">"),
	/** If this filter is a occurs-between operation */
	OCCURS_BETWEEN("Occurs between"),
	/** If this filter is a occurs-after operation */
	OCCURS_AFTER("Occurs after"),
	/** If this filter is a occurs-before operation */
	OCCURS_BEFORE("Occurs before"),
	/** If this filter is a contains operation */
	CONTAINS("Contains"),
	/** If this filter is a starts-with operation */
	STARTS_WITH("Starts with");
	
	/**
	 * Return the enum that the given string represents
	 * 
	 * @param str
	 *            String to parse
	 * @return The enum value, or null if it doesnt exist
	 */
	public static FilterOperation getFromString(final String str) {
		for (final FilterOperation operation : FilterOperation.values()) {
			if (str.equals(operation.toString())) {
				return operation;
			}
		}
		
		return null;
	}
	
	/** Nicely formated name for the toString method */
	private final String name;
	
	private FilterOperation(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
