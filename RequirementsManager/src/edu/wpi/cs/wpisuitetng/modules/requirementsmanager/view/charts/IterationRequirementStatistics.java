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
*******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.List;

import org.jfree.chart.JFreeChart;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

/**
* class to contain data on how many requirements are assigned to each iteration
*
*/
public class IterationRequirementStatistics extends AbstractRequirementStatistics {

/* (non-Javadoc)
* @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.IRequirementStatistics#update()
*/
@Override
public void update(){

List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();	// refresh list of requirements TODO: is there a better way to do this?

// for every possible status
for(Iteration iteration: IterationDatabase.getInstance().getAllIterations()){
this.data.put(iteration.getName(), new Integer(0));	// set the number of counted requirements with that status to zero
}

// for every requirement in this project
for(Requirement requirement : requirements){

try{
Iteration iteration = IterationDatabase.getInstance().getIteration(requirement.getIteration());
Integer oldValue = this.data.get(iteration);
this.data.put(iteration.getName(), new Integer(oldValue.intValue() + 1));	// increment the number of requirements for a given iteration
}
catch(IterationNotFoundException e){
// do not account for iterations which do not exist
}

}

}

public JFreeChart buildPieChart(){
return this.buildPieChart("Requirements by Iteration");
}

public JFreeChart buildBarChart(){
return this.buildBarChart("Requirements by Iteration", "Iteration");
}

}