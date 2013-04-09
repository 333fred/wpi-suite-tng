/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author 
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

public interface IDatabaseListener {

	/**
	 * Called when a change is detected in the database, to be used for updating
	 * the UI
	 */
	public void update();

	/**
	 * @return whether or not the listener should be removed
	 */
	public boolean shouldRemove();

}
