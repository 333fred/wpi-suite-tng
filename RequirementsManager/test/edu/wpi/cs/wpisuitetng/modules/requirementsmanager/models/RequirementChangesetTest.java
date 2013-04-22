package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;

public class RequirementChangesetTest {

	User user;
	Session session;
	
	@Before
	public void setup(){
		this.user = new User("User's Name", "username", "swordfish", 1);
		this.session = new Session(this.user, "SSID");
	}
	
	@Test
	public void testTitleRetrieval(){
		RequirementChangeset rc = new RequirementChangeset(this.user);
		assertTrue(rc.getTitle().contains(("User's Name")));
	}
	
	@Test
	public void testEmptyContent(){
		RequirementChangeset rc = new RequirementChangeset();
		assertEquals(rc.getContent(), "");
	}
	
	@Test
	public void testCreationLog(){
		Requirement r = new Requirement();
		r.logCreation(this.session);
		RequirementChangeset creationChangeset = r.getLogs().get(0);
		assertEquals(creationChangeset.getContent(), "Created Requirement<br></html>");
	}
	
}
