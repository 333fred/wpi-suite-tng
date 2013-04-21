/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Nick Massa
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree;

import java.util.Comparator;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class RequirementComparator implements Comparator<Requirement> {

	/**
	 * Compares two iterations based upon their starting dates
	 * 
	 * Compares its two arguments for order. Returns a negative integer, zero,
	 * or a positive integer as the first argument is less than, equal to, or
	 * greater than the second.
	 * 
	 * first argument is less than second return negative int first argument is
	 * equal to seccond return 0 first argument is greater than second return
	 * postive int
	 * 
	 */

	@Override
	public int compare(Requirement requirement1, Requirement requirement2) {

		if (requirement1.getrUID() < requirement2.getrUID()) {
			return -1; // first argument is less, or before second
		} else if (requirement1.getrUID() > requirement2.getrUID()) {
			return 1; // first argument is more, or after second
		}
		return 0; // requirements are equal
	}
}
