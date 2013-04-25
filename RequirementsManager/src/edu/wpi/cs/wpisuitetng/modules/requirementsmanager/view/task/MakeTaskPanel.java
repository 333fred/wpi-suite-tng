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
 *    Matt C
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.task;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentNumberAndSizeFilter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentSizeFilter;

/**
 * A panel containing a a creation and edit form
 * for the tasks in a requirement
 * 
 * @author Nick M, Matt C
 */
@SuppressWarnings ("serial")
public class MakeTaskPanel extends JPanel {
	
	// The fields to make a task
	private final JTextArea taskName;
	private final JTextArea taskDescription;
	private final JButton addTask;
	private final JLabel addTaskLabel;
	private final JCheckBox taskComplete;
	private final JComboBox userAssigned;
	
	// Jlabels for task fields
	private final JLabel taskStatus;
	private final JLabel nameTaskLabel;
	private final JLabel descTaskLabel;
	private final JLabel userAssignedLabel;
	
	private final JLabel estimateLabel;
	private final JTextField taskEstimate;
	
	private static final int VERTICAL_PADDING = 5;
	private static final int note_FIELD_HEIGHT = 50;
	private final JScrollPane taskFieldPane;
	
	/**
	 * Construct the panel, add and layout components.
	 * 
	 * @param model
	 *            the requirement to which a notes made with this class will be
	 *            saved
	 * @param parentView
	 *            the view of the requirement in question
	 */
	public MakeTaskPanel(final Requirement model, final DetailPanel parentView) {
		
		// setup the task name field
		taskName = new JTextArea(1, 40);
		taskName.setLineWrap(true);
		taskName.setWrapStyleWord(true);
		taskName.setMaximumSize(new Dimension(40, 1));
		final AbstractDocument textNameDoc = (AbstractDocument) taskName
				.getDocument();
		textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		taskName.setBorder((new JTextField()).getBorder());
		taskName.setName("Name");
		taskName.setDisabledTextColor(Color.GRAY);
		
		taskName.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						taskName.transferFocus();
					} else {
						taskName.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume();
				}
			}
		});
		
		// setup the task description field
		taskDescription = new JTextArea();
		taskDescription.setLineWrap(true);
		taskDescription.setWrapStyleWord(true);
		taskDescription.setDisabledTextColor(Color.GRAY);
		
		taskDescription.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						taskDescription.transferFocus();
					} else {
						taskDescription.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					taskDescription.append("\n");
					event.consume();
				}
			}
		});
		
		estimateLabel = new JLabel("Estimate:");
		
		taskEstimate = new JTextField(9);
		taskEstimate.setBorder((new JTextField()).getBorder());
		taskEstimate.setMaximumSize(taskEstimate.getPreferredSize());
		taskEstimate.setName("Estimate");
		taskEstimate.setDisabledTextColor(Color.GRAY);
		final AbstractDocument textEstimateDoc = (AbstractDocument) taskEstimate
				.getDocument();
		textEstimateDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(12));
		
		// setup all the buttons and label text
		addTask = new JButton("Save");
		taskStatus = new JLabel(
				"No tasks selected. Fill name and description to create a new one.");
		addTaskLabel = new JLabel("Task:");
		nameTaskLabel = new JLabel("Name:");
		descTaskLabel = new JLabel("Description:");
		userAssignedLabel = new JLabel("Users:");
		taskComplete = new JCheckBox("Completed");
		userAssigned = new JComboBox();
		userAssigned.setBackground(Color.white);
		
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		final SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		taskFieldPane = new JScrollPane(taskDescription);
		
		// Setup the layout of the task Panel
		layout.putConstraint(SpringLayout.NORTH, addTaskLabel, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, addTaskLabel, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, taskStatus, 0,
				SpringLayout.SOUTH, addTaskLabel);
		layout.putConstraint(SpringLayout.NORTH, nameTaskLabel,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH, taskStatus);
		layout.putConstraint(SpringLayout.NORTH, taskName,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				nameTaskLabel);
		layout.putConstraint(SpringLayout.WEST, taskName, 0, SpringLayout.WEST,
				nameTaskLabel);
		layout.putConstraint(SpringLayout.EAST, taskName, 0, SpringLayout.EAST,
				this);
		layout.putConstraint(SpringLayout.NORTH, descTaskLabel,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH, taskName);
		layout.putConstraint(SpringLayout.NORTH, taskFieldPane,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				descTaskLabel);
		layout.putConstraint(SpringLayout.WEST, taskFieldPane, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, taskFieldPane, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, taskFieldPane,
				MakeTaskPanel.note_FIELD_HEIGHT, SpringLayout.NORTH,
				taskFieldPane);
		
		layout.putConstraint(SpringLayout.NORTH, taskComplete,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				taskFieldPane);
		layout.putConstraint(SpringLayout.WEST, taskComplete, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, this,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				taskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, userAssignedLabel,
				MakeTaskPanel.VERTICAL_PADDING + 5, SpringLayout.SOUTH,
				taskComplete);
		layout.putConstraint(SpringLayout.WEST, userAssignedLabel, 4,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, userAssignedLabel, 0,
				SpringLayout.EAST, taskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, userAssigned,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				taskComplete);
		layout.putConstraint(SpringLayout.WEST, userAssigned, 60,
				SpringLayout.WEST, userAssignedLabel);
		layout.putConstraint(SpringLayout.EAST, userAssigned, 60,
				SpringLayout.EAST, taskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, estimateLabel,
				MakeTaskPanel.VERTICAL_PADDING + 5, SpringLayout.SOUTH,
				userAssignedLabel);
		layout.putConstraint(SpringLayout.WEST, estimateLabel, 4,
				SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, taskEstimate,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				userAssigned);
		layout.putConstraint(SpringLayout.WEST, taskEstimate, 60,
				SpringLayout.WEST, estimateLabel);
		layout.putConstraint(SpringLayout.EAST, taskEstimate, 60,
				SpringLayout.EAST, taskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, addTask,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				userAssignedLabel);
		layout.putConstraint(SpringLayout.EAST, addTask, 0, SpringLayout.EAST,
				this);
		layout.putConstraint(SpringLayout.SOUTH, this,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH, addTask);
		
		// add all of the swing components to the this task panel
		this.add(taskEstimate);
		this.add(estimateLabel);
		this.add(taskStatus);
		this.add(userAssignedLabel);
		this.add(userAssigned);
		this.add(addTaskLabel);
		this.add(addTask);
		this.add(taskName);
		this.add(nameTaskLabel);
		this.add(descTaskLabel);
		this.add(taskComplete);
		this.add(taskFieldPane);
		
		// default the add button and complete checkbox
		// to un-enabled
		addTask.setEnabled(false);
		taskComplete.setEnabled(false);
		
	}
	
	public JButton getAddTask() {
		return addTask;
	}
	
	public JTextField getEstimate() {
		return taskEstimate;
	}
	
	public JCheckBox getTaskComplete() {
		return taskComplete;
	}
	
	/**
	 * A function to the get the text area
	 * 
	 * @return the note JTextArea
	 */
	public JTextArea getTaskField() {
		return taskDescription;
	}
	
	public JScrollPane getTaskFieldPane() {
		return taskFieldPane;
	}
	
	public JTextComponent getTaskName() {
		return taskName;
	}
	
	public JLabel getTaskStatus() {
		return taskStatus;
	}
	
	public JComboBox getUserAssigned() {
		return userAssigned;
	}
	
	/**
	 * Enables and disables input on this panel.
	 * 
	 * @param value
	 *            if value is true, input is enabled, otherwise input is
	 *            disabled.
	 */
	public void setInputEnabled(final boolean value) {
		taskName.setEnabled(false);
		taskDescription.setEnabled(value);
		addTask.setEnabled(value);
		userAssigned.setEnabled(value);
		taskEstimate.setEnabled(value);
		if (value) {
			addTaskLabel.setForeground(Color.black);
		} else {
			addTaskLabel.setForeground(Color.gray);
		}
	}
	
}
