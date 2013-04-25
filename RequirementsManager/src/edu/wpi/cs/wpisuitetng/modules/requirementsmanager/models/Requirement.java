/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
 *    @author Jason Whitehouse
 *    @author Conor Geary
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.logger.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.ATestStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging.RequirementChangeset;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree.RequirementComparator;

/**
 * This is the basic requirement model. It contains all the fields that can be
 * assigned to a single requirement.
 */
public class Requirement extends AbstractModel {
	
	/**
	 * Converts a given string encoded in JSON format to a Requirement Object.
	 * 
	 * @param content
	 *            The JSON encoded string
	 * @return The Requirement extracted from the string
	 */
	public static Requirement fromJSON(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Requirement.class);
	}
	
	/**
	 * Generates an array of Requirements from a JSON encoded string.
	 * 
	 * @param content
	 *            The array of Requirements encoded in JSON format
	 * @return The array of requirements encoded in the string
	 */
	public static Requirement[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Requirement[].class);
	}
	
	/**
	 * Sorts the given list of requirements by creation date
	 * 
	 * @param requirements
	 *            the requirements to sort
	 * @return the sorted list of requirements
	 */
	public static List<Requirement> sortRequirements(
			final List<Requirement> requirements) {
		Collections.sort(requirements, new RequirementComparator());
		return requirements;
	}
	
	// Requirement Attributes
	private String name;
	private int rUID; // Requirement Unique ID
	private String description;
	private Type type;
	private Status status;
	private Priority priority;
	// Date and scheduling attributes
	private String releaseNum;
	private int iteration;
	private int estimate;
	private int effort; // Initially zero, if subRequirements.length() > 0, then
	// sum
	// Assignees, Subrequirements, and Parents
	private List<String> assignees; // Team Member usernames
	private List<Integer> subRequirements;
	private List<Integer> pUID; // Parent unique ID's
	// Notes and the log, and tasks
	private List<Note> notes;
	
	private List<RequirementChangeset> logs;
	
	private List<Task> tasks;
	
	private List<ATest> aTests;
	
	/**
	 * Creates a new Requirement, with default values.
	 */
	public Requirement() {
		// Set everything to either the empty string, empty list, 0, or blank,
		// depending on the attribute. This is because we don't know anything
		// about what the requirement is or will have.
		name = "";
		rUID = 0;
		description = "";
		type = Type.BLANK;
		status = Status.BLANK;
		priority = Priority.BLANK;
		releaseNum = "";
		iteration = -1;
		estimate = 0;
		effort = 0;
		assignees = new ArrayList<String>();
		subRequirements = new ArrayList<Integer>();
		pUID = new ArrayList<Integer>();
		notes = new ArrayList<Note>();
		logs = new ArrayList<RequirementChangeset>();
		// logger = new Logger();
		tasks = new ArrayList<Task>();
		aTests = new ArrayList<ATest>();
	}
	
	/**
	 * Creates a new Requirement, with every option except the log and the
	 * status being set by input.
	 * 
	 * @param name
	 *            The name of the requirement, must be between 1 and 100
	 *            characters
	 * @param description
	 *            The given description, must be between 1 and infinite
	 *            characters
	 * @param releaseNum
	 *            The given release number
	 * @param type
	 *            The type of the requirement
	 * @param subRequirements
	 *            A list of all subrequirements
	 * @param notes
	 *            A list of all notes
	 * @param iteration
	 *            The specified iteration
	 * @param estimate
	 *            the initial estimate for this class
	 * @param effort
	 *            The specified effort
	 * @param assignees
	 *            A list of all users assigned to this project
	 * @param pUID
	 *            A list of parent requirements
	 * @param tasks
	 *            the list of tasks to start off with
	 */
	public Requirement(final String name, final String description,
			final String releaseNum, final Type type,
			final List<Integer> subRequirements, final List<Note> notes,
			final int iteration, final int estimate, final int effort,
			final List<String> assignees, final List<Integer> pUID,
			final List<Task> tasks) {
		// Get the next UID for this requirement
		
		// Assign all inputs
		this.name = name;
		this.description = description;
		this.releaseNum = releaseNum;
		this.type = type;
		this.subRequirements = subRequirements;
		this.notes = notes;
		this.iteration = iteration;
		this.estimate = estimate;
		this.effort = effort;
		this.assignees = assignees;
		this.pUID = pUID;
		
		// Set the task to new, and create a new linked list for the log
		status = Status.NEW;
		logs = new ArrayList<RequirementChangeset>();
		
		this.tasks = tasks;
	}
	
	/**
	 * Adds a note to the list of notes.
	 * 
	 * @param note
	 *            the note to add
	 */
	public void addNote(final Note note) {
		notes.add(note);
	}
	
	/**
	 * Adds the given parent UID to the list of pUID's
	 * 
	 * @param id
	 *            the id to add
	 */
	public void addPUID(final Integer id) {
		pUID.add(id);
	}
	
	/**
	 * Add the given subrequirement to the list
	 * 
	 * @param id
	 *            the id to add
	 */
	public void addSubRequirement(final Integer id) {
		subRequirements.add(id);
	}
	
	/**
	 * Adds a given task to the list of tasks
	 * 
	 * @param task
	 */
	public void addTask(final Task task) {
		tasks.add(task);
	}
	
	/**
	 * Adds the given test to the list of tests
	 * 
	 * @param aTest
	 *            the new test
	 */
	public void addTest(final ATest aTest) {
		aTests.add(aTest);
	}
	
	/**
	 * Adds a given user to the list of of assignees to this requirement
	 * 
	 * @param newUser
	 *            the username of the user to be added
	 */
	public void addUser(final String newUser) {
		assignees.add(newUser);
	}
	
	/**
	 * note that save and delete don't do anything at the moment, even in the
	 * core's models
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the total effort for the requirement
	 * 
	 * @return the effort
	 */
	public int getEffort() {
		return effort;
	}
	
	/**
	 * @return the estimate
	 */
	public int getEstimate() {
		return estimate;
	}
	
	/**
	 * Gets the iteration for this requirement. TODO: Is this actually going to
	 * be an int?
	 * 
	 * @return the iteration
	 */
	public int getIteration() {
		return iteration;
	}
	
	/**
	 * Returns the list of log events
	 * 
	 * @return the log
	 */
	public List<RequirementChangeset> getLogs() {
		// return logger.getLogs();
		return logs;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the current notes
	 * 
	 * @return the notes
	 */
	public List<Note> getNotes() {
		return notes;
	}
	
	/**
	 * Gets the priority of the Requirement
	 * 
	 * @return the priority
	 */
	public Priority getPriority() {
		return priority;
	}
	
	/**
	 * Returns the list of parent requirements
	 * 
	 * @return the pUID
	 */
	public List<Integer> getpUID() {
		return pUID;
	}
	
	/**
	 * @return the releaseNum
	 */
	public String getReleaseNum() {
		return releaseNum;
	}
	
	/**
	 * Gets the rUID of the requirement
	 * 
	 * @return the rUID
	 */
	public int getrUID() {
		return rUID;
	}
	
	/**
	 * Gets the current status of the requirement
	 * 
	 * @return the status object of the requirement
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Return the list of subRequirement ID's
	 * 
	 * @return the subRequirements
	 */
	public List<Integer> getSubRequirements() {
		return subRequirements;
	}
	
	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}
	
	/**
	 * @return the tasks
	 */
	public List<ATest> getTests() {
		return aTests;
	}
	
	/**
	 * Gets the type of Requirement
	 * 
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Gets a list of the current users assigned to this requirement
	 * 
	 * @return the list of usernames
	 */
	public List<String> getUsers() {
		return assignees;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(final Object o) {
		// Check to see if o is a requirement
		if (!(o instanceof Requirement)) {
			// If it isn't, check to see if it's an ID, and return true if it
			// the correct rUID
			if (o instanceof Integer) {
				return ((Integer) o).equals(new Integer(rUID));
			} else {
				return false;
			}
		} else {
			return rUID == ((Requirement) o).rUID;
		}
	}
	
	/**
	 * Logs the creation of the requirement
	 * 
	 * @param s
	 *            the session containing the current user
	 */
	public void logCreation(final Session s) {
		final RequirementChangeset creation = new RequirementChangeset(
				s.getUser());
		creation.getChanges().put("creation",
				new FieldChange<String>("creation", "creation"));
		logs = new ArrayList<RequirementChangeset>();
		logs.add(creation);
	}
	
	/**
	 * OldLog events contained in a list
	 * 
	 * @param changes
	 *            the events to log
	 */
	public void logEvents(final RequirementChangeset changes) {
		logs.add(0, changes);
	}
	
	/**
	 * Remove the given note from the list of notes
	 * 
	 * @param note
	 *            the note to remove
	 * @return True if the note in the list, false otherwise
	 */
	public boolean removeNote(final String note) {
		return notes.remove(note);
	}
	
	/**
	 * Removes the given parent from the list of parents
	 * 
	 * @param id
	 *            the ID to remove
	 * @return if the ID was in the list or not
	 */
	public boolean removePUID(final Integer id) {
		pUID.remove(id);
		return true;
	}
	
	/**
	 * Deletes a given ID from the list of subRequirements.
	 * 
	 * @param id
	 *            The ID to delete
	 * @return true if the id was present, false if not
	 */
	public boolean removeSubRequirement(final Integer id) {
		subRequirements.remove(id);
		return true;
	}
	
	/**
	 * Removes the given assignee from this requirement's list TODO: Need to
	 * look at permission
	 * 
	 * @param user
	 *            the username of the user to remove
	 * @return True if the username was in the list, false otherwise
	 */
	public boolean removeUser(final String user) {
		return assignees.remove(user);
	}
	
	/**
	 * note that save and delete don't do anything at the moment, even in the
	 * core's models
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	
	/**
	 * Sets the effort for the requirement
	 * 
	 * @param effort
	 *            the effort to set
	 */
	public void setEffort(final int effort) {
		this.effort = effort;
	}
	
	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimate(final int estimate) {
		this.estimate = estimate;
	}
	
	/**
	 * Sets the current iteration TODO: change this to work on an Iteration
	 * object
	 * 
	 * @param iteration
	 *            the iteration to set
	 */
	public void setIteration(final int iteration) {
		this.iteration = iteration;
	}
	
	/**
	 * Sets the log. Make sure that you know what you're doing with this, as it
	 * will erase any logs stored in the manager. If you just want to add a log,
	 * then use addLog
	 * 
	 * @param linkedList
	 *            the log to set
	 */
	public void setLog(final List<RequirementChangeset> linkedList) {
		logs = linkedList;
	}
	
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Sets the current notes
	 * 
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(final List<Note> notes) {
		this.notes = notes;
	}
	
	/**
	 * Sets the priority of the requirement TODO: Determine if we can do this,
	 * or if the type is final
	 * 
	 * @param priority
	 *            the type to set
	 */
	public void setPriority(final Priority priority) {
		this.priority = priority;
	}
	
	/**
	 * Sets the list of parent requirements
	 * 
	 * @param pUID
	 *            the pUID to set
	 */
	public void setpUID(final List<Integer> pUID) {
		this.pUID = pUID;
	}
	
	/**
	 * @param releaseNum
	 *            the releaseNum to set
	 */
	public void setReleaseNum(final String releaseNum) {
		this.releaseNum = releaseNum;
	}
	
	/**
	 * Sets the rUID of the requirement
	 * 
	 * @param id
	 *            the id number to set rUID to
	 */
	public void setrUID(final int id) {
		rUID = id;
	}
	
	/**
	 * Sets the status of the requirement
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final Status status) {
		this.status = status;
	}
	
	/**
	 * Sets the list of subrequirement ID's
	 * 
	 * @param subRequirements
	 *            the subRequirements to set
	 */
	public void setSubRequirements(final List<Integer> subRequirements) {
		this.subRequirements = subRequirements;
	}
	
	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(final List<Task> tasks) {
		this.tasks = tasks;
	}
	
	/**
	 * @param aTests
	 *            the tests to set
	 */
	public void setTests(final List<ATest> aTests) {
		this.aTests = aTests;
	}
	
	/**
	 * Sets the type of the requirement TODO: Determine if we can do this, or if
	 * the type is final
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(final Type type) {
		this.type = type;
	}
	
	/**
	 * Sets the list of users assigned to this requirement
	 * 
	 * @param assignees
	 *            the list of usernames
	 */
	public void setUsers(final List<String> assignees) {
		this.assignees = assignees;
	}
	
	/**
	 * Gets whether all subrequirements of this requirement have been deleted.
	 * If they have been, then this can be closed. Otherwise, it cannot be
	 * 
	 * @return whether all subreqirements have been completed
	 */
	public boolean subReqsCompleted() {
		for (final Integer R : subRequirements) {
			try {
				if (RequirementDatabase.getInstance().get(R).getStatus() != Status.COMPLETE) {
					return false;
				}
			} catch (final RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * Checks the task list for completed tasks
	 * 
	 * @return true if all tasks in the list are complete
	 */
	public boolean tasksCompleted() {
		for (final Task t : tasks) {
			if (!t.isCompleted()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks the task list for completed tasks
	 * 
	 * @return true if all tasks in the list are complete
	 */
	public boolean testsPassed() {
		for (final ATest t : aTests) {
			if ((t.getStatus() == ATestStatus.FAILED)
					|| (t.getStatus() == ATestStatus.BLANK)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Converts this Requirement to a JSON string.
	 * 
	 * @return The JSON string representing this object
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Requirement.class);
	}
	
	/**
	 * method to construct a short string describing this Requirement suitable
	 * for use in a list view
	 * 
	 * @return a short summary of this Requirement
	 */
	public String toListString() {
		
		// TODO: determine what else to add to this method, if anything
		// Add the requirement UID and name
		final String listString = rUID + " " + name;
		
		return listString;
		
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
