package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;

/** RequirementListView is the basic GUI that will display a list of the current Requirements, fetched from the database.
 * It will allow the user to select a given requirement and view/edit it.
 * It also allows users to create new requirements, and refresh the list of requirements
 * 
 * @author Mitchell
 *
 */

public class RequirementListView extends JPanel {	

	/** JList used to display a list of all the requirements */
	private JList<String> requirementsList;
	/** Buttons panel used to hold the 3 buttons */
	private JPanel buttonsPanel;
	/** Create button used to open the tab to create a new requirement */
	private JButton createButton;
	/** Refresh button, refreshes the list of requirements */
	private JButton refreshButton;
	/** Refresh Button, opens the GUI to view a single requirement in detail */
	private JButton viewButton;
	
	/** List of string values that will be displayed in the list view, requirements[i].getName() for now */
	private String[] listValues;
	/** The list of requirements that the view is displaying */
	private Requirement[] requirements;
	
	/** Construct for a RequirementListView
	 * 
	 * 
	 */
	
	public RequirementListView() {
		//initialize the listValues, preset values right now for testing
		initialize();
	
		//set this JPanel to use a border layout
		setLayout(new BorderLayout(0, 0));
		
		//initialize the requirements list
		requirementsList= new JList<String>(listValues);
		add(requirementsList, BorderLayout.CENTER);
		
		//initialize the panel for holding the buttons, and add it to the main view
		buttonsPanel = new JPanel();
		add(buttonsPanel, BorderLayout.SOUTH);
		
		//initialize each of the buttons, then add them to the buttons pane
		createButton = new JButton("Create");
		buttonsPanel.add(createButton);				
		
		viewButton = new JButton("View");
		buttonsPanel.add(viewButton);
		
		refreshButton = new JButton("Refresh");
		buttonsPanel.add(refreshButton);
		
	}
	
	/** Initializes the Requirements and ListValues, so they can be added to the GUI
	 * 
	 * 
	 */
	
	private void initialize() {
		//initialize list values to constant values now just for testing
		listValues =  new String[3];
		listValues[0] = "Requirement 1";
		listValues[1] = "Requirement 2";
		listValues[2] = "Requirement 3";	
	}
	
	/** Takes a list of requirements, and turns them into a list of Strings that the View will display
	 * 
	 * @param requirements The list of requirements 
	 * @return A list of requirements in string form, that can be showed on the JList
	 */
	
	private String[] parseRequirements(Requirement[] requirements) {
		return null;
	}
	
	/** Function to retreive the requirements from the server
	 * 
	 * Mostly a place holder function until the backend is implemented
	 * 
	 * TODO: Implement this function once the backend is completed
	 */
	
	private void getRequirementsFromServer() {
		
	}

}
