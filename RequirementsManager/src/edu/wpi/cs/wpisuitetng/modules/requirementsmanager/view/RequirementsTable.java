/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Alex
 * 
 */
public class RequirementsTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	// TODO: How to get the actual number for this?
	private boolean[] editedRows;
	private final RequirementTableView view;
	
	/**
	 * @param rowData
	 * @param columnNames
	 */
	public RequirementsTable(final Vector rowData, final Vector columnNames,
			final RequirementTableView view) {
		super(rowData, columnNames);
		this.view = view;
	}
	
	public void clearUpdated() {
		editedRows = new boolean[super.getRowCount()];
	};
	
	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		if (editedRows == null) {
			editedRows = new boolean[super.getRowCount()];
		} else if (editedRows.length < super.getRowCount()) {
			final boolean[] temp = new boolean[super.getRowCount()];
			editedRows = temp;
		}
		
		if (editedRows[super.convertRowIndexToModel(row)]
				&& (super.convertColumnIndexToModel(column) == 6)) {
			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setBackground(Color.yellow);
			return renderer;
		} else {
			return super.getCellRenderer(row, column);
		}
	}
	
	public boolean[] getEditedRows() {
		return editedRows;
	}
	
	@Override
	public boolean isCellEditable(final int row, final int column) {
		// Only the "Estimate" column is currently editable
		final String status = (String) super.getModel().getValueAt(
				convertRowIndexToModel(row), 4);
		final boolean statusEditable = !status.equals("In Progress")
				&& !status.equals("Deleted") && !status.equals("Complete");
		
		return (view.isEditable()
				&& (super.convertColumnIndexToModel(column) == 6) && statusEditable);
	}
	
	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		if (!view.isEditable()) {
			return;
		}
		if (editedRows == null) {
			editedRows = new boolean[super.getRowCount()];
		} else if (editedRows.length < super.getRowCount()) {
			final boolean[] temp = new boolean[super.getRowCount()];
			editedRows = temp;
		}
		
		// The estimate column should only accept non-negative integers
		try {
			if (super.convertColumnIndexToModel(col) == 6) {
				final int i = Integer.parseInt((String) value);
				if ((i < 0)
						|| (i == Integer.parseInt((String) super.getValueAt(
								row, col)))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(Integer.toString(i), row, col);
				}
			} else {
				selectionModel.removeSelectionInterval(
						convertRowIndexToModel(row),
						convertRowIndexToModel(row));
				super.getCellEditor().stopCellEditing();
				super.setValueAt(value, row, col);
			}
		} catch (final NumberFormatException e) {
			return;
		}
	}
}
