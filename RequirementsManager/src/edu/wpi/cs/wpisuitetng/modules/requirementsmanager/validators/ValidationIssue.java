/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 * 					  -- Changes made by Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Andrew Hurle
 *    @author Team Swagasaurus
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

/**
 * Represents an error in model validation.
 */
public class ValidationIssue {
	
	private final String message;
	private final String fieldName;
	
	/**
	 * Create a generic ValidationIssue with no specific field at fault
	 * 
	 * @param message
	 *            An error message ("You are not allowed to edit defects")
	 */
	public ValidationIssue(final String message) {
		this(message, null);
	}
	
	/**
	 * Create a ValidationIssue caused by a particular field in the model.
	 * 
	 * @param message
	 *            An error message ("Must be 5-100 characters")
	 * @param fieldName
	 *            The relevant field name ("title")
	 */
	public ValidationIssue(final String message, final String fieldName) {
		this.message = message;
		this.fieldName = fieldName;
	}
	
	/**
	 * @return the fieldName causing this error
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * @return the message associated with this error
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return true if this error has a fieldName
	 */
	public boolean hasFieldName() {
		return fieldName != null;
	}
	
}
