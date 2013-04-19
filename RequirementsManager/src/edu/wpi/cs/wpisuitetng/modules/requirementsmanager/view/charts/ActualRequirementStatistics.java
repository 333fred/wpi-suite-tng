/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * *Chris Keane
 * *Maddie Burris
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on how effort spent on requirements
 * 
 */

public class ActualRequirementStatistics extends AbstractRequirementStatistics {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.
	 * IRequirementStatistics#update()
	 */
	@Override
	public void update() {

		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getAll(); // refresh list of requirements

		// for each requirement
		for (Requirement requirement : requirements) {
			// get amount of effort spent per requirement
			data.put(requirement.getName(), requirement.getEffort());

		}

	}

	public JFreeChart buildLineChart() {
		this.update();
		return this.buildLineChart("Requirements by Actual Effort",
				"Requirment", "Effort");
	}

	public JFreeChart buildPieChart() {
		return this.buildPieChart("Requirements by Actual Effort");
	}

	public JFreeChart buildBarChart() {
		JFreeChart barChart = this.buildBarChart(
				"Requirements by Actual Effort", "Requirement", "Effort");
		return barChart;
	}

}