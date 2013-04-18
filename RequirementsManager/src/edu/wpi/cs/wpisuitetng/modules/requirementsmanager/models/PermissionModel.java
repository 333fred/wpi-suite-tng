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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevels;

/**
 * Contains the permission for the current user
 */

public class PermissionModel extends AbstractModel {

	private User user;
	private UserPermissionLevels permission;
	private static PermissionModel model;
	private static boolean initialized = false;

	/**
	 * Private function to initialize the model;
	 */
	private static void init() {
		if (!initialized) {
			initialized = true;
			model = new PermissionModel();
		}
	}

	/**
	 * Gets the singleton permission instance for this object
	 * 
	 * @return the singleton permission instance
	 */
	public static PermissionModel getInstance() {
		init();
		return model;
	}

	/**
	 * Private constructor for a PermissionModel
	 */
	public PermissionModel() {
		this.user = null;
		this.permission = UserPermissionLevels.NONE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, PermissionModel.class);
	}

	/**
	 * Gets the content from the JSON string and returns the updated singleton
	 * instance
	 * 
	 * @param content
	 *            the json encoded string
	 * @return the singleton permission model
	 */
	public static PermissionModel fromJSONSingleton(String content) {
		init();
		final Gson parser = new Gson();
		System.out.println("Json Received " + content);
		PermissionModel json = parser
				.fromJson(content, PermissionModel[].class)[0];
		setUserPermissionLevelStatic(json.getPermission());
		setUserStatic(json.getUser());
		return getInstance();
	}

	/**
	 * Returns a non-singleton version of the given JSON encoded string. Should
	 * not be called on the client, ever. For any reason.
	 * 
	 * @param content
	 *            the JSON encoded server
	 * @return the non-singleton permission model
	 */
	public static PermissionModel fromJSON(String content) {
		init();
		final Gson parser = new Gson();
		return parser.fromJson(content, PermissionModel.class);
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the user from the static permission model
	 * 
	 * @return the permission model
	 */
	public static User getUserStatic() {
		init();
		return model.getUser();
	}

	/**
	 * Sets the user in the static permission model
	 * 
	 * @param u
	 *            the user to set
	 */
	public static void setUserStatic(User u) {
		init();
		model.setUser(u);
	}

	/**
	 * @return the permission
	 */
	public UserPermissionLevels getPermission() {
		return permission;
	}

	/**
	 * Gets the permission level from the static permission model
	 * 
	 * @return the permission level
	 */
	public static UserPermissionLevels getPermissionStatic() {
		init();
		return model.getPermission();
	}

	/**
	 * Sets the permission level of the static permission model
	 * 
	 * @param level
	 *            the level to set
	 */
	public static void setUserPermissionLevelStatic(UserPermissionLevels level) {
		init();
		model.setPermission(level);
	}

	/**
	 * @param permission
	 *            the permission to set
	 */
	public void setPermission(UserPermissionLevels permission) {
		this.permission = permission;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof PermissionModel) {
			return ((PermissionModel) o).getUser().equals(user);
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
	}

}
