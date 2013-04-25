/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Frederic Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

/**
 * An exception to manage iteration not found
 */

public class IterationNotFoundException extends NotFoundException {
	
	/** The auto-generated serial version id */
	private static final long serialVersionUID = 1660484672547645673L;
	
	/**
	 * Creates a new not found exception for an iteration
	 * 
	 * @param id
	 *            the not found iteration
	 */
	public IterationNotFoundException(final Integer id) {
		super(id);
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return (Integer) notFound;
		
	}
}
