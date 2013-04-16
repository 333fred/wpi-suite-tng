/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
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

import java.util.Date;

/**
 * Value object for filtering iterations between a specified dates 
 */

public class FilterIterationBetween {

	
	private Date startDate;
	private Date endDate;
	
	
	public FilterIterationBetween(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/** Determines if the iteration passed in falls between the given dates
	 * 
	 * @param iteration The iteration to compare
	 * @return True if it is in the dates, false otherwise
	 */
	
	public boolean isIterationBetween(Iteration iteration) {
		return false;
	                                                        
	}
}
