/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Conor
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class PermissionsNotFoundException extends Exception {
	
	private User u;
	
	public PermissionsNotFoundException(User u) {
		this.u = u;
	}
	
	public User getUser() {
		return u;
	}

}
