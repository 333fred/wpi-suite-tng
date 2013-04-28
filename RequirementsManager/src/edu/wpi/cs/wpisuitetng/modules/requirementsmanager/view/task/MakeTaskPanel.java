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
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentNumberAndSizeFilter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentSizeFilter;

/**
 * A panel containing a a creation and edit form for the tasks in a requirement
 * 
 * @author Nick M, Matt C
 */
@SuppressWarnings ("serial")
public class MakeTaskPanel extends JPanel {
	
	// The fields to make a task
	private final JTextArea txtTaskName;
	private final JTextArea txtTaskDescription;
	private final JButton butSave;
	private final JButton butCancel;
	private final JLabel labAddTask;
	private final JCheckBox cbxTaskComplete;
	@SuppressWarnings ("rawtypes")
	private final JComboBox cbxUserAssigned;
	
	// Jlabels for task fields
	private final JLabel labTaskStatus;
	private final JLabel labNnameTask;
	private final JLabel labDescTask;
	private final JLabel labUserAssigned;
	private final JLabel labEstimate;
	
	/** Label for realtime validation */
	private final JLabel labSaveError;
	private final JTextField txtTaskEstimate;
	
	private static final int VERTICAL_PADDING = 5;
	private static final int note_FIELD_HEIGHT = 50;
	private final JScrollPane taskFieldPane;
	
	/** The requriement this task panel is in */
	private final Requirement requirement;
	
	/** Make task listener used for listening to hte text boxes for changes */
	private final MakeTaskListener makeTaskListener;
	
	/** The detail task view */
	private DetailTaskView taskView;
	
	/** id of the task being edited, -2 if none */
	private int taskId;
	
