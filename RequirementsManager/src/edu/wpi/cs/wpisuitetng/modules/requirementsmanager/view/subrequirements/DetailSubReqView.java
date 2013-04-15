/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Nick Massa
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

public class DetailSubReqView extends JPanel{
	/** For Notes */

	private Requirement requirement;
	private DetailPanel parentView;
	private SubRequirementPanel makeSubReqPanel;

	/**
	 * Construct the panel and add layout components
	 * 
	 * @param requirement
	 *            the requirement
	 * @param parentView
	 *            the parent view
	 */
	public DetailSubReqView(Requirement requirement, DetailPanel parentView) {
		this.requirement = requirement;
		this.parentView = parentView;

		setLayout(new BorderLayout());
		// Set up the note panel
		makeSubReqPanel = new SubRequirementPanel(requirement, parentView);
	}
}
