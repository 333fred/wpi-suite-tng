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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

/**
 * Entity manager for users
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.StringListModel;

/**
 * Simple entity manager to allow for getting all user names from the database
 */

public class UserEntityManager implements EntityManager<StringListModel> {
	
	Data db;
	
	/**
	 * Creates a new entity manager for retrieving users from the team
	 * 
	 * @param db
	 *            the database to interact with
	 */
	public UserEntityManager(final Data db) {
		this.db = db;
	}
	
	@Override
	public String advancedGet(final Session s, final String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String advancedPost(final Session s, final String string,
			final String content) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String advancedPut(final Session s, final String[] args,
			final String content) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * We don't store anything in the database, so return 0
	 */
	@Override
	public int Count() {
		return 0;
	}
	
	@Override
	public void deleteAll(final Session s) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * We don't store anything in the database, so we don't do anything here
	 */
	@Override
	public boolean deleteEntity(final Session s, final String id) {
		return true;
	}
	
	/**
	 * There is nothing stored in the database, so this function returns an
	 * array with a blank model
	 */
	@Override
	public StringListModel[] getAll(final Session s) {
		final List<User> userList = Arrays.asList(s.getProject().getTeam());
		final StringListModel model = new StringListModel();
		final List<String> names = new ArrayList<String>();
		for (final User u : userList) {
			if (u != null) {
				names.add(u.getUsername());
			}
		}
		model.setUsers(names);
		final StringListModel[] array = { model };
		return array;
	}
	
	/**
	 * This function does nothing but return a new StringListModel, as we don't
	 * save anything for this in the db
	 */
	@Override
	public StringListModel[] getEntity(final Session s, final String id) {
		final StringListModel uList = new StringListModel();
		final List<String> user = new ArrayList<String>();
		user.add(s.getUser().getUsername());
		uList.setUsers(user);
		final StringListModel[] array = { uList };
		return array;
	}
	
	/**
	 * Gets all of the users on the database, and stores them in a list inside
	 * of the StringListModel
	 */
	@Override
	public StringListModel makeEntity(final Session s, final String content) {
		// Get all users, loop through the array and add all their names to the
		// list, then return a new model with the list
		final List<String> users = new ArrayList<String>();
		final List<User> userList = Arrays.asList(s.getProject().getTeam());
		if (userList.get(0) == null) {
			return new StringListModel(users);
		} else {
			for (final User user : userList) {
				users.add(user.getUsername());
			}
		}
		
		return new StringListModel(users);
	}
	
	/**
	 * We don't store anything in the database, so we don't do anything here
	 */
	@Override
	public void save(final Session s, final StringListModel model) {
		
	}
	
	/**
	 * Gets all of the users on the database, and stores them in a list inside
	 * of the StringListModel
	 */
	@Override
	public StringListModel update(final Session s, final String content) {
		final List<String> users = new ArrayList<String>();
		final List<User> userList = Arrays.asList(s.getProject().getTeam());
		if (userList.get(0) == null) {
			return new StringListModel(users);
		} else {
			for (final User user : userList) {
				users.add(user.getUsername());
			}
		}
		
		return new StringListModel(users);
	}
	
}
