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

import java.awt.Color;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SavePermissionRequestObserver;

public class PermissionsTable extends JTable {
	
	private static final long serialVersionUID = 1L;
	/** List of locally stored permissions */
	private final List<PermissionModel> localPermissions;
	
	/**
	 * 
	 * @param rowData
	 *            The data to populate the table
	 * @param columnNames
	 *            The column headers
	 */
	public PermissionsTable(final String[][] rowData,
			final String[] columnNames,
			final List<PermissionModel> localPermissions) {
		super(rowData, columnNames);
		this.localPermissions = localPermissions;
	}
	
	/**
	 * Override superclass to make permission cell editor be a combobox
	 */
	@Override
	public TableCellEditor getCellEditor(final int row, final int column) {
		if (convertColumnIndexToModel(column) == 2) {
			// Create the combo box
			final String[] items1 = { "Admin", "Update", "Observe" };
			final JComboBox comboBox1 = new JComboBox(items1);
			// select the correct value
			comboBox1.setSelectedItem(getValueAt(row, column));
			final DefaultCellEditor dce1 = new DefaultCellEditor(comboBox1);
			return dce1;
		} else {
			return super.getCellEditor(row, column);
		}
	}
	
	/**
	 * Non-editable rows should be gray
	 */
	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		if (!isCellEditable(row, convertColumnIndexToView(2))) {
			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setBackground(Color.lightGray);
			return renderer;
		} else {
			return super.getCellRenderer(row, column);
		}
	}
	
	/**
	 * Override superclass to make permission cells editable
	 */
	@Override
	public boolean isCellEditable(final int row, final int column) {
		if (!localPermissions.get(convertRowIndexToModel(row)).getUser()
				.getUsername()
				.equals(PermissionModel.getInstance().getUser().getUsername())
				&& !localPermissions.get(convertRowIndexToModel(row)).getUser()
						.getUsername().equals("admin")) {
			return convertColumnIndexToModel(column) == 2;
		} else {
			return false;
		}
	}
	
	/**
	 * Sets the updated value into the table, and also updates the permission on
	 * the server. This will occur whenever a change is completed
	 */
	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		super.setValueAt(value, row, col);
		if (convertColumnIndexToModel(col) == 2) {
			final PermissionModelController controller = new PermissionModelController();
			final SavePermissionRequestObserver observer = new SavePermissionRequestObserver();
			
			PermissionModel model;
			// we get the model from our local list, set its permissions, and
			// save it
			model = localPermissions.get(row);
			model.setPermLevel(UserPermissionLevel.valueOf(((String) value)
					.toUpperCase()));
			controller.save(model, observer);
		}
	}
}
