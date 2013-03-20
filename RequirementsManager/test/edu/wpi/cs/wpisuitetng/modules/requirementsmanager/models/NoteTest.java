package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

/**
 * @author Nick Massa
 *
 */

import static org.junit.Assert.*;

import java.util.Date;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import org.junit.Before;
import org.junit.Test;

public class NoteTest {
	
	String bob;
	Date date;
	
	@Before
	public void setUp(){
	bob = "bob";
	date = new Date();
	}

	@Test
	public void testGetNote() {
		Note noteTest = new Note(null, null, null);
		assertEquals(noteTest.getNote(),null);
		noteTest.setNote("Test");
		assertEquals(noteTest.getCreator(),"Test");
	}
	
	@Test
	public void testGetNote2() {
		Note noteTest = new Note("Test", null, null);
		assertEquals(noteTest.getNote(),"Test");
	}
	
	@Test
	public void testGetDate() {
		Note noteTest = new Note(null, null, null);
		assertEquals(noteTest.getDate(),null);
		noteTest.setDate(date);
		assertEquals(noteTest.getCreator(),date);
	}
	
	@Test
	public void testGetDate2() {
		Note noteTest = new Note(null, date, null);
		assertEquals(noteTest.getDate(),date);
	}
	
	@Test
	public void testGetDate3() {
		Note noteTest = new Note("Note",bob);
		assertTrue(!noteTest.getDate().equals(null));
	}
	
	@Test
	public void testGetCreator() {
		Note noteTest = new Note(null, null, null);
		assertEquals(noteTest.getCreator(),null);
		noteTest.setCreator(bob);
		assertEquals(noteTest.getCreator(),bob);
	}
	
	@Test
	public void testGetCreator2() {
		Note noteTest = new Note(null, null, bob);
		assertEquals(noteTest.getCreator(),bob);		
	}

}
