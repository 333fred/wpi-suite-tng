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

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/** Class for creating a right click menu in IterationTreeView, when a user right clicks an iteration node
*
* @author Mitchell
*
*/

public class IterationPopupMenu extends JPopupMenu {

	/** Menu options for the PopupMenu */
	private JMenuItem menuCreateRequirement;
	private JMenuItem menuEditIteration;
	
	public IterationPopupMenu() {
		
		menuCreateRequirement = new JMenuItem("New Requirement");
		menuEditIteration = new JMenuItem("New Iteration");
		
		add(menuCreateRequirement);
		add(menuEditIteration);
	}
	
}
