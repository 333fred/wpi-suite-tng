package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

public class EventTable extends JTable implements TableColumnModelListener, TableModelListener {

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

	private void updateRowHeights() {
		try {
			for (int row = 0; row < getRowCount(); row++) {
				int rowHeight = getRowHeight();
				for (int column = 0; column < getColumnCount(); column++) {
					Component comp = prepareRenderer(
							getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
				}

				setRowHeight(row, rowHeight);
			}
		} catch (ClassCastException e) {
		}
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		updateRowHeights();
	}
	
	@Override
	public void columnMarginChanged(ChangeEvent e) {
		updateRowHeights();
	}
}
