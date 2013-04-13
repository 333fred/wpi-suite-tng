/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * This provides a tab component with a close button to the left of the title.
 */
@SuppressWarnings("serial")
public class ClosableTabComponent extends JPanel implements ActionListener {

    private final JTabbedPane tabbedPane;

    private final MainTabController tabController;

    /**
     * Create a closable tab component belonging to the given tabbedPane. The
     * title is extracted with {@link JTabbedPane#getTitleAt(int)}.
     * 
     * @param tabbedPane
     *            The JTabbedPane this tab component belongs to
     */
    public ClosableTabComponent(MainTabController tabController) {
	super(new FlowLayout(FlowLayout.LEFT, 0, 0));
	this.tabController = tabController;
	tabbedPane = tabController.getTabView();
	setOpaque(false);

	final JLabel label = new JLabel() {
	    // display the title according to what's set on our JTabbedPane
	    @Override
	    public String getText() {
		final JTabbedPane tabbedPane = ClosableTabComponent.this.tabbedPane;
		final int index = tabbedPane
			.indexOfTabComponent(ClosableTabComponent.this);
		return index > -1 ? tabbedPane.getTitleAt(index) : "";
	    }
	};

	label.setBorder(BorderFactory.createEmptyBorder(3, 0, 2, 7));
	add(label);

	final JButton closeButton = new JButton("\u2716");
	closeButton.setFont(closeButton.getFont().deriveFont((float) 8));
	closeButton.setMargin(new Insets(0, 0, 0, 0));
	closeButton.addActionListener(this);
	add(closeButton);

	// add the mouse listeners
	MiddleMouseListener mouseListener = new MiddleMouseListener(this);
	addMouseListener(mouseListener);
	closeButton.addMouseListener(mouseListener);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	// close this tab when close button is clicked
	final int index = tabbedPane.indexOfTabComponent(this);
	if (index > -1) {
	    Tab tab = (Tab) tabbedPane.getComponentAt(index);
	    // check if the tab can be closed, or if tab will close itself
	    if (tab.onTabClosed()) {
		tabbedPane.remove(index);
	    }
	}
    }

    private class MiddleMouseListener extends MouseAdapter {

	private ClosableTabComponent component;

	public MiddleMouseListener(ClosableTabComponent component) {
	    this.component = component;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    if (e.getButton() == MouseEvent.BUTTON2) {
		// close the tab
		final int index = tabbedPane.indexOfTabComponent(component);
		if (index > -1) {
		    Tab tab = (Tab) tabbedPane.getComponentAt(index);
		    if (tab.onTabClosed()) {
			tabbedPane.remove(index);
		    }
		}
	    } else if (e.getButton() == MouseEvent.BUTTON1) {
		final int index = tabbedPane.indexOfTabComponent(component);
		if (index > -1) {
		    tabbedPane.setSelectedIndex(index);
		}
	    } else if (e.getButton() == MouseEvent.BUTTON3) {
		final int index = tabbedPane.indexOfTabComponent(component);
		if (index > -1) {
		    int tabsOpen = tabController.getNumberOfOpenTabs();
		    TabPopupMenu popupMenu;
		    if (tabsOpen == 2) {
			// only one closable tab component open
			popupMenu = new TabPopupMenu(index, tabController,
				TabPopupMenu.CloseMode.CLOSE_ONLY_THIS);
		    } else {
			popupMenu = new TabPopupMenu(index, tabController,
				TabPopupMenu.CloseMode.CLOSE_ALL);
		    }
		    popupMenu.show(component, 5, 5); // off set of 5,5
		    // tabbedPane.setSelectedIndex(index);
		}
	    }
	}

    }

}
