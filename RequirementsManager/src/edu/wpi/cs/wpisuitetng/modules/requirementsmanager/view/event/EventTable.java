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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

public class EventTable extends JTable implements TableColumnModelListener,
		TableModelListener {
	
	/** the table model for this */
	private final EventTableModel tableModel;
	
	public EventTable(final EventTableModel tableModel) {
		super(tableModel);
		this.tableModel = tableModel;
	}
	
	@Override
	public void columnMarginChanged(final ChangeEvent e) {
		updateRowHeights();
	}
	
	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		return new EventCellRenderer();
	}
	
	@Override
	public void tableChanged(final TableModelEvent e) {
		super.tableChanged(e);
		updateRowHeights();
	}
	
	private void updateRowHeights() {
		try {
			for (int row = 0; row < getRowCount(); row++) {
				int rowHeight = getRowHeight();
				for (int column = 0; column < getColumnCount(); column++) {
					final Component comp = prepareRenderer(
							getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
				}
				
				setRowHeight(row, rowHeight);
			}
		} catch (final ClassCastException e) {
			System.out.println("class cast exception in event table");
		}
	}
}
