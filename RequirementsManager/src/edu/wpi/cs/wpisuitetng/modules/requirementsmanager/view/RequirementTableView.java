package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveRequirementByIDController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.RefreshAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.ViewRequirementAction;

/** RequirementListView is the basic GUI that will display a list of the current Requirements, fetched from the database.
 * It will allow the user to select a given requirement and view/edit it.
 * It also allows users to create new requirements, and refresh the list of requirements
 * 
 * @author Steve, Mitchell
 *
 */
@SuppressWarnings("serial")
public class RequirementTableView extends FocusableTab implements IToolbarGroupProvider, IReceivedAllRequirementNotifier {	

	/** The MainTabController that this view is inside of */
	private final MainTabController tabController;
	
	/** Controller for receiving all requirements from the server */
	private RetrieveAllRequirementsController retreiveAllRequirementsController;
	
	/** The list of requirements that the view is displaying */
	private Requirement[] requirements;	
	
	/** The View that will display on the toolbar and contain buttons relating to this view */
	private ToolbarGroupView toolbarView;
	
	/** The View and Refresh buttons used on the toolbar */
	private JButton butView;
	private JButton butRefresh;
	
	/** Flag used to make paint only refresh the requirements once, on load */
	private boolean firstPaint;
	
	@SuppressWarnings("rawtypes")
	private Vector<Vector> rowData;
	
	private JTable table;
	
	private IterationTreeView iterationTree;
	
	/** Constructor for a RequirementTableView
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public RequirementTableView(MainTabController tabController) {
		this.tabController = tabController;
		this.iterationTree = new IterationTreeView();
		
		firstPaint = false;
		//create the Retreive All Requiments Controller
		retreiveAllRequirementsController = new RetrieveAllRequirementsController(this);
		//init the toolbar group
		initializeToolbarGroup();
		
		requirements = new Requirement[0];
	
		JPanel mainPanel = new JPanel(new BorderLayout());
		//setLayout(new BorderLayout(0, 0));
		GridLayout mainLayout = new GridLayout(0, 1);
		setLayout(mainLayout);	
		
	    Vector<String> columnNames = new Vector<String>();
	    columnNames.addElement("Name");
	    columnNames.addElement("Type");
	    columnNames.addElement("Priority");
	    columnNames.addElement("Status");
	    columnNames.addElement("Iteration");
	    columnNames.addElement("Effort");
	    columnNames.addElement("Estimate");
	    columnNames.addElement("Release Number");
		
	    this.rowData = new Vector<Vector>();
	    
		this.table = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = 1L;
	        public boolean isCellEditable(int row, int column) {                
	        	return false;               
		    };
		};
				
		JScrollPane scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.setAutoCreateRowSorter(true);
		mainPanel.add(scrollPane,BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,iterationTree,mainPanel);
		add(splitPane);

		splitPane.setResizeWeight(0.1);
		
		//Add double click event listener		
		  this.table.addMouseListener(new MouseAdapter() {
			   public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         int row = target.getSelectedRow();
			         onDoubleClick(row);
			         }
			   }
			});
		  
	}
	
	
	/*
	 * @author Steve Kordell
	 * 
	 */
	//TODO: More documentation
	void onDoubleClick(int index) {	
		//update to use this function instead
		viewRequirement(index);
	}
	
	/** Initializes the toolbar group, and adds the buttons that will be displayed to it.
	 * 
	 * 
	 */
	private void initializeToolbarGroup() {
		toolbarView = new ToolbarGroupView("Requirements");
		butView = new JButton("Edit Requirement");
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
	
		this.rowData.clear();
		for(int i = 0; i < requirements.length; i++){
			Vector<String> row = new Vector<String>();
			row.addElement(requirements[i].getName());
			row.addElement(requirements[i].getType().toString());
			row.addElement(requirements[i].getPriority().toString());
			row.addElement(requirements[i].getStatus().toString());
			row.addElement(String.valueOf(requirements[i].getIteration()));
			row.addElement(String.valueOf(requirements[i].getEffort()));
			row.addElement("");
			row.addElement(String.valueOf(requirements[i].getReleaseNum()));
			this.rowData.add(row);
		}
	}
	
	/** Function to retreive the requirements from the server
	 * 
	 * Mostly a place holder function until the backend is implemented
	 * The list view will automaticaly be updated once the request comes back
	 * 
	 */
	
	private void getRequirementsFromServer() {
		System.out.println("We are getting requirements from the server");
		retreiveAllRequirementsController.getAll();
	}
	
	/** Updates the list view acording to the values in the Requirements Array
	 * 
	 * */
	
	private void updateListView() {
		System.out.println("We are updating the table view");
		//update the string array
		parseRequirements();
		//set the list values of the requirementsList to the values in the String aray
		//requirementsList.setListData(listValues.toArray(new String[0]));
		//invalidate the list so it is forced to be redrawn
		//this.table.invalidate();
		this.table.repaint();
		this.iterationTree.refresh();
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
	}
	
	/** Open a new tab containing a view of the selected requirement in the list view
	   TODO: Possibly implement view requirement mode
	 */
	
	public void viewRequirement() {
		//obtain the currently selected requirement
		int selectedIndex = this.table.getSelectedRow();
										
		if (selectedIndex < 0) {
			//nothing is currently selected
			return;
		}
		
		viewRequirement(selectedIndex);
	}
	
	/** Views a requirement with the given index
	 * 
	 * @param index Index of the requirement to view
	 */
	
	public void viewRequirement(int index) {
		boolean requirementIsOpen = false;
		
				
		if (index <0 || index >= requirements.length) {
			//invalid index
			System.out.println("Invalid index");
		}
		
		//get the requirement to update from the array
		Requirement requirementToFetch = requirements[index];
		
		for (int i = 0; i < this.tabController.getTabView().getTabCount(); i++) {
			if (this.tabController.getTabView().getTitleAt(i).equals(requirementToFetch.getName())) {
				this.tabController.switchToTab(i);
				requirementIsOpen = true;
			}
		}
		
		if (!requirementIsOpen) {
			// create the controller for fetching the new requirement
			RetrieveRequirementByIDController retreiveRequirementController = new RetrieveRequirementByIDController(
					new OpenRequirementTabAction(tabController, requirementToFetch));
			
			//get the requirement from the server
			retreiveRequirementController.get(requirementToFetch.getrUID());
		}
	}

	/** The updated requirements data has been received, update the list
	 * 
	 * @param requirements The new requirement data
	 */
	
	@Override
	public void receivedData(Requirement[] requirements) {
		this.requirements = requirements;
		updateListView();
	}

	/** There was an error processing the data request.
	 * TODO: Implement what we do
	 *
	 */
	
	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		System.out.println("Received an error fecting from server: |" + RetrieveAllRequirementsRequestObserver);
	}

	/** Method that updates the content of the list view when this tab gains focus
	 * 
	 */
	
	public void onTabFocus() {
		refresh();
	}
	
	/** Retrieve the requirements from the server when the GUI is first painted
	 * 
	 * @param g The Graphics object
	 * 
	 */
	
	public void paint(Graphics g) {
		//call super so there is no change to functionality
		super.paint(g);
		//refresh the requirements, the first time this is called
		if (!firstPaint) {
			refresh();
			firstPaint = true;
		}
	}

}
