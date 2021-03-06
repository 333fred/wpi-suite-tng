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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on how many requirements are assigned to each status
 */
public class StatusRequirementStatistics extends AbstractRequirementStatistics {

	@Override
	public JFreeChart buildBarChart() {
		update();
		return this.buildBarChart("Requirements by Status", "Status",
				"Requirements");
	}

	@Override
	public JFreeChart buildLineChart() {
		update();
		return this.buildLineChart("Requirements by Status", "Status",
				"Requirements");
	}

	@Override
	public JFreeChart buildPieChart() {
		update();
		return this.buildPieChart("Requirements by Status");
	}

	@Override
	public void update() {
		final List<Requirement> requirements = RequirementDatabase
				.getInstance().getFilteredRequirements(); // refresh list of
															// requirements
		Map<String, Integer> mapData = new HashMap<String, Integer>();
		
		// for every possible status
		for (final Status status : Status.values()) {
			mapData.put(status.toString(), 0); // insert the status in the
											// data set with zero
											// counted requirements
		}
		// for every requirement in this project
		for (final Requirement requirement : requirements) {
			final Status status = requirement.getStatus();
			final Integer oldValue = mapData.get(status.toString());
			mapData.put(status.toString(), new Integer(oldValue.intValue() + 1));
			// increment the number of requirements for a given status
		}

		mapData.remove("BLANK");
		
		//parse into a list
		for (String key : mapData.keySet()) {
			data.add(new StringIntegerPair(key, mapData.get(key)));
		}
	}

}