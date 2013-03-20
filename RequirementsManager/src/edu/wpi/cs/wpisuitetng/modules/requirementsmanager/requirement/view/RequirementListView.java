package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/** RequirementListView is the basic GUI that will display a list of the current Requirements, fetched from the database.
 * It will allow the user to select a given requirement and view/edit it.
 * It also allows users to create new requirements, and refresh the list of requirements
 * 
 * @author Mitchell
 *
 */

public class RequirementListView extends JPanel implements IToolbarGroupProvider {	

	/** The MainTabController that this view is inside of */
	private final MainTabController tabController;
	
	/** JList used to display a list of all the requirements */
	private JList requirementsList;	
	
	/** List of string values that will be displayed in the list view, requirements[i].getName() for now */
	private ArrayList<String> listValues;
	/** The list of requirements that the view is displaying */
	private Requirement[] requirements;	
	
	/** The View that will display on the toolbar and contain buttons relating to this view */
	private ToolbarGroupView toolbarView;
	
	/** The View and Refresh buttons used on the toolbar */
	private JButton butView;
	private JButton butRefresh;
	
	/** Construct for a RequirementListView
	 * 
	 * 
	 */
	
	public RequirementListView(MainTabController tabController) {
		this.tabController = tabController;
		//initialize the listValues, preset values right now for testing
		initialize();
		
		//init the toolbar group
		initializeToolbarGroup();
		
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
	
	/** Initializes the toolbar group, and adds the buttons that will be displayed to it.
	 * 
	 * TODO: Implement actions on the buttons
	 * 
	 */
	
	private void initializeToolbarGroup() {
		toolbarView = new ToolbarGroupView("Requirements");
		butView = new JButton("View Requirement");
		butRefresh = new JButton("Refresh");
		
		butRefresh.setAction(new RefreshAction(this));
		butView.setAction(new ViewRequirementAction(this));
		//create and add the buttons that will be displayed
		toolbarView.getContent().add(butView);
		toolbarView.getContent().add(butRefresh);
		//set the width of the group so it is not too long
		toolbarView.setPreferredWidth((int)(butView.getPreferredSize().getWidth() + butRefresh.getPreferredSize().getWidth() + 40));
	}
	
	/** Takes a list of requirements, and turns them into a list of Strings that the View will display
	 * 
	 * @param requirements The list of requirements 
	 * @return a list of requirements in string form, that can be shown on the JList
	 */
	
	private void parseRequirements() {		
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
	
	/** Updates the list view acording to the values in the Requirements Array
	 * 
	 * */
	
	private void updateListView() {
		//update the string array
		parseRequirements();
		//set the list values of the requirementsList to the values in the String aray
		requirementsList.setListData(listValues.toArray(new String[0]));
		//invalidate the list so it is forced to be redrawn
		requirementsList.invalidate();
		
	}

	/** Inherited from IToolBarGroup Provider, Provides the toolbar buttons used by this view
	 * 
	 */
	
	@Override
	public ToolbarGroupView getGroup() {
		return toolbarView;
	}
	
	/** Retreives the list of requirements from the server, then updates the List View acordingly 
	 * 
	 */
	
	public void refresh() {
		//retreive a new copy of requirements, and update the list view
		getRequirementsFromServer();
		updateListView();
	}
	
	/** Open a new tab containing a view of the selected requirement in the list view
	 * TODO: Add the code to open a detailed view once it is available
	 */
	
	public void viewRequirement() {
		//obtain the currently selected requirement
		int selectedIndex = requirementsList.getSelectedIndex();
		
		if (selectedIndex < 0) {
			//nothing is currently selected
			return;
		}
		
		//indexes should be synced, retreive the requirement from the array
		Requirement selectedRequirement = requirements[selectedIndex];
		
		
	}

}
