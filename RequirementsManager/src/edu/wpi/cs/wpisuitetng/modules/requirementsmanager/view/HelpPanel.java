/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

/**
 * Panel for the user manual
 * 
 * @author Nick Massa, Pat Bobell
 *
 */
public class HelpPanel extends JPanel {

	Requirement requirement;
	
	/**
	 * Construct the panel.
	 * @throws Exception 
	 */
	public HelpPanel() {
		
		JTextPane helpPane = new JTextPane();
		add(helpPane);
		
		helpPane.setContentType("text/html");
		helpPane.setBounds(new Rectangle(10, 10, 260, 365));
		
		URL url = null;

		/*
		File input = new File("main.html");
		try {
			FileInputStream inStream = new FileInputStream(input);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		try {
			url = new URL("http://pastehtml.com/iew/cxh3xp4jm.html");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			helpPane.setPage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
