/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

public class UnauthorizedException extends Exception {

	public UserPermissionLevel required;
	public UserPermissionLevel have;

	/**
	 * Creates an exception with the current permissions and the necessary
	 * permissions
	 * 
	 * @param required
	 *            the necessary permission
	 * @param have
	 *            the possessed permission
	 */
	public UnauthorizedException(UserPermissionLevel required,
			UserPermissionLevel have) {
		this.required = required;
		this.have = have;
	}

	/**
	 * @return the required
	 */
	public UserPermissionLevel getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(UserPermissionLevel required) {
		this.required = required;
	}

	/**
	 * @return the have
	 */
	public UserPermissionLevel getHave() {
		return have;
	}

	/**
	 * @param have the have to set
	 */
	public void setHave(UserPermissionLevel have) {
		this.have = have;
	}
	
	

}
