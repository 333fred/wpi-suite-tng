/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Gorowara
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

public class FilterValidatorTest {
	
	Session session;
	Session otherSession;
	User userOne;
	User userTwo;
	
	@Before
	public void setup() {
		userOne = new User("User's Name", "username", "password", 1);
		userTwo = new User("Other Name", "notuser", "swordfish", 2);
		session = new Session(userOne, "Session ID");
		otherSession = new Session(userTwo, "Session Identification");
	}
	
	@Test
	public void testWithImproperUser() {
		final Filter f = new Filter(userOne.getUsername());
		final FilterValidator validator = new FilterValidator();
		final List<ValidationIssue> issueList = validator.validate(
				otherSession, f);
		Assert.assertEquals(issueList.size(), 1);
		Assert.assertFalse(issueList.get(0).hasFieldName());
	}
	
	@Test
	public void testWithProperUser() {
		final Filter f = new Filter(userOne.getUsername());
		final FilterValidator validator = new FilterValidator();
		final List<ValidationIssue> issueList = validator.validate(session, f);
		Assert.assertEquals(issueList.size(), 0);
	}
	
}
