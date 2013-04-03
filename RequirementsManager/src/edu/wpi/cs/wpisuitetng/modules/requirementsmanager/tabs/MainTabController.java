package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.HelpPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Controller wrapper around the MainTabView
 * Provides convient methods for operating on Tabs from the MainTabView
 * 
 * Adapted from MainTabController in the DefectModule
 * 
 * @author Mitchell
 *
 */

public class MainTabController {
	
	/** The MainTabView that this controller manages */
	private final MainTabView tabView;

	/** Creates a new instance of TabController to manage the specified view
	 * 
	 * @param tabView The view to manage
	 */
	
	public MainTabController(MainTabView tabView) {
		this.tabView = tabView;
		
		
	    tabView.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				onChangeTab();
			}
	    });
	}
	
	/** Called when the selected tab has been changed, notifies the tab that is is being displayed
	 * 
	 * TODO: Remove the instanceof checking
	 */
	
	private void onChangeTab() {
		Component selectedComponent = tabView.getSelectedComponent();
		if (selectedComponent instanceof TabFocusListener) {
			TabFocusListener listener = (TabFocusListener) selectedComponent;
			listener.onGainedFocus();
		}
		
	}
	
	/** Adds a tab to the TabView that this controller manages, and returns a new instance of Tab representing the new tab created
	 * 
	 * @param title The title of the tab
	 * @param icon The tabs icon
	 * @param component The component that the tab will display
	 * @param tip The tooltip that the tab will display
	 * @return The new instance of Tab representing the one added
	 */
	
	public Tab addTab(String title, Icon icon, Component component, String tip) {
		tabView.addTab(title,icon, component,tip); // add the tab to the TabView
		int index = tabView.getTabCount() - 1; // get the index of the newly added tab
		tabView.setSelectedIndex(index); // set the current tab to the newly added tab
		return new Tab(tabView, tabView.getTabComponentAt(index));
	}
	
	/** Adds an unclosable tab to the TabView that this controller manages, and returns a new instance of Tab representing the new tab created
	 * 
	 * @param title The title of the tab
	 * @param icon The tabs icon
	 * @param component The component that the tab will display
	 * @param tip The tooltip that the tab will display
	 * @return The new instance of Tab representing the one added
	 */
	
	public Tab addUnclosableTab(String title, Icon icon, Component component, String tip) {
		tabView.addUnclosableTab(title,icon, component,tip); // add the tab to the TabView
		int index = tabView.getTabCount() - 1; // get the index of the newly added tab
		tabView.setSelectedIndex(index); // set the current tab to the newly added tab
		return new Tab(tabView, tabView.getTabComponentAt(index));
	}
	
	/** Adds a tab to create a new requirement
	 * 
	 * TODO: Implement this.
	 * @return The tab that was added
	 */
	
	public Tab addCreateRequirementTab() {
		DetailPanel emptyDetailView = new DetailPanel(new Requirement(), this); 
		IterationTreeTab view = new IterationTreeTab(emptyDetailView, null); 
		return addTab("New Requirement", new ImageIcon(), view, "New Requirement");		
	}
	
	//TEST
	public Tab addHelpPanelTab() {
		HelpPanel emptyDetailView = new HelpPanel(); 
		return addTab("User Manual", new ImageIcon(), emptyDetailView, "User Manual");		
	}
	
	public Tab addCreateIterationTab() {
		IterationView iterationView = new IterationView(this);
		IterationTreeTab view = new IterationTreeTab(iterationView, null); 
		return addTab("New Iteration", new ImageIcon(), view, "New Iteration");
	}
	
	/** Adds a new View Requirement tab that shows the details about the given requirement
	 * 
	 * TODO: implement this
	 * @param requirement The requirement to view
	 * @return The tab that was added
	 */
	
	public Tab addViewRequirementTab(Requirement requirement) {
		DetailPanel requirmentDetailView = new DetailPanel(requirement, this);
		IterationTreeTab view = new IterationTreeTab(requirmentDetailView, null); 
		return addTab(requirement.getName(), new ImageIcon(), view, requirement.getName());
	}
	
	/** Adds teh Requirement Table View to the tabs
	 * 
	 * @return The tab that was added
	 */
	
	public Tab addRequirementsTab() {
		RequirementTableView requirementListView = new RequirementTableView(this);
		IterationTreeTab view = new IterationTreeTab(requirementListView, requirementListView);
		return addUnclosableTab("Requirements", new ImageIcon(), view, "The list of requirements");
	}
	
	/** Adds a tab to Edit a given Requirement
	 * 
	 * @param requirement The requirement to be edited
	 * @return The tab that was added
	 */
	
	public Tab addEditRequirementTab(Requirement requirement) {
		return null;
	}
	
	/**
	 * Add a change listener to the view this is controlling.
	 * @param listener the ChangeListener that should receive ChangeEvents
	 */
	public void addChangeListener(ChangeListener listener) {
		tabView.addChangeListener(listener);
	}
	
	/**
	 * Closes the currently active tab
	 */
	public void closeCurrentTab() {
		try {
			tabView.removeTabAt(tabView.getSelectedIndex());
		}
		catch (IndexOutOfBoundsException e) {
			// do nothing, tried to close tab that does not exist
		}
	}
	
	/**
	 * Changes the selected tab to the tab with the given index
	 * @param tabIndex the index of the tab to select
	 */
	public void switchToTab(int tabIndex) {
		try {
			tabView.setSelectedIndex(tabIndex);
		}
		catch (IndexOutOfBoundsException e) {
			// an invalid tab was requested, do nothing
		}
	}


	public MainTabView getTabView() {
		return tabView;
	}
	
}
