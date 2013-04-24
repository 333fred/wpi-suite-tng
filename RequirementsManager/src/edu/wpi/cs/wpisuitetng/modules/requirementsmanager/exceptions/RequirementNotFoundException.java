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
 * An exception to indicate a requirement not found
 */
public class RequirementNotFoundException extends NotFoundException {

	/**
	 * Creates a new exception with the given id
	 * 
	 * @param id
	 */
	public RequirementNotFoundException(int id) {
		super(id);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return (Integer) notFound;
	}

}
