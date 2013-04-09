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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
* class to contain data on how many requirements are assigned to each iteration
* note that user assignees here are stored as strings, as they are in Requirements themselves
*
*/

public class AssigneeRequirementStatistics extends AbstractRequirementStatistics {

/* (non-Javadoc)
* @see edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts.IRequirementStatistics#update()
*/
@Override
public void update(){

List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();	// refresh list of requirements TODO: is there a better way to do this?

// TODO: replace with a method to get all users, record them as zero-counts in the map, and then simply work through and increment
// for each requirement
for(Requirement requirement: requirements){

// for each set of assigned users
for(String user : requirement.getUsers()){

// if a user has not been encountered before, add him/her to the map
if(data.get(user) == null){
data.put(user, new Integer(1));	// note that this requirement is one to which the user is assigned!
}

// otherwise, simply increment the value
else{
Integer oldValue = (Integer)data.get(user);
data.put(user, new Integer(oldValue.intValue() + 1));
}

}

}

}

public JFreeChart buildPieChart(){
return this.buildPieChart("Requirements by User");
}

public JFreeChart buildBarChart(){
return this.buildBarChart("Requirements by User", "User");
}

}