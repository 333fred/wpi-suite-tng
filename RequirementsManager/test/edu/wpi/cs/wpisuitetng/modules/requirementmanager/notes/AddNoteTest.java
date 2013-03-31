
/**
 * 
 */


package edu.wpi.cs.wpisuitetng.modules.requirementmanager.notes;


/**
 * @author Kyle
 *
 */

/**
import static org.junit.Assert.*;
import org.uispec4j.*;

import org.junit.Before;
import org.junit.Test;

//import Janeway/src/edu.wpi.cs.wpisuitetng.janeway;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

public class AddNoteTest extends UISpecTestCase {

 //      UISpec4J.init();
  //}
	
	@Before
	//protected void setUp() throws Exception {
	 //   setAdapter(new MainClassAdapter(Main.class, new String[0]));
	  //  }
	
	@Test
	public void testAddNote() throws Exception  {
		Window window = getMainWindow();
		Table table = window.getTable();
		Button button = window.getButton("Add note");
		
		window.getTextBox("Add a new note:").setText("Test note.");
		button.click();
		assertTrue(table.contentEquals(new String[][]{{"Test note."}}));
	}
}
*/




