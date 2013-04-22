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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
	public void setup(){
		this.userOne = new User("User's Name", "username", "password", 1);
		this.userTwo = new User("Other Name", "notuser", "swordfish", 2);
		this.session = new Session(this.userOne, "Session ID");
		this.otherSession = new Session(this.userTwo, "Session Identification");
	}
	
	@Test
	public void testWithProperUser(){
		Filter f = new Filter(userOne);
		FilterValidator validator = new FilterValidator();
		List<ValidationIssue> issueList = validator.validate(this.session, f);
		assertEquals(issueList.size(), 0);
	}
	
	@Test
	public void testWithImproperUser(){
		Filter f = new Filter(userOne);
		FilterValidator validator = new FilterValidator();
		List<ValidationIssue> issueList = validator.validate(this.otherSession, f);
		assertEquals(issueList.size(), 1);
		assertFalse(issueList.get(0).hasFieldName());
	}
	
}
