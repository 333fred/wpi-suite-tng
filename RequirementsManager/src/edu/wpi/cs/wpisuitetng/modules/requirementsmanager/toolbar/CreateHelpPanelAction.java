/**
 * Action invoked upon use of the Create Requirement key
 * Heavily adapted from CreateDefectAction in the DefectTracker module
 * 
 * @author Alex Gorowara 
 * 
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Action that calls {@link MainTabController#addCreateDefectTab()}, default mnemonic key is C. 
 */
@SuppressWarnings("serial")
public class CreateHelpPanelAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public CreateHelpPanelAction(MainTabController controller) {
		super("User Manual");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_U);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//controller.addHelpPanelTab();     Commented out, in favor of not opening a tab!!!
		URL index = null; //Will be our main index page
		
		File input = new File("index.html"); //Html file is our input
		String path = new String("file:///"+input.getAbsolutePath()); //Grab the url of the path
		
		try {
			index = new URL(path); //Try to set our index url given the path
		} catch (MalformedURLException e1) {
			System.out.println("Error setting URL path!");
			e1.printStackTrace();
		}
		//Create a desktop type in order to launch the user's default browser
		 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) { //If desktop was created and a browser is supported
		        try {
		            desktop.browse(index.toURI()); //Convert link to identifier and launch default browser
		        } catch (Exception f) {
		        	System.out.println("Error launching browser!");
		            f.printStackTrace();
		        }
		    }
	}

}
