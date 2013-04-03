package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.Component;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/** The JTabbedPane that will be shown in the RequirementsManager Module.
 * 
 * Adapted on the MainTabView from DefectTracker module
 * 
 * 
 * @author Mitchell
 *
 */

public class MainTabView extends JTabbedPane implements WindowListener, WindowStateListener {

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
	
	/** Adds an unclosable tab to this JTabbedPane
	 * 
	 *  * @param title The title of tab
	 * @param icon The icon that will be displayed in this tab
	 * @param component The component that the tab will display
	 * @param tip The tooltip to be displayed for the tab
	 * @param index The position to inster the tab
	 * 
	 */
	
	public void addUnclosableTab(String title, Icon icon, Component component, String tip) {
		super.addTab(title,icon,component,tip);
		int index = getTabCount() - 1; // the tab was just added, so we assume that it was at the end

		//set the tab component to un closable.
		setTabComponentAt(index, new JLabel(title));
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

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("Got Close event");
		RequirementDatabase.getInstance().interrupt();
		IterationDatabase.getInstance().interrupt();
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if (e.getNewState() == WindowEvent.WINDOW_CLOSED){
			RequirementDatabase.getInstance().interrupt();
			IterationDatabase.getInstance().interrupt();
			System.out.println("Window State Listener");
		}
		
	}
	
}
