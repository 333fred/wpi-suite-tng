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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Model class to hold a list of user names, only for querying the server
 */
public class StringListModel extends AbstractModel {
	
	/**
	 * Converts a given StringListModel from JSON to an actual object
	 * 
	 * @param content
	 *            The JSON encoded object
	 * @return the instance of the object
	 */
	public static StringListModel fromJson(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, StringListModel.class);
	}
	
	public static StringListModel[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, StringListModel[].class);
	}
	
	List<String> users;
	
	/**
	 * Standard constructor, initializes the list of users to an array list
	 */
	public StringListModel() {
		users = new ArrayList<String>();
	}
	
	/**
	 * General constructor, given some list of users
	 * 
	 * @param users
	 *            the users to hold
	 */
	public StringListModel(final List<String> users) {
		this.users = users;
	}
	
	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void delete() {
		// Unnecessary function for this model
	}
	
	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public Permission getPermission(final User u) {
		return null;
	}
	
	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the users
	 */
	public List<String> getUsers() {
		return users;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(final Object o) {
		return equals(o);
	}
	
	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void save() {
		// Unnecessary function for this model
	}
	
	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void setPermission(final Permission p, final User u) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void setProject(final Project p) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(final List<String> users) {
		this.users = users;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, StringListModel.class);
	}
	
}
