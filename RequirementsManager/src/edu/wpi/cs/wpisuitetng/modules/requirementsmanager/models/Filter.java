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

import java.util.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterValueType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;

/**
 * This is a model for a filter. They are used by the UI to determine what to
 * display on the screen. We and reload them on the database so that the user
 * doesn't loose filters upon restarting Janeway
 * 
 * The value of the filter is stored in a JSON String to make it correctly load
 * and save to the server, The FilterValueType enum assists with this as well
 * 
 */

public class Filter extends AbstractModel {
	
	public static Filter cloneFilter(final Filter toClone) {
		return new Filter(toClone.getCreator(), toClone.getId(),
				toClone.getField(), toClone.getOperation(), toClone.getValue(),
				toClone.getStringValue(), toClone.getActive(),
				toClone.isDeleted());
	}
	
	/**
	 * Converts the given Json encoded filter to an actual filter object
	 * 
	 * @param content
	 *            the Json encoded filter
	 * @return the filter instance
	 */
	public static Filter fromJSON(final String content) {
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
	public static Filter[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Filter[].class);
	}
	
	private int id;
	
	private String creator;
	private FilterField field;
	private FilterOperation operation;
	/** String of the value, will store the JSON for the object */
	private String jsonValue;
	
	private String stringValue;
	
	private boolean active;
	
	private boolean deleted;
	
	/** Enum representing the type of value in this filter */
	private FilterValueType valueType;
	
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
	public Filter(final String u) {
		this(u, FilterField.NAME, FilterOperation.EQUAL, new String());
	}
	
	/**
	 * Creates a filter with the given fields
	 * 
	 * @param user
	 *            The user who created the filter
	 * @param field
	 *            The field that this filter operates on
	 * @param operation
	 *            The operation for the filter
	 * @param value
	 *            The value the filter looks for
	 */
	public Filter(final String user, final FilterField field,
			final FilterOperation operation, final Object value) {
		id = -1;
		creator = user;
		this.field = field;
		this.operation = operation;
		setValue(value); // set the value and valu type
		active = true;
		setDeleted(false);
	}
	
	private Filter(final String user, final int id, final FilterField field,
			final FilterOperation operation, final Object value,
			final String stringValue, final boolean active,
			final boolean deleted) {
		creator = user;
		this.id = id;
		this.field = field;
		this.operation = operation;
		this.stringValue = stringValue;
		setValue(value);
		this.active = active;
		this.deleted = deleted;
	}
	
	/**
	 * Helper function for shouldFilter that checks enum values
	 * 
	 * @param value
	 *            enum value to check
	 * @return whether to filter or not
	 */
	
	private boolean checkEnum(final Enum value) {
		switch (getOperation()) {
			case EQUAL:
				return value.equals(getValue());
				
			case NOT_EQUAL:
				return !value.equals(getValue());
			default:
				return false;
		}
	}
	
	/**
	 * Helper function for shouldFilter that checks integer values
	 * 
	 * @param value
	 *            integer value to check
	 * @return whether to filter or not
	 */
	
	private boolean checkInteger(final int value) {
		switch (getOperation()) {
			case EQUAL:
				return value == (Integer) getValue();
			case NOT_EQUAL:
				return value != (Integer) getValue();
			case LESS_THAN:
				return value < (Integer) getValue();
			case LESS_THAN_EQUAL:
				return value <= (Integer) getValue();
			case GREATER_THAN_EQUAL:
				return value >= (Integer) getValue();
			case GREATER_THAN:
				return value > (Integer) getValue();
			default:
				return false;
				
		}
	}
	
	/**
	 * Helper function for shouldFilter that checks iteration values
	 * 
	 * @param value
	 *            The id of the iteration to check
	 * @return whether to filter or not
	 */
	
	/*
	 * LESS_THAN("<"), LESS_THAN_EQUAL("<="), EQUAL("Equal"), NOT_EQUAL(
	 * "Not equal"), GREATER_THAN_EQUAL(">="), GREATER_THAN(">"),
	 * OCCURS_BETWEEN( "Occurs between"), OCCURS_AFTER("Occurs after"),
	 * OCCURS_BEFORE( "Occurs before"), CONTAINS("Contains"),
	 * STARTS_WITH("Starts with"); // lets check which field we need to
	 */
	
	private boolean checkIteration(final int value) {
		Iteration reqIteration;
		Date valueDate;
		try {
			reqIteration = IterationDatabase.getInstance().get(value);
		} catch (final IterationNotFoundException e) {
			// iteration is not value
			return false;
		}
		
		switch (getOperation()) {
			case EQUAL:
				return value == (Integer) getValue();
			case NOT_EQUAL:
				return value != (Integer) getValue();
			case OCCURS_AFTER:
				valueDate = (Date) getValue();
				// less than one if this date is before argument
				return reqIteration.getStartDate().compareTo(valueDate) >= 0;
			case OCCURS_BEFORE:
				valueDate = (Date) getValue();
				// less than one if this date is before argument
				return reqIteration.getStartDate().compareTo(valueDate) <= 0;
			case OCCURS_BETWEEN:
				return ((FilterIterationBetween) getValue())
						.isIterationBetween(reqIteration);
			default:
				return false;
		}
	}
	
