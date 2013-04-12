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
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

/**
 * Class to invoke the updating of the tab later, calling setTabComponent at in
 * a different thread than the swing thread can cause wierd issues, this should
 * solve them
 * 
 * @author Mitchell
 * 
 */

public class CreateClosableTabInvokable implements Runnable {

	/** The tabController to change the tab in */
	private MainTabController tabController;

	/** the index of the tab to change */
	private int index;

	/** the tab object that will be updated */
	private Tab tabToUpdate;

	/**
	 * Creates a new Closable tab class
	 * 
	 * @param tabView
	 *            The tab view
	 * @param index
	 *            The index of hte tab to change
	 * @param tabToUpdate
	 *            Tab object represeting the tab to change
	 */

	public CreateClosableTabInvokable(MainTabController tabController, int index,
			Tab tabToUpdate) {
		this.tabController = tabController;
		this.index = index;
		this.tabToUpdate = tabToUpdate;
	}

	@Override
	public synchronized void run() {
		tabController.getTabView().setTabComponentAt(index, tabToUpdate.getTabComponent(tabController));
		tabController.getTabView().invalidate();
	}
}
