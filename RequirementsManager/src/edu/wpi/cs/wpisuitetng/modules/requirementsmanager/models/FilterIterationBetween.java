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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Value object for filtering iterations between a specified dates
 */
public class FilterIterationBetween implements Serializable {
	
	private static final long serialVersionUID = 490510282591238771L;
	
	/**
	 * Returns the string representation of the given data, disregarding the the
	 * time of day
	 * 
	 * @param date
	 *            the date to turn into a string
	 * @return the string version of the date, without the time
	 */
	public static String dateToStringNoTime(final Date date) {
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}
	
	private final Date startDate;
	
	private final Date endDate;
	
	/**
	 * Creates a new FilterIterationBetween with the given start and end dates
	 * 
	 * @param startDate
	 *            the start date of the filter
	 * @param endDate
	 *            the end date of the filter
	 */
	public FilterIterationBetween(final Date startDate, final Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 * @return the end date of the filter
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * @return the start date of the filter
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Determines if the iteration passed in falls between the given dates
	 * 
	 * @param iteration
	 *            The iteration to compare
	 * @return True if it is in the dates, false otherwise
	 */
	
	public boolean isIterationBetween(final Iteration iteration) {
		final Date iterationStartDate = iteration.getStartDate();
		final Date iterationEndDate = iteration.getEndDate();
		
		return (iterationStartDate.compareTo(startDate) >= 0)
				&& (iterationEndDate.compareTo(endDate) <= 0);
	}
	
	@Override
	public String toString() {
		return FilterIterationBetween.dateToStringNoTime(startDate) + " and "
				+ FilterIterationBetween.dateToStringNoTime(endDate);
	}
}
