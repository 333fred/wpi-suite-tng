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
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.StatView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree.SubRequirementTreeView;

/**
 * Controller wrapper around the MainTabView Provides convient methods for
 * operating on Tabs from the MainTabView
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
	
	/** THe filter view on the left */
	private FilterView filterView;
	
	private SubRequirementTreeView subRequirementTreeView;

	/**
	 * Creates a new instance of TabController to manage the specified view
	 * 
	 * @param tabView
	 *            The view to manage
	 */

	public MainTabController() {		
		this.iterationTreeView = new IterationTreeView(this);
		subRequirementTreeView = new SubRequirementTreeView(this);
		filterView = new FilterView();
		tabView = new MainTabView(this);

		tabView.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				onChangeTab();
			}
		});
	}

	/**
	 * Called when the selected tab has been changed, notifies the tab that is
	 * is being displayed
	 * 
	 * TODO: Remove the instanceof checking
	 */

	private void onChangeTab() {
		refreshIterationTree();
		
		System.out.println("Change Tab Controller");
		Component selectedComponent = tabView.getSelectedComponent();
		Tab selectedTab = (Tab) selectedComponent;
		selectedTab.onGainedFocus();

		// tabView.setTabComponentAt(tabView.getSelectedIndex(),
		// selectedTab.getTabComponent(tabView));

	}

	/**
	 * Adds a tab to the TabView that this controller manages, and returns a new
	 * instance of Tab representing the new tab created
	 * 
	 * @param title
	 *            The title of the tab
	 * @param icon
	 *            The tabs icon
	 * @param component
	 *            The component that the tab will display
	 * @param tip
	 *            The tooltip that the tab will display
	 * @return The new instance of Tab representing the one added
	 */
	
	public TabWrap addTab(String title, Icon icon, Tab tab, String tip) {		
		SwingUtilities.invokeLater(new AddTabInvokable(tabView,title,icon,tab,tip));
		return new TabWrap(tabView, tab);
	}

	/**
	 * Adds a tab to create a new requirement
	 * 
	 * @return The tab that was added
	 */

	public TabWrap addCreateRequirementTab() {
		DetailPanel emptyDetailView = new DetailPanel(new Requirement(), this);
		return addTab("New Requirement", new ImageIcon(), emptyDetailView,
				"New Requirement");
	}

	/**
	 * Adds a tab to create a new requirement assigned to the given iteration
	 * 
	 * @return The tab that was added
	 */

	public TabWrap addCreateRequirementTab(Iteration iteration) {
		DetailPanel emptyDetailView = new DetailPanel(iteration, this);
		return addTab("New Requirement", new ImageIcon(), emptyDetailView,
				"New Requirement");
	}

	// TEST
	/*
	 * public TabWrap addHelpPanelTab() { HelpPanel emptyDetailView = new
	 * HelpPanel(); return addTab("User Manual", new ImageIcon(),
	 * emptyDetailView, "User Manual"); }
	 */

	public TabWrap addCreateIterationTab() {
		IterationView iterationView = new IterationView(this);
		return addTab("New Iteration", new ImageIcon(), iterationView,
				"New Iteration");
	}

	// TODO Document
	public TabWrap addStatTab() {
		return addTab("Statistics", new ImageIcon(), StatView.getInstance(), "Statistics");
	}

	public TabWrap addEditIterationTab(Iteration iteration) {

		// check if this iteration is open already
		boolean iterationOpen = false;

		for (int j = 0; j < getTabView().getTabCount(); j++) {
			Component tabComponent = getTabView().getComponentAt(j);
			if (tabComponent instanceof IterationView) {
				IterationView tabOpen = (IterationView) tabComponent;
				if (tabOpen.getIterationId() == iteration.getId()) {
					switchToTab(j);
					return null;
				}
			}
		}

		// iteration was not open, add it
		IterationView iterationView = new IterationView(iteration, this);
		return addTab(iteration.getName(), new ImageIcon(), iterationView,
				iteration.getName());
	}

	/**
	 * Adds a new View Requirement tab that shows the details about the given
	 * requirement
	 * 
	 * TODO: implement this
	 * 
	 * @param requirement
	 *            The requirement to view
	 * @return The tab that was added
	 */

	public TabWrap addViewRequirementTab(Requirement requirement) {
		DetailPanel requirmentDetailView = new DetailPanel(requirement, this);

		// check if this requirement is already opened
		for (int i = 0; i < getTabView().getTabCount(); i++) {
			if (getTabView().getComponentAt(i) instanceof DetailPanel) {
				if (((((DetailPanel) getTabView().getComponentAt(i)))
						.getModel().getrUID()) == (requirement.getrUID())) {
					switchToTab(i);
					return null;
				}
			}
		}

		return addTab(requirement.getName(), new ImageIcon(),
				requirmentDetailView, requirement.getName());
	}

	/**
	 * Adds teh Requirement Table View to the tabs
	 * 
	 * @return The tab that was added
	 */

	public TabWrap addRequirementsTab() {
		RequirementTableView requirementListView = RequirementTableView
				.getInstance(this);

		return addTab("Requirements", new ImageIcon(), requirementListView,
				"The list of requirements");
	}

	/**
	 * Add a change listener to the view this is controlling.
	 * 
	 * @param listener
	 *            the ChangeListener that should receive ChangeEvents
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
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Tried to close a tab that does not exist: MainTabController:238");
		}
	}
	
	public void closeTabAt(int index) {
		try {
			Tab tab = (Tab) tabView.getComponentAt(index);
			if (tab.onTabClosed()) {				
				tabView.removeTabAt(index);
			}
		}
		catch (IndexOutOfBoundsException e) {
			System.out.println("Tried to close a tab that does not exist: MainTabController:250");
		}
	}
	
	public void closeTab(Tab tab) {
		int index = tabView.indexOfComponent(tab);		
		if (tabView.getTabComponentAt(index) instanceof ClosableTabComponent && tab.onTabClosed()) {			
			tabView.remove(tab);
		}
	}

	/**
	 * Changes the selected tab to the tab with the given index
	 * 
	 * @param tabIndex
	 *            the index of the tab to select
	 */
	public void switchToTab(int tabIndex) {
		try {				
			tabView.setSelectedIndex(tabIndex);

		} catch (IndexOutOfBoundsException e) {
			System.out.println("Tried to close an invalid tab : MainTabController:272");
		}
	}
	
	public void switchToTab(Tab tab) {
		int index =  tabView.indexOfComponent(tab);
		switchToTab(index);
	}

	public MainTabView getTabView() {
		return tabView;
	}

	public void refreshIterationTree() {
		iterationTreeView.refresh();
	}
	
	public void refreshFilterView() {
		filterView.refreshTableView();
	}
	
	public void refreshSubReqView() {
		subRequirementTreeView.getRequirementsFromServer();
	}

	public IterationTreeView getIterationTreeView() {
		return iterationTreeView;
	}

	public FilterView getFilterView() {
		return filterView;
	}
	
	public SubRequirementTreeView getSubReqView() {
		return subRequirementTreeView;
	}
	/** Closes other tabs
	 * 
	 * @param currentIndex Index of the tab not to close
	 */
	
	public void closeOtherTabs(int currentIndex) {
		Tab[] openTabs = getOpenTabs();
		for (int i=0;i<openTabs.length;i++) {
			if (i == currentIndex) continue;
			closeTab(openTabs[i]);
		}
	}
	
	/** Closes all open tabs, that can be closed */
	
	public void closeAllTabs() {
		Tab[] openTabs = getOpenTabs();
		for (int i=0;i<openTabs.length;i++) {
			closeTab(openTabs[i]);
			System.out.println("Ya?");

		}
	}
	
	/** Returns the number of tabs currently open */
	
	public int getNumberOfOpenTabs() {
		return tabView.getTabCount();
	}
	
	/** Returns a list of all the open tabs */
	
	public Tab[] getOpenTabs() {
		Tab[] openTabs = new Tab[getNumberOfOpenTabs()];
		for (int i=1;i<openTabs.length;i++) {
			openTabs[i] = (Tab)tabView.getComponentAt(i);
		}	
		return openTabs;
	}

}
