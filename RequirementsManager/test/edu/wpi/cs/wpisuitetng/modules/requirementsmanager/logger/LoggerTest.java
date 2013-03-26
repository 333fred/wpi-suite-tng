/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger;

import java.util.List;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger.Logger;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Log;
import edu.wpi.cs.wpisuitetng.Session;


/**
 * @author Alex Gorowara
 *
 */
public class LoggerTest {
	
	Requirement requirement;
	Session session;
	
	@Before
	public void setUp(){
		
		requirement = new Requirement();
		session = new Session(new User("John Doe", "JDoe", "Password", 7), "ssid");
		
	}
	
	/**
	 * Trivial test to ensure that JUnit is playing nicely;
	 * should always pass.
	 */
	@Test
	public void trivialTest(){
		
		assertEquals(1, 1);
		
	}
	
	/**
	 * Test to confirm that a creation message can be generated for a requirement.
	 */
	@Test
	public void testCreationLog(){
		
		requirement = new Requirement();
		Logger.logCreation(requirement, session);
		assertEquals(requirement.getLog().get(0).getCreator(), session.getUser());
		assertEquals(requirement.getLog().get(0).getLog(), "Created requirement<br>");
		
	}
	
	/**
	 * Test to confirm that the Logger can log a lack of events with a lack of logs.
	 */
	@Test
	public void testNoEvents(){
		
		requirement = new Requirement();
		Logger.logEvents(requirement, null, session);
		assertTrue(requirement.getLog().isEmpty());
		
	}
	
	/**
	 * Test to confirm that a single event can be added to a log.
	 */
	@Test
	public void testSingleEvent(){
		
		requirement = new Requirement();
		Logger.Event event = Logger.getInstance().new Event("Old Description", "New Description", Logger.EventType.DESC_CHANGE);
		List<Logger.Event> eventList = new ArrayList<Logger.Event>();
		eventList.add(event);
		Logger.logEvents(requirement, eventList, session);
		assertEquals(requirement.getLog().size(), 1);
		// TODO: add assertion that the log is what is expected		
	}
}
