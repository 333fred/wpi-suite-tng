/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Alex Gorowara
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/**
 * Action invoked upon use of the User Manual key
 * 
 * Action, default mnemonic key is F1.
 * 
 * @author Nicholas Massa
 */
@SuppressWarnings("serial")
public class CreateHelpPanelAction extends AbstractAction {

	private final MainTabController controller;

	/**
	 * Create a CreateHelpPanelAction
	 * 
	 * @param controller
	 *            When the action is performed, controller.addCreateDefectTab()
	 *            is called
	 */
	public CreateHelpPanelAction(MainTabController controller) {
		super("User Manual");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_F1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//File input = new File("http://dastardlybanana.com/Docs/RequirementsManager/"); // Html file is our input
		URI input = null;
		try {
			input = new URI("http://dastardlybanana.com/Docs/RequirementsManager/");
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Create a desktop type in order to launch the user's default browser
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) { // If
																				// desktop
																				// was
																				// created
																				// and
																				// a
																				// browser
																				// is
																				// supported
			try {
				desktop.browse(input); // Convert link to identifier and launch
										// default browser
			} catch (Exception f) {
				System.out.println("Error launching browser!");
				f.printStackTrace();
			}
		}
	}

}
