/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.sql.Date;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterValueType;

public class FilterValue {


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

	public FilterValue(Object value) {
		this.valueType = FilterValueType.getFromClassType(value.getClass());
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
	
	/** Returns the filter value of this object
	 * 
	 * @return
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
	 * {@inheritDoc}
	 */
	public String toJSON() {
		return new Gson().toJson(this, FilterValue.class);
	}

	/**
	 * Converts the given Json encoded filter to an actual filter object
	 * 
	 * @param content
	 *            the Json encoded filter
	 * @return the filter instance
	 */
	public static FilterValue fromJSON(String content) {
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
	public static FilterValue[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, FilterValue[].class);
	}

	/**
	 * @return the intValue
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * @param intValue the intValue to set
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	/**
	 * @return the dateValue
	 */
	public Date getDateValue() {
		return dateValue;
	}

	/**
	 * @param dateValue the dateValue to set
	 */
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	/**
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * @return the fibValue
	 */
	public FilterIterationBetween getFibValue() {
		return fibValue;
	}

	/**
	 * @param fibValue the fibValue to set
	 */
	public void setFibValue(FilterIterationBetween fibValue) {
		this.fibValue = fibValue;
	}

	/**
	 * @return the valueType
	 */
	public FilterValueType getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(FilterValueType valueType) {
		this.valueType = valueType;
	}
}
