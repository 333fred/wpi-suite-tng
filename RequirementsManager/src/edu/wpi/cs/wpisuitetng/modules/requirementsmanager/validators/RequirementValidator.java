/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.validators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.RequirementActionMode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Log;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Validates Requirements so that they fit in with the given Data implementation.
 * @author Jason Whitehouse
 *
 */
public class RequirementValidator {

	private Data data;
	
	/**
	 * Create a Requirement Validator
	 * 
	 * @param data The Data implementation to use
	 */
	public RequirementValidator(Data data) {
		this.data = data;
	}
	
	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Return the User with the given username if they already exist in the database.
	 * 
	 * @param username the username of the User
	 * @param issues list of errors to add to if user doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The User with the given username, or null if they don't exist
	 * @throws WPISuiteException 
	 */
	User getExistingUser(String username, List<ValidationIssue> issues, String fieldName) throws WPISuiteException {
		final List<Model> existingUsers = data.retrieve(User.class, "username", username);
		if(existingUsers.size() > 0 && existingUsers.get(0) != null) {
			return (User) existingUsers.get(0);
		} else {
			issues.add(new ValidationIssue("User doesn't exist", fieldName));
			return null;
		}
	}
	
	/**
	 * Checks if the given string is valid. Valid means that it is not null, has length greater 
	 * than 0 but less than maxLength
	 * 
	 * @param toCheck the string to check
	 * @param maxLength maximum valid length of the string, negative value means there is no max
	 * @return whether or not the string is valid
	 */
	boolean stringValid (String toCheck, int maxLength) {
		if(toCheck == null) return false;
		if(toCheck.length() > maxLength && maxLength > 0) return false;
		if(toCheck.length() == 0) return false;
		
	/*	//iterate through characters in string
		for (int i = 0; i < toCheck.length(); i++){
			//if we find a character which is not whitespace, then we know it must be valid
		    char c = toCheck.charAt(i);        
		    if (!Character.isWhitespace(c))
		    	return true;
		}*/
		
		//if we fail to find a an issue return true
		return true;
	}
	
	/**
	 * Return the Requirement with the given id if it already exists in the database.
	 * 
	 * @param id the id of the Requirement
	 * @param project the project this requirement belongs to
	 * @param issues list of errors to add to if requirement doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The Requirement with the given id, or null if it doesn't exist
	 * @throws WPISuiteException 
	 */
	Requirement getExistingRequirement(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldRequirements = data.retrieve(Requirement.class, "rUID", id, project);
		if(oldRequirements.size() < 1 || oldRequirements.get(0) == null) {
			issues.add(new ValidationIssue("Requirement with id does not exist in project", fieldName));
			return null;
		} else {
			return (Requirement) oldRequirements.get(0);
		}
	}
	
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param requirement The requirement model to validate
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	
	 //TODO: @param mode The mode to validate for
	public List<ValidationIssue> validate(Session session, Requirement requirement, RequirementActionMode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		
		//Null requirements are invalid
		if(requirement == null) {
			issues.add(new ValidationIssue("Requirement cannot be null"));
			return issues;
		}
		
		
		// new requirements should always have new status
		if(mode == RequirementActionMode.CREATE) {
			requirement.setStatus(Status.NEW);
			//and no iteration
			requirement.setIteration(-1);
		} else if(requirement.getStatus() != Status.DELETED && requirement.getStatus() != Status.COMPLETE){
			//Automatic status changes
			
			//If a new or open requirement is assigned to an iteration then set it to in progress
			if((requirement.getStatus() == Status.NEW || requirement.getStatus() == Status.OPEN) && requirement.getIteration() != -1)
				requirement.setStatus(Status.IN_PROGRESS);
			
			//if an in-progress requirement is removed from an iteration, it is set to open
			if(requirement.getStatus() == Status.IN_PROGRESS && requirement.getIteration() == -1){
				requirement.setStatus(Status.OPEN);
			}

		}
		
		//deleted requirements should not have an iteration
		if(requirement.getStatus() == Status.DELETED){
			requirement.setIteration(-1);
		}
		
		//trim whitespace if possible
		if(requirement.getName() != null)
			requirement.setName(requirement.getName().trim());
		if(requirement.getDescription() != null)
			requirement.setDescription(requirement.getDescription().trim());
		
		// make sure title and description size are within constraints
		if(!stringValid(requirement.getName(), 100))
			issues.add(new ValidationIssue("Required, must be 1-100 characters", "name"));
		
		if(!stringValid(requirement.getDescription(), 0))
			issues.add(new ValidationIssue("Required, must be 1 or more characters", "description"));

		
		//Initialize any null lists to be empty
		if(requirement.getLog() == null){
			requirement.setLog(new LinkedList<Log>());
		}
		
		if (requirement.getNotes() == null) {
			requirement.setNotes(new LinkedList<Note>());
		}
		
		if (requirement.getpUID() == null) {
			requirement.setpUID(new LinkedList<Integer>());
		}
		
		if (requirement.getSubRequirements() == null) {
			requirement.setSubRequirements(new LinkedList<Integer>());
		}
		
		if (requirement.getUsers() == null) {
			requirement.setUsers(new LinkedList<String>());
		}
		
		//Give BLANK type if none is given
		if(requirement.getType() == null){
			requirement.setType(Type.BLANK);
		}
		
		//Make sure all assignees actually exist
		for (String u : requirement.getUsers()){
			getExistingUser(u, issues, "Assignee");
		}
				
		return issues;
	}	
}