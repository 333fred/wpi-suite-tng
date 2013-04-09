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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Interface for listening to when a tab has gained focus
 * 
 * @author Mitchell
 * 
 */

public abstract class Tab extends JPanel {

	/**
	 * Function called when the tab gains focus
	 * 
	 */

	public void onGainedFocus() {

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

	public Component getTabComponent(JTabbedPane tabbedPane) {
		return new ClosableTabComponent(tabbedPane);
	}

}
