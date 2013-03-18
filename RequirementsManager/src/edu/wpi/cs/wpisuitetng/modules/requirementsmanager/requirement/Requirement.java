/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;

/**
 * This is the basic requirement model. It contains all the fields that can be
 * assigned to a single requirement.
 * 
 * @author Fredric
 * 
 */
public class Requirement implements Model {

	// TODO: Reorganize these to make sense
	private String name;
	final private int rUID; // Requirement Unique ID
	private String description;
	private int releaseNum = 0; // TODO: Implement Releases
	private Type type;
	private List<Integer> subRequirements;
	private List<String> notes;
	private int iteration = 0; // TODO: Implement Iterations
	private int effort; // Initially zero, if subRequirements.length() > 0, then
						// null/sum
						// TODO: Determine if we sum the subrequirements or not
	private List<Integer> tID; // Team Member ID
	private List<Integer> pUID; // Parent unique ID's
	private List<String> log;
	private Status status;

	// Static UID field and UID generator
	private static int UID = 0;

	/**
	 * Gets the next unique id.
	 * 
	 * @return The next unique id.
	 */
	private static int getUID() {
		return ++UID;
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
	 * @param tID
	 *            A list of all users assigned to this project
	 * @param pUID
	 *            A list of parent requirements
	 */
	public Requirement(String name, String description, int releaseNum,
			Type type, List<Integer> subRequirements, List<String> notes,
			int iteration, int effort, List<Integer> tID, List<Integer> pUID) {
		// Get the next UID for this requirement
		rUID = getUID();

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
		this.tID = tID;
		this.pUID = pUID;

		// Set the task to new, and create a new linked list for the log
		this.status = Status.NEW;
		this.log = new LinkedList<String>();
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
		String listString = new String();	// the variable to hold the summary
		listString = listString.concat(this.name + " ");	// add this Requirement's name
		
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
	public List<String> getNotes() {
		return notes;
	}

	/**
	 * Sets the current notes
	 * 
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(List<String> notes) {
		this.notes = notes;
	}

	/**
	 * Adds a note to the list of notes.
	 * 
	 * @param note
	 *            the note to add
	 */
	public void addNote(String note) {
		this.notes.add(note);
	}
	
	/**
	 * Remove the given note from the list of notes
	 * @param note the note to remove
	 * @return True if the note in the list, false otherwise
	 */
	public boolean removeNote(String note){
		return this.notes.remove(note);
	}

	/**
	 * Gets the iteration for this requirement.
	 * TODO: Is this actually going to be an int?
	 * 
	 * @return the iteration
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * Sets the current iteration
	 * TODO: Is this actually going to be an int
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
	 * Gets a list of the current team member id's
	 * 
	 * @return the tID
	 */
	public List<Integer> gettID() {
		return tID;
	}

	/**
	 * Sets the team member id's
	 * 
	 * @param tID
	 *            the tID to set
	 */
	public void settID(List<Integer> tID) {
		this.tID = tID;
	}
	
	/**
	 * Adds a given team id number to the list of id's
	 * @param id
	 */
	public void addTID(int id){
		this.tID.add(id);
	}

	/**
	 * Removes the given team member id from the list
	 * @param id the ID to remove
	 * @return True if the ID was in the list, false otherwise
	 */
	public boolean removeTID(int id){
		return this.tID.remove(id) != null;
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
	 * @param id the id to add
	 */
	public void addPUID(int id){
		this.pUID.add(id);
	}
	
	/**
	 * Removes the given parent from the list of parents
	 * @param id the ID to remove
	 * @return if the ID was in the list or not
	 */
	public boolean removePUID(int id){
		return this.pUID.remove(id) != null;
	}

	/**
	 * Returns the list of log events
	 * 
	 * @return the log
	 */
	public List<String> getLog() {
		return log;
	}

	/**
	 * Sets the log.  This really should never be called.
	 * TODO: Determine if this should even exist
	 * 
	 * @param log the log to set
	 */
	public void setLog(List<String> log) {
		this.log = log;
	}
	
	/**
	 * Add the given log to the list of logs
	 * @param log the log to add
	 */
	public void addLog(String log){
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
	 * Gets the UID of the requirement
	 * 
	 * @return the rUID
	 */
	public int getrUID() {
		return rUID;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Permission getPermission(User u) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermission(Permission p, User u) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProject(Project p) {
		// TODO Auto-generated method stub
	}
}
