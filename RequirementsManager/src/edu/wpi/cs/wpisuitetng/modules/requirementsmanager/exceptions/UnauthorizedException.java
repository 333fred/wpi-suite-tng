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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevels;

public class UnauthorizedException extends Exception {

	public UserPermissionLevels required;
	public UserPermissionLevels have;

	/**
	 * Creates an exception with the current permissions and the necessary
	 * permissions
	 * 
	 * @param required
	 *            the necessary permission
	 * @param have
	 *            the possessed permission
	 */
	public UnauthorizedException(UserPermissionLevels required,
			UserPermissionLevels have) {
		this.required = required;
		this.have = have;
	}

	/**
	 * @return the required
	 */
	public UserPermissionLevels getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(UserPermissionLevels required) {
		this.required = required;
	}

	/**
	 * @return the have
	 */
	public UserPermissionLevels getHave() {
		return have;
	}

	/**
	 * @param have the have to set
	 */
	public void setHave(UserPermissionLevels have) {
		this.have = have;
	}
	
	

}
