/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mitchell Caisse
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

/** The view for viewing and creating filters 
 * 
 * @author Mitchell
 *
 */

public class FilterView extends JPanel {

	/** View for displaying and enabling / disabling filters */
	private FilterTableView filterTableView;
	
	/** View for creating and editing filters */
	private CreateFilterView createFilterView;
	
	public FilterView() {
		
		filterTableView = new FilterTableView();
		createFilterView = new CreateFilterView();
		
		/*
		SpringLayout layout = new SpringLayout();
		
		layout.putConstraint(SpringLayout.NORTH, filterTableView, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, filterTableView, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, filterTableView, 0, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.SOUTH, filterTableView, 0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, createFilterView, 0, SpringLayout.VERTICAL_CENTER, this);
		
		layout.putConstraint(SpringLayout.SOUTH, createFilterView, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, createFilterView, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, createFilterView, 0, SpringLayout.WEST, this);
		
		setLayout(layout);*/
		
		//JScrollPane createScrollPane = new JScrollPane(createFilterView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filterTableView, createFilterView);
	
		splitPane.setDividerLocation(0.75f);
		
		setLayout(new BorderLayout());
		
		add(splitPane, BorderLayout.CENTER);
		
	

	}
	
}
