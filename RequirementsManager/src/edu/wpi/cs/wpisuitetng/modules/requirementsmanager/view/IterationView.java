/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mitchell Caisse, Matt Costi
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JCalendar;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.listeners.IterationViewListener;

/**
 * View for creating or editing a iteration
 */
public class IterationView extends Tab implements ISaveNotifier {

	/** Status enum, whether created or edited */
	public enum Status {
		CREATE, EDIT, VIEW
	}

	/** Controller for iteration interaction */
	private IterationController iterationController;

	/** The maintab controller */
	private MainTabController mainTabController;

	/** The iteration object this view will be displaying / creating */
	private Iteration iteration;

	/** The status enum, whether editing or creating */
	private Status status;

	/** Swing components */
	private JLabel labName;
	private JLabel labStartDate;
	private JLabel labEndDate;
	private JLabel labEstimate;

	/** Error message components */
	private JLabel labErrorMessage;
	private JLabel labNameError;
	private JLabel labCalendarError;

	/** Buttons for saving and canceling iteration */

	private JButton butSave;
	private JButton butCancel;

	/** Textfield for entering the name of the iteration */
	private JTextField txtName;
	private JTextField txtEstimate;

	/** The JCalendars for selecting dates */
	private JCalendar calStartDate;
	private JCalendar calEndDate;

	/** Booleans indicating if there is an error with name, or calendar field */
	private boolean nameError;
	private boolean calendarError;

	/** Padding constants */

	private final int VERTICAL_PADDING = 10;
	private final int HORIZONTAL_PADDING = 20;

	public IterationView(MainTabController mainTabController) {
		this(new Iteration(), Status.CREATE, mainTabController);
	}

