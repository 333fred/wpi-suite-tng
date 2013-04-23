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

import java.util.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
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
 * The value of the fitler is stored in a JSON String to make it correctly load
 * and save to the server, The FilterValueType enum assists with this as well
 * 
 */

public class Filter extends AbstractModel {

	private int id;
	private String creator;
	private FilterField field;
	private FilterOperation operation;

	/** String of the value, will store the JSON for the object */
	private String jsonValue;
	private String stringValue;
	private boolean active;

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
	public Filter(String u) {
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
	public Filter(String user, FilterField field, FilterOperation operation,
			Object value) {
		this.id = -1;
		this.creator = user;
		this.field = field;
		this.operation = operation;
		setValue(value); // set the value and valu type
		active = true;
	}

	/**
	 * Checks a requirement to see if it should be filtered
	 * 
	 * @param toFilter
	 *            the requirement to check
	 * @return true if the requirement should be kept, false if it should be
	 *         removed
	 */
	public boolean shouldFilter(Requirement toFilter) {
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
	 * Helper function for shouldFilter that checks string values
	 * 
	 * @param value
	 *            String value to check
	 * @return whether to filter or not
	 */

	private boolean checkString(String value) {
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
	 * Helper function for shouldFilter that checks enum values
	 * 
	 * @param value
	 *            enum value to check
	 * @return whether to filter or not
	 */

	private boolean checkEnum(Enum value) {
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

	private boolean checkInteger(int value) {
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

	private boolean checkIteration(int value) {
		Iteration reqIteration;
		Date valueDate;
		try {
			reqIteration = IterationDatabase.getInstance().get(value);
		} catch (IterationNotFoundException e) {
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
	 * Returns a properly casted type of the Value Object
	 * 
	 * @return
	 */

	public Object getValue() {
		final Gson gson = new Gson();
		Object o = gson.fromJson(jsonValue, valueType.getClassType());
		return o;
	}

	/**
	 * Sets the value type of the filter with the given value.
	 * 
	 * @param value
	 */

	public void setValue(Object value) {
		valueType = FilterValueType.getFromClassType(value.getClass());
		final Gson gson = new Gson();
		jsonValue = gson.toJson(value); // convert the object to JSON
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[Filter ID:" + id + " Field:" + field + " Operation:"
				+ operation + " Value: " + jsonValue + " Active: " + active
				+ "]";
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
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator) {
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
	 * @return the jsonValue
	 */
	public String getJsonValue() {
		return jsonValue;
	}

	/**
	 * @param jsonValue
	 *            the jsonValue to set
	 */
	public void setJsonValue(String jsonValue) {
		this.jsonValue = jsonValue;
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
	 * @return the active
	 */
	public boolean getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		if (field == FilterField.ITERATION
				&& (operation == FilterOperation.EQUAL || operation == FilterOperation.NOT_EQUAL)) {
			Iteration iteration;
			try {
				iteration = IterationDatabase.getInstance().get(
						(Integer) getValue());
			} catch (IterationNotFoundException e) {
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
	 * @param stringValue
	 *            the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public boolean equalToWithoutId(Filter other) {
		return getField().equals(other.getField())
				&& getOperation().equals(other.getOperation())
				&& getValue().equals(other.getValue());
	}

}
