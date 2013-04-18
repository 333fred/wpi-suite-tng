/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick Massa
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class NoteTest {

	Note n1;
	Note n2;
	Note n3;
	Note n4;
	Note n5;
	String bob;
	String note;
	Date date;

	@Before
	public void setUp() {

		bob = "bob";
		note = "test";
		date = new Date();
	}

	@Test
	public void testGetNote() {
		Note noteTest = new Note(null, null, null);
		assertEquals(noteTest.getNote(), null);
		noteTest.setNote(note);
		assertEquals(noteTest.getNote(), note);
	}

	@Test
	public void testGetNote2() {
		Note noteTest = new Note(note, null, null);
		assertEquals(noteTest.getNote(), note);
	}

	@Test
	public void testGetDate() {
		Note noteTest = new Note(null, null, null);
		assertEquals(noteTest.getDate(), null);
		noteTest.setDate(date);
		assertEquals(noteTest.getDate(), date);
	}

	@Test
	public void testGetDate2() {
		Note noteTest = new Note(null, date, null);
		assertEquals(noteTest.getDate(), date);
	}

	@Test
	public void testGetDate3() {
		Note noteTest = new Note("Note", bob);
		assertNotNull(noteTest.getDate());
	}

	@Test
	public void testGetDate4() {
		Note noteTest = new Note();
		assertTrue(noteTest.getDate().equals(new Date()));
	}

	@Test
	public void testGetCreator() {
		Note noteTest = new Note(null, null, null);
		assertEquals(noteTest.getCreator(), null);
		noteTest.setCreator(bob);
		assertEquals(noteTest.getCreator(), bob);
	}

	@Test
	public void testGetCreator2() {
		Note noteTest = new Note(null, null, bob);
		assertEquals(noteTest.getCreator(), bob);
	}

	@Test
	public void testEquals() {
		n1 = new Note(bob, date, note);
		n2 = new Note(bob, date, note);
		n3 = new Note("example", date, note);
		n4 = new Note(bob, new Date(1, 2, 3), note);
		n5 = new Note(bob, date, "example");
		assertTrue(n2.equals(n1));
		assertFalse(n1.equals(date));
		assertFalse(n3.equals(n1));
		assertFalse(n4.equals(n1));
		assertFalse(n5.equals(n1));

	}

	@Test
	public void testGetTitle() {
		n1 = new Note(bob, new Date(1, 2, 3), note);
		assertEquals(n1.getTitle(), "<html><font size=4><b>" + n1.getCreator()
				+ "<font size=.25></b> added on "
				+ new SimpleDateFormat("MM/dd/yy hh:mm a").format(n1.getDate())
				+ "</html>");
	}

	@Test
	public void testGetContent() {
		n1 = new Note(bob, new Date(1, 2, 3), note);
		assertEquals(n1.getContent(),
		"<I>" + n1.parseNewLines(n1.getNote()) + "</I>");		

	}
}
