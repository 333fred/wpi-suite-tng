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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.PermissionsPanel;

public class PermissionSelectionChangedListener implements
		ListSelectionListener {

	PermissionsPanel panel;

	public PermissionSelectionChangedListener(PermissionsPanel panel) {
		this.panel = panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		for (PermissionModel m : panel.getLocalDatabase()) {
			if (panel.getUserList().getSelectedValue()
					.equals(m.getUser().getName()))
				panel.setSelectedButtons(m.getPermLevel());
		}
	}
}