	public IterationView(Iteration iteration, Status status,
			MainTabController mainTabController) {
		this.iteration = iteration;
		this.status = status;
		this.mainTabController = mainTabController;

		nameError = false;
		calendarError = false;

		// initilize the add iteration controller
		iterationController = new IterationController();
		// initlaize JComponents

		labName = new JLabel("Name:");
		labStartDate = new JLabel("Starting Date:");
		labEndDate = new JLabel("Ending Date:");
		labEstimate = new JLabel("Estimate:");

		labErrorMessage = new JLabel(" ");
		labNameError = new JLabel(" ");
		labCalendarError = new JLabel(" ");

		labNameError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		labCalendarError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

		butSave = new JButton();
		butSave.setAction(new SaveAction(this));

		if (status == Status.CREATE) {
			butSave.setText("Create");
		} else {
			butSave.setText("Save");
		}

		butCancel = new JButton("Cancel");
		butCancel.setAction(new CancelAction());

		txtName = new JTextField();
		txtName.setDisabledTextColor(Color.BLACK);
		txtEstimate = new JTextField(10);

		// this text field will always be disabled, as you cant edit it
		txtEstimate.setEnabled(false);
		txtEstimate.setOpaque(false);
		txtEstimate.setDisabledTextColor(Color.BLACK);

		calStartDate = new JCalendar();
		calEndDate = new JCalendar();

		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue((int) this.iteration.getProgress());
		JLabel labProgress = new JLabel("Progress:");

		// populate fields, if editing
		if (status == Status.EDIT || status == Status.VIEW) {
			txtName.setText(iteration.getName());
			calStartDate.setDate((Date) iteration.getStartDate().clone());
			calEndDate.setDate((Date) iteration.getEndDate().clone());
			txtEstimate.setText(iteration.getEstimate() + "");
		}

		butSave.setEnabled(false);

		if (status == Status.VIEW) {
			txtName.setEnabled(false);
			calStartDate.setEnabled(false);
			calEndDate.setEnabled(false);
			butCancel.setText("Close");
			butSave.setEnabled(false);
		} else {
			txtName.addKeyListener(new IterationViewListener(this, txtName));

			IterationViewListener startDateListener = new IterationViewListener(
					this, calStartDate);
			IterationViewListener endDateListener = new IterationViewListener(
					this, calEndDate);

			calStartDate.addPropertyChangeListener(startDateListener);
			calEndDate.addPropertyChangeListener(endDateListener);
		}

		SpringLayout layout = new SpringLayout();

		layout.putConstraint(SpringLayout.WEST, labName, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labName, VERTICAL_PADDING,
				SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, labNameError,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labNameError,
				VERTICAL_PADDING, SpringLayout.SOUTH, labName);

		layout.putConstraint(SpringLayout.WEST, txtName, HORIZONTAL_PADDING,
				SpringLayout.EAST, labName);
		layout.putConstraint(SpringLayout.NORTH, txtName, VERTICAL_PADDING,
				SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST, labStartDate,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labStartDate,
				VERTICAL_PADDING, SpringLayout.SOUTH, labNameError);

		layout.putConstraint(SpringLayout.NORTH, labEndDate, 0,
				SpringLayout.NORTH, labStartDate); // align them, no padding

		layout.putConstraint(SpringLayout.NORTH, calStartDate,
				VERTICAL_PADDING, SpringLayout.SOUTH, labStartDate);
		layout.putConstraint(SpringLayout.WEST, calStartDate, 0,
				SpringLayout.WEST, labStartDate);

		layout.putConstraint(SpringLayout.NORTH, calEndDate, VERTICAL_PADDING,
				SpringLayout.SOUTH, labEndDate);
		layout.putConstraint(SpringLayout.WEST, calEndDate, HORIZONTAL_PADDING,
				SpringLayout.EAST, calStartDate);

		layout.putConstraint(SpringLayout.WEST, labCalendarError,
				HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labCalendarError,
				VERTICAL_PADDING, SpringLayout.SOUTH, calStartDate);

		layout.putConstraint(SpringLayout.WEST, labEndDate, 0,
				SpringLayout.WEST, calEndDate);

		layout.putConstraint(SpringLayout.EAST, txtName, 0, SpringLayout.EAST,
				calEndDate);

		if (status == Status.EDIT || status == Status.VIEW) {
			layout.putConstraint(SpringLayout.NORTH, labEstimate,
					VERTICAL_PADDING, SpringLayout.SOUTH, labCalendarError);
			layout.putConstraint(SpringLayout.WEST, labEstimate,
					HORIZONTAL_PADDING, SpringLayout.WEST, this);

			layout.putConstraint(SpringLayout.NORTH, txtEstimate, 0,
					SpringLayout.NORTH, labEstimate);
			layout.putConstraint(SpringLayout.WEST, txtEstimate,
					HORIZONTAL_PADDING, SpringLayout.EAST, labEstimate);

			layout.putConstraint(SpringLayout.NORTH, butSave, VERTICAL_PADDING,
					SpringLayout.SOUTH, labEstimate);

		} else {
			layout.putConstraint(SpringLayout.NORTH, butSave, VERTICAL_PADDING,
					SpringLayout.SOUTH, labCalendarError);
		}

		layout.putConstraint(SpringLayout.WEST, butSave, HORIZONTAL_PADDING,
				SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.WEST, butCancel, HORIZONTAL_PADDING,
				SpringLayout.EAST, butSave);
		layout.putConstraint(SpringLayout.NORTH, butCancel, 0,
				SpringLayout.NORTH, butSave);

		layout.putConstraint(SpringLayout.WEST, labErrorMessage,
				HORIZONTAL_PADDING, SpringLayout.EAST, butCancel);
		layout.putConstraint(SpringLayout.NORTH, labErrorMessage, 0,
				SpringLayout.NORTH, butCancel);

		layout.putConstraint(SpringLayout.VERTICAL_CENTER, labProgress, 0,
				SpringLayout.VERTICAL_CENTER, txtEstimate);

		layout.putConstraint(SpringLayout.WEST, labProgress,
				HORIZONTAL_PADDING, SpringLayout.EAST, txtEstimate);

		layout.putConstraint(SpringLayout.VERTICAL_CENTER, progressBar, 0,
				SpringLayout.VERTICAL_CENTER, labProgress);

		layout.putConstraint(SpringLayout.WEST, progressBar,
				HORIZONTAL_PADDING, SpringLayout.EAST, labProgress);

		setLayout(layout);

		// add all the components

		add(labName);
		add(txtName);

		add(labStartDate);
		add(labEndDate);

		if (status == Status.EDIT || status == Status.VIEW) {
			add(labEstimate);
			add(txtEstimate);
			add(labProgress);
			add(progressBar);
		}

		add(calStartDate);
		add(calEndDate);

		add(butSave);
		add(butCancel);

		add(labErrorMessage);
		add(labNameError);
		add(labCalendarError);

	}

