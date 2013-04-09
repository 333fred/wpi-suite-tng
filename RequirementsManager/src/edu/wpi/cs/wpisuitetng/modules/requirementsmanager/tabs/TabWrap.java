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

/**
 * Wrapper class around MainTabView that allows tabs to easily change thier
 * title and icons
 * 
 * Adapted from Tab from DefectTracker module.
 * 
 * @author Mitchell
 * 
 */

public class TabWrap {

	private final MainTabView tabView;
	private final Component tabComponent;

	/**
	 * Creates a new instance of Tab with the given tab view, and component
	 * 
	 * @param tabView
	 * @param tabComponent
	 */

	public TabWrap(MainTabView tabView, Component tabComponent) {
		this.tabView = tabView;
		this.tabComponent = tabComponent;
	}

	/**
	 * Returns the index of this tab
	 * 
	 * @return This tabs index
	 */

	private int getIndex() {
		return tabView.indexOfTabComponent(tabComponent);
	}

	/**
	 * Returns the title of this tab
	 * 
	 * @return This tabs title
	 */

	public String getTitle() {
		return tabView.getTitleAt(getIndex());
	}

	/**
	 * Changes the title of the tab to the given title
	 * 
	 * @param title
	 *            The new title of the tab
	 */

	public void setTitle(String title) {
		tabView.setTitleAt(getIndex(), title);
		tabComponent.invalidate(); // have the tab redraw itself to update to
									// the new string length
	}

	/**
	 * Returns the icon of this tab
	 * 
	 * @return This tabs icon
	 */

	public Icon getIcon() {
		return tabView.getIconAt(getIndex());
	}

	/**
	 * Sets this tabs icon to the given icon
	 * 
	 * @param icon
	 *            Tabs new icon
	 */

	public void setIcon(Icon icon) {
		tabView.setIconAt(getIndex(), icon);
	}

	/**
	 * Returns thsi tabs tooltip text
	 * 
	 * @return Tooltip text
	 */

	public String getToolTipText() {
		return tabView.getToolTipTextAt(getIndex());
	}

	/**
	 * Sets this tabs tool tip text
	 * 
	 * @param toolTipText
	 *            Set the tooltip of the Tab to this String
	 */
	public void setToolTipText(String toolTipText) {
		tabView.setToolTipTextAt(getIndex(), toolTipText);
	}

	/**
	 * Returns this tabs component
	 * 
	 * @return Tabs component
	 */

	public Component getComponent() {
		return tabView.getComponentAt(getIndex());
	}

	/**
	 * Sets this tabs component to the given component
	 * 
	 * @param component
	 *            Set the component contained by this Tab to this Component
	 */
	public void setComponent(Component component) {
		tabView.setComponentAt(getIndex(), component);
	}
}
