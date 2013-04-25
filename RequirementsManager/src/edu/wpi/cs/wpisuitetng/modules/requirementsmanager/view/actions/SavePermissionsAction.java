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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.PermissionModelController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.PermissionModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.SavePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.PermissionsPanel;

/**
 * An action impementation to handle saving the permissions designated by an
 * admin in the PermissionsPanel
 */
@SuppressWarnings ("serial")
public class SavePermissionsAction extends AbstractAction {
	
	PermissionModel model;
	PermissionsPanel panel;
	
	/**
	 * Creates a new save permission action with the given panel and model
	 * 
	 * @param panel
	 *            the permission panel for this action
	 * @param model
	 *            the model to save
	 */
	public SavePermissionsAction(final PermissionsPanel panel,
			final PermissionModel model) {
		super("Save Permission");
		this.model = model;
		this.panel = panel;
	}
	
	/**
	 * Saves the currently selected permission in the manger
	 */
	@Override
	public void actionPerformed(final ActionEvent e1) {
		final PermissionModelController controller = new PermissionModelController();
		final SavePermissionRequestObserver observer = new SavePermissionRequestObserver();
		
		model.setPermLevel(panel.getPermission());
		
		controller.save(model, observer);
	}
	
}
