/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Log;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * This is the basic requirement model. It contains all the fields that can be
 * assigned to a single requirement.
 * 
 * @author Fredric
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
	private int releaseNum; // TODO: Implement Releases
	private int iteration; // TODO: Implement Iterations
	private int effort; // Initially zero, if subRequirements.length() > 0, then
						// sum
	// Assignees, Subrequirements, and Parents
	private List<User> assignees; // Team Member ID
	private List<Integer> subRequirements;
	private List<Integer> pUID; // Parent unique ID's
	// Notes and the Log
	private List<Note> notes;
	private List<Log> log;

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
		releaseNum = 0;
		iteration = 0;
		effort = 0;
		assignees = new ArrayList<User>();
		subRequirements = new ArrayList<Integer>();
		pUID = new ArrayList<Integer>();
		notes = new ArrayList<Note>();
		log = new ArrayList<Log>();
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
	 * @param effort
	 *            The specified effort
	 * @param assignees
	 *            A list of all users assigned to this project
	 * @param pUID
	 *            A list of parent requirements
	 */
	public Requirement(String name, String description, int releaseNum,
			Type type, List<Integer> subRequirements, List<Note> notes,
			int iteration, int effort, List<User> assignees, List<Integer> pUID) {
		// Get the next UID for this requirement

		// Assign all inputs
		this.name = name; // TODO: Support length checking, should throw an
							// exception
		this.description = description;
		this.releaseNum = releaseNum;
		this.type = type;
		this.subRequirements = subRequirements;
		this.notes = notes;
		this.iteration = iteration;
		this.effort = effort;
		this.assignees = assignees;
		this.pUID = pUID;

		// Set the task to new, and create a new linked list for the log
		this.status = Status.NEW;
		this.log = new LinkedList<Log>();
	}


	/**
	 * Converts this Requirement to a JSON string.
	 * 
	 * @return The JSON string representing this object
	 */
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
	 * method to construct a short string describing this Requirement
	 * suitable for use in a list view
	 * @return a short summary of this Requirement
	 */
	public String toListString(){
		
		// TODO: determine what else to add to this method, if anything
		// Add the requirement UID and name
		String listString = this.rUID + " " + this.name ;	
		
		return listString;
		
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
	public int getReleaseNum() {
		return releaseNum;
	}

	/**
	 * @param releaseNum
	 *            the releaseNum to set
	 */
	public void setReleaseNum(int releaseNum) {
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
	 * Sets the priority of the requirement TODO: Determine if we can do this, or if
	 * the type is final
	 * 
	 * @param type
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
	public void addSubRequirement(int id) {
		this.subRequirements.add(id);
	}

	/**
	 * Deletes a given ID from the list of subRequirements.
	 * 
	 * @param id
	 *            The ID to delete
	 * @return true if the id was present, false if not
	 */
	public boolean removeSubRequirement(int id) {
		return this.subRequirements.remove(id) != null;
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
	 * Sets the current iteration TODO: Is this actually going to be an int
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
	 * Gets a list of the current team member id's TODO: Needs to be switched to
	 * Users
	 * 
	 * @return the tID
	 */
	public List<User> gettID() {
		return assignees;
	}

	/**
	 * Sets the team member id's TODO: Needs to be switched to users
	 * 
	 * @param assignees
	 *            the tID to set
	 */
	public void settID(List<User> assignees) {
		this.assignees = assignees;
	}

	/**
	 * Adds a given team id number to the list of id's TODO: Needs to be
	 * switched to users
	 * 
	 * @param id
	 */
	public void addTID(User newUser) {
		this.assignees.add(newUser);
	}

	/**
	 * Removes the given team member id from the list TODO: Needs to be switched
	 * to users TODO: Need to look at permission
	 * 
	 * @param id
	 *            the ID to remove
	 * @return True if the ID was in the list, false otherwise
	 */
	public boolean removeTID(User user) {
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
	public void addPUID(int id) {
		this.pUID.add(id);
	}

	/**
	 * Removes the given parent from the list of parents
	 * 
	 * @param id
	 *            the ID to remove
	 * @return if the ID was in the list or not
	 */
	public boolean removePUID(int id) {
		return this.pUID.remove(id) != null;
	}

	/**
	 * Returns the list of log events
	 * 
	 * @return the log
	 */
	public List<Log> getLog() {
		return log;
	}

	/**
	 * Sets the log. Make sure that you know what you're doing with this, as it
	 * will erase any logs stored in the manager. If you just want to add a log,
	 * then use addLog
	 * 
	 * @param log the log to set
	 */
	public void setLog(List<Log> linkedList) {
		this.log = linkedList;
	}

	/**
	 * Add the given log to the list of logs
	 * @param log the log to add
	 */
	public void addLog(Log log) {
		this.log.add(log);
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
	 * @param status the status to set
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
}
