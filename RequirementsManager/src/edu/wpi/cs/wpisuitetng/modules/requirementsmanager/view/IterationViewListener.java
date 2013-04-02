/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse, Matt Costi
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

/** 
 * IterationViewListener that is used to determine when a change has been made to a component, TextField, or a JCalendar
 */
<<<<<<< HEAD
public class IterationViewListener implements KeyListener, MouseListener {
=======

public class IterationViewListener implements KeyListener, PropertyChangeListener {
>>>>>>> 6df9a4559c994a69fa3cfc38a1e87c0782137fb2

	
	public enum Type {
		TEXT,
		OTHER
	}
	/** The iteration view this listener is in */
	private IterationView iterationView;
	
	/** The component this listener is listening on */
	private JComponent component;
	
	/** set to false after the first key press, used to stop the blinking */
	private boolean first;
	
	/** Creates a new IterationView Listener with the given view and component
	 * 
	 * @param iterationView
	 * @param component
	 */
	
	public IterationViewListener(IterationView iterationView, JComponent component) {
		this.iterationView = iterationView;
		this.component = component;
		first = true;
	}
	
	/** Calls the appropriate update function in IterationView
	 * 
	 * 
	 */
	
	private void update() {
		iterationView.updateSave(component);
	}
	
	/** Triggers an update when a key is released in a field, seems to be working good
	 * 
	 */
	
	public void keyReleased(KeyEvent e) {
		update();		
	}
	
	/** Property CHange Listener method, used to trigger an update when a change is made to the JCalendar
	 * 
	 */
	public void propertyChange(PropertyChangeEvent e) {
		update();	
		System.out.println("Property Change fired");
	}
	
	

	
	public void keyPressed(KeyEvent e) {
		//update();		
	}

	
	public void keyTyped(KeyEvent e) {
		//update();		
	}

}