	/**
	 * Construct the panel, add and layout components.
	 * 
	 * @param requirement
	 *            The requirement to add tasks for
	 * @param parentView
	 *            The detail panel parent view
	 * @param taskView
	 *            the parent task view
	 */
	@SuppressWarnings ("rawtypes")
	public MakeTaskPanel(final Requirement requirement,
			final DetailPanel parentView, DetailTaskView taskView) {
		this.requirement = requirement;
		this.taskView = taskView;
		
		// setup the task name field
		txtTaskName = new JTextArea(1, 40);
		txtTaskName.setLineWrap(true);
		txtTaskName.setWrapStyleWord(true);
		txtTaskName.setMaximumSize(new Dimension(40, 1));
		final AbstractDocument textNameDoc = (AbstractDocument) txtTaskName
				.getDocument();
		textNameDoc.setDocumentFilter(new DocumentSizeFilter(100));
		txtTaskName.setBorder((new JTextField()).getBorder());
		txtTaskName.setName("Name");
		txtTaskName.setDisabledTextColor(Color.GRAY);
		
		txtTaskName.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						txtTaskName.transferFocus();
					} else {
						txtTaskName.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					event.consume();
				}
			}
		});
		
		// setup the task description field
		txtTaskDescription = new JTextArea();
		txtTaskDescription.setLineWrap(true);
		txtTaskDescription.setWrapStyleWord(true);
		txtTaskDescription.setDisabledTextColor(Color.GRAY);
		
		txtTaskDescription.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_TAB) {
					if (event.getModifiers() == 0) {
						txtTaskDescription.transferFocus();
					} else {
						txtTaskDescription.transferFocusBackward();
					}
					event.consume();
				}
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					txtTaskDescription.append("\n");
					event.consume();
				}
			}
		});
		
		labEstimate = new JLabel("*Estimate:");
		
		txtTaskEstimate = new JTextField(9);
		txtTaskEstimate.setBorder((new JTextField()).getBorder());
		txtTaskEstimate.setMaximumSize(txtTaskEstimate.getPreferredSize());
		txtTaskEstimate.setName("Estimate");
		txtTaskEstimate.setDisabledTextColor(Color.GRAY);
		txtTaskEstimate.setText("0");
		final AbstractDocument textEstimateDoc = (AbstractDocument) txtTaskEstimate
				.getDocument();
		textEstimateDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(12));
		
		// setup all the buttons and label text
		butSave = new JButton("Save");
		labTaskStatus = new JLabel(
				"No tasks selected. Fill name and description to create a new one.");
		labAddTask = new JLabel("Task:");
		labNnameTask = new JLabel("*Name:");
		labDescTask = new JLabel("*Description:");
		labUserAssigned = new JLabel("User:");
		cbxTaskComplete = new JCheckBox("Completed");
		cbxUserAssigned = new JComboBox();
		cbxUserAssigned.setBackground(Color.white);
		
		labSaveError = new JLabel();
		butCancel = new JButton("Cancel");
		
		butCancel.setAction(new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				onCancel();
				txtTaskDescription.setBackground(Color.white);
				txtTaskEstimate.setBackground(Color.white);
				txtTaskName.setBackground(Color.white);
			}
			
		});
		
		butCancel.setText("Cancel");
		butCancel.setEnabled(false);
		
		makeTaskListener = new MakeTaskListener(this);
		// add the key listeners
		txtTaskDescription.addKeyListener(makeTaskListener);
		txtTaskName.addKeyListener(makeTaskListener);
		txtTaskEstimate.addKeyListener(makeTaskListener);
		
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.black, 1),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		final SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		taskFieldPane = new JScrollPane(txtTaskDescription);
		
		// Setup the layout of the task Panel
		layout.putConstraint(SpringLayout.NORTH, labAddTask, 0,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labAddTask, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labTaskStatus, 0,
				SpringLayout.SOUTH, labAddTask);
		layout.putConstraint(SpringLayout.NORTH, labNnameTask,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				labTaskStatus);
		layout.putConstraint(SpringLayout.NORTH, txtTaskName,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				labNnameTask);
		layout.putConstraint(SpringLayout.WEST, txtTaskName, 0,
				SpringLayout.WEST, labNnameTask);
		layout.putConstraint(SpringLayout.EAST, txtTaskName, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, labDescTask,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH, txtTaskName);
		layout.putConstraint(SpringLayout.NORTH, taskFieldPane,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH, labDescTask);
		layout.putConstraint(SpringLayout.WEST, taskFieldPane, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, taskFieldPane, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, taskFieldPane,
				MakeTaskPanel.note_FIELD_HEIGHT, SpringLayout.NORTH,
				taskFieldPane);
		
		layout.putConstraint(SpringLayout.NORTH, cbxTaskComplete,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				taskFieldPane);
		layout.putConstraint(SpringLayout.WEST, cbxTaskComplete, 0,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, this,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				cbxTaskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, labUserAssigned,
				MakeTaskPanel.VERTICAL_PADDING + 5, SpringLayout.SOUTH,
				cbxTaskComplete);
		layout.putConstraint(SpringLayout.WEST, labUserAssigned, 4,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, labUserAssigned, 0,
				SpringLayout.EAST, cbxTaskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, cbxUserAssigned,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				cbxTaskComplete);
		layout.putConstraint(SpringLayout.WEST, cbxUserAssigned, 60,
				SpringLayout.WEST, labUserAssigned);
		layout.putConstraint(SpringLayout.EAST, cbxUserAssigned, 60,
				SpringLayout.EAST, cbxTaskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, labEstimate,
				MakeTaskPanel.VERTICAL_PADDING + 5, SpringLayout.SOUTH,
				labUserAssigned);
		layout.putConstraint(SpringLayout.WEST, labEstimate, 4,
				SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, txtTaskEstimate,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				cbxUserAssigned);
		layout.putConstraint(SpringLayout.WEST, txtTaskEstimate, 60,
				SpringLayout.WEST, labEstimate);
		layout.putConstraint(SpringLayout.EAST, txtTaskEstimate, 60,
				SpringLayout.EAST, cbxTaskComplete);
		
		layout.putConstraint(SpringLayout.NORTH, butCancel,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH,
				labUserAssigned);
		layout.putConstraint(SpringLayout.EAST, butCancel, 0,
				SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, this,
				MakeTaskPanel.VERTICAL_PADDING, SpringLayout.SOUTH, butCancel);
		
		layout.putConstraint(SpringLayout.NORTH, butSave, 0,
				SpringLayout.NORTH, butCancel);
		layout.putConstraint(SpringLayout.EAST, butSave, -4, SpringLayout.WEST,
				butCancel);
		
		layout.putConstraint(SpringLayout.SOUTH, labSaveError, -8,
				SpringLayout.NORTH, butSave);
		layout.putConstraint(SpringLayout.WEST, labSaveError, 12,
				SpringLayout.EAST, cbxUserAssigned);
		
		// add all of the swing components to the this task panel
		add(txtTaskEstimate);
		add(labEstimate);
		add(labTaskStatus);
		add(labUserAssigned);
		add(cbxUserAssigned);
		add(labAddTask);
		add(butSave);
		add(txtTaskName);
		add(labNnameTask);
		add(labDescTask);
		add(cbxTaskComplete);
		add(taskFieldPane);
		add(butCancel);
		
		add(labSaveError);
		// default the add button and complete checkbox
		// to un-enabled
		butSave.setEnabled(false);
		cbxTaskComplete.setEnabled(false);
		taskId = -2;
		
	}
	
	/**
	 * Gets the save button in the panel
	 * 
	 * @return the save button
	 */
	public JButton getAddTask() {
		return butSave;
	}
	
	/**
	 * Gets the task estimate text box
	 * 
	 * @return the text box
	 */
	public JTextField getEstimate() {
		return txtTaskEstimate;
	}
	
	/**
	 * Gets the task complete checkbox
	 * 
	 * @return the checkbox
	 */
	public JCheckBox getTaskComplete() {
		return cbxTaskComplete;
	}
	
	/**
	 * A function to the get the text area
	 * 
	 * @return the note JTextArea
	 */
	public JTextArea getTaskField() {
		return txtTaskDescription;
	}
	
	/**
	 * Gets the task display pane
	 * 
	 * @return the pane
	 */
	public JScrollPane getTaskFieldPane() {
		return taskFieldPane;
	}
	
	/**
	 * Gets the name text box
	 * 
	 * @return the text box
	 */
	public JTextComponent getTaskName() {
		return txtTaskName;
	}
	
	/**
	 * Gets the task status label
	 * 
	 * @return the label
	 */
	public JLabel getTaskStatus() {
		return labTaskStatus;
	}
	
	/**
	 * Gets the user assigned checkbox
	 * 
	 * @return the checkbox
	 */
	@SuppressWarnings ("rawtypes")
	public JComboBox getUserAssigned() {
		return cbxUserAssigned;
	}
	
	/**
	 * Enables and disables input on this panel.
	 * 
	 * @param value
	 *            if value is true, input is enabled, otherwise input is
	 *            disabled.
	 */
	public void setInputEnabled(final boolean value) {
		txtTaskName.setEnabled(false);
		txtTaskDescription.setEnabled(value);
		butSave.setEnabled(value);
		cbxUserAssigned.setEnabled(value);
		txtTaskEstimate.setEnabled(value);
		if (value) {
			labAddTask.setForeground(Color.black);
		} else {
			labAddTask.setForeground(Color.gray);
		}
	}
	
	/**
	 * Sets the error field to the given string, which displays to the user
	 * 
	 * @param errorText
	 *            the new erro text
	 */
	public void setErrorField(final String errorText) {
		labSaveError.setText(errorText);
	}
	
	/**
	 * Validates the given input to make sure there are no errors
	 */
	public void validateInput() {
		boolean error = false;
		String errorText = "";
		
		txtTaskDescription.setBackground(Color.white);
		txtTaskEstimate.setBackground(Color.white);
		txtTaskName.setBackground(Color.white);
		
		String taskEstimate = txtTaskEstimate.getText().trim();
		if (taskEstimate.isEmpty()) {
			error = true;
			txtTaskEstimate.setBackground(new Color(243, 243, 209));
			errorText = "Estimate field must not be blank";
		} else {
			try {
				final int est = Integer.parseInt(taskEstimate);
				// check if estimates sum correctly
				int estimateSum = 0;
				
				for (final Task altTask : requirement.getTasks()) {
					if (altTask.getId() != taskId) {
						estimateSum = estimateSum + altTask.getEstimate();
					}
				}
				
				if ((est + estimateSum) > requirement.getEstimate()) {
					error = true;
					txtTaskEstimate.setBackground(new Color(243, 243, 209));
					errorText = "Sum of task estimates too high";
				}
				
			} catch (NumberFormatException e) {
				error = true;
				txtTaskEstimate.setBackground(new Color(243, 243, 209));
				errorText = "Estimate field must be a number";
			}
		}
		
		if (txtTaskDescription.getText().trim().isEmpty()) {
			error = true;
			txtTaskDescription.setBackground(new Color(243, 243, 209));
			errorText = "Description must not be blank";
		}
		
		String taskName = txtTaskName.getText().trim();
		if (taskName.isEmpty()) {
			error = true;
			txtTaskName.setBackground(new Color(243, 243, 209));
			errorText = "Name must not be blank";
		}
		
		// enable / disable save button
		if (txtTaskName.getText().isEmpty()
				&& txtTaskDescription.getText().isEmpty()
				&& txtTaskEstimate.getText().equals("0")) {
			butCancel.setEnabled(false);
		} else {
			butCancel.setEnabled(true);
		}
		
		if (error) {
			labSaveError.setText(errorText);
			butSave.setEnabled(false);
		} else {
			labSaveError.setText("");
			butSave.setEnabled(true);
		}
		
	}
	
	/**
	 * Sets the id of the edited task for reference
	 * 
	 * @param id
	 *            , the id
	 */
	public void setTaskId(int id) {
		taskId = id;
	}
	
	/**
	 * Called when the cancel button is pressed
	 * 
	 */
	
	public void onCancel() {
		txtTaskName.setText("");
		txtTaskDescription.setText("");
		txtTaskEstimate.setText("0");
		
		labSaveError.setText("");
		butSave.setEnabled(false);
		taskId = -2;
		taskView.clearSelection();
		butCancel.setEnabled(false);
	}
	
}
