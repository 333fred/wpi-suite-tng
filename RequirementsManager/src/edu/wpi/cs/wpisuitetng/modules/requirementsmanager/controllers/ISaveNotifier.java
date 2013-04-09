/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers;

/**
 * Notifier for Saving an iteration / requirement
 */

public interface ISaveNotifier {

	/** Called when saving was successful */

	public void responseSuccess();

	/**
	 * Called when there was an error response from the sever
	 * 
	 * @param statusCode
	 *            The status code
	 * @param statusMessage
	 *            The status message
	 */

	public void responseError(int statusCode, String statusMessage);

	/**
	 * Called when saving completely failed,
	 * 
	 * @param exception
	 *            The exception that was created
	 */

	public void fail(Exception exception);
}
