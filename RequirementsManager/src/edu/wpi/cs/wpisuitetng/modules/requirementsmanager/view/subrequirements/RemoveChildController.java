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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

public class RemoveChildController {
	
	//The the subrequirement panel calling this controller
	private final SubRequirementPanel view;
	
	//the detail view that the subReqPanel is in
	private final DetailPanel detailView;
	
	//the requirement to assign a parent to
	private final Requirement model;
	
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
		this.detailView = ChildView;
	}

	/**
	 * Save a child subRequirement to the server. It removes the selected requirement as a child 
	 * of the passed requirement by removing all links between the two requirements.
	 */
	public void saveChild() {

		String selectedIndex = (String) view.getListSubReq().getSelectedValue();
		Requirement anReq = RequirementDatabase.getInstance().getRequirement(
				selectedIndex);

		Integer modelID = new Integer(model.getrUID());
		Integer anReqID = new Integer(anReq.getrUID());

		model.removeSubRequirement(anReqID);
		anReq.removePUID(modelID);

		RequirementsController controller = new RequirementsController();
		UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
				this.detailView);
		controller.save(model, observer);
		// observer = new UpdateRequirementRequestObserver(
		// new SaveOtherRequirement());
		controller.save(anReq, observer);

		view.refreshTopPanel();
		view.refreshParentLabel();
		if (view.isParentSelected()) {
			view.refreshValidParents();
		} else {
			view.refreshValidChildren();
		}

	}

}
