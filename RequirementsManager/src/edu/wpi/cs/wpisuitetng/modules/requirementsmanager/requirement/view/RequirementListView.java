package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

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
	private JList requirementsList;	
	
	/** List of string values that will be displayed in the list view, requirements[i].getName() for now */
	private ArrayList<String> listValues;
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
		
		//initialize the requirements list, with an array of strings retreived from the listValues ArrayList
		requirementsList= new JList(listValues.toArray(new String[0]));
		add(requirementsList, BorderLayout.CENTER);
		
	}
	
	/** Initializes the Requirements and ListValues, so they can be added to the GUI
	 *  TODO: repurpose this method to use the retreiveRequirementsFromServer and parseRequirements, once
	 *  	backend functions for server communication are available.
	 * 
	 */
	private void initialize() {
		//initialize list values to constant values now just for testing
		listValues = new ArrayList<String>();
		listValues.add("Requirement 1");
		listValues.add("Requirement 2");
		listValues.add("Requirement 3");	
	}
	
	/** Takes a list of requirements, and turns them into a list of Strings that the View will display
	 * 
	 * @param requirements The list of requirements 
	 * @return a list of requirements in string form, that can be shown on the JList
	 */
	
	private void parseRequirements(Requirement[] requirements) {
		
		// for every requirement
		
		for(int i = 0; i < requirements.length; i++){
			// produce a summary String for the list
			listValues.add(requirements[i].toListString());
		}
		
	
		
	}
	
	/** Function to retreive the requirements from the server
	 * 
	 * Mostly a place holder function until the backend is implemented
	 * 
	 * TODO: Implement this function once the backend is completed
	 */
	
	private void getRequirementsFromServer() {
		this.requirements = new Requirement[0];
		
	}

}
