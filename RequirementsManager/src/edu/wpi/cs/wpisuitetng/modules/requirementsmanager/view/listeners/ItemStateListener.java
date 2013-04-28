/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Chen
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * A listener for listening to detail panel changes.
 */
@SuppressWarnings ("rawtypes")
public class ItemStateListener implements ItemListener {
	
	protected final DetailPanel panel;
	protected final JComboBox component;
	
	/**
	 * Creates a new listener for the given detail panel and combobox
	 * 
	 * @param panel
	 *            the panel to listen on
	 * @param comboBoxType
	 *            the box to listen on
	 */
	public ItemStateListener(final DetailPanel panel,
			final JComboBox comboBoxType) {
		this.panel = panel;
		component = comboBoxType;
	}
	
	/**
	 * Checks to see if the box has been updated
	 * 
	 * @param e
	 *            the event that could cause an update
	 */
	public void checkIfUpdated(final ItemEvent e) {
		if ((panel.getTextName().getText().trim().length() > 0)
				&& (panel.getTextDescription().getText().trim().length() > 0)) {
			panel.enableSaveButton();
		}
	}
	
	@Override
	public void itemStateChanged(final ItemEvent e) {
		checkIfUpdated(e);
	}
}
