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
 * A default Save Notifier that does nothing
 */

public class DefaultSaveNotifier implements ISaveNotifier {

	public DefaultSaveNotifier() {

	}

	@Override
	public void responseSuccess() {

	}

	@Override
	public void responseError(int statusCode, String statusMessage) {

	}

	@Override
	public void fail(Exception exception) {

	}

}
