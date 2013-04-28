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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.AbstractDocument;

import com.toedter.calendar.JDateChooser;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.FilterOperation;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.FilterController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.FilterDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Filter;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.FilterIterationBetween;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddFilterRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateFilterRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.DocumentNumberAndSizeFilter;

/**
 * View for creating and editing filters
 * 
 * @author Mitchell
 * 
 */
@SuppressWarnings ({ "serial", "rawtypes", "unchecked" })
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
	
	private static final int VERTICAL_PADDING = 8;
	private static final int HORIZONTAL_PADDING = 8;
	
	/**
	 * Checks to see if the given filter is a duplicate filter.
	 * 
	 * @param toCheck
	 *            The filter to check
	 * @return True if the filter is a duplicate. False otherwise
	 */
	public static boolean isFilterDuplicate(final Filter toCheck) {
		final List<Filter> filters = FilterDatabase.getInstance().getAll();
		for (final Filter filter : filters) {
			if (filter.equalToWithoutId(toCheck)) {
				return true;
			}
		}
		return false;
	}
	
	/** The filter this view is working on */
	private Filter filter;
	
	/** The editing mode this filter view is in */
	private Mode mode;
	/** The UI Components */
	private final JLabel labField;
	private final JLabel labOperation;
	private final JLabel labEqualTo;
	private final JLabel labEqualToBetween;
	private final JLabel labSaveError;
	
	private final JComboBox cboxField;
	
	private final JComboBox cboxOperation;
	private final JComboBox cboxEqualTo;
	
	private final JTextField txtEqualTo;
	private final JDateChooser calEqualTo;
	
	private final JDateChooser calEqualToBetween;
	private final JButton butSave;
	
	private final JButton butCancel;
	private final int butCancelWidth;
	
	private final int butSaveWidth;
	
	/** Filter view */
	private final FilterView filterView;
	
	/** Controller for saving a filter */
	private final FilterController filterController;
	
	/** The listener to do intime validation */
	private final CreateFilterViewListener createFilterViewListener;
	
	/** Iterations for the combo box */
	private final List<Iteration> iterations;
	
	/**
	 * Sets up a filter view with the create mode
	 * 
	 * @param filterView
	 *            The view to set up
	 * 
	 */
	public CreateFilterView(final FilterView filterView) {
		this(filterView, new Filter(), Mode.CREATE);
	}
	
	/**
	 * Creates a filter view to edit an existing filter
	 * 
	 * @param filterView
	 *            the filter to set up
	 * 
	 * @param filter
	 *            The filter to edit
	 */
	public CreateFilterView(final FilterView filterView, final Filter filter) {
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
	
	private CreateFilterView(final FilterView filterView, final Filter filter,
			final Mode mode) {
		this.filterView = filterView;
		this.filter = filter;
		this.mode = mode;
		
		setBorder(BorderFactory.createTitledBorder("Create filter"));
		
		filterController = new FilterController();
		
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
		
		butSave = new JButton("Create");
		butCancel = new JButton("Cancel Editing");
		
		butCancelWidth = butCancel.getPreferredSize().width;
		butSaveWidth = butSave.getPreferredSize().width;
		
		butCancel.setText("Cancel");
		butCancel.setPreferredSize(new Dimension(butCancelWidth, butCancel
				.getPreferredSize().height));
		
		butSave.setEnabled(false);
		
		final SpringLayout layout = new SpringLayout();
		
		layout.putConstraint(SpringLayout.NORTH, labField,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labField,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, cboxField,
				CreateFilterView.VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH,
				labField);
		layout.putConstraint(SpringLayout.WEST, cboxField,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.EAST, cboxField, 0,
				SpringLayout.EAST, butCancel);
		
		layout.putConstraint(SpringLayout.NORTH, labOperation,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.SOUTH,
				cboxField);
		layout.putConstraint(SpringLayout.WEST, labOperation,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, cboxOperation,
				CreateFilterView.VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH,
				labOperation);
		layout.putConstraint(SpringLayout.WEST, cboxOperation,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.EAST, cboxOperation, 0,
				SpringLayout.EAST, butCancel);
		
		layout.putConstraint(SpringLayout.NORTH, labEqualTo,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.SOUTH,
				cboxOperation);
		layout.putConstraint(SpringLayout.WEST, labEqualTo,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, txtEqualTo,
				CreateFilterView.VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH,
				labEqualTo);
		layout.putConstraint(SpringLayout.WEST, txtEqualTo,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, txtEqualTo, 0,
				SpringLayout.EAST, cboxField);
		
		layout.putConstraint(SpringLayout.EAST, txtEqualTo, 0,
				SpringLayout.EAST, butCancel);
		
		layout.putConstraint(SpringLayout.NORTH, cboxEqualTo,
				CreateFilterView.VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH,
				labEqualTo);
		layout.putConstraint(SpringLayout.WEST, cboxEqualTo,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.EAST, cboxEqualTo, 0,
				SpringLayout.EAST, butCancel);
		
		layout.putConstraint(SpringLayout.NORTH, calEqualTo,
				CreateFilterView.VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH,
				labEqualTo);
		layout.putConstraint(SpringLayout.WEST, calEqualTo,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.EAST, calEqualTo, 0,
				SpringLayout.EAST, butCancel);
		
		layout.putConstraint(SpringLayout.WEST, labEqualToBetween,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labEqualToBetween,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.SOUTH,
				calEqualTo);
		
		layout.putConstraint(SpringLayout.WEST, calEqualToBetween,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, calEqualToBetween, 0,
				SpringLayout.EAST, butCancel);
		layout.putConstraint(SpringLayout.NORTH, calEqualToBetween,
				CreateFilterView.VERTICAL_PADDING_CLOSE, SpringLayout.SOUTH,
				labEqualToBetween);
		
		layout.putConstraint(SpringLayout.WEST, labSaveError,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labSaveError,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.SOUTH,
				calEqualToBetween);
		
		layout.putConstraint(SpringLayout.NORTH, butSave,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.SOUTH,
				labSaveError);
		layout.putConstraint(SpringLayout.WEST, butSave,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.WEST, this);
		
		layout.putConstraint(SpringLayout.NORTH, butCancel,
				CreateFilterView.VERTICAL_PADDING, SpringLayout.SOUTH,
				labSaveError);
		layout.putConstraint(SpringLayout.WEST, butCancel,
				CreateFilterView.HORIZONTAL_PADDING, SpringLayout.EAST, butSave);
		
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
		
		setMinimumSize(new Dimension(butSave.getPreferredSize().width
				+ (CreateFilterView.HORIZONTAL_PADDING * 4)
				+ butCancel.getPreferredSize().width, 275));
		setPreferredSize(new Dimension(butSave.getPreferredSize().width
				+ (CreateFilterView.HORIZONTAL_PADDING * 4)
				+ butCancel.getPreferredSize().width, 275));
		
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
		
		iterations = new ArrayList<Iteration>();
		// update the iterations
		updateIterations();
		// populate the fields in the combo boxes
		populateFieldComboBox();
		populateOperationComboBox();
		updateEqualsField();
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		final Object source = e.getSource();
		if (source.equals(cboxField)) {
			populateOperationComboBox();
			updateEqualsField();
			updateSave();
		} else if (source.equals(cboxOperation)) {
			if ((cboxOperation.getItemCount() != 0)) {
				updateEqualsField();
				updateSave();
			}
		} else if (source.equals(cboxEqualTo)) {
			updateSave();
		} else if (source.equals(butSave)) {
			onSavePressed();
			
		} else if (source.equals(butCancel)) {
			if (mode == Mode.CREATE) {
				onCancelPressed();
			} else {
				// clear the selected filter when user presses cancel edit
				filterView.clearSelectedFilters();
				cancelEdit();
			}
		}
		
	}
	
	/**
	 * Cancels the editing of the filter
	 * 
	 */
	
	public void cancelEdit() {
		filter = new Filter();
		updateMode(Mode.CREATE);
		onCancelPressed(); // clear all fields
	}
	
	/**
	 * Enables editing of the given filter
	 * 
	 * @param toEdit
	 *            Filter to edit
	 */
	
	public void editFilter(final Filter toEdit) {
		filter = toEdit;
		updateMode(Mode.EDIT);
	}
	
	@Override
	public void fail(final Exception exception) {
		System.out.println("Filter Failed!!");
		exception.printStackTrace();
	}
	
	/**
	 * Action for pressing the cancel button. It clears everything
	 */
	public void onCancelPressed() {
		labSaveError.setText("  ");
		txtEqualTo.setText("");
		calEqualTo.setDate(null);
		calEqualToBetween.setDate(null);
		cboxField.setSelectedItem("Name");
		
		populateOperationComboBox();
		updateEqualsField();
		
	}
	
	/**
	 * Action for pressing save. Validates the filter and saves it to the
	 * database
	 */
	public void onSavePressed() {
		
		boolean error = false;
		String errorString = "";
		final FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());
		final FilterOperation operation = FilterOperation
				.getFromString((String) cboxOperation.getSelectedItem());
		
		String equalToStr;
		// String filterStringValue = null;
		
		// get the equal to value from the text box or combo box
		switch (field) {
			case EFFORT:
			case ESTIMATE:
				equalToStr = txtEqualTo.getText().trim();
				// check to make sure this is an int
				try {
					filter.setValue(Integer.parseInt(equalToStr));
				} catch (final NumberFormatException e) {
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
					final Date startDate = calEqualTo.getDate();
					final Date endDate = calEqualToBetween.getDate();
					
					if (endDate.after(startDate)) {
						filter.setValue(new FilterIterationBetween(calEqualTo
								.getDate(), calEqualToBetween.getDate()));
					} else {
						errorString = "Start date must before end date";
						error = true;
						
					}
				} else if ((operation == FilterOperation.EQUAL)
						|| (operation == FilterOperation.NOT_EQUAL)) {
					final int iterationIndex = cboxEqualTo.getSelectedIndex();
					if (iterationIndex >= iterations.size()) {
						error = true;
						errorString = "Invalid iteration";
					}
					// save the ID of the iteration
					filter.setValue(iterations.get(iterationIndex).getId());
				} else {
					if (calEqualTo.getDate() == null) {
						error = true;
						errorString = "Date cannot be blank";
					} else {
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
			default:
				break;
		}
		if (!error) {
			
			// no error, we shall save
			filter.setField(FilterField.getFromString((String) cboxField
					.getSelectedItem()));
			filter.setOperation(FilterOperation
					.getFromString((String) cboxOperation.getSelectedItem()));
			
			if (CreateFilterView.isFilterDuplicate(filter)) {
				labSaveError.setText("Identical Filter already exists");
				
			} else {
				if (mode == Mode.CREATE) {
					final AddFilterRequestObserver observer = new AddFilterRequestObserver(
							this);
					filterController.create(filter, observer);
					
				} else {
					final UpdateFilterRequestObserver observer = new UpdateFilterRequestObserver(
							this);
					filterController.save(filter, observer);
				}
				txtEqualTo.setBackground(Color.WHITE);
				calEqualTo.setBackground(Color.WHITE);
				calEqualToBetween.setBackground(Color.WHITE);
			}
			
		} else {
			// there was an error set text box
			labSaveError.setText(errorString);
			txtEqualTo.setBackground(new Color(243, 243, 209));
			
		}
	}
	
	private void populateEqualComboBox() {
		// cboxEqualTo
		cboxEqualTo.removeAllItems();
		
		final FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());
		final FilterOperation operation = FilterOperation
				.getFromString((String) cboxOperation.getSelectedItem());
		
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
		} else if ((field == FilterField.ITERATION)
				&& ((operation == FilterOperation.EQUAL) || (operation == FilterOperation.NOT_EQUAL))) {
			// update the iterations
			updateIterations();
			
			cboxEqualTo.removeAllItems();
			for (final Iteration iteration : iterations) {
				cboxEqualTo.addItem(iteration.getName());
			}
		}
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
	
	/**
	 * Populates the filter view fields from a pre-existing filter
	 */
	public void populateFieldsFromFilter() {
		cboxField.setSelectedItem(filter.getField().toString());
		cboxOperation.setSelectedItem(filter.getOperation().toString());
		
		switch (filter.getField()) {
		// special iteration case. woo woo
			case ITERATION:
				switch (filter.getOperation()) {
					case EQUAL:
					case NOT_EQUAL:
						
						final int iterationId = ((Integer) filter.getValue());
						Iteration iteration;
						try {
							iteration = IterationDatabase.getInstance().get(
									iterationId);
						} catch (final IterationNotFoundException e) {
							// iteration is no longer in existance, we should
							// probally
							// delete this filter,
							cancelEdit();
							return;
						}
						
						cboxEqualTo.setSelectedItem(iteration.getName());
						
						break;
					case OCCURS_AFTER:
					case OCCURS_BEFORE:
						calEqualTo.setDate((Date) filter.getValue());
						break;
					case OCCURS_BETWEEN:
						final FilterIterationBetween fib = (FilterIterationBetween) filter
								.getValue();
						calEqualTo.setDate(fib.getStartDate());
						calEqualToBetween.setDate(fib.getEndDate());
						break;
					default:
				}
				
				break;
			
			// string and integer types
			case NAME:
			case RELEASE_NUMBER:
			case EFFORT:
			case ESTIMATE:
				txtEqualTo.setText(filter.getValue().toString());
				break;
			
			// theese are the three enum types
			case PRIORITY:
			case STATUS:
			case TYPE:
				cboxEqualTo.setSelectedItem(filter.getValue().toString());
				break;
			default:
				break;
		
		}
	}
	
	private void populateOperationComboBox() {
		
		final FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());
		
		cboxOperation.removeAllItems();
		if ((field == FilterField.NAME)
				|| (field == FilterField.RELEASE_NUMBER)) {
			cboxOperation.addItem("Equal");
			cboxOperation.addItem("Not equal");
			cboxOperation.addItem("Starts with");
			cboxOperation.addItem("Contains");
		} else if ((field == FilterField.ESTIMATE)
				|| (field == FilterField.EFFORT)) {
			
			cboxOperation.addItem("<");
			cboxOperation.addItem("<=");
			cboxOperation.addItem("Equal");
			cboxOperation.addItem("Not equal");
			cboxOperation.addItem(">=");
			cboxOperation.addItem(">");
			
		} else if ((field == FilterField.STATUS)
				|| (field == FilterField.PRIORITY)
				|| (field == FilterField.TYPE)) {
			
			cboxOperation.addItem("Equal");
			cboxOperation.addItem("Not equal");
		} else if (field == FilterField.ITERATION) {
			cboxOperation.addItem("Equal");
			cboxOperation.addItem("Not equal");
			cboxOperation.addItem("Occurs before");
			cboxOperation.addItem("Occurs after");
			cboxOperation.addItem("Occurs between");
			
		}
		
	}
	
	@Override
	public void responseError(final int statusCode, final String statusMessage) {
		System.out.println("Filter Errored!! " + statusMessage + "code: "
				+ statusCode);
	}
	
	@Override
	public void responseSuccess() {
		onCancelPressed();
		filterView.refreshTableView();
		filterView.notifyListeners();
	}
	
	private void updateEqualsField() {
		
		final FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());
		
		if ((field == FilterField.TYPE) || (field == FilterField.PRIORITY)
				|| (field == FilterField.STATUS)) {
			
			cboxEqualTo.setVisible(true);
			txtEqualTo.setVisible(false);
			calEqualTo.setVisible(false);
			calEqualToBetween.setVisible(false);
			labEqualToBetween.setVisible(false);
			populateEqualComboBox();
		} else if (field == FilterField.ITERATION) {
			txtEqualTo.setVisible(false);
			final FilterOperation operation = FilterOperation
					.getFromString((String) cboxOperation.getSelectedItem());
			if (operation == FilterOperation.OCCURS_BETWEEN) {
				calEqualTo.setVisible(true);
				calEqualToBetween.setVisible(true);
				labEqualToBetween.setVisible(true);
				cboxEqualTo.setVisible(false);
			} else if ((operation == FilterOperation.EQUAL)
					|| (operation == FilterOperation.NOT_EQUAL)) {
				
				populateEqualComboBox();
				
				calEqualTo.setVisible(false);
				calEqualToBetween.setVisible(false);
				labEqualToBetween.setVisible(false);
				cboxEqualTo.setVisible(true);
			} else {
				calEqualTo.setVisible(true);
				calEqualToBetween.setVisible(false);
				labEqualToBetween.setVisible(false);
				cboxEqualTo.setVisible(false);
			}
		} else if (field == FilterField.NAME || field == FilterField.RELEASE_NUMBER){
			cboxEqualTo.setVisible(false);
			calEqualTo.setVisible(false);
			calEqualToBetween.setVisible(false);
			labEqualToBetween.setVisible(false);
			txtEqualTo.setVisible(true);
			txtEqualTo.setText("");
			final AbstractDocument txtEqualToDoc = (AbstractDocument) txtEqualTo
					.getDocument();
			txtEqualToDoc.setDocumentFilter(null);
		}
		else {
			cboxEqualTo.setVisible(false);
			calEqualTo.setVisible(false);
			calEqualToBetween.setVisible(false);
			labEqualToBetween.setVisible(false);
			txtEqualTo.setVisible(true);
			txtEqualTo.setText("");
			final AbstractDocument txtEqualToDoc = (AbstractDocument) txtEqualTo
					.getDocument();
			txtEqualToDoc.setDocumentFilter(new DocumentNumberAndSizeFilter(12));
		}
	}
	
	private void updateIterations() {
		iterations.clear();
		
		for (final Iteration iteration : IterationDatabase.getInstance()
				.getAll()) {
			if (iteration.isOpen()) {
				iterations.add(iteration);
			}
		}
	}
	
	/**
	 * Updates the mode
	 * 
	 * @param newMode
	 *            The mode to switch to
	 * 
	 */
	
	public void updateMode(final Mode newMode) {
		mode = newMode;
		if (mode == Mode.CREATE) {
			butSave.setText("Create");
			butCancel.setText("Cancel");
			butCancel.setPreferredSize(new Dimension(butCancelWidth, butCancel
					.getPreferredSize().height));
		} else {
			butSave.setText("Save");
			butSave.setPreferredSize(new Dimension(butSaveWidth, butSave
					.getPreferredSize().height));
			butCancel.setText("Cancel Editing");
			populateFieldsFromFilter(); // populate the fields from the given
			// filter
			updateSave();
		}
	}
	
	/** Updates the status of the save button */
	
	public void updateSave() {		
		if (cboxOperation.getItemCount() == 0) {
			labSaveError.setText("");
			return;
		}
		boolean error = false;
		String errorString = "";
		final FilterField field = FilterField.getFromString((String) cboxField
				.getSelectedItem());
		final FilterOperation operation = FilterOperation
				.getFromString((String) cboxOperation.getSelectedItem());
		
		final Filter checkFilter = new Filter();
		checkFilter.setField(field);
		checkFilter.setOperation(operation);
		
		String equalToStr;
		
		// get the equal to value from the text box or combo box
		switch (field) {
			case EFFORT:
			case ESTIMATE:
				equalToStr = txtEqualTo.getText().trim();
				// check to make sure this is a positive int
				try {
					final int value = Integer.parseInt(equalToStr);
					if (value < 0) {
						error = true;
						errorString = "Value must be non-negative";
					} else {
						checkFilter.setValue(value);
					}
				} catch (final NumberFormatException e) {
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
					checkFilter.setValue(equalToStr);
				}
				break;
			
			case ITERATION:
				if (operation == FilterOperation.OCCURS_BETWEEN) {
					final Date startDate = calEqualTo.getDate();
					final Date endDate = calEqualToBetween.getDate();
					if (calEqualTo.getDate() == null) {
						errorString = "Start Date cannot be blank";
						error = true;
					} else if (calEqualToBetween.getDate() == null) {
						errorString = "End Date cannot be blank";
						error = true;
					} else if (endDate.before(startDate)) {
						errorString = "Start date must before end date";
						error = true;
					} else {
						checkFilter.setValue(new FilterIterationBetween(
								startDate, endDate));
					}
				} else if ((operation == FilterOperation.EQUAL)
						|| (operation == FilterOperation.NOT_EQUAL)) {
					
					if (cboxEqualTo.getItemCount() == 0) {
						// no items in the equal to box
						error = true;
						errorString = "Equal To combobox not populatd yet";
					} else {
						
						final int iterationIndex = cboxEqualTo
								.getSelectedIndex();
						// save the ID of the iteration
						checkFilter.setValue(iterations.get(iterationIndex)
								.getId());
					}
					
				} else {
					if (calEqualTo.getDate() == null) {
						error = true;
						errorString = "Date cannot be blank";
					} else {
						checkFilter.setValue(calEqualTo.getDate());
					}
				}
				break;
			
			case PRIORITY:
				
				if (cboxEqualTo.getSelectedIndex() != -1) {
					final Priority p = Priority
							.getFromString((String) cboxEqualTo
									.getSelectedItem());
					if (p == null) {
						error = true;
						errorString = "EqualTo combobox has not updated yet";
					} else {
						checkFilter.setValue(p);
					}
				}
				break;
			case STATUS:
				if (cboxEqualTo.getSelectedIndex() != -1) {
					final Status s = Status.getFromString((String) cboxEqualTo
							.getSelectedItem());
					if (s == null) {
						error = true;
						errorString = "EqualTo combobox has not updated yet";
					} else {
						checkFilter.setValue(s);
					}
				}
				break;
			case TYPE:
				if (cboxEqualTo.getSelectedIndex() != -1) {
					final Type t = Type.getFromString((String) cboxEqualTo
							.getSelectedItem());
					if (t == null) {
						error = true;
						errorString = "EqualTo combobox has not updated yet";
					} else {
						checkFilter.setValue(t);
					}
				}
				break;
			default:
				break;
		}
		
		if (!error && (checkFilter.getValue() != null)
				&& CreateFilterView.isFilterDuplicate(checkFilter)) {
			error = true;
			errorString = "Similar filter already exists";
		}
		
		if (!error) {
			labSaveError.setText("  ");
			butSave.setEnabled(true);
			txtEqualTo.setBackground(Color.WHITE);
			calEqualTo.setBackground(Color.WHITE);
		} else {
			// there was an error set text bot
			labSaveError.setText(errorString);
			butSave.setEnabled(false);
			txtEqualTo.setBackground(new Color(243, 243, 209));
			calEqualTo.setBackground(new Color(243, 243, 209));
		}
	}
	
	/**
	 * @return the cboxField
	 */
	public JComboBox getCboxField() {
		return cboxField;
	}
	
	/**
	 * @return the cboxOperation
	 */
	public JComboBox getCboxOperation() {
		return cboxOperation;
	}
	
	/**
	 * @return the cboxEqualTo
	 */
	public JComboBox getCboxEqualTo() {
		return cboxEqualTo;
	}
	
	/**
	 * @return the txtEqualTo
	 */
	public JTextField getTxtEqualTo() {
		return txtEqualTo;
	}
	
	/**
	 * @return the calEqualTo
	 */
	public JDateChooser getCalEqualTo() {
		return calEqualTo;
	}
	
	/**
	 * @return the calEqualToBetween
	 */
	public JDateChooser getCalEqualToBetween() {
		return calEqualToBetween;
	}
	
	/**
	 * @return the butSave
	 */
	public JButton getButSave() {
		return butSave;
	}
	
	/**
	 * @return the butCancel
	 */
	public JButton getButCancel() {
		return butCancel;
	}
	
	/**
	 * @return the labSaveError
	 */
	public JLabel getLabSaveError() {
		return labSaveError;
	}
	
	/**
	 * Will disable all of the fields in this View
	 * 
	 */
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		cboxField.setEnabled(enabled);
		cboxOperation.setEnabled(enabled);
		cboxEqualTo.setEnabled(enabled);
		txtEqualTo.setEnabled(enabled);
		calEqualTo.setEnabled(enabled);
		calEqualToBetween.setEnabled(enabled);
		butCancel.setEnabled(enabled);
		if (enabled) {
			// appropriatly update the save button
			updateSave();
		} else {
			// disalbe everything else
			butSave.setEnabled(enabled);
			
		}
		
	}
	
}
