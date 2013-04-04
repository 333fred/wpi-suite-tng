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
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.HelpPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;
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
	
	/** The iteration tree view that is displayed accross this module */
	private IterationTreeView iterationTreeView;

	/** Creates a new instance of TabController to manage the specified view
	 * 
	 * @param tabView The view to manage
	 */
	
	public MainTabController(MainTabView tabView) {
		this.tabView = tabView;
		this.iterationTreeView = new IterationTreeView(this);
		
		
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
		refreshIterationTree();
		System.out.println("Change Tab Controller");
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
		return addTab("New Requirement", new ImageIcon(), emptyDetailView, "New Requirement");		
	}
	
	//TEST
	public Tab addHelpPanelTab() {
		HelpPanel emptyDetailView = new HelpPanel(); 
		return addTab("User Manual", new ImageIcon(), emptyDetailView, "User Manual");		
	}
	
	public Tab addCreateIterationTab() {
		IterationView iterationView = new IterationView(this);
		return addTab("New Iteration", new ImageIcon(), iterationView, "New Iteration");
	}
	
	public Tab addEditIterationTab(Iteration iteration) {
		IterationView iterationView = new IterationView(iteration, this);
		return addTab(iteration.getName(), new ImageIcon(), iterationView, iteration.getName());
	}
	
	/** Adds a new View Requirement tab that shows the details about the given requirement
	 * 
	 * TODO: implement this
	 * @param requirement The requirement to view
	 * @return The tab that was added
	 */
	
	public Tab addViewRequirementTab(Requirement requirement) {
		DetailPanel requirmentDetailView = new DetailPanel(requirement, this);

		return addTab(requirement.getName(), new ImageIcon(), requirmentDetailView, requirement.getName());
	}
	
	/** Adds teh Requirement Table View to the tabs
	 * 
	 * @return The tab that was added
	 */
	
	public Tab addRequirementsTab() {
		RequirementTableView requirementListView = RequirementTableView.getInstance(this);

		return addUnclosableTab("Requirements", new ImageIcon(), requirementListView, "The list of requirements");
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
	
	public void refreshIterationTree() {
		iterationTreeView.refresh();
	}
	
	public IterationTreeView getIterationTreeView() {
		return iterationTreeView;
	}
	
}
