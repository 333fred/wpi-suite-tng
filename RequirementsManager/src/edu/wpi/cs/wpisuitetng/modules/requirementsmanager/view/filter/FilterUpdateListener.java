/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mitchell Caisse
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

/**
 * Listener that will notify when a filter has been changed
 * 
 */

public interface FilterUpdateListener {
	
	/**
	 * Called when the active / inactive fitlers have been updated, or a new
	 * filter has been added
	 * 
	 */
	
	public void filtersUpdated();
}
