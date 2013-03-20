package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

/**
 * @author Nick Massa
 *
 */

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class LogTest {
	
	User bob;
	Date date;
	
	@Before
	public void setUp(){
	//bob = new User(null, null, null, 0);
	date = new Date();
	}

	@Test
	public void testGetLog() {
		Log logTest = new Log(null, null, null);
		assertEquals(logTest.getLog(),null);
		logTest.setLog("Test");
		assertEquals(logTest.getCreator(),"Test");
	}
	
	@Test
	public void testGetLog2() {
		Log logTest = new Log("Test", null, null);
		assertEquals(logTest.getLog(),"Test");
	}
	
	@Test
	public void testGetDate() {
		Log logTest = new Log(null, null, null);
		assertEquals(logTest.getDate(),null);
		logTest.setDate(date);
		assertEquals(logTest.getCreator(),date);
	}
	
	@Test
	public void testGetDate2() {
		Log logTest = new Log(null, date, null);
		assertEquals(logTest.getDate(),date);
	}
	
	@Test
	public void testGetDate3() {
		Log logTest = new Log("Log",bob);
		assertTrue(!logTest.getDate().equals(null));
	}
	
	@Test
	public void testGetCreator() {
		Log logTest = new Log(null, null, null);
		assertEquals(logTest.getCreator(),null);
		logTest.setCreator(bob);
		assertEquals(logTest.getCreator(),bob);
	}
	
	@Test
	public void testGetCreator2() {
		Log logTest = new Log(null, null, bob);
		assertEquals(logTest.getCreator(),bob);
		
	}

}