	/**
	 * Action to save the given Iteration
	 * 
	 * @author Mitchell, Matt
	 * 
	 */

	private class SaveAction extends AbstractAction {

		private IterationView view;

		/**
		 * Creates a save action with the given view for a parent
		 * 
		 * @param view
		 */
		private SaveAction(IterationView view) {
			this.view = view;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// pull the values from the fields
			String name = txtName.getText().trim();
			Date startDate = calStartDate.getDate();
			Date endDate = calEndDate.getDate();

			if (status == Status.CREATE) {
				Iteration toAdd = new Iteration(name, startDate, endDate);
				AddIterationRequestObserver observer = new AddIterationRequestObserver(
						view);
				iterationController.create(toAdd, observer);
			} else {
				iteration.setName(name);
				iteration.setStartDate(startDate);
				iteration.setEndDate(endDate);
				UpdateIterationRequestObserver observer = new UpdateIterationRequestObserver(
						view);
				iterationController.save(iteration, observer);
			}

		}

	}

	/**
	 * Class for the cancel action of the cancel button Closes the current tab
	 * 
	 * @author Mitchell, Matt
	 * 
	 */

	private class CancelAction extends AbstractAction {

		private CancelAction() {
			super("Cancel");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			mainTabController.closeCurrentTab();
		}
	}

	public MainTabController getMainTabController() {
		return mainTabController;
	}

	/**
	 * Displays an error message when there is an issue with saving
	 * 
	 * @param error
	 *            The error to display
	 */

	public void displaySaveError(String error) {
		labErrorMessage.setText(error);
	}

	/**
	 * Determiens the proper error message to be shown in the Name Error field
	 * 
	 */

	private void setNameError() {
		if (txtName.getText().trim().isEmpty()) {
			labNameError.setText("**Name cannot be blank**");
			txtName.setBackground(new Color(243, 243, 209));
			nameError = true;
		} else if (!isNameUnique()) {
			labNameError.setText("**Iteration names must be unique**");
			txtName.setBackground(new Color(243, 243, 209));
			nameError = true;
		} else {
			labNameError.setText(" ");
			txtName.setBackground(Color.WHITE);
			nameError = false;
		}
	}

	/**
	 * Determines the proper error message to be shown in the Calendar Error
	 * Field
	 * 
	 */

	private void setCalendarError() {
		if (calStartDate.getDate().after(calEndDate.getDate())) {
			labCalendarError.setText("**Start date must be before End date**");
			calendarError = true;
		} else if (compareDatesWithoutTime(calStartDate.getDate(),
				calEndDate.getDate()) == 0) {
			// dates are equal
			labCalendarError
					.setText("**Start date and end date cannot be equal**");
			calendarError = true;
		} else if (doIterationsOverlapWithCurrent()) {
			calendarError = true;
			labCalendarError.setText("**Iterations cannot overlap**");
		} else {
			labCalendarError.setText(" ");
			calendarError = false;
		}
	}

