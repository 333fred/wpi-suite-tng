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

public class NoteViewListener implements KeyListener {
	
	protected DetailNoteView panel;
	protected JTextComponent component;
	
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
