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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.UserPermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SavePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.PermissionsPanel;

public class SavePermissionsAction extends AbstractAction {

	PermissionModel model;
	PermissionsPanel panel;

	public SavePermissionsAction(PermissionsPanel panel, PermissionModel model) {
		this.model = model;
		this.panel = panel;
	}

	public void actionPerformed(ActionEvent e) {
		PermissionModelController controller = new PermissionModelController();
		SavePermissionRequestObserver observer = new SavePermissionRequestObserver();
		
		model.setPermission(panel.getPermission());
		
		controller.save(model, observer);
	}

}
