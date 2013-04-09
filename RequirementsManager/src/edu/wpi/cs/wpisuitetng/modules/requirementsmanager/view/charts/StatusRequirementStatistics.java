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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;

/**
* class to contain data on how many requirements are assigned to each status
*
*/
public class StatusRequirementStatistics extends AbstractRequirementStatistics {

public void update(){

List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();	// refresh list of requirements TODO: is there a better way to do this?

// for every possible status
for(Status status: Status.values()){
this.data.put(status.toString(), 0);	// insert the status in the data set with zero counted requirements
}

// for every requirement in this project
for(Requirement requirement : requirements){

Status status = requirement.getStatus();
Integer oldValue = this.data.get(status);
this.data.put(status.toString(), new Integer(oldValue.intValue() + 1));	// increment the number of requirements for a given status

}

}

public JFreeChart buildPieChart(){
return this.buildPieChart("Requirements by Status");
}

public JFreeChart buildBarChart(){
return this.buildBarChart("Requirements by Status", "Status");
}

}