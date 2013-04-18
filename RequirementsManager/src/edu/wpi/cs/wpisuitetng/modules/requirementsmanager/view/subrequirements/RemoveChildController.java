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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

public class RemoveChildController {
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
	public RemoveChildController(SubRequirementPanel subRequirementPanel,
			Requirement model, DetailPanel ChildView) {
		this.view = subRequirementPanel;
		this.model = model;
		this.ChildView = ChildView;
	}

	/**
	 * Save a subRequirementt to the server
	 */
	public void saveChild() {

		String selectedIndex = (String) view.getListSubReq().getSelectedValue();
		Requirement anReq = RequirementDatabase.getInstance().getRequirement(
				selectedIndex);

		Integer modelID = new Integer(model.getrUID());
		Integer anReqID = new Integer(anReq.getrUID());

		model.removeSubRequirement(anReqID);
		anReq.removePUID(modelID);
		SaveRequirementController controller = new SaveRequirementController(
				this.ChildView);
		controller.SaveRequirement(model, false);
		controller = new SaveRequirementController(new SaveOtherRequirement());
		controller.SaveRequirement(anReq, false);

		view.refreshTopPanel();
		view.refreshParentLabel();
		if(view.parentSelected)
			view.refreshValidParents();
		else
			view.refreshValidChildren();

	}

}
