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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

/**
 * Enum for the status of an acceptance test, or ATest for short. An ATest can
 * be either blank, passed, or failed.
 */

public enum ATestStatus {
	/** No/Unassigned status */
	BLANK,
	/** Passed status */
	PASSED,
	/** Failed status */
	FAILED
}
