/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Gorowara
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;

/**
 * class to contain data on how many requirements are assigned to each status
 *
 */
public class StatusRequirementStatistics {

	Map<Status, Integer> requirementsPerStatus;
	
	public StatusRequirementStatistics(){
		
		this.requirementsPerStatus = new HashMap<Status, Integer>();		
		this.update();
		
	}
	
	public void update(){
		
		List<Requirement> requirements = RequirementDatabase.getInstance().getAllRequirements();	// refresh list of requirements TODO: is there a better way to do this?
		
		// for every possible status
		for(Status status: Status.values()){
			this.requirementsPerStatus.put(status, new Integer(0));	// set the number of counted requirements with that status to zero
		}
		
		// for every requirement in this project
		for(Requirement requirement : requirements){
			
			Status status = requirement.getStatus();
			Integer oldValue = this.requirementsPerStatus.get(status);
			this.requirementsPerStatus.put(status, new Integer(oldValue.intValue() + 1));	// increment the number of requirements for a given status
			
		}
		
	}
	
}
