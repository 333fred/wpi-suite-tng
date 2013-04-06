/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Mitchell Caisse
 ********************************************************************************/ 

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.popupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/** Class for creating a right click menu in IterationTreeView, when a user right clicks on the root node
 *
 * @author Mitchell
 *
 */

public class RootPopupMenu extends JPopupMenu implements ActionListener {
	
	/** Menu options for the PopupMenu */
	private JMenuItem menuNewIteration;
	
	/** The tab controller to open a new iteration tab in */
	private MainTabController tabController;
	
	/** Creates a RootPopupMenu with the tabcontroller that it needs to add tabs
	 * 
	 * @param tabController The tab controller to add the new iteration tab too
	 */
	
	public RootPopupMenu(MainTabController tabController) {
		this.tabController = tabController;
		
		menuNewIteration = new JMenuItem("New Iteration");
		menuNewIteration.addActionListener(this);
		
	}
	
	/** The action listener for this menu item, when the user clicks on a menu option, (only one in this case),
	 * it will open a new tab to create an iteration
	 */
	
	public void actionPerformed(ActionEvent e) {
		tabController.addCreateIterationTab();
	}
	
	
}
