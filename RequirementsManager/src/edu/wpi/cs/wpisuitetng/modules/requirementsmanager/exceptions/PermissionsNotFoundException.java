/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * An exception for a user with no permissions
 */

public class PermissionsNotFoundException extends NotFoundException {
	
	/** The auto-generated serial version id */
	private static final long serialVersionUID = 5792135152007298020L;
	
	/**
	 * Creates a new exception for a not found permission
	 * 
	 * @param u
	 *            the user for which there are no permissions
	 */
	public PermissionsNotFoundException(final User u) {
		super(u);
	}
	
	/**
	 * @return the not found user
	 */
	public User getUser() {
		return (User) notFound;
	}
	
}
