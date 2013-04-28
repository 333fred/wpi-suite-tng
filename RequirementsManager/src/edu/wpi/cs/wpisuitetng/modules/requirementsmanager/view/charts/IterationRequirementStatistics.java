/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		@author Alex Gorowara
 * 		@author Steve Kordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on how many requirements are assigned to each iteration
 */
public class IterationRequirementStatistics extends
		AbstractRequirementStatistics {
	
	@Override
	public JFreeChart buildBarChart() {
		update();
		return this.buildBarChart("Requirements by Iteration", "Iteration",
				"Requirements");
	}
	
	@Override
	public JFreeChart buildLineChart() {
		update();
		return this.buildLineChart("Requirements by Iteration", "Iteration",
				"Requirements");
	}
	
	@Override
	public JFreeChart buildPieChart() {
		update();
		return this.buildPieChart("Requirements by Iteration");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update() {
		
		final List<Requirement> requirements = RequirementDatabase
				.getInstance().getFilteredRequirements(); // refresh list of
															// requirements
		
		Map<String, Integer> mapData = new HashMap<String, Integer>();
		
		// for every possible status
		for (final Iteration iteration : IterationDatabase.getInstance()
				.getAll()) {
			mapData.put(iteration.getName(), new Integer(0)); // set the
															// number of
															// counted
															// requirements
															// with that
															// status to
															// zero
		}
		
		// for every requirement in this project
		for (final Requirement requirement : requirements) {
			
			try {
				final Iteration iteration = IterationDatabase.getInstance()
						.get(requirement.getIteration());
				final Integer oldValue = mapData.get(iteration.getName());
				mapData.put(iteration.getName(), new Integer(
						oldValue.intValue() + 1)); // increment the
													// number of
													// requirements
													// for a given
													// iteration
			} catch (final IterationNotFoundException e) {
				System.out
						.println("Iteration wasn't found, disregarding: IterationRequirementStatistics:54");
			}
			
		}
		
		
		//parse into a list
		for (String key : mapData.keySet()) {
			data.add(new StringIntegerPair(key, mapData.get(key)));
		}
		
	}
	
}