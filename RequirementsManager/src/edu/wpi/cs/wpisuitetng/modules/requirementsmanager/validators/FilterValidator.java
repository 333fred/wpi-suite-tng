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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

/**
 * Validates Filters so that they fit in with the given Data
 * implementation.
 */
public class FilterValidator {

	/**
	 * Validates a given filter to ensure that it is correct before saving
	 * 
	 * @param s
	 *            the current session
	 * @param f
	 *            the filter to validate
	 * @return a list of validation issues. If there are none, this is the empy
	 *         list
	 */
	public List<ValidationIssue> validate(Session s, Filter f) {

		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();

		// Ensure the user is set correctly. If it isn't, then add an issue
		
		if ( !(f.getCreator().equals(s.getUsername())) ) {
			issues.add(new ValidationIssue(
					"Filter's user must be the current user!"));
		}


		// TODO: Do more than validate that the user is set correctly

		return issues;

	}

}
