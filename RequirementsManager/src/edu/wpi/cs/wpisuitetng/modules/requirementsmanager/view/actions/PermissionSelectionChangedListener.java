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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.PermissionsPanel;

/**
 * Need to comment!
 */
public class PermissionSelectionChangedListener implements MouseListener {
	
	PermissionsPanel panel;
	
	/**
	 * Creates a new listener for PermissionSelections.
	 * 
	 * @param panel
	 *            the permission panel to listen on
	 */
	public PermissionSelectionChangedListener(final PermissionsPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseClicked(final MouseEvent e) {
		// unused
	}
	
	/**
	 * Unused method
	 */
	@Override
	public void mouseEntered(final MouseEvent e) {
		// unused
	}
	
	/**
	 * Unused method
	 */
	@Override
	public void mouseExited(final MouseEvent e) {
		// unused
	}
	
	/**
	 * Unused method
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		// unused
	}
	
	/**
	 * Update the radio buttons when the mouse button is released
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
		final JTable target = (JTable) e.getSource();
		final int row = target.getSelectedRow();
		for (int i = 0; i < panel.getLocalDatabase().size(); i++) {
			if (panel
					.getUserList()
					.getValueAt(row, 0)
					.equals(panel.getLocalDatabase().get(i).getUser().getName())) {
				panel.setSelectedButtons(panel.getLocalDatabase().get(i)
						.getPermLevel());
			}
		}
	}
	
	/**
	 * Called when the value of a permission is changed, sets up the the button
	 * values
	 * 
	 * @param e
	 *            the event that triggered this event
	 */
	public void valueChanged(final ListSelectionEvent e) {
		// iterate through the local database, check the name being equal as the
		// name selected in the table, set the radio buttons appropriately
		for (int i = 0; i < panel.getLocalDatabase().size(); i++) {
			if (panel
					.getUserList()
					.getValueAt(i, 0)
					.equals(panel.getLocalDatabase().get(i).getUser().getName())) {
				panel.setSelectedButtons(panel.getLocalDatabase().get(i)
						.getPermLevel());
			}
		}
	}
}
