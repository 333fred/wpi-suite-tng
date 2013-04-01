/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

public class IterationNotFoundException extends Exception {
	private long id;

	public IterationNotFoundException(long id2) {
		this.id = id2;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
}
