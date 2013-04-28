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
 * 		@author Steven Kordell
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * class to contain data on how many requirements are assigned to each iteration
 * note that user assignees here are stored as strings, as they are in
 * Requirements themselves
 * 
 */

public class AssigneeRequirementStatistics extends
		AbstractRequirementStatistics {
	
	@Override
	public JFreeChart buildBarChart() {
		return this
				.buildBarChart("Requirements by User", "Requirement", "User");
	}
	
	@Override
	public JFreeChart buildLineChart() {
		update();
		return this.buildLineChart("Requirements by User", "Requirement",
				"User");
	}
	
	@Override
	public JFreeChart buildPieChart() {
		return this.buildPieChart("Requirements by User");
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void update() {
		final List<Requirement> requirements = RequirementDatabase
				.getInstance().getFilteredRequirements(); // refresh list of
															// requirements
		// TODO: replace with a method to get all users, record them as
		// zero-counts in the map, and then simply work through and increment
		// for each requirement
		Map<String, Integer> mapData = new HashMap<String, Integer>();
		for (final Requirement requirement : requirements) {
			// for each set of assigned users
			for (final String user : requirement.getUsers()) {
				// if a user has not been encountered before, add him/her to the
				// map
				if (mapData.get(user) == null) {
					mapData.put(user, new Integer(1)); // note that this
													// requirement is one to
													// which the user is
													// assigned!
				}
				// otherwise, simply increment the value
				else {
					final Integer oldValue = mapData.get(user);
					mapData.put(user, new Integer(oldValue.intValue() + 1));
				}
			}
		}
		
		//parse into a list
		for (String key : mapData.keySet()) {
			data.add(new StringIntegerPair(key, mapData.get(key)));
		}
		
	}
	
}