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
	private Requirement lastExistingRequirement;
	
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
		//TODO: Make mode actually do something
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(requirement == null) {
			issues.add(new ValidationIssue("Requirement cannot be null"));
			return issues;
		}
		
		//TODO: validate the rest of the fields once they are implemented
		Requirement oldRequirement = null;
		lastExistingRequirement = oldRequirement;
		
		// new requirements should always have new status
		requirement.setStatus(Status.NEW);
		
		// make sure title and description size are within constraints
		if(requirement.getName() == null || requirement.getName().length() > 100 || requirement.getName().length() == 0) {
			issues.add(new ValidationIssue("Required, must be 1-100 characters", "name"));
		}
		if(requirement.getDescription() == null || requirement.getDescription().length() == 0) {
			// empty descriptions are not allowed
			issues.add(new ValidationIssue("Required, must be 1-5000 characters", "description"));
		} else if(requirement.getDescription().length() > 5000) {
			issues.add(new ValidationIssue("Cannot be greater than 5000 characters", "description"));
		}
		
		//handle null lists
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
		
		if (requirement.gettID() == null) {
			requirement.settID(new LinkedList<User>());
		}
		
		//Give BLANK type if none is given
		if(requirement.getType() == null){
			requirement.setType(Type.BLANK);
		}
		
		//Make sure all assignees exist
		/*for (User u : requirement.gettID()){
			getExistingUser(u);
		}*/
				
		return issues;
	}

	/**
	 * @return The last existing requirement the validator fetched if in edit mode
	 */
	public Requirement getLastExistingRequirement() {
		return lastExistingRequirement;
	}
	
}