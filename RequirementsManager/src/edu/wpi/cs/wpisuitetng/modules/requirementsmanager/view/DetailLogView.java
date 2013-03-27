/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Log;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;


/**
 * A panel containing a form for adding a new log to a requirement
 * @author Zac Chupka, Maddie Burris
 */
public class DetailLogView extends JPanel{
	/** For Notes */
	protected DefaultListModel logList;
	protected JList log;
	private Requirement requirement;
	private DetailPanel parentView;
	
	
	/**
 	* Construct the panel and add layout components
 	* @param requirement the requirement 
 	* @param parentView the parent view
 	*/
	public DetailLogView(Requirement requirement, DetailPanel parentView){
		this.requirement = requirement;
		this.parentView = parentView;
		
		setLayout(new BorderLayout());

		// Create the log list
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
		
		
		
	}
	
	/** Adds the log from the requirement to the list 
	 * 
	 */
	
	private void updateLogList() {		
		logList.clear(); //clear the old list
		
		//add the logs to the list
		//TODO: Possibly change getLog() when we deterime how to refactor requirement
		for (Log aLog : requirement.getLog()) {
			this.logList.addElement(aLog);
		}
	}
	
	/** Updates the requirement with the given new requirement
	 * 
	 * @param newRequirement The updated requirement
	 */
	
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;
		
		//update the list of logs
		updateLogList();
	}

	
}
