/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick M
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.text.AbstractDocument;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DocumentSizeFilter;

/**
 * A panel containing a form for adding a new note to a requirement
 * @author spkordell
 */
@SuppressWarnings("serial")
public class MakeTaskPanel extends JPanel{

	private JTextArea taskName;
	private final JTextArea taskField;
	private final JButton addtask;
	//private final JButton deleteNote;
	private final JLabel addTaskLabel;
	
	private final JLabel nameTaskLabel;
	private final JLabel descTaskLabel;

	private static final int VERTICAL_PADDING = 5;
	private static final int note_FIELD_HEIGHT = 50;
	
	/**
	 * Construct the panel, add and layout components.
	 * @param model the requirement to which a notes made with this class will be saved
	 * @param parentView the view of the requirement in question
	 */
	public MakeTaskPanel(Requirement model, DetailPanel parentView) {
		
		
		taskName = new JTextArea(1, 40);
		taskName.setLineWrap(true);
		taskName.setWrapStyleWord(true);
		taskName.setMaximumSize(new Dimension(40, 2));
		//AbstractDocument textNameDoc = (AbstractDocument) taskName.getDocument();
		//textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		taskName.setBorder((new JTextField()).getBorder());
		taskName.setName("Name");
		taskName.setDisabledTextColor(Color.GRAY);
		
		taskField = new JTextArea();
		taskField.setLineWrap(true);
		taskField.setWrapStyleWord(true);
		taskField.setBorder((new JTextField()).getBorder());
		
		taskField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						taskField.transferFocus();
					}
					else {
						taskField.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				 	taskField.append("\n");
				 	/*
					if (event.isShiftDown()) {
						// user is doing a shift + enter
						taskField.append("\n");
					}
					else {
						// save the note
						addtask.doClick(0);
					}*/
					event.consume();
				}
			}
		});

		addtask = new JButton("Save");
		addTaskLabel = new JLabel("Task:");
		nameTaskLabel = new JLabel("Name:");
		descTaskLabel = new JLabel("Description:");
		
		
		//deleteNote = new JButton("Delete note");
		
		//addtask.setAction(new SaveNoteAction(new SaveNoteController(this, model, parentView)));
		
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, 1), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		final JScrollPane taskFieldPane = new JScrollPane(taskField);
		
		layout.putConstraint(SpringLayout.NORTH, addTaskLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, addTaskLabel, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, nameTaskLabel, VERTICAL_PADDING, SpringLayout.SOUTH, addTaskLabel);
		layout.putConstraint(SpringLayout.NORTH, taskName, VERTICAL_PADDING, SpringLayout.SOUTH, nameTaskLabel);
		layout.putConstraint(SpringLayout.WEST, taskName, 0, SpringLayout.WEST, nameTaskLabel);
		layout.putConstraint(SpringLayout.EAST, taskName, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, descTaskLabel, VERTICAL_PADDING, SpringLayout.SOUTH, taskName);
		layout.putConstraint(SpringLayout.NORTH, taskFieldPane, VERTICAL_PADDING, SpringLayout.SOUTH, descTaskLabel);
		layout.putConstraint(SpringLayout.WEST, taskFieldPane, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, taskFieldPane, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, taskFieldPane, note_FIELD_HEIGHT, SpringLayout.NORTH, taskFieldPane);
		layout.putConstraint(SpringLayout.NORTH, addtask, VERTICAL_PADDING, SpringLayout.SOUTH, taskFieldPane);
		layout.putConstraint(SpringLayout.EAST, addtask, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, this, VERTICAL_PADDING, SpringLayout.SOUTH, addtask);
		
				
		this.add(addTaskLabel);		
		this.add(addtask);
		this.add(taskName);
		this.add(nameTaskLabel);
		this.add(descTaskLabel);
		this.add(taskFieldPane);
		
	}
	
	/**
	 * A function to the get the text area
	 * @return the note JTextArea
	 */
	public JTextArea gettaskField() {
		return taskField;
	}
		
	/**
	 * Enables and disables input on this panel.
	 * @param value if value is true, input is enabled, otherwise input is disabled.
	 */
	public void setInputEnabled(boolean value) {
		taskField.setEnabled(value);
		addtask.setEnabled(value);
		if (value) {
			addTaskLabel.setForeground(Color.black);
		}
		else {
			addTaskLabel.setForeground(Color.gray);
		}
	}	
}
