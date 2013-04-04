/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Chris Keane
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.DefaultSaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * The action for saving an edited requirement, used when "Save Requirement"
 * button is pressed
 * 
 * @author Chris
 * 
 */
public class EditRequirementAction extends AbstractAction {

	private Requirement requirement;
	private DetailPanel parentView;

	/**
	 * Constructor for EditRequirementAction Parent view is used to interact
	 * with the GUI Requirement is used to update the requirement being edited
	 * 
	 * @param requirement
	 * @param parentView
	 */
	public EditRequirementAction(Requirement requirement, DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}

	/**
	 * This function is called when the save requirement button is pressed on a
	 * requirement being edited It validates input in sets responses to the user
	 * if an error is made
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		SaveRequirementController controller = new SaveRequirementController(
				this.parentView);

		// Checks to make sure the name entered is valid and updates the GUI if
		// it is
		if (!parentView.getTextName().getText().trim().equals("")) {
			parentView.getTextName().setBackground(Color.WHITE);
			parentView.getTextNameValid().setText("");
		}

		// Checks to make sure the description entered is valid and updates the
		// GUI if it is
		if (!parentView.getTextDescription().getText().trim().equals("")) {
			parentView.getTextDescription().setBackground(Color.WHITE);
			parentView.getTextDescriptionValid().setText("");
		}

		if (!parentView.getTextName().getText().trim().equals("")
				&& !parentView.getTextDescription().getText().trim().equals("")) {
			// Checks to make sure that both the name and descriptions are not
			// empty, and attempts to save the requirements
			requirement.setName(parentView.getTextName().getText().trim());
			requirement.setDescription(parentView.getTextDescription()
					.getText());
			requirement.setUsers(parentView.getAssignedUsers());
			requirement.setReleaseNum(parentView.getTextRelease().getText());
			requirement.setEffort(Integer.parseInt(parentView.getTextActual().getText()));

			if (parentView.getComboBoxStatus().getSelectedItem().toString().equals("Deleted")) {
				parentView.getComboBoxIteration().setSelectedItem("Backlog");
			}
			
			try {

				SaveIterationController saveIterationController = new SaveIterationController(
						new DefaultSaveNotifier());

				try {
					Iteration anIteration = IterationDatabase.getInstance()
							.getIteration(requirement.getIteration());
					anIteration.removeRequirement(requirement.getrUID());
					saveIterationController.saveIteration(anIteration);
				} catch (IterationNotFoundException e1) {
					e1.printStackTrace();
				} catch (RequirementNotFoundException ex){
					ex.printStackTrace();
				}

				requirement.setIteration(IterationDatabase
						.getInstance()
						.getIteration(
								parentView.getTextIteration().getSelectedItem()
										.toString()).getId());
				Iteration anIteration = IterationDatabase.getInstance()
						.getIteration(
								parentView.getTextIteration().getSelectedItem()
										.toString());
				anIteration.addRequirement(requirement.getrUID());
				saveIterationController.saveIteration(anIteration);

				try {
					requirement.setPriority(Priority.valueOf(parentView
							.getComboBoxPriority().getSelectedItem().toString()
							.toUpperCase().replaceAll(" ", "_")));
				} catch (IllegalArgumentException except) {
					requirement.setPriority(Priority.BLANK);
				}

				try {
					requirement.setType(Type.valueOf(parentView
							.getComboBoxType().getSelectedItem().toString()
							.toUpperCase().replaceAll(" ", "_").replaceAll("-", "_")));
				} catch (IllegalArgumentException except) {
					requirement.setType(Type.BLANK);
				}

				requirement.setStatus(Status.valueOf(parentView
						.getComboBoxStatus().getSelectedItem().toString()
						.toUpperCase().replaceAll(" ", "_")));

				try {
					requirement.setEstimate(Integer.parseInt(parentView
							.getTextEstimate().getText()));
				} catch (NumberFormatException except) {
					// not necessary to do anything
				}

				controller.SaveRequirement(requirement, true);
				parentView.closeTabAfterSave();
			} catch (NumberFormatException except) {
				parentView
						.displaySaveError("Iteration must be an integer value");
			} catch (RequirementNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			if (parentView.getTextName().getText().trim().equals("")) {
				parentView.getTextName()
						.setBackground(new Color(243, 243, 209));
				parentView.getTextNameValid().setText(
						"** Field must be non-blank **");
			}
			if (parentView.getTextDescription().getText().trim().equals("")) {
				parentView.getTextDescription().setBackground(
						new Color(243, 243, 209));
				parentView.getTextDescriptionValid().setText(
						"** Field must be non-blank **");

			}
		}
	}

}
