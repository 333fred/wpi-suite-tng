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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

/** Interface for rendering event panels (Notes and Logs) 
 * 
 * 
 * @author Mitchell
 *
 */

public interface Event {

	/** Returns this events title
	 * 
	 * @return The title
	 */
	public String getTitle();
	
	/** Returns this events content
	 * 
	 * @return The content
	 */	
	public String getContent();
	
}
