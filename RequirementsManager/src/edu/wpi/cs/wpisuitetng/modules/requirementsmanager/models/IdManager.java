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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

public class IdManager extends AbstractModel {

	String type;
	int curId;

	/**
	 * Creates a new id manager for the given type
	 * 
	 * @param type
	 *            the type to manage
	 */
	public IdManager(String type) {
		this.type = type;
		this.curId = 0;
	}

	/**
	 * Gets the next id for a model
	 * 
	 * @return the next available id
	 */
	public int getNextId() {
		return ++curId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the curId
	 */
	public int getCurId() {
		return curId;
	}

	/**
	 * @param curId
	 *            the curId to set
	 */
	public void setCurId(int curId) {
		this.curId = curId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, IdManager.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
