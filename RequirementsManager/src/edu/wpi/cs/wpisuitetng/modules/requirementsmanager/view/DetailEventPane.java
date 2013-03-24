package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Class for displaying the note view and the log view on the right side of the Detail View
 * 
 * @author Mitchell
 *
 */

public class DetailEventPane extends JTabbedPane {

	JPanel notesListPane;
	JPanel logListPane;
	
	/** Creates a new DetailEvent pane, that displays the given NotesListPane, and LogListPane
	 * 
	 * @param notesListPane The JPanel that represents the list of notes to represent
	 * @param logListPane TheJPanel that represents the list of logs to represent
	 */
	
	public DetailEventPane(JPanel notesListPane, JPanel logListPane) {
		this.notesListPane = notesListPane;
		this.logListPane = logListPane;
		
		//add the given tabs to the pane
		addTab("Notes", new ImageIcon(), notesListPane, "The notes for this requirement");		
		addTab("Log", new ImageIcon(), logListPane, "The log for this requirement");
		
	}
	
}
