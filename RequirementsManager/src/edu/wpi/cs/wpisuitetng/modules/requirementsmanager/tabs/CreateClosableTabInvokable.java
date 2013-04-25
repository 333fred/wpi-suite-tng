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
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

/**
 * Class to invoke the updating of the tab later, calling setTabComponent at in
 * a different thread than the swing thread can cause weird issues, this should
 * solve them.
 */

public class CreateClosableTabInvokable implements Runnable {
	
	/** The tabController to change the tab in */
	private final MainTabController tabController;
	
	/** the index of the tab to change */
	private final int index;
	
	/** the tab object that will be updated */
	private final Tab tabToUpdate;
	
	/**
	 * Creates a new Closable tab class
	 * 
	 * @param tabView
	 *            The tab view
	 * @param index
	 *            The index of the tab to change
	 * @param tabToUpdate
	 *            Tab object representing the tab to change
	 */
	
	public CreateClosableTabInvokable(final MainTabController tabController,
			final int index, final Tab tabToUpdate) {
		this.tabController = tabController;
		this.index = index;
		this.tabToUpdate = tabToUpdate;
	}
	
	@Override
	public synchronized void run() {
		tabController.getTabView().setTabComponentAt(index,
				tabToUpdate.getTabComponent(tabController));
		tabController.getTabView().invalidate();
	}
}
