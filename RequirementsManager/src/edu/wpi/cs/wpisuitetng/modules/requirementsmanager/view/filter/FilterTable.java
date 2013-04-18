package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class FilterTable extends  JTable {

	/** the table model used to represnt data in the table */
	private FilterTableModel tableModel;
	
	public FilterTable(FilterTableModel tableModel) {
		super(tableModel);
		this.tableModel = tableModel;		
	}
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		
		if (!tableModel.getFilterAt(row).isActive()) {
			c.setBackground(Color.LIGHT_GRAY);
		}
		else {
			c.setBackground(Color.WHITE);
		}

		return c;
	}
}
