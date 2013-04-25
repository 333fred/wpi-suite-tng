/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Steve Kordell
 *    @author Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

/**
 * Allows user to close other tabs but disallows closing the active tab
 */
public class UnclosableTabComponent extends JLabel {
	
	private class MouseListener extends MouseAdapter {
		
		private final UnclosableTabComponent component;
		
		private MouseListener(final UnclosableTabComponent component) {
			this.component = component;
		}
		
		@Override
		public void mouseReleased(final MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				final int index = tabbedPane.indexOfTabComponent(component);
				if (index > -1) {
					tabbedPane.setSelectedIndex(index);
				}
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				
				final int index = tabbedPane.indexOfTabComponent(component);
				if (index > -1) {
					final int tabsOpen = tabController.getNumberOfOpenTabs();
					if (tabsOpen > 1) {
						final TabPopupMenu popupMenu = new TabPopupMenu(index,
								tabController,
								TabPopupMenu.CloseMode.CLOSE_ONLY_OTHERS);
						popupMenu.show(component, 5, 5); // off set of 5,5
					}
				}
			}
		}
		
	}
	
	/** The tabcontroller */
	private final MainTabController tabController;
	
	/** The tabbed pane */
	private final JTabbedPane tabbedPane;
	
	public UnclosableTabComponent(final MainTabController tabController,
			final String text) {
		super(text);
		this.tabController = tabController;
		tabbedPane = tabController.getTabView();
		
		addMouseListener(new MouseListener(this));
	}
	
}
