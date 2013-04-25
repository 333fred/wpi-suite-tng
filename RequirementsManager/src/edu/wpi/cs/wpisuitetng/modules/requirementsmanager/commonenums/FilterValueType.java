/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mitchell Caisse
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

import java.sql.Date;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.FilterIterationBetween;

/**
 * Enum representing the value for this FilterValue
 */
@SuppressWarnings ("rawtypes")
public enum FilterValueType {
	/** If this filter value is an integer */
	INTEGER(Integer.class),
	/** If this filter value is a string */
	STRING(String.class),
	/** If this filter value is a date */
	DATE(Date.class),
	/** If this filter value is a FilterIterationBetween */
	FIB(FilterIterationBetween.class),
	/** If this filter value is a priority */
	PRIORITY(Priority.class),
	/** If this filter value is a type */
	TYPE(Type.class),
	/** If this filter value is a status */
	STATUS(Status.class);
	
	/**
	 * gets the value for the given class type
	 * 
	 * @param type
	 *            the type to find the value for
	 * @return the value of the type
	 */
	public static FilterValueType getFromClassType(final Class type) {
		for (final FilterValueType value : FilterValueType.values()) {
			if (value.type.equals(type)) {
				return value;
			}
		}
		return null;
	}
	
	/** The class type of this value */
	private Class type;
	
	/**
	 * Creates a new value wit hteh given type
	 * 
	 * @param type
	 */
	private FilterValueType(final Class type) {
		this.type = type;
	}
	
	/**
	 * Gets the class type for this value
	 * 
	 * @return the class type
	 */
	
	public Class getClassType() {
		return type;
	}
}