	/**
	 * Called when IterationViewListener reports there has been a change
	 * 
	 * @param source
	 *            The source component
	 */

	public void updateSave(JComponent source) {
		setNameError();
		setCalendarError();

		if (nameError || calendarError) {
			butSave.setEnabled(false);
		} else if (status == Status.EDIT) {
			if (txtName.getText().trim().equals(iteration.getName())
					&& compareDatesWithoutTime(calStartDate.getDate(),
							iteration.getStartDate()) == 0
					&& compareDatesWithoutTime(calEndDate.getDate(),
							iteration.getEndDate()) == 0) {
				butSave.setEnabled(false);
			} else {
				butSave.setEnabled(true);
			}

		} else {
			butSave.setEnabled(true);
		}
	}

	/**
	 * Compares the two given dates based upon, year, month, and day
	 * 
	 * @param date1
	 *            The first date
	 * @param date2
	 *            The second date
	 * @return 0 if the dates are equal, -1 if date1 is before than date2, 1 if
	 *         date1 is after date 2
	 */

	public static int compareDatesWithoutTime(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		if (calendar1.get(Calendar.DAY_OF_MONTH) == calendar2
				.get(Calendar.DAY_OF_MONTH)
				&& calendar1.get(Calendar.MONTH) == calendar2
						.get(Calendar.MONTH)
				&& calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {

			// dates are equal
			return 0;
		}
		return date1.compareTo(date2);
	}

	/**
	 * Determines if the current Iteration being created / edited overlaps with
	 * the iterations in the cache of the server
	 * 
	 * @return True if the iterations overlap, false if they dont't
	 */

	private boolean doIterationsOverlapWithCurrent() {
		// get all iterations from the local database
		List<Iteration> iterations = IterationDatabase.getInstance().getAll();

		Date origStart = iteration.getStartDate();
		Date origEnd = iteration.getEndDate();

		iteration.setEndDate(calEndDate.getDate());
		iteration.setStartDate(calStartDate.getDate());

		for (Iteration i : iterations) {
			if (i.getId() == iteration.getId()) {
				continue;
			}
			if (IterationValidator.overlapExists(iteration, i)) {
				return true; // an overlap exists
			}
		}

		iteration.setStartDate(origStart);
		iteration.setEndDate(origEnd);

		return false;
	}

	/**
	 * Determines if the name being used, has been used for other requirements
	 * 
	 * @return True if it is unique, false if not
	 */

	private boolean isNameUnique() {
		// get the current name of this iteration
		String name = txtName.getText().trim();

		// get all iterations from the local database
		List<Iteration> iterations = IterationDatabase.getInstance().getAll();

		for (Iteration i : iterations) {
			if (i.getId() == iteration.getId()) {
				continue; // skip over the current (editing) iteration
			}
			if (i.getName().equals(name)) {
				// we have found an equal name, return false
				return false;
			}
		}

		return true;
	}

	@Override
	public void responseSuccess() {
		mainTabController.refreshSubReqView();
		mainTabController.refreshIterationTree();
		mainTabController.closeCurrentTab();
	}

	@Override
	public void responseError(int statusCode, String statusMessage) {
		displaySaveError("Server returned save error: " + statusCode + ":"
				+ statusMessage);

	}

	@Override
	public void fail(Exception exception) {
		displaySaveError("Server returned save error: "
				+ exception.getMessage());
	}

	public int getIterationId() {
		return iteration.getId();
	}

	@Override
	public boolean onTabClosed() {
		if (butSave.isEnabled()) {
			Object[] options = { "Save Changes", "Discard Changes", "Cancel" };
			int res = JOptionPane
					.showOptionDialog(
							this,
							"There are unsaved changes, are you sure you want to continue?",
							"Confirm Close", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[2]);

			if (res == 0) {
				butSave.getAction().actionPerformed(null);
			} else if (res == 1) {
				return true;
			} else {
				return false;
			}

		}
		return true;
	}
}
