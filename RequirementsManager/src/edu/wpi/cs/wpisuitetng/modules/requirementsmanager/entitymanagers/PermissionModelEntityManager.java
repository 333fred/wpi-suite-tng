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
 *    @author Conor
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;

/**
 * Entity manager for permission models
 */

public class PermissionModelEntityManager implements
		EntityManager<PermissionModel> {

	Data db;
	ModelMapper updateMapper;

	/**
	 * Creates a new entity manager with the given database backend
	 * 
	 * @param db
	 *            the database backend
	 */
	public PermissionModelEntityManager(Data db) {
		this.db = db;
		this.updateMapper = new ModelMapper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PermissionModel makeEntity(Session s, String content)
			throws BadRequestException, WPISuiteException {

		// The user already has permissions assigned to them, so there's been a
		// problem. Throw a bad request
		if (db.retrieve(PermissionModel.class, "id", s.getUser().getIdNum(),
				s.getProject()).toArray(new PermissionModel[0]).length != 0) {
			throw new BadRequestException();
		}

		// Create a permission model for the user. If the user is an admin, then
		// set them to be an admin. Otherwise, they have no permissions by
		// default
		PermissionModel model = new PermissionModel();
		model.setUser(s.getUser());
		model.setId(s.getUser().getIdNum());
		model.setPermLevel(s.getUser().getRole() == Role.ADMIN ? UserPermissionLevel.ADMIN
				: UserPermissionLevel.OBSERVE);

		// Save the permission to the database
		if (!db.save(model, s.getProject())) {
			throw new WPISuiteException();
		}

		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PermissionModel[] getEntity(Session s, String id)
			throws WPISuiteException {
		try {
			// Attempt to make permissions for the user
			PermissionModel[] perms = { makeEntity(s, "") };
			return perms;
		} catch (BadRequestException e) {
			// Get the pre-existing perms and return it
			return db.retrieve(PermissionModel.class, "id",
					s.getUser().getIdNum(), s.getProject()).toArray(
					new PermissionModel[0]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PermissionModel[] getAll(Session s) {
		PermissionModel model;
		for (User u : s.getProject().getTeam()) {
			if (u != null) {
				System.out.println(u);
				if (!modelExists(s, u.getIdNum())) {
					model = new PermissionModel();
					model.setUser(u);
					model.setId(u.getIdNum());
					db.save(model, s.getProject());
				}
			}
		}
		if (!modelExists(s, s.getProject().getOwner().getIdNum())) {
			model = new PermissionModel();
			model.setUser(s.getProject().getOwner());
			model.setId(s.getProject().getOwner().getIdNum());
			db.save(model, s.getProject());
		}
		return db.retrieveAll(new PermissionModel(), s.getProject()).toArray(
				new PermissionModel[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PermissionModel update(Session s, String content)
			throws WPISuiteException {
		PermissionModel updatedModel = PermissionModel.fromJSON(content);
		PermissionModel oldModel;
		// If the permission exists, then get it. Otherwise, create a new model
		if ((oldModel = (PermissionModel) db.retrieve(PermissionModel.class,
				"id", updatedModel.getUser().getIdNum(), s.getProject()).get(0)) == null) {
			oldModel = new PermissionModel();
		}

		// Update the model
		updateMapper.map(updatedModel, oldModel);
		if (!db.save(oldModel, s.getProject())) {
			throw new WPISuiteException();
		}

		return oldModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, PermissionModel model) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEntity(Session s, String id) {
		// Cannot delete permissions, that would be insecure
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedGet(Session s, String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll(Session s) {
		// Cannot delete permissions, that would be insecure

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPost(Session s, String string, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Helper for populating permissions db for all users
	 * 
	 * @param s
	 *            Current session
	 * @param id
	 *            User id integer
	 * @return True if user already has permissions on database False if
	 *         permissions for user don't exist
	 */
	private boolean modelExists(Session s, int id) {
		try {
			if (db.retrieve(PermissionModel.class, "id", id, s.getProject())
					.toArray(new PermissionModel[0]).length != 0) {
				return true;
			}
			return false;
		} catch (BadRequestException e) {
			return true;
		} catch (WPISuiteException e) {
			e.printStackTrace();
			return true;
		}

	}

}
