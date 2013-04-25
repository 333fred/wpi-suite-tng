/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * The table model for holding all the events
 */

public class EventTableModel implements TableModel {
	
	/** List of events this table will show */
	private List<Event> events;
	
	/** listeners */
	private final List<TableModelListener> listeners;
	
	/**
	 * Creates a new table model with an empty list
	 */
	public EventTableModel() {
		this(new ArrayList<Event>());
	}
	
	/**
	 * Creates a new table model with the given list of eventF
	 * 
	 * @param events
	 *            the events for the table model to hold
	 */
	public EventTableModel(final List<Event> events) {
		this.events = events;
		listeners = new ArrayList<TableModelListener>();
	}
	
	/**
	 * Adds the given event to the model
	 * 
	 * @param e
	 *            event to add
	 */
	public void addEvent(final Event e) {
		events.add(e);
		notifyListeners();
	}
	
	@Override
	public void addTableModelListener(final TableModelListener l) {
		listeners.add(l);
	}
	
	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		return Event.class;
	}
	
	@Override
	public int getColumnCount() {
		return 1;
	}
	
	@Override
	public String getColumnName(final int columnIndex) {
		return "";
	}
	
	@Override
	public int getRowCount() {
		return events.size();
	}
	
	/**
	 * Gets the all the row data
	 * 
	 * @return the list of events
	 */
	public List<Event> getRowData() {
		return events;
	}
	
	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		return events.get(rowIndex);
	}
	
	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return false;
	}
	
	private void notifyListeners() {
		for (final TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
	
	@Override
	public void removeTableModelListener(final TableModelListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Sets the row data to the given list of events
	 * 
	 * @param events
	 *            the new list
	 */
	public void setRowData(final List<Event> events) {
		this.events = events;
		notifyListeners();
	}
	
	@Override
	public void setValueAt(final Object aValue, final int rowIndex,
			final int columnIndex) {
	}
	
}
