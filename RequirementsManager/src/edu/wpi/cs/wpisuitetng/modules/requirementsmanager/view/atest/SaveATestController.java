/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.atest;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.ATest;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventTableModel;

/**
 * This controller handles saving requirement aTests to the server
 * 
 * @author Nick Massa, Matt Costi, Steve Kordell
 */
public class SaveATestController {

	private final MakeATestPanel view;
	private final Requirement model;
	private final DetailPanel parentView;
	private final EventTableModel testModel;
	private final EventTable testTable;

	/**
	 * Construct the controller
	 * 
	 * @param view
	 *            the MakeaTestPanel containing the aTest field
	 * @param model
	 *            the requirement to which aTests are being added
	 * @param parentView
	 *            the DetailPanel displaying the current requirement
	 */
	public SaveATestController(MakeATestPanel view, Requirement model,
			DetailPanel parentView, EventTable testTable) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
		this.testTable = testTable;
		this.testModel = (EventTableModel) testTable.getModel();
	}

	/**
	 * Save a aTest to the server
	 */
	public void saveaTest(int[] selectedRows) {
		final String testText = view.getaTestField().getText();
		final String testName = view.getaTestName().getText();
		if (selectedRows == null) { // Creating a aTest!
		} else if (selectedRows.length < 1) {
			// aTest must have a name and description of at least one character
			if (testText.length() > 0 && testName.length() > 0) {
				ATest tempTest = new ATest(testName, testText);
				tempTest.setId(this.model.getTests().size() + 1);
				this.model.addTest(tempTest);
				testModel.addEvent(tempTest);
				view.getaTestName().setText("");
				view.getaTestField().setText("");
				view.getaTestField().requestFocusInWindow();
				// We want to save the aTest to the server immediately, but only
				// if the requirement hasn't been just created
				if (model.getName().length() > 0) {
					// Save to requirement!
					RequirementsController controller = new RequirementsController();
					UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
							this.parentView);
					controller.save(model, observer);
				}
			}
		} else {

			// Modifying aTests
			
			List<ATest> selectedATests = new ArrayList<ATest>();
			
			for (int i : selectedRows) {
				selectedATests.add((ATest) testModel.getValueAt(i, 0));
			}
			
			for (Object aTest : selectedATests) {
				if (selectedATests.size() == 1) {
					// If only one is selected, edit the fields
					if (testText.length() > 0 && testName.length() > 0) {
						((ATest) aTest).setName(view.getaTestName().getText());
						((ATest) aTest).setDescription(view.getaTestField()
								.getText());
					}
				}
				// Check the completion status on the aTests
				((ATest) aTest).setStatus(ATest.ATestStatus
						.valueOf(view.getaTestStatusBox().getSelectedItem()
								.equals("") ? "BLANK" : view
								.getaTestStatusBox().getSelectedItem()
								.toString()));
			}

			// Save to requirement!
			if (model.getName().length() > 0) {
				RequirementsController controller = new RequirementsController();
				UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
						this.parentView);
				controller.save(model, observer);
			}
			view.getaTestName().setText("");
			view.getaTestField().setText("");
			view.getaTestField().requestFocusInWindow();
		}

		testTable.clearSelection();
		view.getaTestStatus()
				.setText(
						"No aTests selected. Fill name and description to create a new one.");
		view.getaTestStatusBox().setEnabled(false);
		view.getaTestStatusBox().setSelectedItem("");
		view.getaTestField().setEnabled(true);
		view.getaTestName().setEnabled(true);
		view.getaTestField().setText("");
		view.getaTestName().setText("");
		view.getaTestField().setBackground(Color.white);
		view.getaTestName().setBackground(Color.white);
		view.getAddaTest().setEnabled(false);

	}
}
