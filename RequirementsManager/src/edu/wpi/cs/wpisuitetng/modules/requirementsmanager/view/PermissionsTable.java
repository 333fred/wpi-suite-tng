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

import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.PermissionsNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.PermissionsDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SavePermissionRequestObserver;

public class PermissionsTable extends JTable {
	
	/** List of locally stored permissions */
	private List<PermissionModel> localPermissions;
	
	/**
	 * 
	 * @param rowData
	 *            The data to populate the table
	 * @param columnNames
	 *            The column headers
	 */
	public PermissionsTable(String[][] rowData, String[] columnNames, List<PermissionModel> localPermissions) {
		super(rowData, columnNames);
		this.localPermissions = localPermissions;
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
    
    /**
     * Sets the updated value into the table, and also updates the permission on
     * the server. This will occur whenever a change is completed
     */
    @Override
	public void setValueAt(Object value, int row, int col) {
    	super.setValueAt(value, row, col);
		if (convertColumnIndexToModel(col) == 1) {
			PermissionModelController controller = new PermissionModelController();
			SavePermissionRequestObserver observer = new SavePermissionRequestObserver();
			
			PermissionModel model;
			System.out.println("Permissions changed in table at:" + row);
			//we get the model from our local list, set its permissions, and save it
			model = localPermissions.get(row);
			model.setPermLevel(UserPermissionLevel.valueOf((String) value));
			controller.save(model, observer);
		}
    }

}
