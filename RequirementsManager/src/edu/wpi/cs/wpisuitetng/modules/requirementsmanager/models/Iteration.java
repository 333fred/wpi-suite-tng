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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.InvalidDateException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationComparator;

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
	}

	/**
	 * Constructor for an iteration with the given start, and end It has a blank
	 * list of requirements
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
	 * Adds a given requirement to this iteration. If the requirement is already
	 * a member of requirements we do nothing
	 * 
	 * @param rUID
	 *            the new requirement
	 */
	public void addRequirement(int rUID) {

		// first check if the requirement is already on here
		for (Integer id : this.requirements) {
			if (id == rUID)
				return; // if it is we are already done
		}
		this.requirements.add(rUID);
	}

	/**
	 * Removes a given requirement from this iteration
	 * 
	 * @param rUID
	 *            the removed requirement
	 */
	public void removeRequirement(int rUID) {

		Iterator<Integer> iter = this.requirements.iterator();
		while (iter.hasNext()) {
			if (iter.next() == rUID)
				iter.remove();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof Iteration) {
			Iteration i = (Iteration) o;
			return i.getId() == this.id;
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
	public void setStartDate(Date startDate){
		this.startDate = startDate;
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
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Method to confirm that the end date of an iteration is in fact after its
	 * start date
	 * 
	 * @return true if this iteration has an end date which follows its start
	 *         date; false otherwise
	 */
	public boolean validateDate() {
		return (startDate.compareTo(endDate) < 0);
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
	 * @return the sum of the estimates of this iteration's requirements does
	 *         not care if a rUID does not point to a valid requirement; simply
	 *         ignores it in that case
	 */
	public int getEstimate() {

		int estimate = 0;

		for (Integer rUID : requirements) {

			try {
				estimate += RequirementDatabase.getInstance()
						.getRequirement(rUID).getEstimate();
			} catch (RequirementNotFoundException e) {
				System.out.println("Requirement id " + rUID + " not found");
			}

		}

		return estimate;

	}	

	public static List<Iteration> sortIterations(List<Iteration> iterations) {
		Collections.sort(iterations, new IterationComparator());
		return iterations;
	}
	
	/** Returns a boolean indicating whether this iteration is open
	 *  Checks to make sure that the end date is not before now
	 * 
	 * @return True if it is open, false otherwise
	 */
	
	public boolean isOpen() {
		Date currentDate = new Date();
		return (currentDate.compareTo(getEndDate()) <= 0 || getId() == -1 || getId() == -2);
	}

	public String toString() {
		if ((new Date()).compareTo(this.getEndDate()) > 0 && this.getId() != -1) {
			return this.getName()+ " (Closed)"; 
		} else {
			return this.getName();
		}
	}
	
}
