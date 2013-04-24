/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Conor Geary
 *    @author Fredric Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.PermissionsNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllPermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;

/**
 * Maintains a local database of user permissions.
 */
public class PermissionsDatabase extends AbstractDatabase<PermissionModel> {

	private Map<User, PermissionModel> permissions;
	private PermissionModelController controller;
	private static PermissionsDatabase db;

	private PermissionsDatabase() {
		super(300000);
		permissions = new HashMap<User, PermissionModel>();
		this.controller = new PermissionModelController();
	}

	/**
	 * Gets the singleton Permissions Database instance
	 * 
	 * @return the permissions database
	 */
	public static PermissionsDatabase getInstance() {
		if (db == null) {
			db = new PermissionsDatabase();
		}
		return db;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void set(List<PermissionModel> permissions) {
		this.permissions = new HashMap<User, PermissionModel>();
		for (PermissionModel i : permissions) {
			this.permissions.put(i.getUser(), i);
		}
		ToolbarView.getInstance().refreshPermissions();
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void addAll(List<PermissionModel> permissions) {
		for (PermissionModel i : permissions) {
			this.permissions.put(i.getUser(), i);
		}
		updateListeners();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void add(PermissionModel i) {
		permissions.put(i.getUser(), i);
		updateListeners();
	}

	/**
	 * Get a specific permission from the local database
	 * 
	 * @param u
	 *            the user of requirement to get
	 * @return the permissions requested
	 * @throws PermissionsNotFoundException
	 *             if the permissions wasn't there
	 */
	public synchronized PermissionModel getPermissions(User u)
			throws PermissionsNotFoundException {
		if (permissions.get(u) != null) {
			return permissions.get(u);
		} else {
			throw new PermissionsNotFoundException(u);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized PermissionModel get(int id)
			throws PermissionsNotFoundException {
		throw new PermissionsNotFoundException(PermissionModel.getUserStatic());
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized List<PermissionModel> getAll() {
		List<PermissionModel> list = new ArrayList<PermissionModel>();
		list = Arrays.asList(permissions.values().toArray(
				new PermissionModel[0]));
		return list;
	}

	/**
	 * Runs every 5 minutes and updates the local requirements database, which
	 * will trigger an update of all listeners
	 */
	@Override
	public void run() {
		// Clear interrupted status
		Thread.interrupted();
		while (!Thread.interrupted()) {
			// Trigger an update
			RetrieveAllPermissionsRequestObserver observer = new RetrieveAllPermissionsRequestObserver();
			controller.getAll(observer);
			try {
				// Sleep for five minutes
				Thread.sleep(secs);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	/**
	 * Searches perm database for a username to find permissions for
	 * 
	 * @param name
	 *            of user to find perm for
	 * @return Permissions for named user or null if permission does not exist
	 */
	public PermissionModel getPermission(String name) {
		for (PermissionModel aPer : permissions.values()) {
			if (PermissionModel.getInstance().getUser().getUsername()
					.equals(name)) {
				return aPer;
			}
		}
		return null;
	}
}
