/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Jason Whitehouse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationComparator;

/**
 * Class representing an iteration of a project
 * 
 * @author Jason Whitehouse
 */
public class Iteration extends AbstractModel {
	
	/**
	 * Converts a JSON encoded iteration to a instantiated iteration
	 * 
	 * @param content
	 *            the JSON encoded iteration
	 * @return the instantiated iteration
	 */
	public static Iteration fromJSON(final String content) {
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
	public static Iteration[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Iteration[].class);
	}
	
	/**
	 * Sorts the given iterations by start date
	 * 
	 * @param iterations
	 *            the iterations to sort
	 * @return the list of sorted iterations
	 */
	public static List<Iteration> sortIterations(
			final List<Iteration> iterations) {
		Collections.sort(iterations, new IterationComparator());
		return iterations;
	}
	
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
		name = "";
		startDate = null;
		endDate = null;
		id = -1;
		requirements = new ArrayList<Integer>();
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
	public Iteration(final String name, final Date startDate, final Date endDate) {
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
	public Iteration(final String name, final Date startDate,
			final Date endDate, final int id) {
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
	public Iteration(final String name, final Date startDate,
			final Date endDate, final int id, final List<Integer> reqs) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		requirements = reqs;
	}
	
	/**
	 * Adds a given requirement to this iteration. If the requirement is already
	 * a member of requirements we do nothing
	 * 
	 * @param rUID
	 *            the new requirement
	 */
	public void addRequirement(final int rUID) {
		
		// first check if the requirement is already on here
		for (final Integer rid : requirements) {
			if (rid == rUID) {
				return; // if it is we are already done
			}
		}
		requirements.add(rUID);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * @return the sum of the estimates of this iteration's requirements does
	 *         not care if a rUID does not point to a valid requirement; simply
	 *         ignores it in that case
	 */
	public int getEstimate() {
		
		int estimate = 0;
		
		for (final Integer rUID : requirements) {
			
			try {
				estimate += RequirementDatabase.getInstance().get(rUID)
						.getEstimate();
			} catch (final RequirementNotFoundException e) {
				System.out.println("Requirement id " + rUID + " not found");
			}
			
		}
		
		return estimate;
		
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the current progress of the iteration
	 */
	public float getProgress() {
		float total = 0;
		float done = 0;
		
		for (final Integer req : requirements) {
			Requirement requirement;
			try {
				requirement = RequirementDatabase.getInstance().get(req);
				total += requirement.getEstimate();
				if (requirement.getStatus() == Status.COMPLETE) {
					done += requirement.getEstimate();
				}
			} catch (final RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (total == 0) {
			return 0;
		} else {
			return (done / total) * 100;
		}
	}
	
	/**
	 * @return the requirements
	 */
	public List<Integer> getRequirements() {
		return requirements;
	}
	
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(final Object o) {
		if (o instanceof Iteration) {
			final Iteration i = (Iteration) o;
			return i.getId() == id;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns whether the current status of the iteration is in progress
	 * 
	 * @return true if the iteration is in progress, false otherwise
	 */
	public boolean isInProgress() {
		final Date currentDate = new Date();
		return currentDate.after(startDate) && currentDate.before(endDate);
	}
	
	/**
	 * Returns a boolean indicating whether this iteration is open Checks to
	 * make sure that the end date is not before now
	 * 
	 * @return True if it is open, false otherwise
	 */
	
	public boolean isOpen() {
		final Date currentDate = new Date();
		return ((currentDate.compareTo(getEndDate()) <= 0) || (getId() == -1) || (getId() == -2));
	}
	
	/**
	 * Removes a given requirement from this iteration
	 * 
	 * @param rUID
	 *            the removed requirement
	 */
	public void removeRequirement(final int rUID) {
		
		final Iterator<Integer> iter = requirements.iterator();
		while (iter.hasNext()) {
			if (iter.next() == rUID) {
				iter.remove();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
	}
	
	/**
	 * Sets the end date of the iteration. Note that this must be after the
	 * start date
	 * 
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * @param requirements
	 *            the requirements to set
	 */
	public void setRequirements(final List<Integer> requirements) {
		this.requirements = requirements;
	}
	
	/**
	 * Sets the start date of the iteration. Note that the start date must be
	 * after the end date
	 * 
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Creates a JSON string representing this class
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Iteration.class);
	}
	
	@Override
	public String toString() {
		if (((new Date()).compareTo(getEndDate()) > 0) && (getId() != -1)) {
			return "<HTML><B>" + getName() + " (Closed)" + "</B></HTML>";
		} else if (this.isInProgress()) {
			return "<HTML><B>" + getName() + " (Active)" + "</B></HTML>";
		} else {
			return "<HTML><B>" + getName() + "</B></HTML>";
		}
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
}
