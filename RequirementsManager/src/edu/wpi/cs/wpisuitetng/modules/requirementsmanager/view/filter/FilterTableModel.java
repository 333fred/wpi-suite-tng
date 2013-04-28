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

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

/**
 * Table model to hold the filters that a user has created and not deleted.
 */

public class FilterTableModel implements TableModel {
	
	/** Array list representing the data in the table */
	private final List<String[]> tableData;
	
	/** The listeners for this table model */
	private final List<TableModelListener> listeners;
	
	/** the list of the fitlers being displaying */
	private List<Filter> filters;
	
	/** Array of the names of the columns */
	private final String[] columnNames;
	
	/**
	 * Creates a new Filter Table Model with the given filters
	 * 
	 * @param filters
	 *            The initial set of filters to display
	 */
	
	public FilterTableModel(final List<Filter> filters) {
		tableData = new ArrayList<String[]>();
		listeners = new ArrayList<TableModelListener>();
		
		final String[] cols = { "Id", "Field", "Operation", "Value" };
		this.columnNames = cols;
		
		updateFilters(filters);
	}
	
	@Override
	public void addTableModelListener(final TableModelListener l) {
		listeners.add(l);
	}
	
	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return String.class;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public String getColumnName(final int columnIndex) {
		return columnNames[columnIndex];
	}
	
	/**
	 * Returns the filter at the given row index
	 * 
	 * @param rowIndex
	 *            the index to look under
	 * @return the requested filter. Null if the filter doesn't exist
	 */
	public Filter getFilterAt(final int rowIndex) {
		return getFilterById(Integer.parseInt((String) getValueAt(rowIndex, 0)));
	}
	
	private Filter getFilterById(final int id) {
		for (final Filter filter : filters) {
			if (filter.getId() == id) {
				return filter;
			}
		}
		return null;
	}
	
	@Override
	public int getRowCount() {
		return tableData.size();
	}
	
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		Object o = new Object();
		try {
			if (rowIndex < tableData.size()) {
				o = tableData.get(rowIndex)[columnIndex];
			} else {
				System.out
						.println("Row index was not less than tableData.size() SIZE:"
								+ tableData.size() + " INDEX: " + rowIndex);
			}
		} catch (final IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("RowIndex: " + rowIndex + " ColumnIndex: "
					+ columnIndex + " ListSize " + tableData.size()
					+ " arraySize: " + tableData.get(0).length);
		}
		return o;
	}
	
	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}
	
	@Override
	public void removeTableModelListener(final TableModelListener l) {
		listeners.remove(l);
	}
	
	@Override
	public void setValueAt(final Object aValue, final int rowIndex,
			final int columnIndex) {
		
	}
	
	/**
	 * Updates the data displayed with the new list of filters
	 * 
	 * @param newFilters
	 */
	
	public void updateFilters(final List<Filter> newFilters) {
		filters = newFilters;
		tableData.clear(); // clear out the table data
		for (final Filter filter : newFilters) {
			// create the new column data
			final String[] columnData = new String[getColumnCount()];
			int ci = 0;
			columnData[ci++] = filter.getId() + "";
			columnData[ci++] = filter.getField().toString();
			columnData[ci++] = filter.getOperation().toString();
			columnData[ci++] = filter.getStringValue();
			// add the row
			tableData.add(columnData);
		}
		// notify the listeners
		for (final TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
	
}
