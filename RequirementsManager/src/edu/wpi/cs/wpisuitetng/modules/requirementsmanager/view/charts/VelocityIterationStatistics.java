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
 *
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

/**
 * class to contain data on how many requirements are assigned to each iteration
 * note that user assignees here are stored as strings, as they are in
 * Requirements themselves
 * 
 */

public class VelocityIterationStatistics extends AbstractRequirementStatistics {

	@Override
	public JFreeChart buildBarChart() {
		final JFreeChart barChart = this.buildBarChart(
				"Iterations by Actual Effort", "Iteration", "Effort");
		return barChart;
	}

	@Override
	public JFreeChart buildLineChart() {
		update();
		return this.buildLineChart("Iterations by Actual Effort", "Iteration",
				"Effort");
	}

	@Override
	public JFreeChart buildPieChart() {
		return this.buildPieChart("Iterations by Actual Effort");
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public void update() {

		List<Iteration> iterations = IterationDatabase.getInstance()
				.getAll(); // refresh list of iterations
		
		//lets sort the iterations by start date		
		iterations = Iteration.sortIterations(iterations);

		for (final Iteration anIteration : iterations) {

			if (anIteration.getId() != -1 && anIteration.getId() != -2) {
				System.out.println("Adding an iteration to chart: " + anIteration.getName());
				data.put(anIteration.getName(), anIteration.getEstimate());
			}
		}

	}

}
