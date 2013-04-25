/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

/**
 * Class that embodies the UserPermissionLevel, and abstract the rights of the
 * user into a single place
 * 
 */

public class UserPermissions {
	
	/** The level of the users permissions */
	private final UserPermissionLevel permissionLevel;
	
	/**
	 * Creates a User Permission with the given level
	 * 
	 * @param permissionLevel
	 *            The permission level
	 */
	public UserPermissions(final UserPermissionLevel permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
	
	/**
	 * Determines if a user can create an iteration
	 * 
	 * @return true if the user can create an iteration, false otherwise
	 */
	public boolean canCreateIteration() {
		return permissionLevel == UserPermissionLevel.ADMIN;
	}
	
	/**
	 * Determines if a user can create a requirement
	 * 
	 * @return true if the user can create a requirement, false otherwise
	 */
	public boolean canCreateRequirement() {
		return permissionLevel == UserPermissionLevel.ADMIN;
	}
	
	/**
	 * Determines if a user can edit an iteration
	 * 
	 * @return true if the user can edit an iteration, false otherwise
	 */
	public boolean canEditIteration() {
		return permissionLevel == UserPermissionLevel.ADMIN;
	}
	
	/**
	 * Determines if a user can edit a requirement
	 * 
	 * @return true if the user can edit a requirement, false otherwise
	 */
	public boolean canEditRequirement() {
		return permissionLevel == UserPermissionLevel.ADMIN;
	}
	
	/**
	 * Determines if a use can update a requirement
	 * 
	 * @return true if the user can update a requirement, false otherwise
	 */
	public boolean canUpdateRequirement() {
		return permissionLevel == UserPermissionLevel.UPDATE;
	}
	
	/**
	 * Returns the permission level enum of the user
	 * 
	 * @return the permission level of the user
	 */
	public UserPermissionLevel getPermissionLevel() {
		return permissionLevel;
	}
}
