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
 * 
 */

public enum FilterValueType {
	INTEGER(Integer.class), STRING(String.class), DATE(Date.class), FIB(
			FilterIterationBetween.class), PRIORITY(Priority.class), TYPE(
			Type.class), STATUS(Status.class);
	
	/**
	 * gets the value for the given class type
	 * 
	 * @param type
	 * @return
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
	 * @return
	 */
	
	public Class getClassType() {
		return type;
	}
}