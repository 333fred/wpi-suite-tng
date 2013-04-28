/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		@author Maddie Burris
 * 		@author Chris Keane
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on how many tasks are assigned to each requirement note
 * that Tasks here are stored in lists, as they are in classes themselves
 * 
 */

public class TaskRequirementStatistics extends AbstractRequirementStatistics {
	
	@Override
	public JFreeChart buildBarChart() {
		return this.buildBarChart("Requirements by Task", "Requirement",
				"Tasks");
	}
	
	@Override
	public JFreeChart buildLineChart() {
		return this.buildLineChart("Requirements by Task", "Requirement",
				"Tasks");
	}
	
	@Override
	public JFreeChart buildPieChart() {
		return this.buildPieChart("Requirements by Task");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		final List<Requirement> requirements = RequirementDatabase
				.getInstance().getFilteredRequirements(); // refresh list of
															// requirements
		
		// for each requirement
		for (final Requirement requirement : requirements) {
			// get the length of the list of tasks
			data.add(new StringIntegerPair(requirement.getName(),requirement.getTasks().size()));
		}
	}
	
}