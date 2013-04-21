/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Alex Woodyard
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.JTable;

public class PermissionsTable extends JTable {
/**
 * 
 * @param rowData The data to populate the table
 * @param columnNames The column headers
 */
	public PermissionsTable(String[][] rowData, String[] columnNames) {
		super(rowData, columnNames);
	}
/**
 * Override superclass to make cells uneditable
 */
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
