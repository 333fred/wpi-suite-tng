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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.AddRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * Action to save a requirement.
 */
@SuppressWarnings ("serial")
public class SaveRequirementAction extends AbstractAction {
	
	private final Requirement requirement;
	
	private final DetailPanel parentView;
	
	/**
	 * Creates a new SaveRequirement action with the given DetailPanel view and
	 * requirement
	 * 
	 * @param requirement
	 *            the requirement to save
	 * @param parentView
	 *            the DetailPanel view
	 */
	public SaveRequirementAction(final Requirement requirement,
			final DetailPanel parentView) {
		super("Save Requirement");
		this.requirement = requirement;
		this.parentView = parentView;
	}
	
	/**
	 * Method to save a requirement if it is of acceptable format
	 * 
	 * @e any ActionEvent
	 */
	@Override
	public void actionPerformed(final ActionEvent e1) {
		final AddRequirementRequestObserver observer = new AddRequirementRequestObserver(
				parentView);
		final RequirementsController controller = new RequirementsController();
		
		if (!parentView.getTextName().getText().trim().equals("")) {
			parentView.getTextName().setBackground(Color.WHITE);
			parentView.getTextNameValid().setText("");
		}
		
		if (!parentView.getTextDescription().getText().trim().equals("")) {
			parentView.getTextDescription().setBackground(Color.WHITE);
			parentView.getTextDescriptionValid().setText("");
		}
		
		if (!parentView.getTextName().getText().trim().equals("")
				&& !parentView.getTextDescription().getText().trim().equals("")) {
			requirement.setName(parentView.getTextName().getText().trim());
			requirement.setDescription(parentView.getTextDescription()
					.getText());
			requirement.setUsers(parentView.getAssignedUsers());
			requirement.setReleaseNum(parentView.getTextRelease().getText());
			
			try {
				requirement.setIteration(-1);
				
				try {
					requirement.setPriority(Priority.valueOf(parentView
							.getComboBoxPriority().getSelectedItem().toString()
							.toUpperCase().replaceAll(" ", "_")));
				} catch (final IllegalArgumentException except) {
					requirement.setPriority(Priority.BLANK);
				}
				
				try {
					requirement.setType(Type.valueOf(parentView
							.getComboBoxType().getSelectedItem().toString()
							.toUpperCase().replaceAll(" ", "_")
							.replaceAll("-", "_")));
				} catch (final IllegalArgumentException except) {
					requirement.setType(Type.BLANK);
				}
				
				requirement.setStatus(Status.valueOf(parentView
						.getComboBoxStatus().getSelectedItem().toString()
						.toUpperCase().replaceAll(" ", "_")));
				
				controller.create(requirement, observer);
			} catch (final NumberFormatException except) {
				parentView
						.displaySaveError("Iteration must be an integer value");
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
	
	/**
	 * Gets the requirement that this action saves
	 * 
	 * @return the requirement
	 */
	public Requirement getRequirement() {
		return requirement;
	}
}
