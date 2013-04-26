/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.FilterController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.DeleteFilterRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllFiltersRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateFilterRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetrieveAllFiltersNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class FilterTableView extends JPanel implements
		IRetrieveAllFiltersNotifier, ListSelectionListener, ISaveNotifier,
		ActionListener {
	
	/** List of all the filters this is displaying */
	List<Filter> filters;
	
	/** the table view for the filters */
	private final JTable tableView;
	
	/** The table model for the table view */
	private final FilterTableModel tableModel;
	
	/** The scroll pane for the filter table */
	private final JScrollPane scrollPane;
	
	/** Button used to enable or disable a filter */
	private final JButton butEnable;
	
	/** Button used to delete a filter */
	private final JButton butDelete;
	
	/** Panel to hold stuff in the scrollPane */
	private final JPanel butPanel;
	
	/** The controller to retrieve filters */
	private final FilterController filterController;
	
	/** The FilterView this view is contained in */
	private final FilterView filterView;
	
	public FilterTableView(final FilterView filterView) {
		this.filterView = filterView;
		final List<Filter> filters = new ArrayList<Filter>();
		filterController = new FilterController();
		
		butPanel = new JPanel();
		
		butEnable = new JButton("Disable");
		butEnable.setEnabled(false);
		butEnable.addActionListener(this);
		
		butDelete = new JButton("Delete");
		butDelete.setEnabled(false);
		butDelete.addActionListener(this);
		
		butPanel.add(butEnable);
		butPanel.add(butDelete);
		
		tableModel = new FilterTableModel(filters);
		
		tableView = new FilterTable(tableModel);
		tableView.setFillsViewportHeight(true);
		tableView.getSelectionModel().addListSelectionListener(this);
		
		tableView.getColumnModel().removeColumn(
				tableView.getColumnModel().getColumn(0));
		
		scrollPane = new JScrollPane(tableView,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		setLayout(new BorderLayout());
		
		add(scrollPane, BorderLayout.CENTER);
		add(butPanel, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(100, 500));
		
		refresh();
		
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		final Object source = e.getSource();
		if (source.equals(butEnable)) {
			boolean active;
			if (butEnable.getText().equals("Enable")) {
				active = true;
			} else {
				active = false;
			}
			
			// change the active status of the filters
			final int[] selRows = tableView.getSelectedRows();
			for (final int row : selRows) {
				final Filter filter = tableModel.getFilterAt(row);
				filter.setActive(active);
				final UpdateFilterRequestObserver observer = new UpdateFilterRequestObserver(
						this);
				filterController.save(filter, observer);
			}
			// notify the listeners that we made changes
			filterView.notifyListeners();
			
		} else if (source.equals(butDelete)) {
			final int[] selRows = tableView.getSelectedRows();
			for (final int row : selRows) {
				final Filter filter = tableModel.getFilterAt(row);
				final DeleteFilterRequestObserver observer = new DeleteFilterRequestObserver(
						this, filter);
				filterController.delete(filter, observer);
			}
			// notify the listeners that we made changes
			filterView.notifyListeners();
		}
	}
	
	@Override
	public void fail(final Exception exception) {
		System.out.println("Filter table View Exception!!");
		exception.printStackTrace();
	}
	
	public Requirement getDraggedRequirement() {
		return null;
	}
	
	public MainTabController getTabController() {
		return null;
	}
	
	@Override
	public void receivedData(final Filter[] filters) {
		updateFilters();
	}
	
	/**
	 * Refreshes the table view
	 * 
	 */
	
	public void refresh() {
		// get the filters from the server
		final RetrieveAllFiltersRequestObserver observer = new RetrieveAllFiltersRequestObserver(
				this);
		filterController.getAll(observer);
		updateFilters();
	}
	
	@Override
	public void responseError(final int statusCode, final String statusMessage) {
		System.out.println("Response error " + statusCode + " : "
				+ statusMessage);
	}
	
	@Override
	public void responseSuccess() {
		updateFilters();
		filterView.notifyListeners();
		tableView.repaint();
	}
	
	private void updateFilters() {
		final List<Filter> filters = FilterDatabase.getInstance().getAll();
		tableModel.updateFilters(filters);
	}
	
	@Override
	public void valueChanged(final ListSelectionEvent e) {
		updateButtonStatus();		
	}
	
	/** Updates the status of the buttoms 
	 * 
	 */
	
	private void updateButtonStatus() {
		if (tableView.getSelectedRowCount() == 0) {
			butEnable.setEnabled(false);
			butDelete.setEnabled(true);
			filterView.cancelEdit();
		} else if (tableView.getSelectedRowCount() == 1) {
			butEnable.setEnabled(true);
			butDelete.setEnabled(true);
			
			final int selRow = tableView.getSelectedRow();
			final Filter filter = tableModel.getFilterAt(selRow);
			if (filter.isActive()) {
				butEnable.setText("Disable");
			} else {
				butEnable.setText("Enable");
			}
			
			filterView.editFilter(filter);
			
		} else {
			// multiple filters are selected
			butEnable.setEnabled(true);
			butDelete.setEnabled(true);
			filterView.cancelEdit();
		}	
	}
	
	/** Will disable all of the fields in this View
	 * 
	 */
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tableView.setEnabled(enabled);
		if (enabled) {
			//if we are enabled make sure we properly enable the buttons
			updateButtonStatus();
		}
		else {
			//disabling, just disable the buttons
			butDelete.setEnabled(enabled);
			butEnable.setEnabled(enabled);
		}
	}
}
