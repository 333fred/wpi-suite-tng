/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/** The JTabbedPane that will be shown in the RequirementsManager Module.
 * 
 * Adapted on the MainTabView from DefectTracker module
 * 
 * 
 * @author Mitchell
 *
 */

public class MainTabView extends JTabbedPane  {

	public MainTabView() {
		setTabPlacement(TOP); //set the tabs to be placed at the top
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT); //allow the tabs to be scrollable whne there are too many to fit on the screen
		
		//TabView starts off empty.
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
	}
	
	public void addTab(String title, Icon icon, Tab tab, String tip) {
		super.addTab(title,icon, tab, tip);		
		int index = getTabCount() - 1; // the tab was just added, so we assume that it was at the end
		//add the tab with the given tab component
		
		//invoke this later to solve issues
		SwingUtilities.invokeLater(new CreateClosableTabInvokable(this, index, tab));
		
		//setTabComponentAt(index, tab.getTabComponent(this));
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
