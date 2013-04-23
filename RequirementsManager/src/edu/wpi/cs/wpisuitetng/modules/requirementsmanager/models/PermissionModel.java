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
 *    @contributor Conor
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.Comparator;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.exceptions.UnauthorizedException;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

/**
 * Contains the permLevel for the current user
 */

public class PermissionModel extends AbstractModel implements Comparable {

	private int id;
	private User user;
	private UserPermissionLevel permLevel;
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
	 * Gets the singleton permLevel instance for this object
	 * 
	 * @return the singleton permLevel instance
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
		this.permLevel = UserPermissionLevel.OBSERVE;
		this.setId(-1);
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
	 * @return the singleton permLevel model
	 */
	public static PermissionModel fromJSONSingleton(String content) {
		init();
		final Gson parser = new Gson();
		PermissionModel json = parser
				.fromJson(content, PermissionModel[].class)[0];
		setUserPermissionLevelStatic(json.getPermLevel());
		setUserStatic(json.getUser());
		return getInstance();
	}

	/**
	 * Converts a JSON encoded array of Permission model to an instantiated
	 * object
	 * 
	 * @param content
	 *            the JSON encoded array
	 * @return the array of permLevel models
	 */
	public static PermissionModel[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, PermissionModel[].class);
	}

	/**
	 * Returns a non-singleton version of the given JSON encoded string. Should
	 * not be called on the client, ever. For any reason.
	 * 
	 * @param content
	 *            the JSON encoded server
	 * @return the non-singleton permLevel model
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
	 * Gets the user from the static permLevel model
	 * 
	 * @return the permLevel model
	 */
	public static User getUserStatic() {
		init();
		return model.getUser();
	}

	/**
	 * Sets the user in the static permLevel model
	 * 
	 * @param u
	 *            the user to set
	 */
	public static void setUserStatic(User u) {
		init();
		model.setUser(u);
	}

	/**
	 * @return the permLevel encoded as a UserPermissions class
	 */
	public UserPermissions getUserPermissions() {
		return new UserPermissions(permLevel);
	}

	/**
	 * Gets the permLevel level from the static permLevel model
	 * 
	 * @return the permLevel level
	 */
	public static UserPermissionLevel getPermissionStatic() {
		init();
		return model.getPermLevel();
	}

	/**
	 * Sets the permLevel level of the static permLevel model
	 * 
	 * @param level
	 *            the level to set
	 */
	public static void setUserPermissionLevelStatic(UserPermissionLevel level) {
		init();
		model.setPermLevel(level);
	}

	/**
	 * @return the permLevel
	 */
	public UserPermissionLevel getPermLevel() {
		return permLevel;
	}

	/**
	 * @param permLevel the permLevel to set
	 */
	public void setPermLevel(UserPermissionLevel permission) {
		this.permLevel = permission;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Object o){
		if(o instanceof PermissionModel){
			return(this.getUser().getName().compareTo(((PermissionModel) o).getUser().getName()));
		}
		return 999;
	}

}
