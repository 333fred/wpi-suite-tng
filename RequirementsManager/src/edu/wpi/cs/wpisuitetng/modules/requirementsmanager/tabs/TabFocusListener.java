/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

/** Interface for listening to when a tab has gained focus
 * 
 * @author Mitchell
 *
 */

public interface TabFocusListener {

	/** Function called when the tab gains focus 
	 * 
	 */
	
	public void onGainedFocus();
}
