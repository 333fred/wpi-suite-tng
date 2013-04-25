/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Class with a single method to generate, add, and render a panel for a given
 * Event.
 * 
 */
public class EventCellRenderer implements ListCellRenderer, TableCellRenderer {
	
	private int wrapWidth;
	
	/**
	 * Method to create and add a panel to display and paint a specified value.
	 * 
	 * @list currently of no use here
	 * @value the object of display, must be of type Event
	 * @index currently of no use here
	 * @isSelected currently of no use here
	 * @cellHasFocus currently of no use here
	 */
	
	@Override
	public Component getListCellRendererComponent(final JList list,
			final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		final JPanel panel;
		panel = new EventPanel((Event) value);
		
		if (isSelected) {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createCompoundBorder(
							BorderFactory.createEmptyBorder(5, 0, 5, 0),
							BorderFactory.createLineBorder(Color.black, 3)),
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		} else {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createCompoundBorder(
							BorderFactory.createEmptyBorder(5, 0, 5, 0),
							BorderFactory.createLineBorder(Color.black, 1)),
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		}
		
		return panel;
	}
	
	@Override
	public Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int column) {
		
		final JPanel panel;
		panel = new EventPanel((Event) value, table.getSize().width);
		if (isSelected) {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createCompoundBorder(
							BorderFactory.createEmptyBorder(5, 0, 5, 0),
							BorderFactory.createLineBorder(Color.black, 3)),
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		} else {
			panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
					.createCompoundBorder(
							BorderFactory.createEmptyBorder(5, 0, 5, 0),
							BorderFactory.createLineBorder(Color.black, 1)),
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		}
		
		return panel;
	}
}
