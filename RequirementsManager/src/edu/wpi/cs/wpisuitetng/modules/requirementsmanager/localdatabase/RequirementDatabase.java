/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * Maintains a local database of requirements
 * 
 * @author Fredric
 */
public class RequirementDatabase {

	private Map<Integer, Requirement> requirements;
	private static RequirementDatabase db;
	
	private RequirementDatabase(){
		requirements = new HashMap<Integer, Requirement>();
	}
	
	public static RequirementDatabase getInstance(){
		if(db == null){
			db = new RequirementDatabase();
		}
		return db;
	}

	/**
	 * Sets the requirement to the given map
	 * 
	 * @param requirements
	 */
	public synchronized void setRequirements(
			Map<Integer, Requirement> requirements) {
		this.requirements = requirements;
	}

	/**
	 * Sets the requirements to the given list. This removes everything in the map
	 * and adds only things in the list
	 * 
	 * @param requirements
	 *            the requirements to add
	 */
	public synchronized void setRequirements(List<Requirement> requirements) {
		this.requirements = new HashMap<Integer, Requirement>();
		for (Requirement i : requirements) {
			this.requirements.put(i.getrUID(), i);
		}
	}

	/**
	 * Adds the given requirements to the map. The difference between this and set
	 * requirements is that this doesn't erase all requirements, only adds/updates
	 * the given list
	 * 
	 * @param requirements the requirements to add/update
	 */
	public synchronized void addRequirements(List<Requirement> requirements) {
		for (Requirement i : requirements) {
			this.requirements.put(i.getrUID(), i);
		}
	}
	
	/**
	 * Adds or updates a specific requirement
	 * @param i the requirement to add/update
	 */
	public synchronized void addRequirement(Requirement i){
		requirements.put(i.getrUID(), i);
	}

	/**
	 * Get a specific requirement from the local database
	 * 
	 * @param id
	 *            the id of requirement to get
	 * @return the requirement requested
	 * @throws RequirementNotFoundException
	 *             couldn't find the requirement
	 */
	public synchronized Requirement getRequirement(int id)
			throws RequirementNotFoundException {
		if (requirements.get(id) != null) {
			return requirements.get(id);
		} else {
			throw new RequirementNotFoundException(id);
		}
	}

	/**
	 * Gets all the requirements in the local database
	 * 
	 * @return all the current arrays
	 */
	public synchronized List<Requirement> getAllRequirements() {
		List<Requirement> list = new ArrayList<Requirement>();
		list = Arrays.asList(requirements.values().toArray(new Requirement[0]));
		return list;
	}

}
