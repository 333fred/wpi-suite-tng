/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import javax.swing.JTextPane;

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
	 */
	public HelpPanel() {
		
		JTextPane helpPane = new JTextPane();
		add(helpPane);
		
		helpPane.setContentType("text/html");
		helpPane.setBounds(new Rectangle(10, 10, 260, 365));
		
		helpPane.setText("<html><strong>Hello world!</strong></html>");
		

	}
}
