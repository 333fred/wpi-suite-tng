/*******************************************************************************
* Copyright (c) 2013 -- WPI Suite: Team Swagasarus
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Maddie Burris
* Chris Keane
* 
*******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;

/**
* class to contain data on how many tasks are assigned to each requirement
* note that Tasks here are stored in lists, as they are in classes themselves
*
*/

public class TaskRequirementStatistics extends AbstractRequirementStatistics {

	/* (non-Javadoc)
	* @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.IRequirementStatistics#update()
	*/
	@Override
	public void update(){	
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();	// refresh list of requirements TODO: is there a better way to do this?		
		
		// for each requirement
		for(Requirement requirement: requirements){		
			// get the length of the list of tasks
			data.put(requirement.getName(), requirement.getTasks().size());
		}	
	}
	
	public JFreeChart buildPieChart(){
		return this.buildPieChart("Requirements by Task");
	}
	
	public JFreeChart buildBarChart(){
		return this.buildBarChart("Requirements by Tasks", "Tasks", "Requirements");
	}

}