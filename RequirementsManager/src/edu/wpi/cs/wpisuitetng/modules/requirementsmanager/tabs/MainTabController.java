package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

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
		tabView.addTab(title,icon,component,tip); // add the tab to the TabView
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
		return null;
	}
	
	/** Adds a new View Requirement tab that shows the details about the given requirement
	 * 
	 * TODO: implement this
	 * @param requirement The requirement to view
	 * @return The tab that was added
	 */
	
	public Tab addViewRequirementTab(Requirement requirement) {
		return null;
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
	
}
