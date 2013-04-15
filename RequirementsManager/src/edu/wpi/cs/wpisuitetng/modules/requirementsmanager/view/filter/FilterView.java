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
		createFilterView = new CreateFilterView(this);			

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filterTableView, createFilterView);
	
		splitPane.setDividerLocation(0.85f);
		
		setLayout(new BorderLayout());
		
		add(splitPane, BorderLayout.CENTER);

	}
	
	/** Refreshes the filters displayed in the table view 
	 * 
	 */
	
	public void refreshTableView() {
		filterTableView.refresh();
	}                          
	
}
