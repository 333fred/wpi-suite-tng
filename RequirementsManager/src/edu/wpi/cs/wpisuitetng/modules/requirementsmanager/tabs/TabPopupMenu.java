/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Creates right-click menu when right-clicking (or control-clicking) a tab.
 */
public class TabPopupMenu extends JPopupMenu implements ActionListener {
	
	public enum CloseMode {
		CLOSE_ONLY_OTHERS, // status to use on unclosable tabs
		CLOSE_ONLY_THIS, // status to use when only one closable tab is open
		CLOSE_ALL // status to use when multiple closable tabs are open
	}
	
	/** Popup menu item to close this tab */
	private final JMenuItem menuCloseThis;
	
	/** Popup menu item to close other tabs */
	private final JMenuItem menuCloseOther;
	
	/** Popup menu item to close all tabs */
	private final JMenuItem menuCloseAll;
	
	/** The tab controller to operate on */
	private final MainTabController tabController;
	
	/** the Close mode of this PopupMenu */
	private final CloseMode closeMode;
	
	/** The index of the tab that this was opened on */
	private final int tabIndex;
	
	public TabPopupMenu(final int tabIndex,
			final MainTabController tabController, final CloseMode closeMode) {
		this.tabIndex = tabIndex;
		this.tabController = tabController;
		this.closeMode = closeMode;
		
		menuCloseThis = new JMenuItem("Close");
		menuCloseOther = new JMenuItem("Close Others");
		menuCloseAll = new JMenuItem("Close All");
		
		menuCloseThis.addActionListener(this);
		menuCloseOther.addActionListener(this);
		menuCloseAll.addActionListener(this);
		
		if (closeMode == CloseMode.CLOSE_ONLY_OTHERS) {
			add(menuCloseOther);
		} else if (closeMode == CloseMode.CLOSE_ONLY_THIS) {
			add(menuCloseThis);
		} else if (closeMode == CloseMode.CLOSE_ALL) {
			add(menuCloseThis);
			add(menuCloseOther);
			add(menuCloseAll);
		}
		
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		final JMenuItem source = (JMenuItem) e.getSource();
		if (source.equals(menuCloseThis)) {
			tabController.closeTabAt(tabIndex);
		} else if (source.equals(menuCloseOther)) {
			tabController.closeOtherTabs(tabIndex);
		} else {
			tabController.closeAllTabs();
		}
	}
}
