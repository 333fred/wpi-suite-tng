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
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/**
 * The JTabbedPane that will be shown in the RequirementsManager Module.
 * 
 * Adapted on the MainTabView from DefectTracker module
 */

public class MainTabView extends JTabbedPane {
	
	/** boolean indicating whether the first tab has been added */
	private boolean firstTab;
	
	/** The tab controller for this tab view */
	private final MainTabController tabController;
	
	public MainTabView(final MainTabController tabController) {
		this.tabController = tabController;
		firstTab = true;
		setTabPlacement(SwingConstants.TOP); // set the tabs to be placed at the
												// top
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // allow the tabs to
															// be
		// scrollable when there are too
		// many to fit on the screen
		
		// TabView starts off empty.
	}
	
	public void addTab(final String title, final Icon icon, final Tab tab,
			final String tip) {
		super.addTab(title, icon, tab, tip);
		final int index = getTabCount() - 1; // the tab was just added, so we
												// assume
		// that it was at the end
		// add the tab with the given tab component
		
		// invoke this later to solve issues
		
		setTabComponentAt(index, tab.getTabComponent(tabController));
	}
	
	/**
	 * Adds an un-closable tab to this JTabbedPane
	 * 
	 * * @param title The title of tab
	 * 
	 * @param icon
	 *            The icon that will be displayed in this tab
	 * @param component
	 *            The component that the tab will display
	 * @param tip
	 *            The tooltip to be displayed for the tab
	 * @param index
	 *            The position to insert the tab
	 * 
	 */
	
	public void addUnclosableTab(final String title, final Icon icon,
			final Component component, final String tip) {
		super.addTab(title, icon, component, tip);
		final int index = getTabCount() - 1; // the tab was just added, so we
												// assume
		// that it was at the end
	}
	
	/**
	 * Removes the component at the specified index.
	 * 
	 * TODO: Implement this method, Override to stop tabs that are un-closable
	 * from being closed.
	 * 
	 * @param index
	 */
	
	@Override
	public void removeTabAt(final int index) {
		if (getTabComponentAt(index) instanceof ClosableTabComponent) {
			super.removeTabAt(index);
		}
	}
	
	/**
	 * Override setSelctedIndex to stop tabs from being selected, if the current
	 * tab doesn't allow
	 * loss of focus
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedComponent(final Component component) {
		if (firstTab) {
			firstTab = false;
			super.setSelectedComponent(component);
		}
		final int oldIndex = getSelectedIndex();
		final Tab tab = (Tab) getComponentAt(oldIndex);
		if (tab.onLostFocus()) {
			super.setSelectedComponent(component);
		}
	}
	
	/**
	 * Override setSelctedIndex to stop tabs from being selected, if the current
	 * tab doesn't allow
	 * loss of focus
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectedIndex(final int index) {
		if (firstTab) {
			firstTab = false;
			super.setSelectedIndex(index);
		}
		final int oldIndex = getSelectedIndex();
		final Tab tab = (Tab) getComponentAt(oldIndex);
		if (tab.onLostFocus()) {
			super.setSelectedIndex(index);
		}
	}
}
