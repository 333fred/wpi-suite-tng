package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/** The JTabbedPane that will be shown in the RequirementsManager Module.
 * 
 * Adapted on the MainTabView from DefectTracker module
 * 
 * 
 * @author Mitchell
 *
 */

public class MainTabView extends JTabbedPane {

	public MainTabView() {
		setTabPlacement(TOP); //set the tabs to be placed at the top
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT); //allow the tabs to be scrollable whne there are too many to fit on the screen
		
		//TabView starts off empty.
	}
	
	/** Inserts a component at the specified index, with the given title and icon.
	 * 
	 * TODO: Implement this, as of now it will call super method
	 * 		 Implementation will add the closeable tab wrapper around the component.
	 * 
	 * @param title The title of tab
	 * @param icon The icon that will be displayed in this tab
	 * @param component The component that the tab will display
	 * @param tip The tooltip to be displayed for the tab
	 * @param index The position to inster the tab
	 */

	
	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		super.insertTab(title,icon,component,tip,index);
		
		//if it is not a list view, create a closeable tab
		if(!(component instanceof RequirementTableView)) {
			setTabComponentAt(index, new ClosableTabComponent(this));
		}
	}
	
	/** Removes the component at the specified index.
	 * 
	 * TODO: Implement this method, Override to stop tabs that are unclosable from being closed.
	 * @param index
	 */
	
	@Override
	public void removeTabAt(int index) {
		if(getTabComponentAt(index) instanceof ClosableTabComponent) {
			super.removeTabAt(index);
		}
	}
	
	/** Sets the component at the given  index, with the given component
	 *  Also notified the toolbar that the component of the tab has changed, and it should update
	 *  
	 * TODO: Implement this method.
	 * @param index The index of the component to set
	 * @param component The new component
	 */
	
	@Override
	public void setComponentAt(int index, Component component) {
		super.setComponentAt(index, component);
	}
	
}
