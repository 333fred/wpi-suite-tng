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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.contoller;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.SubRequirementPanel;

/**
 * This controller controls the removing of parent requirements to another
 * requirement
 */

public class RemoveParentController {
	
	// The the subrequirement panel calling this controller
	private final SubRequirementPanel view;
	
	// the requirement to assign a parent to
	private final Requirement model;
	
	// the detail view that the subReqPanel is in
	private final DetailPanel detailView;
	
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
	public RemoveParentController(
			final SubRequirementPanel subRequirementPanel,
			final Requirement model, final DetailPanel parentView) {
		view = subRequirementPanel;
		this.model = model;
		detailView = parentView;
	}
	
	/**
	 * Save a parent subRequirement to the server. It removes the selected
	 * requirement as a parent
	 * of the passed requirement by removing all links between the two
	 * requirements.
	 */
	public void saveParent() {
		
		Requirement anReq = null;
		if (model.getpUID().size() != 0) {
			try {
				anReq = RequirementDatabase.getInstance().get(
						model.getpUID().get(0));
			} catch (final RequirementNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			final Integer modelID = new Integer(model.getrUID());
			final Integer anReqID = new Integer(anReq.getrUID());
			
			model.removePUID(anReqID);
			anReq.removeSubRequirement(modelID);
			
			final RequirementsController controller = new RequirementsController();
			final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
					detailView);
			controller.save(model, observer);
			controller.save(anReq, observer);
			
			view.refreshParentLabel();
			if (view.isParentSelected()) {
				view.refreshValidParents();
			} else {
				view.refreshValidChildren();
			}
			
		}
	}
	
}
