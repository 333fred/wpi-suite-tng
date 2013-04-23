package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EventTableModel implements TableModel {

	/** List of events this table will show */
	private List<Event> events;
	
	/** listeners */
	private List<TableModelListener> listeners;

	public EventTableModel() {
		this(new ArrayList<Event>());
	}	
	
	public EventTableModel(List<Event> events) {
		this.events = events;
		listeners = new ArrayList<TableModelListener>();
	}	
	
	@Override
	public int getRowCount() {
		return events.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Event.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return events.get(rowIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}
	
	private void notifyListeners() {
		for (TableModelListener l : listeners) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
	
	public void setRowData(List<Event> events) {
		this.events = events;		
		notifyListeners();
	}
	
	public void addEvent(Event e) {
		events.add(e);
		notifyListeners();
	}
	
	public List<Event> getRowData() {
		return events;
	}

	
		
		
}
