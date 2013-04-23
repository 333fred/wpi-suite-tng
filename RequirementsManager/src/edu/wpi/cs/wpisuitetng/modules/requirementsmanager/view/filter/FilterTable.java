package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class FilterTable extends JTable {

	/** the table model used to represnt data in the table */
	private FilterTableModel tableModel;

	public FilterTable(FilterTableModel tableModel) {
		super(tableModel);
		this.tableModel = tableModel;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int column) {
		Component c = super.prepareRenderer(renderer, row, column);

		if (isRowSelected(row)) {			
			if (!tableModel.getFilterAt(row).isActive()) {
				c.setBackground(new Color(184, 207, 229));
				c.setFont(c.getFont().deriveFont(Font.ITALIC));
			} else {
				c.setBackground(new Color(184, 207, 229));
				c.setFont(c.getFont().deriveFont(Font.BOLD));
			}
		} else {
			if (!tableModel.getFilterAt(row).isActive()) {
				c.setBackground(Color.LIGHT_GRAY);
				c.setFont(c.getFont().deriveFont(Font.ITALIC));
			} else {
				c.setBackground(Color.WHITE);
				c.setFont(c.getFont().deriveFont(Font.BOLD));
			}
		}

		return c;
	}
	/*
	@Override
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
		super.changeSelection(rowIndex,columnIndex, true, false);
	}
	*/
}
