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

public class RemoveParentController {
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
	public RemoveParentController(SubRequirementPanel subRequirementPanel,
			Requirement model, DetailPanel parentView) {
		this.view = subRequirementPanel;
		this.model = model;
		this.parentView = parentView;
	}

	/**
	 * Save a note to the server
	 */
	public void saveParent() {

		Requirement anReq = null;
		if (model.getpUID().size() != 0) {
			try {
				anReq = RequirementDatabase.getInstance().get(
						model.getpUID().get(0));
			} catch (RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Integer modelID = new Integer(model.getrUID());
			Integer anReqID = new Integer(anReq.getrUID());

			model.removePUID(anReqID);
			anReq.removeSubRequirement(modelID);

			RequirementsController controller = new RequirementsController();
			UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
					this.parentView);
			controller.save(model, observer);
			observer = new UpdateRequirementRequestObserver(
					new SaveOtherRequirement());
			controller.save(anReq, observer);

			view.refreshParentLabel();
			view.refreshTopPanel();
			if (view.parentSelected)
				view.refreshValidParents();
			else
				view.refreshValidChildren();

		}
	}

}
