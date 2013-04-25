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

import java.awt.Component;

import javax.swing.JPanel;

/**
 * Interface for listening to when a tab has gained focus
 */

public class Tab extends JPanel {
	
	public Component getTabComponent(final MainTabController tabController) {
		return new ClosableTabComponent(tabController);
	}
	
	/**
	 * Function called when the tab gains focus
	 * 
	 */
	
	public void onGainedFocus() {
		
	}
	
	/**
	 * Called before the user is about to switch off of the tab
	 * 
	 * @return a boolean indicating whether to leave the tab or not, if false
	 *         the tab will stay selected
	 */
	
	public boolean onLostFocus() {
		return true;
		
	}
	
	/**
	 * Called when the tab is about to be closed.
	 * 
	 * @return a boolean indicating whether to close the tab or not, if false
	 *         tab will not close
	 */
	
	public boolean onTabClosed() {
		return true;
	}
	
	/**
	 * Refreshes the tab
	 * 
	 */
	
	public void refresh() {
		
	}
	
}
