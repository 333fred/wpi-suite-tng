/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Conor
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllPermissionsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.PermissionsNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Maintains a local database of user permissions.
 * 
 * @author Conor
 * 
 */
public class PermissionsDatabase extends Thread {

	private Map<User, PermissionModel> permissions;
	private List<IDatabaseListener> listeners;
	private RetrieveAllPermissionsController controller;
	private static PermissionsDatabase db;

	private PermissionsDatabase() {
		permissions = new HashMap<User, PermissionModel>();
		this.listeners = new ArrayList<IDatabaseListener>();
		this.controller = new RetrieveAllPermissionsController();
		setDaemon(true);
	}

	public static PermissionsDatabase getInstance() {
		if (db == null) {
			db = new PermissionsDatabase();
		}
		return db;
	}

	/**
	 * Sets the permissions to the given map
	 * 
	 * @param permissions
	 */
	public synchronized void setPermissions(
			Map<User, PermissionModel> permissions) {
		this.permissions = permissions;
		updateListeners();
	}

	/**
	 * Sets the permissions to the given list. This removes everything in the
	 * map and adds only things in the list
	 * 
	 * @param permissions
	 *            the permissions to add
	 */
	public synchronized void setPermissions(List<PermissionModel> permissions) {
		this.permissions = new HashMap<User, PermissionModel>();
		for (PermissionModel i : permissions) {
			this.permissions.put(i.getUser(), i);
		}
		updateListeners();
	}

	/**
	 * Adds the given permissions to the map. The difference between this and
	 * set permissions is that this doesn't erase all permissions, only
	 * adds/updates the given list
	 * 
	 * @param permissions
	 *            the permissions to add/update
	 */
	public synchronized void addPermissions(List<PermissionModel> permissions) {
		for (PermissionModel i : permissions) {
			this.permissions.put(i.getUser(), i);
		}
		updateListeners();
	}

	/**
	 * Adds or updates a specific permission
	 * 
	 * @param i
	 *            the permission to add/update
	 */
	public synchronized void addPermissions(PermissionModel i) {
		permissions.put(i.getUser(), i);
		updateListeners();
	}

	/**
	 * Get a specific permission from the local database
	 * 
	 * @param u
	 *            the user of requirement to get
	 * @return the permissions requested couldn't find the requirement
	 * @throws PermissionsNotFoundException
	 */
	public synchronized PermissionModel getPermissions(User u)
			throws RequirementNotFoundException, PermissionsNotFoundException {
		if (permissions.get(u) != null) {
			return permissions.get(u);
		} else {
			throw new PermissionsNotFoundException(u);
		}
	}

	/**
	 * Gets all the permissions in the local database
	 * 
	 * @return all the current arrays
	 */
	public synchronized List<PermissionModel> getAllPermissions() {
		List<PermissionModel> list = new ArrayList<PermissionModel>();
		list = Arrays.asList(permissions.values().toArray(
				new PermissionModel[0]));
		return list;
	}

	/**
	 * Call the update method on all registered listeners
	 */
	public synchronized void updateListeners() {
		List<IDatabaseListener> removes = new ArrayList<IDatabaseListener>();
		for (IDatabaseListener l : listeners) {
			l.update();
			if (l.shouldRemove()) {
				removes.add(l);
			}
		}
		for (IDatabaseListener l : removes) {
			listeners.remove(l);
		}
	}

	/**
	 * Registers a database listener
	 * 
	 * @param listener
	 *            the listener to register
	 */
	public synchronized void registerListener(IDatabaseListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a given listener from the list of listeners
	 * 
	 * @param listener
	 *            the listener to be removed
	 * @return True if the listener was removed or did not exits, false
	 *         otherwise
	 */
	public synchronized boolean removeListener(IDatabaseListener listener) {
		return listeners.remove(listener);
	}

	/**
	 * Runs every 5 minutes and updates the local requirements database, which
	 * will trigger an update of all listeners
	 */
	@Override
	public void run() {
		// Clear interrupted status
		interrupted();
		while (!interrupted()) {
			// Trigger an update
			controller.getAll();
			try {
				// Sleep for five minutes
				Thread.sleep(300000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}

	// TODO documentation
	public PermissionModel getPermission(String string) {
		for (PermissionModel aPer : permissions.values()) {
			if (aPer.getInstance().getUser().getName().equals(string)) {
				return aPer;
			}
		}
		return null;
	}
}
