package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.DetailNoteView;

public class NoteViewListener implements KeyListener {
	protected DetailNoteView panel;
	protected JTextComponent component;

	public NoteViewListener(DetailNoteView panel, JTextComponent component) {
		this.panel = panel;
		this.component = component;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (component.getText().trim().equals("")) {
			panel.disableAddNote();
		} else {
			panel.enableAddNote();
		}
	}

}
