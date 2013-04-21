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

public class PermissionSelectionChangedListener implements MouseListener {

	PermissionsPanel panel;

	public PermissionSelectionChangedListener(PermissionsPanel panel) {
		this.panel = panel;
	}

	/**
	 * Unused method
	 */

	public void valueChanged(ListSelectionEvent e) {
		// iterate through the local database, check the name being equal as the
		// name selected in the table, set the radio buttons appropriately
		for (int i = 0; i < panel.getLocalDatabase().size(); i++) {
			if (panel
					.getUserList()
					.getValueAt(i, 0)
					.equals(panel.getLocalDatabase().get(i).getUser().getName()))
				panel.setSelectedButtons(panel.getLocalDatabase().get(i)
						.getPermLevel());
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
<<<<<<< HEAD
		// unused
=======
		// unsued
		return;
>>>>>>> 9319a6f923265dbd9df7c731c7d3ea2caa18dfe4
	}

	/**
	 * Unused method
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// unused
	}

	/**
	 * Update the radio buttons when the mouse button is released
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		JTable target = (JTable) e.getSource();
		int row = target.getSelectedRow();
		for (int i = 0; i < panel.getLocalDatabase().size(); i++) {
			if (panel
					.getUserList()
					.getValueAt(row, 0)
					.equals(panel.getLocalDatabase().get(i).getUser().getName()))
				panel.setSelectedButtons(panel.getLocalDatabase().get(i)
						.getPermLevel());
		}
	}

	/**
	 * Unused method
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// unused
	}

	/**
	 * Unused method
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// unused
	}
}
