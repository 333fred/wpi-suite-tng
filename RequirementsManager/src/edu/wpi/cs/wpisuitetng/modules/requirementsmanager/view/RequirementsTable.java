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
	//TODO: How to get the actual number for this?
	private boolean[] editedRows;
	private RequirementTableView view;

	/**
	 * @param rowData
	 * @param columnNames
	 */
	public RequirementsTable(Vector rowData, Vector columnNames, RequirementTableView view) {
		super(rowData, columnNames);
		this.view = view;
		//editedRows = new boolean[super.getRowCount()];
	}
	
	
	@Override
	public boolean isCellEditable(int row, int column) {
		//Only the "Estimate" column is currently editable
		String status = (String)super.getModel().getValueAt(convertRowIndexToModel(row), 4);
		boolean statusEditable = !status.equals("In Progress") && !status.equals("Deleted") && !status.equals("Complete");

		return (view.isEditable() && super.convertColumnIndexToModel(column) == 6 && statusEditable);
	};
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		if(editedRows == null){
			editedRows = new boolean[super.getRowCount()];
		} else if (editedRows.length < super.getRowCount()){
			boolean[] temp = new boolean[super.getRowCount()];
			System.arraycopy(editedRows, 0, temp, 0, editedRows.length);
			editedRows = temp;
		}

		//The estimate column should only accept non-negative integers
		try {
			if (super.convertColumnIndexToModel(col) == 6) {
				int i = Integer.parseInt((String) value);
				if (i < 0 || i == Integer.parseInt((String)super.getValueAt(row, col))) {
					return;
				} else {
					//we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					selectionModel.removeSelectionInterval(convertRowIndexToModel(row),convertRowIndexToModel(row));
					super.setValueAt(Integer.toString(i), row, col);
				}
			} else {
				editedRows[super.convertRowIndexToModel(row)] = true;
				super.setValueAt(value, row, col);
			}
		} catch (NumberFormatException e) {
			return;
		}
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if(editedRows == null){
			editedRows = new boolean[super.getRowCount()];
		} else if (editedRows.length < super.getRowCount()){
			boolean[] temp = new boolean[super.getRowCount()];
			System.arraycopy(editedRows, 0, temp, 0, editedRows.length);
			editedRows = temp;
		}
			
        if (editedRows[super.convertRowIndexToModel(row)] && super.convertColumnIndexToModel(column) == 6) {
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
            renderer.setBackground(Color.yellow);
            return renderer;
        } else {
            return super.getCellRenderer(row, column);
        }
    }
	
	public void clearUpdated() {
		editedRows = new boolean[super.getRowCount()];
	}
	
	public boolean[] getEditedRows() {
		return editedRows;
	}
}
