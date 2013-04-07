/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    spkordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * A panel containing a form for adding a new note to a requirement
 * @author spkordell
 */
@SuppressWarnings("serial")
public class MakeNotePanel extends JPanel{

	private final JTextArea noteField;
	private final JButton addnote;


	//private final JButton deleteNote;
	private final JLabel addnoteLabel;

	private static final int VERTICAL_PADDING = 5;
	private static final int note_FIELD_HEIGHT = 50;
	
	/**
	 * Construct the panel, add and layout components.
	 * @param model the requirement to which a notes made with this class will be saved
	 * @param parentView the view of the requirement in question
	 */
	public MakeNotePanel(Requirement model, DetailPanel parentView) {
		noteField = new JTextArea();
		noteField.setLineWrap(true);
		noteField.setWrapStyleWord(true);
		noteField.setBorder((new JTextField()).getBorder());
		
		noteField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						noteField.transferFocus();
					}
					else {
						noteField.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				 	noteField.append("\n");
					event.consume();
				}
			}
		});

		addnote = new JButton("Add note");
		addnoteLabel = new JLabel("Add a new note:");
		
		//deleteNote = new JButton("Delete note");
		
		addnote.setAction(new SaveNoteAction(new SaveNoteController(this, model, parentView)));
		
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		final JScrollPane noteFieldPane = new JScrollPane(noteField);
		
		layout.putConstraint(SpringLayout.NORTH, addnoteLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, addnoteLabel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, noteFieldPane, VERTICAL_PADDING, SpringLayout.SOUTH, addnoteLabel);
		layout.putConstraint(SpringLayout.WEST, noteFieldPane, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, noteFieldPane, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, noteFieldPane, note_FIELD_HEIGHT, SpringLayout.NORTH, noteFieldPane);
		layout.putConstraint(SpringLayout.NORTH, addnote, VERTICAL_PADDING, SpringLayout.SOUTH, noteFieldPane);
		layout.putConstraint(SpringLayout.EAST, addnote, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, this, VERTICAL_PADDING, SpringLayout.SOUTH, addnote);
				
		this.add(addnoteLabel);
		this.add(noteFieldPane);
		this.add(addnote);
	}
	
	/**
	 * A function to the get the text area
	 * @return the note JTextArea
	 */
	public JTextArea getnoteField() {
		return noteField;
	}
	
	/**
	 * @return the addnote
	 */
	public JButton getAddnote() {
		return addnote;
	}	
		
	/**
	 * Enables and disables input on this panel.
	 * @param value if value is true, input is enabled, otherwise input is disabled.
	 */
	public void setInputEnabled(boolean value) {
		noteField.setEnabled(value);
		addnote.setEnabled(value);
		if (value) {
			addnoteLabel.setForeground(Color.black);
		}
		else {
			addnoteLabel.setForeground(Color.gray);
		}
	}	
}
