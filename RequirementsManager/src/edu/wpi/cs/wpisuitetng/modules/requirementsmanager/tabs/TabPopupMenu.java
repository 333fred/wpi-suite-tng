package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class TabPopupMenu extends JPopupMenu implements ActionListener {

	/** Popup menu item to close this tab */
	private JMenuItem menuCloseThis;

	/** Popup menu item to close other tabs */
	private JMenuItem menuCloseOther;

	/** Popup menu item to close all tabs */
	private JMenuItem menuCloseAll;

	/** The tab controller to operate on */
	private MainTabController tabController;

	public enum CloseMode {
		CLOSE_ONLY_OTHERS, // status to use on unclosable tabs
		CLOSE_ONLY_THIS, // status to use when only one closable tab is open
		CLOSE_ALL // status to use when multiple closable tabs are open
	}

	/** the Close mode of this PopupMenu */
	private CloseMode closeMode;

	/** The index of the tab that this was opened on */
	private int tabIndex;

	public TabPopupMenu(int tabIndex, MainTabController tabController, CloseMode closeMode) {
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
			add(menuCloseAll);
		}
		else if (closeMode == CloseMode.CLOSE_ONLY_THIS) {
			add(menuCloseThis);
		}
		else if (closeMode == CloseMode.CLOSE_ALL) {
			add(menuCloseThis);
			add(menuCloseOther);
			add(menuCloseAll);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) e.getSource();
		if (source.equals(menuCloseThis)) {
			tabController.closeTabAt(tabIndex);
		}
		else if (source.equals(menuCloseOther)) {
			tabController.closeOtherTabs(tabIndex);
		}
		else {
			tabController.closeAllTabs();
		}
	}
}
