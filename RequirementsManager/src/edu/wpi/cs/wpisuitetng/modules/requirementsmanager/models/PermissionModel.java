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
 *    @contributor Conor Geary
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

/**
 * Contains the permLevel for the current user
 */
@SuppressWarnings ("rawtypes")
public class PermissionModel extends AbstractModel implements Comparable {
	
	/**
	 * Converts a JSON encoded array of Permission model to an instantiated
	 * object
	 * 
	 * @param content
	 *            the JSON encoded array
	 * @return the array of permLevel models
	 */
	public static PermissionModel[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, PermissionModel[].class);
	}
	
	private int id;
	private User user;
	private UserPermissionLevel permLevel;
	private static PermissionModel model;
	
	private static boolean initialized = false;
	
	/**
	 * Returns a non-singleton version of the given JSON encoded string. Should
	 * not be called on the client, ever. For any reason.
	 * 
	 * @param content
	 *            the JSON encoded server
	 * @return the non-singleton permLevel model
	 */
	public static PermissionModel fromJSON(final String content) {
		PermissionModel.init();
		final Gson parser = new Gson();
		return parser.fromJson(content, PermissionModel.class);
	}
	
	/**
	 * Gets the content from the JSON string and returns the updated singleton
	 * instance
	 * 
	 * @param content
	 *            the json encoded string
	 * @return the singleton permLevel model
	 */
	public static PermissionModel fromJSONSingleton(final String content) {
		PermissionModel.init();
		final Gson parser = new Gson();
		final PermissionModel json = parser.fromJson(content,
				PermissionModel[].class)[0];
		PermissionModel.setUserPermissionLevelStatic(json.getPermLevel());
		PermissionModel.setUserStatic(json.getUser());
		return PermissionModel.getInstance();
	}
	
	/**
	 * Gets the singleton permLevel instance for this object
	 * 
	 * @return the singleton permLevel instance
	 */
	public static PermissionModel getInstance() {
		PermissionModel.init();
		return PermissionModel.model;
	}
	
	/**
	 * Gets the permLevel level from the static permLevel model
	 * 
	 * @return the permLevel level
	 */
	public static UserPermissionLevel getPermissionStatic() {
		PermissionModel.init();
		return PermissionModel.model.getPermLevel();
	}
	
	/**
	 * Gets the user from the static permLevel model
	 * 
	 * @return the permLevel model
	 */
	public static User getUserStatic() {
		PermissionModel.init();
		return PermissionModel.model.getUser();
	}
	
	/**
	 * Private function to initialize the model;
	 */
	private static void init() {
		if (!PermissionModel.initialized) {
			PermissionModel.initialized = true;
			PermissionModel.model = new PermissionModel();
		}
	}
	
	/**
	 * Sets the permLevel level of the static permLevel model
	 * 
	 * @param level
	 *            the level to set
	 */
	public static void setUserPermissionLevelStatic(
			final UserPermissionLevel level) {
		PermissionModel.init();
		PermissionModel.model.setPermLevel(level);
	}
	
	/**
	 * Sets the user in the static permLevel model
	 * 
	 * @param u
	 *            the user to set
	 */
	public static void setUserStatic(final User u) {
		PermissionModel.init();
		PermissionModel.model.setUser(u);
	}
	
	/**
	 * Private constructor for a PermissionModel
	 */
	public PermissionModel() {
		user = null;
		permLevel = UserPermissionLevel.OBSERVE;
		setId(-1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(final Object o) {
		if (o instanceof PermissionModel) {
			return (getUser().getName().compareTo(((PermissionModel) o)
					.getUser().getName()));
		}
		return 999;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the permLevel
	 */
	public UserPermissionLevel getPermLevel() {
		return permLevel;
	}
	
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @return the permLevel encoded as a UserPermissions class
	 */
	public UserPermissions getUserPermissions() {
		return new UserPermissions(permLevel);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(final Object o) {
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
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}
	
	/**
	 * @param permission
	 *            the permLevel to set
	 */
	public void setPermLevel(final UserPermissionLevel permission) {
		permLevel = permission;
	}
	
	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, PermissionModel.class);
	}
	
}
