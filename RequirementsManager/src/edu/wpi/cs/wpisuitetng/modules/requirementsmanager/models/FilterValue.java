/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.sql.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterValueType;

/**
 * Object that the filter edits
 */
public class FilterValue {
	
	/**
	 * Converts the given Json encoded filter to an actual filter object
	 * 
	 * @param content
	 *            the Json encoded filter
	 * @return the filter instance
	 */
	public static FilterValue fromJSON(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, FilterValue.class);
	}
	
	/**
	 * Converts the given Json encoded array of filters to an actual array of
	 * filter objects
	 * 
	 * @param content
	 *            the Json encoded filter array
	 * @return the filter array instance
	 */
	public static FilterValue[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, FilterValue[].class);
	}
	
	/** Integer value */
	private int intValue;
	
	/** Date Value */
	private Date dateValue;
	
	/** String Value */
	private String stringValue;
	
	/** FilterIterationBetween value */
	private FilterIterationBetween fibValue;
	
	/** enum representing the type of value this is holding */
	private FilterValueType valueType;
	
	/**
	 * Creates a new FilterValue with the given value, and sets the appropriate
	 * field
	 * 
	 * @param value
	 *            The value to set
	 */
	
	public FilterValue(final Object value) {
		valueType = FilterValueType.getFromClassType(value.getClass());
		switch (valueType) {
			case DATE:
				dateValue = (Date) value;
				break;
			case FIB:
				fibValue = (FilterIterationBetween) value;
				break;
			case INTEGER:
				intValue = (Integer) value;
				break;
			case STRING:
				stringValue = (String) value;
				break;
			default:
				break;
		}
	}
	
	/**
	 * @return the dateValue
	 */
	public Date getDateValue() {
		return dateValue;
	}
	
	/**
	 * @return the fibValue
	 */
	public FilterIterationBetween getFibValue() {
		return fibValue;
	}
	
	/**
	 * Returns the filter value of this object
	 * 
	 * @return the filter value
	 */
	
	public Object getFilterValue() {
		switch (valueType) {
			case DATE:
				return dateValue;
			case FIB:
				return fibValue;
			case INTEGER:
				return intValue;
			case STRING:
				return stringValue;
				
			default:
				return null;
		}
	}
	
	/**
	 * @return the intValue
	 */
	public int getIntValue() {
		return intValue;
	}
	
	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}
	
	/**
	 * @return the valueType
	 */
	public FilterValueType getValueType() {
		return valueType;
	}
	
	/**
	 * @param dateValue
	 *            the dateValue to set
	 */
	public void setDateValue(final Date dateValue) {
		this.dateValue = dateValue;
	}
	
	/**
	 * @param fibValue
	 *            the fibValue to set
	 */
	public void setFibValue(final FilterIterationBetween fibValue) {
		this.fibValue = fibValue;
	}
	
	/**
	 * @param intValue
	 *            the intValue to set
	 */
	public void setIntValue(final int intValue) {
		this.intValue = intValue;
	}
	
	/**
	 * @param stringValue
	 *            the stringValue to set
	 */
	public void setStringValue(final String stringValue) {
		this.stringValue = stringValue;
	}
	
	/**
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(final FilterValueType valueType) {
		this.valueType = valueType;
	}
}
