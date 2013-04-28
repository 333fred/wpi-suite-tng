/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.DetailNoteView;

/**
 * Listener to monitor changes in the note view
 */

public class NoteViewListener implements KeyListener {
	
	protected DetailNoteView panel;
	protected JTextComponent component;
	
	/**
	 * Creates a new listener for the given note view and component
	 * 
	 * @param panel
	 *            the NoteView to listen on
	 * @param component
	 *            the component to listen on
	 */
	public NoteViewListener(final DetailNoteView panel,
			final JTextComponent component) {
		this.panel = panel;
		this.component = component;
	}
	
	@Override
	public void keyPressed(final KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void keyReleased(final KeyEvent e) {
		if (component.getText().trim().equals("")) {
			panel.disableAddNote();
		} else {
			panel.enableAddNote();
		}
	}
	
	@Override
	public void keyTyped(final KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
