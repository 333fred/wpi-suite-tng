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
	private final Date startDate;
	private final Date endDate;
	
	public FilterIterationBetween(final Date startDate, final Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public String dateToStringNoTime(final Date date) {
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
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
		return dateToStringNoTime(startDate) + " and "
				+ dateToStringNoTime(endDate);
	}
}
