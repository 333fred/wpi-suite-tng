/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	@author Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listener to make an aTest
 */
public class MakeATestListener implements KeyListener {
	
	/** the task panel this listens in */
	private final MakeATestPanel makeATestPanel;
	
	/**
	 * Creates a new MakeATest Listener with the MakeATestPanel
	 * 
	 * @param makeATestPanel
	 *            the MakeATestPanel to act on
	 */
	public MakeATestListener(final MakeATestPanel makeATestPanel) {
		this.makeATestPanel = makeATestPanel;
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
	}
	
	/**
	 * Triggers an update when a key is released in a field, seems to be working
	 * good
	 * 
	 */
	
	@Override
	public void keyReleased(final KeyEvent e) {
		update();
	}
	
	@Override
	public void keyTyped(final KeyEvent e) {
	}
	
	/**
	 * Calls the appropriate update function in MakeTask VIew
	 */
	
	private void update() {
		makeATestPanel.validateInput();
	}
	
}