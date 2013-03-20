/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.view.RequirementListView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar.ToolbarView;

/**
 * @author Fredric
 *
 */
public class JanewayModule implements IJanewayModule {

	/** List of tabs that this module will display */
	private List<JanewayTabModel> tabs;
	
	/** The list view that will display the requirements	 */
	private RequirementListView requirementListView;
	
	/** The main view of the module that displays the tabs */
	private MainTabView tabView;
	
	/** The tab controller for tabView */
	private MainTabController tabController;
	
	/** Toolbar view, displayed above the main tab view, and contains action buttons for the tabs */
	private ToolbarView toolbarView;
	
	/** The controller for the toolbarView */
	private ToolbarController toolbarController;
	
	/** Creates a new instance of JanewayModule, initializing the tabs to be displayed
	 * 
	 */
	
	public JanewayModule() {
		//initialize the list of tabs, using an array list
		tabs = new ArrayList<JanewayTabModel>();	
		
		//initialize the requirements list view
		requirementListView = new RequirementListView();
		
		//initialize the tab view public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		tabView = new MainTabView();
		
		//initialize TabController
		tabController = new MainTabController(tabView);
		
		//initialize the toolbarView
		toolbarView = new ToolbarView(tabController);
		
		//initialize the toolbar Controller
		toolbarController = new ToolbarController(toolbarView, tabController);
		
		tabController.addTab("Requirements List", new ImageIcon(), requirementListView, "The list of requirements");
		
		
		//create a new JanewayTabModel, passing in the tab view, and a new JPanel as the toolbar
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), toolbarView, tabView);
		
		//add the tab to the list of tabs
		tabs.add(tab1);
	}
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Requirements Manager";
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 * TODO: Implement This
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		//return an empty list of tabs, for now
		return tabs;
	}

}
