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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import org.junit.Assert;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;

public class TestShouldFilter {
	
	@Test
	public void testFilterName() {
		final Requirement req = new Requirement();
		req.setName("Hello World");
		
		final Filter filter = new Filter();
		filter.setField(FilterField.NAME);
		filter.setOperation(FilterOperation.EQUAL);
		filter.setValue("Hello World");
		
		Assert.assertEquals(filter.shouldFilter(req), true);
	}
	
}