	/**
	 * Helper function for shouldFilter that checks string values
	 * 
	 * @param value
	 *            String value to check
	 * @return whether to filter or not
	 */
	
	private boolean checkString(final String value) {
		switch (getOperation()) {
			case EQUAL:
				return value.equals(getValue());
			case NOT_EQUAL:
				return !value.equals(getValue());
			case CONTAINS:
				return value.contains((String) getValue());
			case STARTS_WITH:
				return value.startsWith((String) getValue());
			default:
				// we shold not get to default, what type of magic is this
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean equalToWithoutId(final Filter other) {
		return getField().equals(other.getField())
				&& getOperation().equals(other.getOperation())
				&& getValue().equals(other.getValue());
	}
	
	/**
	 * @return the active
	 */
	public boolean getActive() {
		return active;
	}
	
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * @return the field
	 */
	public FilterField getField() {
		return field;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the jsonValue
	 */
	public String getJsonValue() {
		return jsonValue;
	}
	
	/**
	 * @return the operation
	 */
	public FilterOperation getOperation() {
		return operation;
	}
	
	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		if ((field == FilterField.ITERATION)
				&& ((operation == FilterOperation.EQUAL) || (operation == FilterOperation.NOT_EQUAL))) {
			Iteration iteration;
			try {
				iteration = IterationDatabase.getInstance().get(
						(Integer) getValue());
			} catch (final IterationNotFoundException e) {
				// this filter has an invalid
				// TODO: we should delete this filter, doesnt seem like a good
				// idea to delete it here
				return "Not Found";
			}
			
			return iteration.getName();
			// we must get the operation, otherwise we return to string
			
		}
		return getValue().toString();
	}
	
	/**
	 * Returns a properly casted type of the Value Object
	 * 
	 * @return
	 */
	
	public Object getValue() {
		final Gson gson = new Gson();
		final Object o = gson.fromJson(jsonValue, valueType.getClassType());
		return o;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(final Object o) {
		if (o instanceof Filter) {
			final Filter f = (Filter) o;
			return f.getId() == id;
		} else {
			return false;
		}
	}
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * @return the whether or not this filter has been permanently deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}
	
	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(final String creator) {
		this.creator = creator;
	}
	
	/**
	 * @param deleted
	 *            set that this filter has been deleted. Once it has been
	 *            deleted, it will not be returned from the server
	 */
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(final FilterField field) {
		this.field = field;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}
	
	/**
	 * @param jsonValue
	 *            the jsonValue to set
	 */
	public void setJsonValue(final String jsonValue) {
		this.jsonValue = jsonValue;
	}
	
	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(final FilterOperation operation) {
		this.operation = operation;
	}
	
	/**
	 * @param stringValue
	 *            the stringValue to set
	 */
	public void setStringValue(final String stringValue) {
		this.stringValue = stringValue;
	}
	
	/**
	 * Sets the value type of the filter with the given value.
	 * 
	 * @param value
	 */
	
	public void setValue(final Object value) {
		valueType = FilterValueType.getFromClassType(value.getClass());
		final Gson gson = new Gson();
		jsonValue = gson.toJson(value); // convert the object to JSON
	}
	
	/**
	 * Checks a requirement to see if it should be filtered
	 * 
	 * @param toFilter
	 *            the requirement to check
	 * @return true if the requirement should be kept, false if it should be
	 *         removed
	 */
	public boolean shouldFilter(final Requirement toFilter) {
		// if this is not active return false
		if (!isActive()) {
			return false;
		}
		
		switch (getField()) {
			case NAME:
				return checkString(toFilter.getName());
			case RELEASE_NUMBER:
				return checkString(toFilter.getReleaseNum());
			case TYPE:
				return checkEnum(toFilter.getType());
			case PRIORITY:
				return checkEnum(toFilter.getPriority());
			case STATUS:
				return checkEnum(toFilter.getStatus());
			case ITERATION:
				return checkIteration(toFilter.getIteration());
			case ESTIMATE:
				return checkInteger(toFilter.getEstimate());
			case EFFORT:
				return checkInteger(toFilter.getEffort());
		}
		
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Filter.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[Filter ID:" + id + " Field:" + field + " Operation:"
				+ operation + " Value: " + jsonValue + " Active: " + active
				+ "]";
	}
	
}
