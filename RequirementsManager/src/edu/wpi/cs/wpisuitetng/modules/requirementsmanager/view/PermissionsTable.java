/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @authors Jason Whitehouse, Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;

public class PermissionsTable extends JTable {
	/**
	 * 
	 * @param rowData
	 *            The data to populate the table
	 * @param columnNames
	 *            The column headers
	 */
	public PermissionsTable(String[][] rowData, String[] columnNames) {
		super(rowData, columnNames);
	}

	/**
	 * Override superclass to make permission cells editable
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return super.convertColumnIndexToModel(column) == 1;
	}
	
	
	/**
	 * Override superclass to make permission cell editor be a combobox
	 */
    public TableCellEditor getCellEditor(int row, int column)
    {
		if (convertColumnIndexToModel(column) == 1) {
			//Create the combo box
			String[] items1 = { "ADMIN", "UPDATE", "NONE" };
			JComboBox comboBox1 = new JComboBox(items1);
			//select the correct value
			comboBox1.setSelectedItem(getValueAt(row, column));
			DefaultCellEditor dce1 = new DefaultCellEditor(comboBox1);
			return dce1;
		} else {
			return super.getCellEditor(row, column);
		}
    }
    
   // @Override
	//public void setValueAt(Object value, int row, int col) {

}
