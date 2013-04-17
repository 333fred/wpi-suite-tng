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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JDateChooser;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddFilterController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveFilterController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.FilterIterationBetween;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;


/**
 * View for creating and editing filters
 * 
 * @author Mitchell
 * 
 */

public class CreateFilterView extends JPanel implements ActionListener,
		ISaveNotifier {

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
	private JLabel labEqualToBetween;
	private JLabel labSaveError;

	private JComboBox cboxField;
	private JComboBox cboxOperation;

	private JComboBox cboxEqualTo;
	private JTextField txtEqualTo;

	private JDateChooser calEqualTo;
	private JDateChooser calEqualToBetween;

	private JButton butSave;
	private JButton butCancel;

	/** Filter view */
	private FilterView filterView;

	/** Controller for saving a filter */
	private SaveFilterController saveFilterController;

	/** Controller for Adding a filter */
	private AddFilterController addFilterController;

	/** The listener to do intime validation */
	private CreateFilterViewListener createFilterViewListener;

	/**
	 * Creates a filter view to create a new filter
	 * 
	 */
	public CreateFilterView(FilterView filterView) {
		this(filterView, new Filter(), Mode.CREATE);
	}

	/**
	 * Creates a filter view to edit an existing filter
	 * 
	 * @param filter
	 *            The filter to edit
	 */
	public CreateFilterView(FilterView filterView, Filter filter) {
		this(filterView, new Filter(), Mode.EDIT);
	}

	/**
	 * Creates a filter view with the given filter and the given mode
	 * 
	 * @param filter
	 *            The filter to work with
	 * @param mode
	 *            The editing mode
	 */

	private CreateFilterView(FilterView filterView, Filter filter, Mode mode) {
		this.filterView = filterView;
		this.filter = filter;
		this.mode = mode;

		setBorder(BorderFactory.createTitledBorder("Create filter"));

		saveFilterController = new SaveFilterController(this);
		addFilterController = new AddFilterController(this);

		createFilterViewListener = new CreateFilterViewListener(this);

		labField = new JLabel("Field");
		labOperation = new JLabel("Operation");
		labEqualTo = new JLabel();
		labEqualToBetween = new JLabel("and");
		labSaveError = new JLabel("JAAAABOUY");

		cboxField = new JComboBox();
		cboxOperation = new JComboBox();
		cboxEqualTo = new JComboBox();
		txtEqualTo = new JTextField();

		calEqualTo = new JDateChooser();
		calEqualToBetween = new JDateChooser();

		cboxField.setBackground(Color.WHITE);
		cboxOperation.setBackground(Color.WHITE);
		cboxEqualTo.setBackground(Color.WHITE);

		butSave = new JButton("Save");
		butCancel = new JButton("Cancel");

		butSave.setEnabled(false);

		SpringLayout layout = new SpringLayout();

		layout.putConstraint(SpringLayout.NORTH, labField, VERTICAL_PADDING,
				SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labField, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, cboxField,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labField);
		layout.putConstraint(SpringLayout.WEST, cboxField, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.EAST, cboxField, 0,
				SpringLayout.EAST, butCancel);

		layout.putConstraint(SpringLayout.NORTH, labOperation,
				VERTICAL_PADDING, SpringLayout.SOUTH, cboxField);
		layout.putConstraint(SpringLayout.WEST, labOperation,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, cboxOperation,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labOperation);
		layout.putConstraint(SpringLayout.WEST, cboxOperation,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.EAST, cboxOperation, 0,
				SpringLayout.EAST, butCancel);

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

		layout.putConstraint(SpringLayout.EAST, txtEqualTo, 0,
				SpringLayout.EAST, butCancel);

		layout.putConstraint(SpringLayout.NORTH, cboxEqualTo,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labEqualTo);
		layout.putConstraint(SpringLayout.WEST, cboxEqualTo,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.EAST, cboxEqualTo, 0,
				SpringLayout.EAST, butCancel);

		layout.putConstraint(SpringLayout.NORTH, calEqualTo,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labEqualTo);
		layout.putConstraint(SpringLayout.WEST, calEqualTo, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.EAST, calEqualTo, 0,
				SpringLayout.EAST, butCancel);

		layout.putConstraint(SpringLayout.WEST, labEqualToBetween,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labEqualToBetween,
				VERTICAL_PADDING, SpringLayout.SOUTH, calEqualTo);

		layout.putConstraint(SpringLayout.WEST, calEqualToBetween,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, calEqualToBetween, 0,
				SpringLayout.EAST, butCancel);
		layout.putConstraint(SpringLayout.NORTH, calEqualToBetween,
				VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH, labEqualToBetween);

		layout.putConstraint(SpringLayout.WEST, labSaveError,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labSaveError,
				VERTICAL_PADDING, SpringLayout.SOUTH, calEqualToBetween);

		layout.putConstraint(SpringLayout.NORTH, butSave, VERTICAL_PADDING,
				SpringLayout.SOUTH, labSaveError);
		layout.putConstraint(SpringLayout.WEST, butSave, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, butCancel, VERTICAL_PADDING,
				SpringLayout.SOUTH, labSaveError);
		layout.putConstraint(SpringLayout.WEST, butCancel, HORIZONTAL_PADDING,
				SpringLayout.EAST, butSave);

		setLayout(layout);

		labEqualToBetween.setVisible(false);

		add(labField);
		add(labOperation);
		add(labEqualTo);
		add(labEqualToBetween);
		add(labSaveError);

		add(cboxField);
		add(cboxOperation);
		add(cboxEqualTo);
		add(txtEqualTo);
		add(calEqualTo);
		add(calEqualToBetween);

		add(butSave);
		add(butCancel);

		this.setMinimumSize(new Dimension(butSave.getPreferredSize().width
				+ HORIZONTAL_PADDING * 3 + butCancel.getPreferredSize().width,
				275));
		this.setPreferredSize(new Dimension(butSave.getPreferredSize().width
				+ HORIZONTAL_PADDING * 3 + butCancel.getPreferredSize().width,
				275));

		// add the action listeners
		cboxField.addActionListener(this);
		cboxOperation.addActionListener(this);
		cboxEqualTo.addActionListener(this);

		// add action listeners to save
		butSave.addActionListener(this);
		butCancel.addActionListener(this);

		txtEqualTo.addKeyListener(createFilterViewListener);
		calEqualTo.addPropertyChangeListener(createFilterViewListener);
		calEqualToBetween.addPropertyChangeListener(createFilterViewListener);

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

		FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());

		cboxOperation.removeAllItems();
		if (field == FilterField.NAME || field == FilterField.RELEASE_NUMBER) {
			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
			cboxOperation.addItem("Starts with");
			cboxOperation.addItem("Contains");
		} else if (field == FilterField.ESTIMATE || field == FilterField.EFFORT) {

			cboxOperation.addItem("<");
			cboxOperation.addItem("<=");
			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
			cboxOperation.addItem(">=");
			cboxOperation.addItem(">");

		} else if (field == FilterField.STATUS || field == FilterField.PRIORITY
				|| field == FilterField.TYPE) {

			cboxOperation.addItem("=");
			cboxOperation.addItem("!=");
		} else if (field == FilterField.ITERATION) {
			cboxOperation.addItem("Equals");
			cboxOperation.addItem("Occurs before");
			cboxOperation.addItem("Occurs after");
			cboxOperation.addItem("Occurs between");

		}

	}

	private void populateEqualComboBox() {
		// cboxEqualTo
		cboxEqualTo.removeAllItems();

		FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());

		if (field == FilterField.TYPE) {
			// BLANK, EPIC, THEME, USER_STORY, NON_FUNCTIONAL, SCENARIO
			cboxEqualTo.addItem("None");
			cboxEqualTo.addItem("Epic");
			cboxEqualTo.addItem("Theme");
			cboxEqualTo.addItem("User Story");
			cboxEqualTo.addItem("Non Functional");
			cboxEqualTo.addItem("Scenario");

		} else if (field == FilterField.PRIORITY) {
			// BLANK, LOW, MEDIUM, HIGH
			cboxEqualTo.addItem("None");
			cboxEqualTo.addItem("Low");
			cboxEqualTo.addItem("Medium");
			cboxEqualTo.addItem("High");
		} else if (field == FilterField.STATUS) {
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

		FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());

		if (field == FilterField.TYPE || field == FilterField.PRIORITY
				|| field == FilterField.STATUS) {

			cboxEqualTo.setVisible(true);
			txtEqualTo.setVisible(false);
			calEqualTo.setVisible(false);
			calEqualToBetween.setVisible(false);
			labEqualToBetween.setVisible(false);
			populateEqualComboBox();
		} else if (field == FilterField.ITERATION) {
			cboxEqualTo.setVisible(false);

			if (cboxOperation.getSelectedItem().equals("Occurs between")) {
				calEqualTo.setVisible(true);
				calEqualToBetween.setVisible(true);
				labEqualToBetween.setVisible(true);
				txtEqualTo.setVisible(false);
			} else if (cboxOperation.getSelectedItem().equals("Equals")) {
				calEqualTo.setVisible(false);
				calEqualToBetween.setVisible(false);
				labEqualToBetween.setVisible(false);
				txtEqualTo.setVisible(true);
			} else {
				calEqualTo.setVisible(true);
				calEqualToBetween.setVisible(false);
				labEqualToBetween.setVisible(false);
				txtEqualTo.setVisible(false);
			}
		} else {
			cboxEqualTo.setVisible(false);
			txtEqualTo.setVisible(true);
			calEqualTo.setVisible(false);
			calEqualToBetween.setVisible(false);
			labEqualToBetween.setVisible(false);
		}
	}

	public void onSavePressed() {

		boolean error = false;
		String errorString = "";
		FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());
		FilterOperation operation = FilterOperation
				.getFromString((String) cboxOperation.getSelectedItem());

		String equalToStr;

		// get the equal to value from the text box or combo box
		switch (field) {
		case EFFORT:
		case ESTIMATE:
			equalToStr = txtEqualTo.getText().trim();
			// check to make sure this is an int
			try {
				Integer.parseInt(equalToStr);
				filter.setValue(equalToStr);
			} catch (NumberFormatException e) {
				error = true;
				errorString = "Value must be a number";
			}

			break;
		case NAME:
		case RELEASE_NUMBER:
			equalToStr = txtEqualTo.getText().trim();
			if (equalToStr.isEmpty()) {
				errorString = "Value cannot be blank";
				error = true;
			} else {
				filter.setValue(equalToStr);
			}

			break;

		case ITERATION:
			if (operation == FilterOperation.OCCURS_BETWEEN) {
				Date startDate = calEqualTo.getDate();
				Date endDate = calEqualToBetween.getDate();

				if (endDate.after(startDate)) {
					filter.setValue(new FilterIterationBetween(calEqualTo
							.getDate(), calEqualToBetween.getDate()));
				} else {
					errorString = "Start date must before end date";
					error = true;

				}
			} else if (operation == FilterOperation.EQUAL) {
				equalToStr = txtEqualTo.getText().trim();
				if (equalToStr.isEmpty()) {
					errorString = "Value cannot be blank";
					error = true;
				}
				else {
					filter.setValue(equalToStr);
				}
			} else {
				if (calEqualTo.getDate() == null) {
					error = true;
					errorString = "Date cannot be blank";
				}
				else {
					filter.setValue(calEqualTo.getDate());	
				}
			}
			break;

		case PRIORITY:
			filter.setValue(Priority.getFromString((String) cboxEqualTo
					.getSelectedItem()));
			break;
		case STATUS:
			filter.setValue(Status.getFromString((String) cboxEqualTo
					.getSelectedItem()));
			break;
		case TYPE:
			filter.setValue(Type.getFromString((String) cboxEqualTo
					.getSelectedItem()));
			break;
		}
		if (!error) {
			// no error, we shall save
			filter.setField(FilterField.getFromString((String) cboxField
					.getSelectedItem()));
			filter.setOperation(FilterOperation
					.getFromString((String) cboxOperation.getSelectedItem()));

			if (mode == Mode.CREATE) {
				addFilterController.addFilter(filter);
			} else {
				saveFilterController.saveFilter(filter);
			}
			onCancelPressed();
		} else {
			// there was an error set text bot
			labSaveError.setText(errorString);
		}
	}

	public void onCancelPressed() {
		labSaveError.setText("");
		txtEqualTo.setText("");
		calEqualTo.setDate(null);
		calEqualToBetween.setDate(null);
		cboxField.setSelectedItem("Name");

		populateOperationComboBox();
		updateEqualsField();

	}

	/** Updates the status of the save button */

	public void updateSave() {
		boolean error = false;
		String errorString = "";
		FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());

		String equalToStr;

		// get the equal to value from the text box or combo box
		switch (field) {
		case EFFORT:
		case ESTIMATE:
			equalToStr = txtEqualTo.getText().trim();
			// check to make sure this is an int
			try {
				Integer.parseInt(equalToStr);
			} catch (NumberFormatException e) {
				error = true;
				errorString = "Value must be a number";
			}

			break;
		case NAME:
		case RELEASE_NUMBER:
			equalToStr = txtEqualTo.getText().trim();
			if (equalToStr.isEmpty()) {
				errorString = "Value cannot be blank";
				error = true;
			}

			break;

		case ITERATION:
			if (filter.getOperation() == FilterOperation.OCCURS_BETWEEN) {
				Date startDate = calEqualTo.getDate();
				Date endDate = calEqualToBetween.getDate();

				if (calEqualTo.getDate() == null) {
					errorString = "Start Date cannot be blank";
					error = true;
				} else if (calEqualToBetween.getDate() == null) {
					errorString = "End Date cannot be blank";
					error = true;
				} else if (endDate.before(startDate)) {
					errorString = "Start date must before end date";
					error = true;
				}
			} else {
				if (calEqualTo.getDate() == null) {
					error = true;
					errorString = "Date cannot be blank";
				}
			}
			break;

		case PRIORITY:
		case STATUS:
		case TYPE:
			break;
		}
		if (!error) {
			labSaveError.setText("");
			butSave.setEnabled(true);
		} else {
			// there was an error set text bot
			labSaveError.setText(errorString);
			butSave.setEnabled(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		updateSave();
		if (source.equals(cboxField)) {
			populateOperationComboBox();
			updateEqualsField();
		} else if (source.equals(cboxOperation)) {
			if (cboxOperation.getItemCount() != 0
					&& cboxField.getSelectedItem().equals("Iteration")) {
				updateEqualsField();
			}
		} else if (source.equals(butSave)) {
			onSavePressed();
		} else if (source.equals(butCancel)) {
			onCancelPressed();
		}
	}

	public void responseSuccess() {
		onCancelPressed();
		filterView.refreshTableView();
	}

	public void responseError(int statusCode, String statusMessage) {
		System.out.println("Filter Errored!! " + statusMessage + "code: "
				+ statusCode);
	}

	public void fail(Exception exception) {
		System.out.println("Filter Failed!!");
		exception.printStackTrace();
	}
}
