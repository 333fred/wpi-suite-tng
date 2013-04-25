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
@SuppressWarnings ("hiding")
public class PermissionsDatabase extends AbstractDatabase<PermissionModel> {
	
	private Map<User, PermissionModel> permissions;
	private final PermissionModelController controller;
	private static PermissionsDatabase db;
	
	/**
	 * Gets the singleton Permissions Database instance
	 * 
	 * @return the permissions database
	 */
	public static PermissionsDatabase getInstance() {
		if (PermissionsDatabase.db == null) {
			PermissionsDatabase.db = new PermissionsDatabase();
		}
		return PermissionsDatabase.db;
	}
	
	private PermissionsDatabase() {
		super(300000);
		permissions = new HashMap<User, PermissionModel>();
		controller = new PermissionModelController();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void add(final PermissionModel model) {
		permissions.put(model.getUser(), model);
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addAll(final List<PermissionModel> permissions) {
		for (final PermissionModel i : permissions) {
			this.permissions.put(i.getUser(), i);
		}
		updateListeners();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized PermissionModel get(final int id)
			throws PermissionsNotFoundException {
		throw new PermissionsNotFoundException(PermissionModel.getUserStatic());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized List<PermissionModel> getAll() {
		List<PermissionModel> list = new ArrayList<PermissionModel>();
		list = Arrays.asList(permissions.values().toArray(
				new PermissionModel[0]));
		return list;
	}
	
	/**
	 * Searches perm database for a username to find permissions for
	 * 
	 * @param name
	 *            of user to find perm for
	 * @return Permissions for named user or null if permission does not exist
	 */
	public PermissionModel getPermission(final String name) {
		for (final PermissionModel aPer : permissions.values()) {
			if (PermissionModel.getInstance().getUser().getUsername()
					.equals(name)) {
				return aPer;
			}
		}
		return null;
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
	public synchronized PermissionModel getPermissions(final User u)
			throws PermissionsNotFoundException {
		if (permissions.get(u) != null) {
			return permissions.get(u);
		} else {
			throw new PermissionsNotFoundException(u);
		}
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
			final RetrieveAllPermissionsRequestObserver observer = new RetrieveAllPermissionsRequestObserver();
			controller.getAll(observer);
			try {
				// Sleep for five minutes
				Thread.sleep(secs);
			} catch (final InterruptedException ex) {
				ex.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void set(final List<PermissionModel> permissions) {
		this.permissions = new HashMap<User, PermissionModel>();
		for (final PermissionModel i : permissions) {
			this.permissions.put(i.getUser(), i);
		}
		ToolbarView.getInstance().refreshPermissions();
		updateListeners();
	}
}
