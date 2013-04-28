/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		@author Chris Keane
 * 		@author Maddie Burris
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on estimate for each requirements
 */

public class EstimateRequirementStatistics extends
		AbstractRequirementStatistics {
	
	@Override
	public JFreeChart buildBarChart() {
		final JFreeChart barChart = this.buildBarChart(
				"Requirements by Estimate", "Requirement", "Estimate");
		return barChart;
	}
	
	@Override
	public JFreeChart buildLineChart() {
		update();
		return this.buildLineChart("Estimates by Requirements", "Requirement",
				"Estimate");
	}
	
	@Override
	public JFreeChart buildPieChart() {
		return this.buildPieChart("Requirements by Estimate");
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
			// get the estimate for each requirement
			data.add(new StringIntegerPair(requirement.getName(), requirement.getEstimate()));
			
		}
		
	}
	
}