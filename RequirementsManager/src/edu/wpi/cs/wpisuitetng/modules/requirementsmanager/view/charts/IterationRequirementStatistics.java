/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alex Gorowara
 * Steve Kordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on how many requirements are assigned to each iteration
 * 
 */
public class IterationRequirementStatistics extends
		AbstractRequirementStatistics {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.
	 * IRequirementStatistics#update()
	 */
	@Override
	public void update() {

		List<Requirement> requirements = RequirementDatabase.getInstance()
				.getFilteredRequirements(); // refresh list of requirements

		// for every possible status
		for (Iteration iteration : IterationDatabase.getInstance()
				.getAll()) {
			this.data.put(iteration.getName(), new Integer(0)); // set the
																// number of
																// counted
																// requirements
																// with that
																// status to
																// zero
		}

		// for every requirement in this project
		for (Requirement requirement : requirements) {

			try {
				Iteration iteration = IterationDatabase.getInstance()
						.get(requirement.getIteration());
				Integer oldValue = this.data.get(iteration.getName());
				this.data.put(iteration.getName(),
						new Integer(oldValue.intValue() + 1)); // increment the
																// number of
																// requirements
																// for a given
																// iteration
			} catch (IterationNotFoundException e) {
				System.out
						.println("Iteration wasn't found, disregarding: IterationRequirementStatistics:54");
			}

		}

	}

	public JFreeChart buildLineChart() {
		this.update();
		return this.buildLineChart("Requirements by Iteration", "Iteration",
				"Requirements");
	}

	public JFreeChart buildPieChart() {
		this.update();
		return this.buildPieChart("Requirements by Iteration");
	}

	public JFreeChart buildBarChart() {
		this.update();
		return this.buildBarChart("Requirements by Iteration", "Iteration",
				"Requirements");
	}

}