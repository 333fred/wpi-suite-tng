/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Team Swagasaurus
 ********************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.IterationActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers.IterationEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.entitymanagers.RequirementsEntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Validates Iterations so that they fit in with the given Data
 * implementation.
 */
public class IterationValidator {
	
	/**
	 * Compares the two given dates based upon, year, month, and day
	 * 
	 * @param date1
	 *            The first date
	 * @param date2
	 *            The second date
	 * @return 0 if the dates are equal, -1 if date1 is before than date2, 1 if
	 *         date1 is after date 2
	 */
	
	public static int compareDatesWithoutTime(final Date date1, final Date date2) {
		final Calendar calendar1 = Calendar.getInstance();
		final Calendar tempCalendar = Calendar.getInstance();
		final Calendar calendar2 = Calendar.getInstance();
		
		// We can simply round off everything smaller than days, and then
		// compare
		tempCalendar.setTime(date1);
		calendar1.set(tempCalendar.get(Calendar.YEAR),
				tempCalendar.get(Calendar.MONTH),
				tempCalendar.get(Calendar.DAY_OF_MONTH));
		
		tempCalendar.setTime(date2);
		calendar2.set(tempCalendar.get(Calendar.YEAR),
				tempCalendar.get(Calendar.MONTH),
				tempCalendar.get(Calendar.DAY_OF_MONTH));
		
		return calendar1.getTime().compareTo(calendar2.getTime());
	}
	
	public static boolean overlapExists(final Iteration alpha,
			final Iteration beta) {
		
		// check if alpha starts before beta starts and ends on or before it
		// starts
		if ((IterationValidator.compareDatesWithoutTime(alpha.getStartDate(),
				beta.getStartDate()) == -1)
				&& (IterationValidator.compareDatesWithoutTime(
						alpha.getEndDate(), beta.getStartDate()) <= 0)) {
			return false;
		}
		
		// check if alpha starts on the date when beta ends or after
		if ((IterationValidator.compareDatesWithoutTime(alpha.getStartDate(),
				beta.getEndDate()) >= 0)
				&& (IterationValidator.compareDatesWithoutTime(
						alpha.getEndDate(), beta.getEndDate()) == 1)) {
			return false;
		}
		return true;
	}
	
	public List<ValidationIssue> validate(final Session s, final Iteration i,
			final IterationActionMode mode, final Data db) {
		final List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		
		// If the iteration is null, then we have an error
		if (i == null) {
			issues.add(new ValidationIssue("Iteration cannot be null"));
			return issues;
		}
		
		// Detect if the iteration has been given a name
		if (i.getName() == null) {
			issues.add(new ValidationIssue("Iteration must have a name"));
		} else {
			i.setName(i.getName().trim());
			if (i.getName().isEmpty()) {
				issues.add(new ValidationIssue(
						"Iteration name cannot be blank!"));
			}
		}
		
		// If the iteration is being created, then we must have no requirements
		if (mode == IterationActionMode.CREATE) {
			i.setRequirements(new ArrayList<Integer>());
		} else {
			// If we are not creating, make sure that requirements aren't null
			if (i.getRequirements() == null) {
				i.setRequirements(new ArrayList<Integer>());
			} else {
				// Loop through all the requirement ids and make sure that they
				// all exist
				final List<Requirement> reqs = Arrays
						.asList(new RequirementsEntityManager(db).getAll(s));
				for (final Integer id : i.getRequirements()) {
					boolean detected = false;
					for (final Requirement req : reqs) {
						if (req.getrUID() == id) {
							detected = true;
						}
					}
					if (!detected) {
						issues.add(new ValidationIssue(
								"Invalid Requirement ID " + id));
					}
				}
			}
		}
		
		// Make sure that the start date is before the end date, if it is not
		// the backlog
		if (!i.validateDate()) {
			issues.add(new ValidationIssue(
					"Iteration must start before it ends"));
		}
		
		// Make sure that this iteration does not overlap with any others
		final IterationEntityManager manager = new IterationEntityManager(db);
		Iteration[] iterations = new Iteration[0];
		// attempt to retrieve iterations for this session's project
		try {
			iterations = manager.getAll(s);
		} catch (final WPISuiteException e) {
			System.out
					.println("There was an error retrieving iterations from the database: IterationValidator:93");
		}
		for (final Iteration itr : iterations) {
			// if an iteration overlaps with the one being validated
			if (itr.getId() == i.getId()) {
				// This is the same iteration, so we don't have to check for
				// overlap.
			} else if (IterationValidator.overlapExists(itr, i)) {
				// report the name of both iterations and that they overlap
				issues.add(new ValidationIssue(itr.toString() + " overlaps "
						+ i.toString()));
			}
			if (i.getName() != null) {
				if (itr.getId() == i.getId()) {
					// This is the same iteration, so of course it will have the
					// same name
				} else if (itr.getName().compareTo(i.getName()) == 0) {
					issues.add(new ValidationIssue(
							"Iteration name must be unique!"));
					break;
				}
			}
		}
		
		return issues;
	}
	
}
