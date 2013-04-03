/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.InvalidDateException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;

/**
 * Class representing an iteration of a project
 * 
 * @author Jason Whitehouse
 */
public class Iteration extends AbstractModel {

	private String name;
	private Date startDate;
	private Date endDate;
	private int id;
	private List<Integer> requirements;
	private int estimate;	// the sum of the estimates of all requirements

	/**
	 * Creates a blank iteration with empty name, start, end, id, and
	 * requirements
	 */
	public Iteration() {
		this.name = "";
		this.startDate = null;
		this.endDate = null;
		this.id = -1;
		this.requirements = new ArrayList<Integer>();
		this.estimate = 0;
	}
	/**
	 * Constructor for an iteration with the given start, and end It has a
	 * blank list of requirements
	 * 
	 * @param name
	 *            the name of the iteration
	 * @param startDate
	 *            Start date of the iteration
	 * @param endDate
	 *            End date of the iteration. Must be later than the
	 */
	public Iteration(String name, Date startDate, Date endDate) {
		this(name, startDate, endDate, -1);
	}	

	/**
	 * Constructor for an iteration with the given start, end, and id. It has a
	 * blank list of requirements
	 * 
	 * @param name
	 *            the name of the iteration
	 * @param startDate
	 *            Start date of the iteration
	 * @param endDate
	 *            End date of the iteration. Must be later than the
	 * @param id
	 *            ID num of the iteration
	 */
	public Iteration(String name, Date startDate, Date endDate, int id) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		requirements = new ArrayList<Integer>();
		this.estimate = 0;
	}

	/**
	 * Creates a Iteration with all given inputs except for estimate
	 * 
	 * @param name
	 *            the name of the iteration
	 * @param startDate
	 *            the start date of the iteration
	 * @param endDate
	 *            the end date of the iteration
	 * @param id
	 *            the id of the iteration
	 * @param reqs
	 *            the requirements assigned to this iteration
	 */
	public Iteration(String name, Date startDate, Date endDate, int id,
			List<Integer> reqs) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		this.requirements = reqs;
		this.estimate = 0;
	}
	
	/**
	 * Creates a Iteration with all given inputs
	 * 
	 * @param name
	 *            the name of the iteration
	 * @param startDate
	 *            the start date of the iteration
	 * @param endDate
	 *            the end date of the iteration
	 * @param id
	 *            the id of the iteration
	 * @param reqs
	 *            the requirements assigned to this iteration
	 */
	public Iteration(String name, Date startDate, Date endDate, int id,
			List<Integer> reqs, int estimate) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		this.requirements = reqs;
		this.estimate = estimate;
	}

	// TODO implement model methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates a JSON string representing this class
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Iteration.class);
	}

	/**
	 * Converts a JSON encoded iteration to a instantiated iteration
	 * 
	 * @param content
	 *            the JSON encoded iteration
	 * @return the instantiated iteration
	 */
	public static Iteration fromJSON(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Iteration.class);
	}

	/**
	 * Convert a JSON encoded Iteration array to a instantiated iteration array
	 * 
	 * @param content
	 *            the encoded array
	 * @return the real array
	 */
	public static Iteration[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Iteration[].class);
	}

	/**
	 * Adds a given requirement to this iteration and add its estimate to this iteration's estimate
	 * 
	 * @param rUID
	 *            the new requirement
	 * @throws RequirementNotFOundException
	 *            if rUID does not correspond to an existing requirement
	 */
	public void addRequirement(int rUID) throws RequirementNotFoundException {

		try{
			Requirement requirement = RequirementDatabase.getInstance().getRequirement(rUID);
			this.requirements.add(rUID);
			this.estimate += requirement.getEstimate();
		}
		catch(RequirementNotFoundException e){
			throw e;
		}
 
	}

	/**
	 * Removes a given requirement from this iteration
	 * 
	 * @param rUID
	 *            the removed requirement
	 * @throws RequirementNotFoundException
	 *            if rUID does not correspond to an existing requirement (will still remove the rUID from the list)
	 *            or if rUID is not in this iteration's list of requirements
	 */
	public void removeRequirement(int rUID) throws RequirementNotFoundException {
	
		for (Integer id : this.requirements) {
		
			if (id == rUID) {
		
				// regardless of whether or not the requirement exists, remove its ID
				this.requirements.remove(id);
				
				try{
					Requirement requirement = RequirementDatabase.getInstance().getRequirement(rUID);
					this.estimate -= requirement.getEstimate();
					return;
				}
				
				catch(RequirementNotFoundException e){
					throw e;
				}
				
			}
			
		}
		
		throw new RequirementNotFoundException(rUID);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof Iteration) {
			Iteration i = (Iteration) o;
			if (i.getId() == this.id) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date of the iteration. Note that the start date must be
	 * after the end date
	 * 
	 * @param startDate
	 *            the startDate to set
	 * @throws InvalidDateException
	 *             if the start date is after the end date
	 */
	public void setStartDate(Date startDate) throws InvalidDateException {
		if (endDate == null || startDate.compareTo(endDate) < 0) {
			this.startDate = startDate;
		} else {
			throw new InvalidDateException(startDate);
		}
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date of the iteration. Note that this must be after the
	 * start date
	 * 
	 * @param endDate
	 *            the endDate to set
	 * @throws InvalidDateException
	 *             if the end date is before the start date
	 */
	public void setEndDate(Date endDate) throws InvalidDateException {
		if (startDate == null || endDate.compareTo(startDate) > 0) {
			this.endDate = endDate;
		} else {
			throw new InvalidDateException(endDate);
		}
	}
	
	/**
	 * Method to confirm that the end date of an iteration
	 * is in fact after its start date
	 * @return true if this iteration has an end date which follows its start date;
	 * 	false otherwise
	 */
	public boolean validateDate(){
		return(startDate.compareTo(endDate) < 0);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the requirements
	 */
	public List<Integer> getRequirements() {
		return requirements;
	}

	/**
	 * @param requirements
	 *            the requirements to set
	 */
	public void setRequirements(List<Integer> requirements) {
		this.requirements = requirements;
	}
	
	/**
	 * @return the estimate
	 */
	public int getEstimate(){
		return this.estimate;
	}
	
	/**
	 * @param estimate
	 * 				the new estimate value to be set
	 */
	// TODO: determine if this method should even exist
	public void setEstimate(int estimate){
		this.estimate = estimate;
	}
	
}
