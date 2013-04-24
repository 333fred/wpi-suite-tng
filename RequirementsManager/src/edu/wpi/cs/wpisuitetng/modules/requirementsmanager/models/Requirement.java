/*******************************************************************************
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Fredric, Jason Whitehouse, Conor Geary
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.logger.FieldChange;
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
 * 
 * @author Fredric, jason, Conor
 * 
 */
public class Requirement extends AbstractModel {

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
	public Requirement(String name, String description, String releaseNum,
			Type type, List<Integer> subRequirements, List<Note> notes,
			int iteration, int estimate, int effort, List<String> assignees,
			List<Integer> pUID, List<Task> tasks) {
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
		this.status = Status.NEW;
		this.logs = new ArrayList<RequirementChangeset>();

		this.tasks = tasks;
	}

	/**
	 * @return the estimate
	 */
	public int getEstimate() {
		return estimate;
	}

	/**
	 * @param estimate
	 *            the estimate to set
	 */
	public void setEstimate(int estimate) {
		this.estimate = estimate;
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
	 * Converts a given string encoded in JSON format to a Requirement Object.
	 * 
	 * @param content
	 *            The JSON encoded string
	 * @return The Requirement extracted from the string
	 */
	public static Requirement fromJSON(String content) {
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
	public static Requirement[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Requirement[].class);
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
		String listString = this.rUID + " " + this.name;

		return listString;

	}

	/**
	 * OldLog events contained in a list
	 * 
	 * @param changes
	 *            the events to log
	 */
	public void logEvents(RequirementChangeset changes) {
		logs.add(0, changes);
	}

	/**
	 * Logs the creation of the requirement
	 * 
	 * @param s
	 *            the session containing the current user
	 */
	public void logCreation(Session s) {
		RequirementChangeset creation = new RequirementChangeset(s.getUser());
		creation.getChanges().put("creation",
				new FieldChange<String>("creation", "creation"));
		logs = new ArrayList<RequirementChangeset>();
		logs.add(creation);
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the releaseNum
	 */
	public String getReleaseNum() {
		return releaseNum;
	}

	/**
	 * @param releaseNum
	 *            the releaseNum to set
	 */
	public void setReleaseNum(String releaseNum) {
		this.releaseNum = releaseNum;
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
	 * Sets the type of the requirement TODO: Determine if we can do this, or if
	 * the type is final
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
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
	 * Sets the priority of the requirement TODO: Determine if we can do this,
	 * or if the type is final
	 * 
	 * @param priority
	 *            the type to set
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
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
	 * Sets the list of subrequirement ID's
	 * 
	 * @param subRequirements
	 *            the subRequirements to set
	 */
	public void setSubRequirements(List<Integer> subRequirements) {
		this.subRequirements = subRequirements;
	}

	/**
	 * Add the given subrequirement to the list
	 * 
	 * @param id
	 *            the id to add
	 */
	public void addSubRequirement(Integer id) {
		this.subRequirements.add(id);
	}

	/**
	 * Deletes a given ID from the list of subRequirements.
	 * 
	 * @param id
	 *            The ID to delete
	 * @return true if the id was present, false if not
	 */
	public boolean removeSubRequirement(Integer id) {
		this.subRequirements.remove(id);
		return true;
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
	 * Sets the current notes
	 * 
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	/**
	 * Adds a note to the list of notes.
	 * 
	 * @param note
	 *            the note to add
	 */
	public void addNote(Note note) {
		this.notes.add(note);
	}

	/**
	 * Remove the given note from the list of notes
	 * 
	 * @param note
	 *            the note to remove
	 * @return True if the note in the list, false otherwise
	 */
	public boolean removeNote(String note) {
		return this.notes.remove(note);
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
	 * Sets the current iteration TODO: change this to work on an Iteration
	 * object
	 * 
	 * @param iteration
	 *            the iteration to set
	 */
	public void setIteration(int iteration) {
		this.iteration = iteration;
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
	 * Sets the effort for the requirement
	 * 
	 * @param effort
	 *            the effort to set
	 */
	public void setEffort(int effort) {
		this.effort = effort;
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
	 * Sets the list of users assigned to this requirement
	 * 
	 * @param assignees
	 *            the list of usernames
	 */
	public void setUsers(List<String> assignees) {
		this.assignees = assignees;
	}

	/**
	 * Adds a given user to the list of of assignees to this requirement
	 * 
	 * @param newUser
	 *            the username of the user to be added
	 */
	public void addUser(String newUser) {
		this.assignees.add(newUser);
	}

	/**
	 * Removes the given assignee from this requirement's list TODO: Need to
	 * look at permission
	 * 
	 * @param user
	 *            the username of the user to remove
	 * @return True if the username was in the list, false otherwise
	 */
	public boolean removeUser(String user) {
		return this.assignees.remove(user);
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
	 * Sets the list of parent requirements
	 * 
	 * @param pUID
	 *            the pUID to set
	 */
	public void setpUID(List<Integer> pUID) {
		this.pUID = pUID;
	}

	/**
	 * Adds the given parent UID to the list of pUID's
	 * 
	 * @param id
	 *            the id to add
	 */
	public void addPUID(Integer id) {
		this.pUID.add(id);
	}

	/**
	 * Removes the given parent from the list of parents
	 * 
	 * @param id
	 *            the ID to remove
	 * @return if the ID was in the list or not
	 */
	public boolean removePUID(Integer id) {
		this.pUID.remove(id);
		return true;
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
	 * Sets the log. Make sure that you know what you're doing with this, as it
	 * will erase any logs stored in the manager. If you just want to add a log,
	 * then use addLog
	 * 
	 * @param linkedList
	 *            the log to set
	 */
	public void setLog(List<RequirementChangeset> linkedList) {
		this.logs = linkedList;
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
	 * Sets the status of the requirement
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
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
	 * Sets the rUID of the requirement
	 * 
	 * @param id
	 *            the id number to set rUID to
	 */
	public void setrUID(int id) {
		rUID = id;
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
	 * note that save and delete don't do anything at the moment, even in the
	 * core's models
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		// Check to see if o is a requirement
		if (!(o instanceof Requirement)) {
			// If it isn't, check to see if it's an ID, and return true if it
			// the correct rUID
			if (o instanceof Integer) {
				return ((Integer) o).equals(new Integer(this.rUID));
			} else {
				return false;
			}
		} else {
			return this.rUID == ((Requirement) o).rUID;
		}
	}

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Checks the task list for completed tasks
	 * 
	 * @return true if all tasks in the list are complete
	 */
	public boolean testsPassed() {
		for (ATest t : this.aTests) {
			if (t.getStatus() == ATest.ATestStatus.FAILED
					|| t.getStatus() == ATest.ATestStatus.BLANK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds the given test to the list of tests
	 * 
	 * @param aTest
	 *            the new test
	 */
	public void addTest(ATest aTest) {
		this.aTests.add(aTest);
	}

	/**
	 * @return the tasks
	 */
	public List<ATest> getTests() {
		return aTests;
	}

	/**
	 * @param aTests
	 *            the tests to set
	 */
	public void setTests(List<ATest> aTests) {
		this.aTests = aTests;
	}

	/**
	 * Checks the task list for completed tasks
	 * 
	 * @return true if all tasks in the list are complete
	 */
	public boolean tasksCompleted() {
		for (Task t : this.tasks) {
			if (!t.isCompleted()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets whether all subrequirements of this requirement have been deleted.
	 * If they have been, then this can be closed. Otherwise, it cannot be
	 * 
	 * @return whether all subreqirements have been completed
	 */
	public boolean subReqsCompleted() {
		for (Integer R : this.subRequirements) {
			try {
				if (RequirementDatabase.getInstance().get(R).getStatus() != Status.COMPLETE) {
					return false;
				}
			} catch (RequirementNotFoundException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * Adds a given task to the list of tasks
	 * 
	 * @param task
	 */
	public void addTask(Task task) {
		this.tasks.add(task);
	}

	public String toString() {
		return this.getName();
	}

	/**
	 * Sorts the given list of requirements by creation date
	 * 
	 * @param requirements
	 *            the requirements to sort
	 * @return the sorted list of requirements
	 */
	public static List<Requirement> sortRequirements(
			List<Requirement> requirements) {
		Collections.sort(requirements, new RequirementComparator());
		return requirements;
	}

}
