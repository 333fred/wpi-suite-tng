package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class EventTable extends JTable {
	
	/** the table model for this */
	private EventTableModel tableModel;
	
	public EventTable(EventTableModel tableModel) {
		super(tableModel);
		this.tableModel = tableModel;
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		return new EventCellRenderer();
	}
}
