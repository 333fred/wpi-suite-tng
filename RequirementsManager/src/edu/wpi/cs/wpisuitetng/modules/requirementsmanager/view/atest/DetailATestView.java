/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick, Conor, Matt
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.ATest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTableModel;

/**
 * Panel containing a aTest for a requirement
 * 
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
@SuppressWarnings ("serial")
public class DetailATestView extends JPanel {

	/** For aTests */
	protected EventTableModel testModel;
	protected EventTable testTable;
	private Requirement requirement;
	private final DetailPanel parentView;
	private final MakeATestPanel makeATestPanel;
	private final JScrollPane aTestScrollPane;

	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailATestView(final Requirement requirement,
			final DetailPanel parentView) {

		this.requirement = requirement;
		this.parentView = parentView;

		setLayout(new BorderLayout());
		// Set up the aTest panel
		makeATestPanel = new MakeATestPanel(requirement, this);
		// Create the aTest list TODO: CHANGE GETSELECTEDVALUES TO
		// GETSELECTEDVALUES
		testModel = new EventTableModel(new ArrayList<Event>());
		testTable = new EventTable(testModel);

		testTable.getTableHeader().setVisible(false);
		// Add the list to the scroll pane
		aTestScrollPane = new JScrollPane();
		aTestScrollPane.getViewport().add(testTable);

		// Set up the frame
		final JPanel aTestPane = new JPanel();
		aTestPane.setLayout(new BorderLayout());
		aTestPane.add(aTestScrollPane, BorderLayout.CENTER);
		aTestPane.add(makeATestPanel, BorderLayout.SOUTH);

		add(aTestPane, BorderLayout.CENTER);

		// adds the aTests to the list model
		addaTestsToList();

		if (requirement.getStatus() != Status.DELETED) {
			// Set the action of the save button to the default (create new
			// aTest)
			makeATestPanel.getAddATest().setAction(
					new SaveATestAction(new SaveATestController(makeATestPanel,
							requirement, parentView, testTable)));

			// Listen for user clicking on acceptance tests

			testTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(final ListSelectionEvent e) {
							updateaTestView();
						}
					});

			makeATestPanel.getAddATest().setEnabled(false);
			// Make sure save button is unavailable if name field is empty
			makeATestPanel.getaTestField().addKeyListener(new KeyAdapter() {

				// For creating a new aTest
				@Override
				public void keyReleased(final KeyEvent e) {
					if (makeATestPanel.getaTestField().getText().trim()
							.equals("")
							&& (testTable.getSelectedRowCount() == 0)) {
						makeATestPanel.getAddATest().setEnabled(false);
					} else if (!makeATestPanel.getaTestName().getText().trim()
							.equals("")) {
						makeATestPanel.getAddATest().setEnabled(true);
					}
				}
			});

			// Make sure save button is unavailable if desc field is empty
			// for creating a new aTest
			makeATestPanel.getaTestName().addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(final KeyEvent e) {
					if (makeATestPanel.getaTestName().getText().trim()
							.equals("")
							&& (testTable.getSelectedRowCount() == 0)) {
						makeATestPanel.getAddATest().setEnabled(false);
					} else if (!makeATestPanel.getaTestField().getText().trim()
							.equals("")) {
						makeATestPanel.getAddATest().setEnabled(true);
					}
				}
			});

		} else {
			// Requirement is set to delted, so disable all of the fields
			makeATestPanel.getaTestFieldPane().setEnabled(false);
			makeATestPanel.getaTestField().setEnabled(false);
			makeATestPanel.getaTestName().setEnabled(false);
			makeATestPanel.getAddATest().setEnabled(false);
			makeATestPanel.getaTestStatus().setText("");

			makeATestPanel.getaTestField().setBackground(
					makeATestPanel.getBackground());
			makeATestPanel.getaTestName().setBackground(
					makeATestPanel.getBackground());
		}

	}

	/**
	 * 
	 * Method to populate this object's list of aTests from the current
	 * requirement's list of aTests
	 */
	private void addaTestsToList() {
		final List<Event> tests = new ArrayList<Event>();

		// add the aTests to the list model.
		for (final ATest aTest : requirement.getTests()) {
			tests.add(aTest);
		}

		testModel.setRowData(tests);

	}

	/**
	 * This function disables interaction with the aTests panel
	 */
	public void disableUserButtons() {
		makeATestPanel.setInputEnabled(false);
	}

	/**
	 * simple getter for the list of aTests of which this view is currently
	 * aware
	 * 
	 * @return the list of aTests
	 */
	public List<ATest> getaTestList() {
		final List<ATest> tests = new ArrayList<ATest>();

		for (final Event e : testModel.getRowData()) {
			tests.add((ATest) e);
		}

		return tests;
	}

	/**
	 * simple getter for the single currently selected aTest
	 * 
	 * @return the selected aTest
	 */
	public ATest getSingleSelectedaTest() {
		return (ATest) testModel.getValueAt(testTable.getSelectedRow(), 0);
	}

	/**
	 * Gets the test panel for this view
	 * 
	 * @return the panel
	 */
	public MakeATestPanel getTestPanel() {
		return makeATestPanel;
	}

	/**
	 * updateaTestView
	 * 
	 * currently not used. Would be called by the timer to update the view aTest
	 * 
	 */
	private void updateaTestView() {
		if (requirement.getStatus() != Status.DELETED) {
			makeATestPanel.getAddATest().setAction(
					new SaveATestAction(new SaveATestController(makeATestPanel,
							requirement, parentView, testTable), testTable
							.getSelectedRows()));

			if (testTable.getSelectedRowCount() == 0) {
				makeATestPanel
				.getaTestStatus()
				.setText(
						"No acceptance tests selected. Fill name and description to create a new one.");
				makeATestPanel.getaTestStatusBox().setEnabled(false);
				makeATestPanel.getaTestStatusBox().setSelectedItem("");
				makeATestPanel.getaTestField().setText("");
				makeATestPanel.getaTestName().setText("");
				makeATestPanel.getaTestField().setBackground(Color.white);
				makeATestPanel.getaTestName().setBackground(Color.white);
				if (makeATestPanel.getaTestName().getText().trim().equals("")
						|| makeATestPanel.getaTestField().getText().trim()
						.equals("")) {
					makeATestPanel.getAddATest().setEnabled(false);
				}
			} else {
				makeATestPanel.getaTestStatusBox().setEnabled(true);
				if (testTable.getSelectedRowCount() > 1) {
					makeATestPanel
					.getaTestStatus()
					.setText(
							"Multiple acceptance tests selected. Can only change status.");
					makeATestPanel.getaTestFieldPane().setEnabled(false);
					makeATestPanel.getaTestField().setEnabled(false);
					makeATestPanel.getaTestName().setEnabled(false);
					makeATestPanel.getaTestStatusBox().setSelectedItem("");
					makeATestPanel.getaTestField().setText("");
					makeATestPanel.getaTestName().setText("");
					makeATestPanel.getaTestField().setBackground(
							makeATestPanel.getBackground());
					makeATestPanel.getaTestName().setBackground(
							makeATestPanel.getBackground());
				} else {
					makeATestPanel
					.getaTestStatus()
					.setText(
							"One acceptance test selected. Fill name AND description to edit. Leave blank to just change status/user.");
					makeATestPanel.getaTestFieldPane().setEnabled(true);
					makeATestPanel.getaTestField().setEnabled(true);
					makeATestPanel.getaTestName().setEnabled(true);
					makeATestPanel.getaTestField().setText(
							getSingleSelectedaTest().getDescription());
					makeATestPanel.getaTestName().setText(
							getSingleSelectedaTest().getName());
					makeATestPanel.getaTestStatusBox().setSelectedItem(
							getSingleSelectedaTest().getStatus().toString());
					makeATestPanel.getaTestField().setBackground(Color.white);
					makeATestPanel.getaTestName().setBackground(Color.white);
				}
			}
		}
	}

	/**
	 * Updates the local display of the current requirement's aTests
	 * 
	 * @param newRequirement
	 *            the most recent version of the current requirement
	 */
	public void updateRequirement(final Requirement newRequirement) {
		requirement = newRequirement;
		// updates the aTests list
		addaTestsToList();
	}

	/** Clears the selected ATests
	 * 
	 */

	public void clearSelection() {
		testTable.getSelectionModel().clearSelection();
	}
}
