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
	String log;
	
	@Before
	public void setUp(){
	//bob = new User(null, null, null, 0);
	date = new Date();
	log = "log";
	}

	@Test
	public void testGetLog() {
		OldLog logTest = new OldLog(null, null, null);
		assertEquals(logTest.getLog(),null);
		logTest.setLog(log);
		assertEquals(logTest.getLog(),log);
	}
	
	@Test
	public void testGetLog2() {
		OldLog logTest = new OldLog(log, null, null);
		assertEquals(logTest.getLog(),log);
	}
	
	@Test
	public void testGetDate() {
		OldLog logTest = new OldLog(null, null, null);
		assertEquals(logTest.getDate(),null);
		logTest.setDate(date);
		assertEquals(logTest.getDate(),date);
	}
	
	@Test
	public void testGetDate2() {
		OldLog logTest = new OldLog(null, date, null);
		assertEquals(logTest.getDate(),date);
	}
	
	@Test
	public void testGetDate3() {
		OldLog logTest = new OldLog("OldLog",bob);
		assertTrue(!logTest.getDate().equals(null));
	}
	
	@Test
	public void testGetCreator() {
		OldLog logTest = new OldLog(null, null, null);
		assertEquals(logTest.getCreator(),null);
		logTest.setCreator(bob);
		assertEquals(logTest.getCreator(),bob);
	}
	
	@Test
	public void testGetCreator2() {
		OldLog logTest = new OldLog(null, null, bob);
		assertEquals(logTest.getCreator(),bob);
		
	}

}
