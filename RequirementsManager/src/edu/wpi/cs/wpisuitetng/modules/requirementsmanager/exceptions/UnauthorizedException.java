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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

/**
 * Exception for attempting to edit a file without the appropriate permission
 * level
 */
public class UnauthorizedException extends Exception {
	
	private UserPermissionLevel required;
	private UserPermissionLevel have;
	
	/**
	 * Creates an exception with the current permissions and the necessary
	 * permissions
	 * 
	 * @param required
	 *            the necessary permission
	 * @param have
	 *            the possessed permission
	 */
	public UnauthorizedException(final UserPermissionLevel required,
			final UserPermissionLevel have) {
		this.required = required;
		this.have = have;
	}
	
	/**
	 * @return the have
	 */
	public UserPermissionLevel getHave() {
		return have;
	}
	
	/**
	 * @return the required
	 */
	public UserPermissionLevel getRequired() {
		return required;
	}
	
	/**
	 * @param have
	 *            the have to set
	 */
	public void setHave(final UserPermissionLevel have) {
		this.have = have;
	}
	
	/**
	 * @param required
	 *            the required to set
	 */
	public void setRequired(final UserPermissionLevel required) {
		this.required = required;
	}
	
}
