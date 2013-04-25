/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PermissionsPanelTest {
	
	/** the permissions panel to test */
	private PermissionsPanel panel;
	
	@Before
	public void setup() {
		panel = null;
	}
	
	/** Make sure that the constructor doesn't error or anything */
	@Test
	public void testConstructor() {
		panel = new PermissionsPanel();
		Assert.assertNotNull(panel);
	}
	
}
