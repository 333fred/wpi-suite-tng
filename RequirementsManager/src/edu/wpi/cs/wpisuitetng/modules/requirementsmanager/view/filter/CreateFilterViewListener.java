/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	Mitchell Caisse
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Listener for all actions performed on the CreateFilterView
 */

public class CreateFilterViewListener implements KeyListener,
		PropertyChangeListener {
	
	/** The iteration view this listener is in */
	private final CreateFilterView createFilterView;
	
	/**
	 * Creates a new IterationView Listener with the given view and component
	 * 
	 * @param createFilterView
	 *            The filter view to listen for changes on
	 */
	public CreateFilterViewListener(final CreateFilterView createFilterView) {
		this.createFilterView = createFilterView;
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
	 * Property CHange Listener method, used to trigger an update when a change
	 * is made to the JCalendar
	 * 
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent e) {
		update();
	}
	
	/**
	 * Calls the appropriate update function in IterationView
	 * 
	 * 
	 */
	
	private void update() {
		createFilterView.updateSave();
	}
	
}
