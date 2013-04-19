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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

/** The view for viewing and creating filters 
 * 
 * @author Mitchell
 *
 */

public class FilterView extends JPanel {

	/** */
	private static FilterView fv;
	
	/** View for displaying and enabling / disabling filters */
	private FilterTableView filterTableView;
	
	/** View for creating and editing filters */
	private CreateFilterView createFilterView;
	
	/** List of the filter update listeners */
	private List<FilterUpdateListener> filterUpdateListeners;
	
	public FilterView() {
		
		filterTableView = new FilterTableView(this);
		createFilterView = new CreateFilterView(this);	
		filterUpdateListeners = new ArrayList<FilterUpdateListener>();

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filterTableView, createFilterView);
	
		splitPane.setDividerLocation(0.75f);
		
		setLayout(new BorderLayout());
		
		add(filterTableView, BorderLayout.CENTER);
		add(createFilterView, BorderLayout.SOUTH);

	}
	
	/**
	 * 
	 * @return
	 */
	
	public static FilterView getInstance() {
		if (fv == null) {
			fv = new FilterView();
		}
		return fv;
	}
	
	/** Refreshes the filters displayed in the table view 
	 * 
	 */
	
	public void refreshTableView() {
		filterTableView.refresh();
	}   
	
	/** Adds the given listener
	 * 
	 * @param listener THe listener to add
	 */
	
	public void addFilterUpdateListener(FilterUpdateListener listener) {
		filterUpdateListeners.add(listener);
	}
	
	/** Removes the given listener 
	 * 
	 * @param listener The listener to remove
	 */
	
	public void removeFilterUpdateListener(FilterUpdateListener listener) {
		filterUpdateListeners.remove(listener);
	}
	
	protected void notifyListeners() {
		for (FilterUpdateListener listener : filterUpdateListeners) {
			listener.filtersUpdated();
		}
	}
	
	/** Edits the given filter
	 * 
	 * @param toEdit
	 */
	
	public void editFilter(Filter toEdit) {
		createFilterView.editFilter(toEdit);
	}
	
	/** Cancels the editing of the filter 
	 * 
	 */
	
	public void cancelEdit() {
		createFilterView.cancelEdit();
	}
	
}
