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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;

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
	private String stringValue;
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
	public Filter(User user, FilterField field, FilterOperation operation,
			Object value) {
		this.id = -1;
		this.creator = user;
		this.field = field;
		this.operation = operation;
		this.value = value;
		active = true;
	}

	/**
	 * Checks a requirement to see if it should be filtered
	 * 
	 * @param toFilter
	 *            the requirement to check
	 * @return true if the requirement should be kept, false if it should be removed
	 */
	public boolean shouldFilter(Requirement toFilter) {
		//if this is not active return false
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
			System.out.println("MAGIC!!!!!!!");
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
			return value.equals(getValue());
		default:
			System.out.println("MAGIC!!!!!!!");
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
			return value == (Double) getValue();
		case NOT_EQUAL:
			return value != (Double) getValue();
		case LESS_THAN:
			return value < (Double) getValue();
		case LESS_THAN_EQUAL:
			return value <= (Double) getValue();
		case GREATER_THAN_EQUAL:
			return value >= (Double) getValue();
		case GREATER_THAN:
			return value > (Double)getValue();
		default:
			System.out.println("MAGIC!!!!!!!");
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
			reqIteration = IterationDatabase.getInstance().getIteration(value);
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
			System.out.println("MAGIC!!!!!!!");
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
				+ operation + " Value: " + value + " Active: " + active + "]";
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
		return stringValue;
	}

	/**
	 * @param stringValue
	 *            the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
