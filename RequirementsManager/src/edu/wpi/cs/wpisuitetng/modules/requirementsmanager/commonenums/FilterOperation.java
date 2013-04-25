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
	
	LESS_THAN("<"), LESS_THAN_EQUAL("<="), EQUAL("Equal"), NOT_EQUAL(
			"Not equal"), GREATER_THAN_EQUAL(">="), GREATER_THAN(">"), OCCURS_BETWEEN(
			"Occurs between"), OCCURS_AFTER("Occurs after"), OCCURS_BEFORE(
			"Occurs before"), CONTAINS("Contains"), STARTS_WITH("Starts with");
	
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
	
	private String name;
	
	private FilterOperation(final String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
