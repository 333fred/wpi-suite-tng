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

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Model to track the last assigned ID and provides the next ID to be assigned
 */
public class IdManager extends AbstractModel {
	
	String type;
	int curId;
	
	/**
	 * Creates a new id manager for the given type
	 * 
	 * @param type
	 *            the type to manage
	 */
	public IdManager(final String type) {
		this.type = type;
		curId = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the curId
	 */
	public int getCurId() {
		return curId;
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
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(final Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param curId
	 *            the curId to set
	 */
	public void setCurId(final int curId) {
		this.curId = curId;
	}
	
	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final String type) {
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, IdManager.class);
	}
	
}
