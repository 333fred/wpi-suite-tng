/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;


/**
 * @author Zac Chupka, Maddie Burris
 *
 */
public class DetailLogView extends JPanel{
	/** For Notes */
	protected DefaultListModel logList;
	protected JList log;
	private Requirement requirement;
	private DetailPanel parentView;
	
	
	public DetailLogView(Requirement requirement, DetailPanel parentView){
		this.requirement = requirement;
		this.parentView = parentView;
		
		setLayout(new BorderLayout());

		// Create the note list
		logList = new DefaultListModel();
		log = new JList(logList);
		log.setCellRenderer(new EventCellRenderer());

		// Add the list to the scroll pane
		JScrollPane noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(log);
		
		// Set up the frame
		JPanel logPane = new JPanel();
		logPane.setLayout(new BorderLayout());
		logPane.add(noteScrollPane, BorderLayout.CENTER);
		
		add(logPane, BorderLayout.CENTER);
		
		
		for (Note aNote : requirement.getNotes()) {
			this.logList.addElement(aNote);
		}
	}

	
}
