/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Chen
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * Listener for the Search box in the RequirementTableView
 * 
 * @author Alex
 * 
 */
public class TextSearchListener implements KeyListener {
	
	protected final RequirementTableView tableView;
	protected final JTextComponent searchBox;
	
	/**
	 * Creates a new listener for the given tableview and component
	 * 
	 * @param tableView
	 *            the TableView to listen on
	 * @param searchBox
	 *            the search box to listen on
	 */
	public TextSearchListener(final RequirementTableView tableView,
			final JTextComponent searchBox) {
		this.tableView = tableView;
		this.searchBox = searchBox;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		String filterText = tableView.getTextSearchBox().getText().trim();
		if (filterText.equals("")) {
			
			tableView.clearSearchFilter();
		} else {
			tableView.nameFilter(filterText);
		}
		if (tableView.getTable().getRowCount() == 0) {
			tableView.getTextFilterInfo().setText("No Requirements Found");
		} else {
			tableView.getTextFilterInfo().setText("");
		}
		
	}
	
}
