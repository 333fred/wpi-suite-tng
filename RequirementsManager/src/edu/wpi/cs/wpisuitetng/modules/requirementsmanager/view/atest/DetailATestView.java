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
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.ATest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.ToggleSelectionModel;

/**
 * Panel containing a aTest for a requirement
 * 
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
@SuppressWarnings("serial")
public class DetailATestView extends JPanel {
	/** For aTests */
	protected DefaultListModel testList;
	protected JList tests;
	private Requirement requirement;
	private DetailPanel parentView;
	private MakeATestPanel makeATestPanel;
	private EventCellRenderer cellRenderer;
	private JScrollPane aTestScrollPane;

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
		makeATestPanel = new MakeATestPanel(requirement, parentView);

		// Create the aTest list TODO: CHANGE GETSELECTEDVALUES TO
		// GETSELECTEDVALUES
		testList = new DefaultListModel();
		tests = new JList(testList);
		cellRenderer = new EventCellRenderer();
		tests.setCellRenderer(cellRenderer);
		tests.setSelectionModel(new ToggleSelectionModel());

		// Add the list to the scroll pane
		aTestScrollPane = new JScrollPane();
		aTestScrollPane.getViewport().add(tests);

		// Set up the frame
		JPanel aTestPane = new JPanel();
		aTestPane.setLayout(new BorderLayout());
		aTestPane.add(aTestScrollPane, BorderLayout.CENTER);
		aTestPane.add(makeATestPanel, BorderLayout.SOUTH);

		add(aTestPane, BorderLayout.CENTER);

		// adds the aTests to the list model
		addaTestsToList();
		// ,aTests.getSelectedValues()

		if (requirement.getStatus() != Status.DELETED) {
			// Set the action of the save button to the default (create new
			// aTest)
			makeATestPanel.getAddaTest().setAction(
					new SaveATestAction(new SaveATestController(makeATestPanel,
							requirement, parentView, tests)));

			//Listen for user clicking on acceptance tests
			tests.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					updateaTestView();
				} 
			});

			makeATestPanel.getAddATest().setEnabled(false);
			// Make sure save button is unavailable if name field is empty
			makeATestPanel.getaTestField().addKeyListener(new KeyAdapter() {
				// For creating a new aTest
				@Override
				public void keyReleased(KeyEvent e) {
					if (makeATestPanel.getaTestField().getText().trim()
							.equals("")
							&& tests.getSelectedValues().length == 0)
						makeATestPanel.getAddaTest().setEnabled(false);
					else if (!makeATestPanel.getaTestName().getText().trim()
							.equals(""))
						makeATestPanel.getAddaTest().setEnabled(true);
				}
			});

			// Make sure save button is unavailable if desc field is empty
			// for creating a new aTest
			makeATestPanel.getaTestName().addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (makeATestPanel.getaTestName().getText().trim()
							.equals("")
							&& tests.getSelectedValues().length == 0)
						makeATestPanel.getAddaTest().setEnabled(false);
					else if (!makeATestPanel.getaTestField().getText().trim()
							.equals(""))
						makeATestPanel.getAddaTest().setEnabled(true);
				}
			});

		} else {
			// Requirement is set to delted, so disable all of the fields
			makeATestPanel.getaTestFieldPane().setEnabled(false);
			makeATestPanel.getaTestField().setEnabled(false);
			makeATestPanel.getaTestName().setEnabled(false);
			makeATestPanel.getAddaTest().setEnabled(false);
			makeATestPanel.getaTestStatus().setText("");

			makeATestPanel.getaTestField().setBackground(
					makeATestPanel.getBackground());
			makeATestPanel.getaTestName().setBackground(
					makeATestPanel.getBackground());
		}

	}

	/**
	 * updateaTestView
	 * 
	 * currently not used. Would be called by the timer to update the view aTest
	 * 
	 */
	private void updateaTestView() {
		if (requirement.getStatus() != Status.DELETED) {
			makeATestPanel.getAddaTest().setAction(
					new SaveATestAction(new SaveATestController(makeATestPanel,
							requirement, parentView, tests), tests
							.getSelectedValues()));

			if (tests.getSelectedValues().length == 0) {
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
								.equals(""))
					makeATestPanel.getAddaTest().setEnabled(false);
			} else {
				makeATestPanel.getaTestStatusBox().setEnabled(true);
				if (tests.getSelectedValues().length > 1) {
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
	 * updateaTestViewTime
	 * 
	 * currently not used. Would be called by the timer to update the view aTest
	 * and populate the fields
	 * 
	 */
	private void updateaTestViewTime() {
		if (requirement.getStatus() != Status.DELETED) {
			makeATestPanel.getAddaTest().setAction(
					new SaveATestAction(new SaveATestController(makeATestPanel,
							requirement, parentView, tests), tests
							.getSelectedValues()));

			if (tests.getSelectedValues().length == 0) {
				makeATestPanel
						.getaTestStatus()
						.setText(
								"No acceptance test selected. Fill name and description to create a new one.");
				makeATestPanel.getaTestStatusBox().setEnabled(false);
				makeATestPanel.getaTestField().setBackground(Color.white);
				makeATestPanel.getaTestName().setBackground(Color.white);
				if (makeATestPanel.getaTestName().getText().trim().equals("")
						|| makeATestPanel.getaTestField().getText().trim()
								.equals(""))
					makeATestPanel.getAddaTest().setEnabled(false);
			} else {
				makeATestPanel.getaTestStatusBox().setEnabled(true);
				if (tests.getSelectedValues().length > 1) {
					makeATestPanel
							.getaTestStatus()
							.setText(
									"Multiple acceptance tests selected. Can only change status.");
					makeATestPanel.getaTestFieldPane().setEnabled(false);
					makeATestPanel.getaTestField().setEnabled(false);
					makeATestPanel.getaTestName().setEnabled(false);
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
					makeATestPanel.getaTestField().setBackground(Color.white);
					makeATestPanel.getaTestName().setBackground(Color.white);
				}
			}
		}
	}

	/**
	 * 
	 * Method to populate this object's list of aTests from the current
	 * requirement's list of aTests
	 */
	private void addaTestsToList() {
		testList.clear();

		// add the aTests to the list model.
		for (ATest aaTest : requirement.getTests()) {
			this.testList.addElement(aaTest);
		}
	}

	/**
	 * simple getter for the list of aTests of which this view is currently
	 * aware
	 * 
	 * @return the list of aTests
	 */
	public DefaultListModel getaTestList() {
		return testList;
	}

	/**
	 * Updates the local display of the current requirement's aTests
	 * 
	 * @param newRequirement
	 *            the most recent version of the current requirement
	 */
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;
		// updates the aTests list
		addaTestsToList();
	}

	/**
	 * simple getter for the single currently selected aTest
	 * 
	 * @return the selected aTest
	 */
	public ATest getSingleSelectedaTest() {
		return (ATest) tests.getSelectedValue();
	}

	/**
	 * This function disables interaction with the aTests panel
	 */
	public void disableUserButtons() {
		makeATestPanel.setInputEnabled(false);
	}

	public MakeATestPanel getTestPanel() {
		// TODO Auto-generated method stub
		return makeATestPanel;
	}

	public void paint(Graphics g) {
		this.cellRenderer.setWrapWidth(this.aTestScrollPane.getViewport()
				.getWidth());
		super.paint(g);
	}
}
