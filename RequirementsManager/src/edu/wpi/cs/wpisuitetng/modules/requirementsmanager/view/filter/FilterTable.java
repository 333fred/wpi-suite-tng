/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class FilterTable extends JTable {
	
	/** the table model used to represnt data in the table */
	private final FilterTableModel tableModel;
	
	public FilterTable(final FilterTableModel tableModel) {
		super(tableModel);
		this.tableModel = tableModel;
	}
	
	@Override
	public Component prepareRenderer(final TableCellRenderer renderer,
			final int row, final int column) {
		final Component c = super.prepareRenderer(renderer, row, column);
		
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
	 * @Override
	 * public void changeSelection(int rowIndex, int columnIndex, boolean
	 * toggle, boolean extend) {
	 * super.changeSelection(rowIndex,columnIndex, true, false);
	 * }
	 */
}
