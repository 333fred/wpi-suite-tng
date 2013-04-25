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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

/**
 * Abstract superclass for all the not found exceptions
 */

public class NotFoundException extends Exception {
	
	/** The auto-generated serial version id */
	private static final long serialVersionUID = 1L;
	/** The not found object */
	protected Object notFound;
	
	/**
	 * Creates a new not found exception for a model
	 * 
	 * @param notFound
	 *            the model not found
	 */
	public NotFoundException(final Object notFound) {
		this.notFound = notFound;
	}
	
	/**
	 * @return the notFound
	 */
	public Object getNotFound() {
		return notFound;
	}
	
}
