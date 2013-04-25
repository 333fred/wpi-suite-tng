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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

/**
 * This is an interface for all database listeners that are registered in the
 * database. The update method is run on the swing thread with
 * SwingUtils.invokeLater, so any code in the update method is swing threadsafe
 */
public interface IDatabaseListener {
	
	/**
	 * @return whether or not the listener should be removed
	 */
	public boolean shouldRemove();
	
	/**
	 * Called when a change is detected in the database, to be used for updating
	 * the UI
	 */
	public void update();
	
}
