/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Kyle
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.ATestStatus;

public class AcceptanceTestTest {
	
	ATest a1;
	
	String name = "name";
	String desc = "desc";
	
	@Before
	public void setUp() {
		a1 = new ATest(name, desc);
	}
	
	@Test
	public void testContent() {
		final String temp = "<i>" + a1.parseNewLines(a1.getDescription());
		Assert.assertEquals(a1.getContent(), temp
				+ "<br><FONT COLOR=\"green\">OPEN</FONT COLOR>" + "</i>");
		a1.setStatus(ATestStatus.PASSED);
		Assert.assertEquals(a1.getContent(), temp
				+ "<br><FONT COLOR=\"blue\">PASSED</FONT COLOR>" + "</i>");
		a1.setStatus(ATestStatus.FAILED);
		Assert.assertEquals(a1.getContent(), temp
				+ "<br><FONT COLOR=\"red\">FAILED</FONT COLOR>" + "</i>");
	}
	
	@Test
	public void testDesciption() {
		a1.setDescription("test");
		Assert.assertEquals(a1.getDescription(), "test");
	}
	
	@Test
	public void testId() {
		a1.setId(1);
		Assert.assertEquals(a1.getId(), 1);
	}
	
	@Test
	public void testName() {
		a1.setName("name");
		Assert.assertEquals(a1.getName(), "name");
	}
	
	@Test
	public void testParseNewLines() {
		final String text = "text\n";
		Assert.assertEquals(a1.parseNewLines(text), "text<br>");
	}
	
	@Test
	public void testStatus() {
		a1.setStatus(ATestStatus.PASSED);
		Assert.assertTrue(a1.isPassed());
		a1.setStatus(ATestStatus.FAILED);
		Assert.assertEquals(a1.getStatus(), ATestStatus.FAILED);
		Assert.assertFalse(a1.isPassed());
	}
	
	@Test
	public void testTitle() {
		Assert.assertEquals(a1.getTitle(), "<html><font size=4><b>" + "name"
				+ "</b></html>");
	}
	
}
