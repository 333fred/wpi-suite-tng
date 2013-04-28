/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

/**
 * A table that handles holding all of the events
 */
@SuppressWarnings ("serial")
public class EventTable extends JTable implements TableColumnModelListener,
		TableModelListener {
	
	/**
	 * Creates a new table with the given model
	 * 
	 * @param tableModel
	 *            the model to hold
	 */
	public EventTable(final EventTableModel tableModel) {
		super(tableModel);
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
				int finalRowHeight = getRowHeight();
				for (int column = 0; column < getColumnCount(); column++) {
					final Component comp = prepareRenderer(
							getCellRenderer(row, column), row, column);
					finalRowHeight = Math.max(finalRowHeight,
							comp.getPreferredSize().height);
				}
				
				setRowHeight(row, finalRowHeight);
			}
		} catch (final ClassCastException e) {
			System.out.println("class cast exception in event table");
		}
	}
}
