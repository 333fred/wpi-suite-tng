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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.FilterController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
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
	private JTable tableView;

	/** The table model for the table view */
	private FilterTableModel tableModel;

	/** The scroll pane for the filter table */
	private JScrollPane scrollPane;

	/** Button used to enable or disable a filter */
	private JButton butEnable;

	/** Button used to delete a filter */
	private JButton butDelete;

	/** Panel to hold stuff in the scrollPane */
	private JPanel butPanel;

	/** The controller to retrieve filters */
	private FilterController filterController;

	/** The FilterView this view is contained in */
	private FilterView filterView;

	public FilterTableView(FilterView filterView) {
		this.filterView = filterView;
		ArrayList<Filter> filters = new ArrayList<Filter>();
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
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.getViewport().add(tableView);

		setLayout(new BorderLayout());

		add(scrollPane, BorderLayout.CENTER);
		add(butPanel, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(100, 500));

		refresh();

	}

	/**
	 * Refreshes the table view
	 * 
	 */

	public void refresh() {
		// get the filters from the server
		RetrieveAllFiltersRequestObserver observer = new RetrieveAllFiltersRequestObserver(
				this);
		filterController.getAll(observer);
		updateFilters();
	}

	@Override
	public void receivedData(Filter[] filters) {
		updateFilters();
	}

	public void valueChanged(ListSelectionEvent e) {
		if (tableView.getSelectedRowCount() == 0) {
			butEnable.setEnabled(false);
			butDelete.setEnabled(true);
		} else if (tableView.getSelectedRowCount() == 1) {
			butEnable.setEnabled(true);
			butDelete.setEnabled(true);

			int selRow = tableView.getSelectedRow();
			Filter filter = tableModel.getFilterAt(selRow);
			if (filter.isActive()) {
				butEnable.setText("Disable");
			} else {
				butEnable.setText("Enable");
			}

		} else {
			// multiple filters are selected
			butEnable.setEnabled(true);
			butDelete.setEnabled(true);
		}

	}

	@Override
	public void responseSuccess() {
		updateFilters();
		tableView.repaint();
	}

	private void updateFilters() {
		List<Filter> filters = FilterDatabase.getInstance().getAll();
		tableModel.updateFilters(filters);
	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
		System.out.println("Response error " + statusCode + " : "
				+ statusMessage);
	}

	@Override
	public void fail(Exception exception) {
		System.out.println("Filter table View Exception!!");
		exception.printStackTrace();
	}

	public MainTabController getTabController() {
		return null;
	}

	public Requirement getDraggedRequirement() {
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(butEnable)) {
			boolean active;
			if (butEnable.getText().equals("Enable")) {
				active = true;
			} else {
				active = false;
			}

			// change the active status of the filters
			int[] selRows = tableView.getSelectedRows();
			for (int row : selRows) {
				Filter filter = tableModel.getFilterAt(row);
				filter.setActive(active);
				UpdateFilterRequestObserver observer = new UpdateFilterRequestObserver(this);
				filterController.save(filter, observer);
			}
			//notify the listeners that we made changes
			filterView.notifyListeners();

		} else if (source.equals(butDelete)) {
			// we will need to work on delete.
		}
	}
}
