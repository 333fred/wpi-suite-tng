/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		@author Alex Gorowara
 ********************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.toolbar;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.Action;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * Action invoked upon use of the User Manual key
 * 
 * Action, default mnemonic key is F1.
 * 
 * @author Nicholas Massa
 */
@SuppressWarnings ("serial")
public class CreateHelpPanelAction extends AbstractAction {
	
	private final MainTabController controller;
	
	/**
	 * Create a CreateHelpPanelAction
	 * 
	 * @param controller
	 *            When the action is performed, controller.addCreateDefectTab()
	 *            is called
	 */
	public CreateHelpPanelAction(final MainTabController controller) {
		super("User Manual");
		this.controller = controller;
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F1);
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		// file is our input
		// go to current url + Documentation/
		String docURI = Network.getInstance().getDefaultNetworkConfiguration()
				.getApiUrl();
		docURI = docURI.substring(0, docURI.length() - 3) + "Documentation/";
		URI input = null;
		try {
			input = new URI(docURI);
		} catch (final URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		// Create a desktop type in order to launch the user's default browser
		final Desktop desktop = Desktop.isDesktopSupported() ? Desktop
				.getDesktop() : null;
		// If desktop was created and a browser is supported
		if ((desktop != null) && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(input); // Convert link to identifier and launch
										// default browser
			} catch (final Exception f) {
				System.out.println("Error launching browser!");
				f.printStackTrace();
			}
		}
	}
	
}
