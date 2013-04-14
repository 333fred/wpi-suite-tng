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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.logger.ModelMapper;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevels;
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
			throws BadRequestException, ConflictException, WPISuiteException {

		// The user already has permissions assigned to them, so there's been a
		// problem. Throw a bad request
		if (db.retrieve(PermissionModel.class, "user", s.getUser(),
				s.getProject()) != null) {
			throw new BadRequestException();
		}

		// Create a permission model for the user. If the user is an admin, then
		// set them to be an admin. Otherwise, they have no permissions by
		// default
		PermissionModel model = new PermissionModel();
		model.setUser(s.getUser());
		model.setPermission(s.getUser().getPermission(s.getUser()) == Permission.WRITE ? UserPermissionLevels.ADMIN
				: UserPermissionLevels.NONE);

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
			throws NotFoundException, WPISuiteException {
		return db.retrieve(PermissionModel.class, "user", s.getUser(),
				s.getProject()).toArray(new PermissionModel[0]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PermissionModel[] getAll(Session s) throws WPISuiteException {
		return db.retrieve(PermissionModel.class, "user", s.getUser(),
				s.getProject()).toArray(new PermissionModel[0]);
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
				"user", updatedModel.getUser(), s.getProject()).get(0)) == null) {
			oldModel = new PermissionModel();
		}

		// Update the model
		updateMapper.map(updatedModel, oldModel);
		if (!db.save(oldModel)) {
			throw new WPISuiteException();
		}

		return oldModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(Session s, PermissionModel model) throws WPISuiteException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// Cannot delete permissions, that would be insecure
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// Cannot delete permissions, that would be insecure

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int Count() throws WPISuiteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

}
