/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Desktop;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;

import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

//TODO: Documentation, and perhaps built-in browser?
/**
 * Panel for the user manual
 * 
 * @author Nick Massa, Pat Bobell
 *
 */
public class HelpPanel extends FocusableTab {

	Requirement requirement;
	
	/**
	 * Construct the panel.
	 * @throws Exception 
	 */
	
	public HelpPanel() {		
		setLayout(new BorderLayout(0, 0));
		JTextPane helpPane = new JTextPane(); //Create and add the panel
		JScrollPane scrollPane = new JScrollPane(helpPane); 
		helpPane.setEditable(false);
		add(scrollPane);
		
		/*helpPane.setContentType("text/html"); //Prepare for html and set the bounds
		helpPane.setBounds(new Rectangle(10, 10, 260, 365));*/
		
		URL index = null; //Will be our main index page
		
		File input = new File("index.html"); //Html file is our input
		String path = new String("file:///"+input.getAbsolutePath()); //Grab the url of the path
		System.out.println(path); //Make sure the path is correct!
		
		try {
			index = new URL(path); //Try to set our index url given the path
		} catch (MalformedURLException e1) {
			System.out.println("Error setting URL path!");
			e1.printStackTrace();
		}
		
		/*try {
			helpPane.setPage(index); //Try to set the page to our index (Will look awful, no CSS support)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		helpPane.setText("A web browser should load with the main page of the user manual.  If a browser does not open, search for index.html in the source and open it with a browser.");
		//Create a desktop type in order to launch the user's default browser
		 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) { //If desktop was created and a browser is supported
		        try {
		            desktop.browse(index.toURI()); //Convert link to identifier and launch default browser
		        } catch (Exception e) {
		        	System.out.println("Error launching browser!");
		            e.printStackTrace();
		        }
		    }

	}
}
