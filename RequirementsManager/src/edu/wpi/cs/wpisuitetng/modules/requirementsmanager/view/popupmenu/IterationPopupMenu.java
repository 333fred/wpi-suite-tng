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
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/** Class for creating a right click menu in IterationTreeView, when a user right clicks an iteration node
*
* @author Mitchell
*
*/

public class IterationPopupMenu extends JPopupMenu implements ActionListener {

	/** Menu options for the PopupMenu */
	//private JMenuItem menuCreateRequirement;
	private JMenuItem menuEditIteration;
	
	/** The tab controller used to create new tabs */
	private MainTabController tabController;
	
	/** The iterations that were selected when this was pressed */
	private List<Iteration> selectedIterations;
	
	/** Creates an IterationPopupMenu with the given tab controller, and selected iterations
	 * 
	 * @param tabController The tab controller to open tabs in
	 * @param selectedIterations The iterations that were selected when right click was pressed
	 */
	
	public IterationPopupMenu(MainTabController tabController, List<Iteration> selectedIterations) {
		this.tabController = tabController;
		this.selectedIterations = selectedIterations;
		
		//we only have on selected iteration
		if (selectedIterations.size() == 1) {
			menuEditIteration = new JMenuItem("Edit Iteration");
			//menuCreateRequirement = new JMenuItem("New Requirement");
			
			menuEditIteration.addActionListener(this); 
			//menuCreateRequirement.addActionListener(this);
			
			add(menuEditIteration);
			//add(menuCreateRequirement);
			
		}
		else {	
			menuEditIteration = new JMenuItem("Edit Iterations");
			menuEditIteration.addActionListener(this);
			add(menuEditIteration);
		}

	}
	
	/** The action listener that is called when the user selects a menu option
	 * 
	 */
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(menuEditIteration)) {
			//edit iteration was selected
			//TODO: Add check that the iteration tab isn't already opened
			for (Iteration i: selectedIterations) {
				tabController.addEditIterationTab(i);
			}
		}
		else {
			//create requiremetn was selected
			//TODO: make this actualy create a requirement under the given iteration
			tabController.addCreateRequirementTab(selectedIterations.get(0));
		}
	}
	
}
