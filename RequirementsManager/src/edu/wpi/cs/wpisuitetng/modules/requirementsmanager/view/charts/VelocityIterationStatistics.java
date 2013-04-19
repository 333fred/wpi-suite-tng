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
* note that user assignees here are stored as strings, as they are in Requirements themselves
*
*/


public class VelocityIterationStatistics extends AbstractRequirementStatistics {

	/* (non-Javadoc)
	* @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.IRequirementStatistics#update()
	*/
	@Override
	public void update(){
	
		List<Iteration> iterations = IterationDatabase.getInstance().getAll();	// refresh list of iterations
		
		for(Iteration anIteration: iterations){
			
			data.put(anIteration.getName(), anIteration.getEstimate());
		
		}
	
	}
	
	public JFreeChart buildLineChart(){
		this.update();
		return this.buildLineChart("Iterations by Actual Effort", "Iteration", "Effort");
	}
	
	public JFreeChart buildPieChart(){
		return this.buildPieChart("Iterations by Actual Effort");
	}
	
	public JFreeChart buildBarChart(){
		JFreeChart barChart = this.buildBarChart("Iterations by Actual Effort", "Iteration", "Effort");
		return barChart;
	}

}
