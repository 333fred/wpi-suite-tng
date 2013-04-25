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

public class CreateFilterViewListener implements KeyListener,
		PropertyChangeListener {
	
	public enum Type {
		TEXT, OTHER
	}
	
	/** The iteration view this listener is in */
	private final CreateFilterView createFilterView;
	
	/** set to false after the first key press, used to stop the blinking */
	private final boolean first;
	
	/**
	 * Creates a new IterationView Listener with the given view and component
	 * 
	 * @param iterationView
	 * @param component
	 */
	
	public CreateFilterViewListener(final CreateFilterView createFilterView) {
		this.createFilterView = createFilterView;
		first = true;
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
		// update();
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
		// update();
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
