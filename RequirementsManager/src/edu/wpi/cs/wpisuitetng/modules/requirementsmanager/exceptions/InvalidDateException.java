/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Frederic Silberberg
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

import java.util.Date;

/**
 * Thrown when a user attempts to put in a date and it is not formatted properly or chronologically possible
 */
public class InvalidDateException extends Exception {

	Date invalidDate;

	public InvalidDateException(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

}
