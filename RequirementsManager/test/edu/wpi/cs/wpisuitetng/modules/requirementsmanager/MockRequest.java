/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class MockRequest extends Request {
	
	protected boolean sent = false;
	
	public MockRequest(final NetworkConfiguration networkConfiguration,
			final String path, final HttpMethod requestMethod) {
		super(networkConfiguration, path, requestMethod);
	}
	
	public boolean isSent() {
		return sent;
	}
	
	@Override
	public void send() throws IllegalStateException {
		// don't actually send
		sent = true;
	}
}
