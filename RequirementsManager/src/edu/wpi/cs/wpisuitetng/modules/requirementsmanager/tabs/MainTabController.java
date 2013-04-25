/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.StatView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter.FilterView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree.SubRequirementTreeView;

/**
 * Controller wrapper around the MainTabView Provides convenient methods for
 * operating on Tabs from the MainTabView
 * 
 * Adapted from MainTabController in the DefectModule
 */

public class MainTabController {
	
	/** The MainTabView that this controller manages */
	private final MainTabView tabView;
	
	/** The iteration tree view that is displayed across this module */
	private final IterationTreeView iterationTreeView;
	
	/** THe filter view on the left */
	private final FilterView filterView;
	
	private final SubRequirementTreeView subRequirementTreeView;
	
	/**
	 * Creates a new instance of TabController to manage the specified view
	 * 
	 * @param tabView
	 *            The view to manage
	 */
	
	public MainTabController() {
		iterationTreeView = new IterationTreeView(this);
		subRequirementTreeView = new SubRequirementTreeView(this);
		filterView = FilterView.getInstance();
		tabView = new MainTabView(this);
		
		tabView.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(final ChangeEvent e) {
				onChangeTab();
			}
		});
	}
	
	/**
	 * Add a change listener to the view this is controlling.
	 * 
	 * @param listener
	 *            the ChangeListener that should receive ChangeEvents
	 */
	public void addChangeListener(final ChangeListener listener) {
		tabView.addChangeListener(listener);
	}
	
	public TabWrap addCreateIterationTab() {
		final IterationView iterationView = new IterationView(this);
		return addTab("New Iteration", new ImageIcon(), iterationView,
				"New Iteration");
	}
	
	/**
	 * Adds a tab to create a new requirement
	 * 
	 * @return The tab that was added
	 */
	
	public TabWrap addCreateRequirementTab() {
		final DetailPanel emptyDetailView = new DetailPanel(new Requirement(),
				DetailPanel.Mode.CREATE, this);
		return addTab("New Requirement", new ImageIcon(), emptyDetailView,
				"New Requirement");
	}
	
	public TabWrap addIterationTab(final Iteration iteration) {
		
		IterationView.Status status;
		if (PermissionModel.getInstance().getUserPermissions()
				.canEditIteration()) {
			status = IterationView.Status.EDIT;
		} else {
			status = IterationView.Status.VIEW;
		}
		
		// check if this iteration is open already
		final boolean iterationOpen = false;
		
		for (int j = 0; j < getTabView().getTabCount(); j++) {
			final Component tabComponent = getTabView().getComponentAt(j);
			if (tabComponent instanceof IterationView) {
				final IterationView tabOpen = (IterationView) tabComponent;
				if (tabOpen.getIterationId() == iteration.getId()) {
					switchToTab(j);
					return null;
				}
			}
		}
		
		// iteration was not open, add it
		final IterationView iterationView = new IterationView(iteration,
				status, this);
		return addTab(iteration.getName(), new ImageIcon(), iterationView,
				iteration.getName());
	}
	
	// TEST
	/*
	 * public TabWrap addHelpPanelTab() { HelpPanel emptyDetailView = new
	 * HelpPanel(); return addTab("User Manual", new ImageIcon(),
	 * emptyDetailView, "User Manual"); }
	 */
	
	/**
	 * Adds a tab to modify permissions
	 * 
	 * @return The permissions tab to be added
	 */
	public TabWrap addPermissionTab() {
		
		return addTab("Permissions", new ImageIcon(), new PermissionsPanel(),
				"Permissions");
	}
	
	/**
	 * Adds the Requirement Table View to the tabs
	 * 
	 * @return The tab that was added
	 */
	
	public TabWrap addRequirementsTab() {
		final RequirementTableView requirementListView = RequirementTableView
				.getInstance(this);
		
		return addTab("Requirements", new ImageIcon(), requirementListView,
				"The list of requirements");
	}
	
	// TODO Document
	public TabWrap addStatTab() {
		return addTab("Statistics", new ImageIcon(), StatView.getInstance(),
				"Statistics");
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
	
	public TabWrap addTab(final String title, final Icon icon, final Tab tab,
			final String tip) {
		SwingUtilities.invokeLater(new AddTabInvokable(tabView, title, icon,
				tab, tip));
		return new TabWrap(tabView, tab);
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
	
	public TabWrap addViewRequirementTab(final Requirement requirement) {
		DetailPanel.Mode mode;
		if (PermissionModel.getInstance().getUserPermissions()
				.canEditRequirement()) {
			mode = DetailPanel.Mode.EDIT;
		} else if (PermissionModel.getInstance().getUserPermissions()
				.canUpdateRequirement()) {
			mode = DetailPanel.Mode.UPDATE;
		} else {
			mode = DetailPanel.Mode.VIEW;
		}
		final DetailPanel requirmentDetailView = new DetailPanel(requirement,
				mode, this);
		
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
	
	/** Closes all open tabs, that can be closed */
	
	public void closeAllTabs() {
		final Component[] openTabs = getOpenTabs();
		for (final Component openTab : openTabs) {
			if (openTab instanceof Tab) {
				closeTab((Tab) openTab);
			}
			
		}
	}
	
	/**
	 * Closes the currently active tab
	 */
	public void closeCurrentTab() {
		try {
			tabView.removeTabAt(tabView.getSelectedIndex());
		} catch (final IndexOutOfBoundsException e) {
			System.out
					.println("Tried to close a tab that does not exist: MainTabController:238");
		}
	}
	
	/**
	 * Closes other tabs
	 * 
	 * @param currentIndex
	 *            Index of the tab not to close
	 */
	
	public void closeOtherTabs(final int currentIndex) {
		final Component[] openTabs = getOpenTabs();
		for (int i = 0; i < openTabs.length; i++) {
			if (i == currentIndex) {
				continue;
			}
			if (openTabs[i] instanceof Tab) {
				closeTab((Tab) openTabs[i]);
			}
		}
	}
	
	public void closeTab(final Tab tab) {
		final int index = tabView.indexOfComponent(tab);
		if ((tabView.getTabComponentAt(index) instanceof ClosableTabComponent)
				&& tab.onTabClosed()) {
			tabView.remove(tab);
		}
	}
	
	public void closeTabAt(final int index) {
		try {
			final Tab tab = (Tab) tabView.getComponentAt(index);
			if (tab.onTabClosed()) {
				tabView.removeTabAt(index);
			}
		} catch (final IndexOutOfBoundsException e) {
			System.out
					.println("Tried to close a tab that does not exist: MainTabController:250");
		}
	}
	
	public FilterView getFilterView() {
		return filterView;
	}
	
	public IterationTreeView getIterationTreeView() {
		return iterationTreeView;
	}
	
	/** Returns the number of tabs currently open */
	
	public int getNumberOfOpenTabs() {
		return tabView.getTabCount();
	}
	
	/** Returns a list of all the open tabs */
	
	public Component[] getOpenTabs() {
		final Component[] openTabs = new Component[getNumberOfOpenTabs()];
		for (int i = 1; i < openTabs.length; i++) {
			openTabs[i] = tabView.getComponentAt(i);
		}
		return openTabs;
	}
	
	public SubRequirementTreeView getSubReqView() {
		return subRequirementTreeView;
	}
	
	public MainTabView getTabView() {
		return tabView;
	}
	
	/**
	 * Called when the selected tab has been changed, notifies the tab that is
	 * is being displayed
	 * 
	 * TODO: Remove the instanceof checking
	 */
	
	private void onChangeTab() {
		refreshIterationTree();
		refreshSubReqView();
		
		final Component selectedComponent = tabView.getSelectedComponent();
		final Tab selectedTab = (Tab) selectedComponent;
		selectedTab.onGainedFocus();
		
		// tabView.setTabComponentAt(tabView.getSelectedIndex(),
		// selectedTab.getTabComponent(tabView));
		
	}
	
	public void refreshFilterView() {
		filterView.refreshTableView();
	}
	
	public void refreshIterationTree() {
		iterationTreeView.refresh();
	}
	
	public void refreshSubReqView() {
		subRequirementTreeView.refresh();
	}
	
	/**
	 * Changes the selected tab to the tab with the given index
	 * 
	 * @param tabIndex
	 *            the index of the tab to select
	 */
	public void switchToTab(final int tabIndex) {
		try {
			tabView.setSelectedIndex(tabIndex);
			
		} catch (final IndexOutOfBoundsException e) {
			System.out
					.println("Tried to close an invalid tab : MainTabController:272");
		}
	}
	
	public void switchToTab(final Tab tab) {
		final int index = tabView.indexOfComponent(tab);
		switchToTab(index);
	}
}
