/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Nick, Matt
 * 		
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

public class AssignParentController {
	private final SubRequirementPanel view;
	private final Requirement model;
	private final DetailPanel parentView;

	/**
	 * Construct the controller
	 * 
	 * @param subRequirementPanel
	 *            the panel holding this
	 * @param model
	 *            the requirement
	 * @param parentView
	 *            the DetailPanel
	 */
	public AssignParentController(SubRequirementPanel subRequirementPanel,
			Requirement model, DetailPanel parentView) {
		this.view = subRequirementPanel;
		this.model = model;
		this.parentView = parentView;
	}

	/**
	 * Save a note to the server
	 */
	public void saveParent() {
		String selectedIndex = (String) view.getList().getSelectedValue();
		Requirement anReq = RequirementDatabase.getInstance().getRequirement(
				selectedIndex);
		Requirement anParReq = null;

		Integer modelID = new Integer(model.getrUID());
		Integer anReqID = new Integer(anReq.getrUID());

		RequirementsController controller = null;

		if (model.getpUID().size() > 0) {
			try {
				anParReq = RequirementDatabase.getInstance().getRequirement(
						model.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
			anParReq.getSubRequirements().remove(modelID);
			model.getpUID().remove(0);
			controller = new RequirementsController();
			UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
					new SaveOtherRequirement());
			controller.save(model, observer);
		}

		model.addPUID(anReqID);
		anReq.addSubRequirement(modelID);

		controller = new RequirementsController();
		UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
				this.parentView);
		controller.save(model, observer);
		observer = new UpdateRequirementRequestObserver(
				new SaveOtherRequirement());
		controller.save(model, observer);

		view.refreshTopPanel();
		view.refreshValidParents();
		view.refreshParentLabel();
	}

}