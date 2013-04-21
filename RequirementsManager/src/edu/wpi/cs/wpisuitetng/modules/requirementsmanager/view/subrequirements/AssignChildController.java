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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

public class AssignChildController {
	private final SubRequirementPanel view;
	private final Requirement model;
	private final DetailPanel ChildView;

	/**
	 * Construct the controller
	 * 
	 * @param subRequirementPanel
	 *            the panel holding this
	 * @param model
	 *            the requirement
	 * @param ChildView
	 *            the DetailPanel
	 */
	public AssignChildController(SubRequirementPanel subRequirementPanel,
			Requirement model, DetailPanel ChildView) {
		this.view = subRequirementPanel;
		this.model = model;
		this.ChildView = ChildView;
	}

	/**
	 * Save a child requirement to the server
	 */
	public void saveChild() {

		String selectedIndex = (String) view.getList().getSelectedValue();
		Requirement anReq = RequirementDatabase.getInstance().getRequirement(
				selectedIndex);

		Integer modelID = new Integer(model.getrUID());
		Integer anReqID = new Integer(anReq.getrUID());

		model.addSubRequirement(anReqID);
		
		anReq.addPUID(modelID);
		RequirementsController controller = new RequirementsController();
		UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
				this.ChildView);
		controller.save(model, observer);
		//observer = new UpdateRequirementRequestObserver(
		//		new SaveOtherRequirement());
		controller.save(anReq, observer);

		view.refreshTopPanel();
		view.refreshValidChildren();

	}

}