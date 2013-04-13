/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steve Kordell, Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

public class UnclosableTabComponent extends JLabel {

    /** The tabcontroller */
    private MainTabController tabController;
    /** The tabbed pane */
    private JTabbedPane tabbedPane;

    public UnclosableTabComponent(MainTabController tabController, String text) {
	super(text);
	this.tabController = tabController;
	tabbedPane = tabController.getTabView();

	addMouseListener(new MouseListener(this));
    }

    private class MouseListener extends MouseAdapter {

	private UnclosableTabComponent component;

	public MouseListener(UnclosableTabComponent component) {
	    this.component = component;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    if (e.getButton() == MouseEvent.BUTTON1) {
		final int index = tabbedPane.indexOfTabComponent(component);
		if (index > -1) {
		    tabbedPane.setSelectedIndex(index);
		}
	    } else if (e.getButton() == MouseEvent.BUTTON3) {

		final int index = tabbedPane.indexOfTabComponent(component);
		if (index > -1) {
		    int tabsOpen = tabController.getNumberOfOpenTabs();
		    if (tabsOpen > 1) {
			TabPopupMenu popupMenu = new TabPopupMenu(index,
				tabController, TabPopupMenu.CloseMode.CLOSE_ONLY_OTHERS);
			popupMenu.show(component, 5, 5); // off set of 5,5
		    }
		}
	    }
	}

    }

}
