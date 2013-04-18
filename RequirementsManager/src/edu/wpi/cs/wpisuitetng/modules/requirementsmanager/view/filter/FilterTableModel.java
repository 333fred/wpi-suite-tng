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

public class FilterTableModel implements TableModel {

	/** Array list representing the data in the table */
	private List<String[]> tableData;
	
	/** The listeners for this table model */
	private List<TableModelListener> listeners;
	
	/** the list of the fitlers being displaying */
	private List<Filter> filters;
	
	/** Array of the names of the columns */
	private String[] columnNames;
	
	/** Creates a new Filter Table Model with the given filters 
	 * 
	 * @param filters The initial set of filters to display 
	 */
	
	public FilterTableModel(List<Filter> filters) {
		tableData = new ArrayList<String[]>();
		listeners = new ArrayList<TableModelListener>();
		
		String[] columnNames = {"Id", "Field", "Operation", "Value"};
		this.columnNames = columnNames;
		
		updateFilters(filters);
	}
	
	/** Updates the data displayed with the new list of filters 
	 * 
	 * @param filters
	 */
	
	public void updateFilters(List<Filter> filters) {
		this.filters = filters;
		tableData.clear(); //clear out the table data
		for (Filter filter: filters) {
			//create the new column data
			String[] columnData = new String[getColumnCount()];
			int ci = 0;
			columnData[ci++] = filter.getId() + "";
			columnData[ci++] = filter.getField().toString();
			columnData[ci++] = filter.getOperation().toString();
			columnData[ci++] = filter.getStringValue();
			//add the row
			tableData.add(columnData);
		}
		//notify the listeners
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
	
	public int getRowCount() {
		return tableData.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableData.get(rowIndex)[columnIndex];
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}

	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}
	
	/** Returns the filter at the given row index 
	 */
	
	public Filter getFilterAt(int rowIndex) {
		return getFilterById(Integer.parseInt((String)getValueAt(rowIndex, 0)));
	}
	
	private Filter getFilterById(int id) {
		for (Filter filter : filters) {
			if (filter.getId() == id) {
				return filter;
			}
		}
		return null;
	}

}
