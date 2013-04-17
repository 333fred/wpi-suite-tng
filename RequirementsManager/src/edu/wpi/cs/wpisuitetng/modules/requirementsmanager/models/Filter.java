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
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;

/**
 * This is a model for a filter. They are used by the UI to determine what to
 * display on the screen. We and reload them on the database so that the user
 * doesn't loose filters upon restarting Janeway
 */

public class Filter extends AbstractModel {
	
	private int id;
	private User creator;
	private FilterField field;
	private FilterOperation operation;
	private Object value;
	private boolean active;

	/**
	 * Creates a blank filter with no user
	 */
	public Filter() {
		this(null, FilterField.NAME, FilterOperation.EQUAL, new String());
	}

	/**
	 * Creates a blank filter with the given user
	 * 
	 * @param u
	 *            the creator of the filter
	 */
	public Filter(User u) {
		this(u, FilterField.NAME, FilterOperation.EQUAL, new String());
	}
	
	/** Creates a filter with the given fields
	 *
	 * @param user The user who created the filter
	 * @param field The field that this filter operates on
	 * @param operation The operation for the filter
	 * @param value The value the filter looks for
	 */
	public Filter(User user, FilterField field, FilterOperation operation, Object value) {
		this.id = -1;
		this.creator = user;
		this.field = field;
		this.operation = operation;
		this.value = value;
		active = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Filter.class);
	}

	/**
	 * Converts the given Json encoded filter to an actual filter object
	 * 
	 * @param content
	 *            the Json encoded filter
	 * @return the filter instance
	 */
	public static Filter fromJSON(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Filter.class);
	}

	/**
	 * Converts the given Json encoded array of filters to an actual array of
	 * filter objects
	 * 
	 * @param content
	 *            the Json encoded filter array
	 * @return the filter array instance
	 */
	public static Filter[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Filter[].class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof Filter) {
			Filter f = (Filter) o;
			return f.getId() == this.id;
		} else {
			return false;
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * @return the field
	 */
	public FilterField getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(FilterField field) {
		this.field = field;
	}

	/**
	 * @return the operation
	 */
	public FilterOperation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(FilterOperation operation) {
		this.operation = operation;
	}

	/**
	 * @return the equalTo
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param equalTo
	 *            the equalTo to set
	 */
	public void setValue(Object value) {
		this.value = value;
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
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	

}
