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

@SuppressWarnings ("serial")
public class MainTabView extends JTabbedPane {
	
	/** boolean indicating whether the first tab has been added */
	private boolean firstTab;
	
	/** The tab controller for this tab view */
	private final MainTabController tabController;
	
	/**
	 * Creates a new MainTabView with the given controller
	 * 
	 * @param tabController
	 *            the controller for this view
	 */
	public MainTabView(final MainTabController tabController) {
		this.tabController = tabController;
		firstTab = true;
		// set the tabs to be placed at the top
		setTabPlacement(SwingConstants.TOP);
		// allow the tabs to be scrollable when there are too many to fit on the
		// screen
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		// TabView starts off empty.
	}
	
	/**
	 * Adds a tab with the given title, icon, and tooltip
	 * 
	 * @param title
	 *            the title of the tabe
	 * @param icon
	 *            the icon for the tab
	 * @param tab
	 *            the content of the tab
	 * @param tip
	 *            the tooltip of the tab
	 */
	public void addTab(final String title, final Icon icon, final Tab tab,
			final String tip) {
		super.addTab(title, icon, tab, tip);
		// the tab was just added, so we assume that it was at the end add the
		// tab with the given tab component
		final int index = getTabCount() - 1;
		setTabComponentAt(index, tab.getTabComponent(tabController));
	}
	
	/**
	 * Adds an un-closable tab to this JTabbedPane
	 * 
	 * @param title
	 *            The title of tab
	 * @param icon
	 *            The icon that will be displayed in this tab
	 * @param component
	 *            The component that the tab will display
	 * @param tip
	 *            The tooltip to be displayed for the tab
	 * 
	 */
	public void addUnclosableTab(final String title, final Icon icon,
			final Component component, final String tip) {
		super.addTab(title, icon, component, tip);
	}
	
	/**
	 * Removes the component at the specified index.
	 * 
	 * @param index
	 *            the index to remove at
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
