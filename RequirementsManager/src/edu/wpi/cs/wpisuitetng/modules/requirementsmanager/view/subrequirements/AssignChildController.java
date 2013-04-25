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
	
	// The the subrequirement panel calling this controller
	private final SubRequirementPanel view;
	
	// the detail view that the subReqPanel is in
	private final DetailPanel detailView;
	
	// the requirement to assign children to
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
	public AssignChildController(final SubRequirementPanel subRequirementPanel,
			final Requirement model, final DetailPanel ChildView) {
		view = subRequirementPanel;
		this.model = model;
		detailView = ChildView;
	}
	
	/**
	 * Save a child requirement to the server. It gets the selected requirement
	 * from the subreq panel
	 * and makes the selected requirement a child of the passed requirement. It
	 * then makes the
	 * passed requirement linked to the selected requirement, to make sure there
	 * is a complete link
	 * from a parent to the child and vice versa
	 */
	public void saveChild() {
		
		final Requirement anReq = (Requirement) view.getList().getSelectedValue();
		
		final Integer modelID = new Integer(model.getrUID());
		final Integer anReqID = new Integer(anReq.getrUID());
		
		model.addSubRequirement(anReqID);
		
		anReq.addPUID(modelID);
		final RequirementsController controller = new RequirementsController();
		final UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
				detailView);
		controller.save(model, observer);
		controller.save(anReq, observer);
		
		view.refreshTopPanel();
		view.refreshValidChildren();
		
	}
	
}