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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

public class CreatePermissionPanelAction extends AbstractAction {
	
	private final MainTabController controller;
	
	public CreatePermissionPanelAction(MainTabController controller){
		super("Edit Permissions");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addPermissionTab();		
	}

}
