/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mitchell Caisse
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

/**
 * View for creating and editing filters
 * 
 * @author Mitchell
 * 
 */

public class CreateFilterView extends JPanel implements ActionListener {

	/**
	 * Enum for setting the mode of this view, either creating or editing
	 * 
	 */
	private enum Mode {
		CREATE, EDIT
	}

	private static final int VERTICAL_PADDING_CLOSE = 4;
	private static final int HORIZONTAL_PADDING_CLOSE = 4;

	private static final int VERTICAL_PADDING = 8;
	private static final int HORIZONTAL_PADDING = 8;

	/** The filter this view is working on */
	private Filter filter;

	/** The editing mode this filter view is in */
	private Mode mode;

	/** The UI Components */
	private JLabel labField;
	private JLabel labOperation;
	private JLabel labEqualTo;

	private JComboBox<String> cboxField;
	private JComboBox<String> cboxOperation;

	private JComboBox<String> cboxEqualTo;
	private JTextField txtEqualTo;

	private JButton butSave;
	private JButton butCancel;

	/**
	 * Creates a filter view to create a new filter
	 * 
	 */
	public CreateFilterView() {
		this(new Filter(), Mode.CREATE);
	}

	/**
	 * Creates a filter view to edit an existing filter
	 * 
	 * @param filter
	 *            The filter to edit
	 */
	public CreateFilterView(Filter filter) {
		this(new Filter(), Mode.EDIT);
	}

	/**
	 * Creates a filter view with the given filter and the given mode
	 * 
	 * @param filter
	 *            The filter to work with
	 * @param mode
	 *            The editing mode
	 */

	private CreateFilterView(Filter filter, Mode mode) {

		labField = new JLabel("Field");
		labOperation = new JLabel("Operation");
		labEqualTo = new JLabel("Equal");

		cboxField = new JComboBox<String>();
		cboxOperation = new JComboBox<String>();
		cboxEqualTo = new JComboBox<String>();
		txtEqualTo = new JTextField();

		butSave = new JButton("Save");
		butCancel = new JButton("Cancel");

		SpringLayout layout = new SpringLayout();

		layout.putConstraint(SpringLayout.NORTH, labField, VERTICAL_PADDING,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labField, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, cboxField,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labField);
		layout.putConstraint(SpringLayout.WEST, cboxField, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, labOperation,
				VERTICAL_PADDING, SpringLayout.SOUTH, cboxField);
		layout.putConstraint(SpringLayout.WEST, labOperation,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, cboxOperation,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labOperation);
		layout.putConstraint(SpringLayout.WEST, cboxOperation,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, labEqualTo, VERTICAL_PADDING,
				SpringLayout.SOUTH, cboxOperation);
		layout.putConstraint(SpringLayout.WEST, labEqualTo, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, txtEqualTo,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labEqualTo);
		layout.putConstraint(SpringLayout.WEST, txtEqualTo, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, txtEqualTo, 0,
				SpringLayout.EAST, cboxField);

		layout.putConstraint(SpringLayout.NORTH, cboxEqualTo,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labEqualTo);
		layout.putConstraint(SpringLayout.WEST, cboxEqualTo,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.SOUTH, butCancel, -VERTICAL_PADDING,
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, butCancel, -HORIZONTAL_PADDING,
				SpringLayout.EAST, this);

		layout.putConstraint(SpringLayout.SOUTH, butSave, -VERTICAL_PADDING,
				SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, butSave, -HORIZONTAL_PADDING,
				SpringLayout.WEST, butCancel);

		setLayout(layout);

		add(labField);
		add(labOperation);
		add(labEqualTo);

		add(cboxField);
		add(cboxOperation);
		add(cboxEqualTo);
		add(txtEqualTo);

		add(butSave);
		add(butCancel);

		this.setMinimumSize(new Dimension(butSave.getPreferredSize().width + HORIZONTAL_PADDING * 3 + 
					butCancel.getPreferredSize().width, 0));
		
		// add the action listeners
		cboxField.addActionListener(this);
		cboxOperation.addActionListener(this);
		cboxEqualTo.addActionListener(this);

		// populate the fields in the combo boxes
		populateFieldComboBox();
		populateOperationComboBox();
		updateEqualsField();
	}

	private void populateFieldComboBox() {
		cboxField.removeAllItems();
		cboxField.addItem("Name");
		cboxField.addItem("Type");
		cboxField.addItem("Priority");
		cboxField.addItem("Status");
		cboxField.addItem("Iteration");
		cboxField.addItem("Estimate");
		cboxField.addItem("Effort");
		cboxField.addItem("Release Number");
	}

	private void populateOperationComboBox() {
		cboxOperation.removeAllItems();
		if (cboxField.getSelectedItem().equals("Name")
				|| cboxField.getSelectedItem().equals("Release Number")) {
			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
		} else if (cboxField.getSelectedItem().equals("Estimate")
				|| cboxField.getSelectedItem().equals("Effort")) {

			cboxOperation.addItem("<");
			cboxOperation.addItem("<=");
			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
			cboxOperation.addItem(">=");
			cboxOperation.addItem(">");

		} else if (cboxField.getSelectedItem().equals("Type")
				|| cboxField.getSelectedItem().equals("Priority")
				|| cboxField.getSelectedItem().equals("Status")) {

			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
		} else if (cboxField.getSelectedItem().equals("Iteration")) {
			cboxOperation.addItem("<");
			cboxOperation.addItem("<=");
			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
			cboxOperation.addItem(">=");
			cboxOperation.addItem(">");
		}

	}

	private void populateEqualComboBox() {
		// cboxEqualTo
		cboxEqualTo.removeAllItems();
		if (cboxField.getSelectedItem().equals("Type")) {
			// BLANK, EPIC, THEME, USER_STORY, NON_FUNCTIONAL, SCENARIO
			cboxEqualTo.addItem("None");
			cboxEqualTo.addItem("Epic");
			cboxEqualTo.addItem("Theme");
			cboxEqualTo.addItem("User Story");
			cboxEqualTo.addItem("Non Functional");
			cboxEqualTo.addItem("Scenario");

		} else if (cboxField.getSelectedItem().equals("Priority")) {
			// BLANK, LOW, MEDIUM, HIGH
			cboxEqualTo.addItem("None");
			cboxEqualTo.addItem("Low");
			cboxEqualTo.addItem("Medium");
			cboxEqualTo.addItem("High");
		} else if (cboxField.getSelectedItem().equals("Status")) {
			// BLANK, NEW, IN_PROGRESS, OPEN, COMPLETE, DELETED
			cboxEqualTo.addItem("None");
			cboxEqualTo.addItem("New");
			cboxEqualTo.addItem("In Progress");
			cboxEqualTo.addItem("Open");
			cboxEqualTo.addItem("Completed");
			cboxEqualTo.addItem("Deleted");
		}
	}

	private void updateEqualsField() {
		if (cboxField.getSelectedItem().equals("Type")
				|| cboxField.getSelectedItem().equals("Priority")
				|| cboxField.getSelectedItem().equals("Status")) {
			cboxEqualTo.setVisible(true);
			txtEqualTo.setVisible(false);
			populateEqualComboBox();
		} else {
			cboxEqualTo.setVisible(false);
			txtEqualTo.setVisible(true);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(cboxField)) {
			populateOperationComboBox();
			updateEqualsField();
		}
	}
}
