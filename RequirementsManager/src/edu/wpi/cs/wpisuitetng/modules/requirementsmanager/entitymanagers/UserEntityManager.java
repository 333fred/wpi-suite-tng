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

	public UserEntityManager(Data db) {
		this.db = db;
	}

	/**
	 * Gets all of the users on the database, and stores them in a list inside
	 * of the StringListModel
	 */
	@Override
	public StringListModel makeEntity(Session s, String content) {
		// Get all users, loop through the array and add all their names to the
		// list, then return a new model with the list
		List<String> users = new ArrayList<String>();
		List<User> userList = Arrays.asList(s.getProject().getTeam());
		if (userList.get(0) == null) {
			return new StringListModel(users);
		} else {
			for (User user : userList) {
				users.add(user.getUsername());
			}
		}

		return new StringListModel(users);
	}

	/**
	 * This function does nothing but return a new StringListModel, as we don't
	 * save anything for this in the db
	 */
	@Override
	public StringListModel[] getEntity(Session s, String id) {
		StringListModel uList = new StringListModel();
		ArrayList<String> user = new ArrayList<String>(); 
		user.add(s.getUser().getUsername());
		uList.setUsers(user);
		StringListModel[] array = { uList };
		return array;
	}

	/**
	 * There is nothing stored in the database, so this function returns an
	 * array with a blank model
	 */
	@Override
	public StringListModel[] getAll(Session s) {
		List<User> userList = Arrays.asList(s.getProject().getTeam());
		StringListModel model = new StringListModel();
		List<String> names = new ArrayList<String>();
		for (User u : userList) {
			names.add(u.getUsername());
		}
		model.setUsers(names);
		StringListModel[] array = { model };
		return array;
	}

	/**
	 * Gets all of the users on the database, and stores them in a list inside
	 * of the StringListModel
	 */
	@Override
	public StringListModel update(Session s, String content) {
		List<String> users = new ArrayList<String>();
		List<User> userList = Arrays.asList(s.getProject().getTeam());
		if (userList.get(0) == null) {
			return new StringListModel(users);
		} else {
			for (User user : userList) {
				users.add(user.getUsername());
			}
		}

		return new StringListModel(users);
	}

	/**
	 * We don't store anything in the database, so we don't do anything here
	 */
	@Override
	public void save(Session s, StringListModel model) {

	}

	/**
	 * We don't store anything in the database, so we don't do anything here
	 */
	@Override
	public boolean deleteEntity(Session s, String id) {
		return true;
	}

	@Override
	public String advancedGet(Session s, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAll(Session s) {
		// TODO Auto-generated method stub

	}

	/**
	 * We don't store anything in the database, so return 0
	 */
	@Override
	public int Count() {
		return 0;
	}

	@Override
	public String advancedPut(Session s, String[] args, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String advancedPost(Session s, String string, String content) {
		// TODO Auto-generated method stub
		return null;
	}

}
