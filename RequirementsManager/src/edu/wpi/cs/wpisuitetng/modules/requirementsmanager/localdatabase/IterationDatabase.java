/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationIsNegativeException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

/**
 * Maintains a local database of iterations
 * 
 * @author Fredric
 */
public class IterationDatabase {

	private Map<Integer, Iteration> iterations;
	private static IterationDatabase db;

	private IterationDatabase(){
		iterations = new HashMap<Integer, Iteration>();
	}
	
	public static IterationDatabase getInstance() {
		if(db == null){
			db = new IterationDatabase();
		}
		return db;
	}

	/**
	 * Sets the iteration to the given map
	 * 
	 * @param iterations
	 */
	public synchronized void setIterations(
			Map<Integer, Iteration> iterations) {
		this.iterations = iterations;
	}

	/**
	 * Sets the iterations to the given list. This removes everything in the map
	 * and adds only things in the list
	 * 
	 * @param iterations
	 *            the iterations to add
	 */
	public synchronized void setIterations(List<Iteration> iterations) {
		System.out.println("Set iterations called!!!");
		this.iterations = new HashMap<Integer, Iteration>();
		for (Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
	}

	/**
	 * Adds the given iterations to the map. The difference between this and set
	 * iterations is that this doesn't erase all iterations, only adds/updates
	 * the given list
	 * 
	 * @param iterations the iterations to add/update
	 */
	public synchronized void addIterations(List<Iteration> iterations) {
		for (Iteration i : iterations) {
			this.iterations.put(i.getId(), i);
		}
	}
	
	/**
	 * Adds or updates a specific iteration
	 * @param i the iteration to add/update
	 */
	public synchronized void addIteration(Iteration i){
		iterations.put(i.getId(), i);
	}

	/**
	 * Get a specific iteration from the local database
	 * 
	 * @param id
	 *            the id of iteration to get
	 * @return the iteration requested
	 * @throws IterationNotFoundException
	 *             couldn't find the iteration
	 * @throws IterationIsNegativeException 
	 */
	public synchronized Iteration getIteration(int id)
			throws IterationNotFoundException, IterationIsNegativeException {
		if (id < 0) {
			throw new IterationIsNegativeException(id);
		}
		if (iterations.get(id) != null) {
			return iterations.get(id);
		} else {
			throw new IterationNotFoundException(id);
		}
	}

	//TODO: Documentation
	public synchronized Iteration getIteration(String name) {
		for (Iteration anIteration : iterations.values()) {
			if (anIteration.getName().equals(name)) {
				return anIteration;
			}
		}
		return null;
	}
	
	/**
	 * Gets all the iterations in the local database
	 * 
	 * @return all the current arrays
	 */
	public synchronized List<Iteration> getAllIterations() {
		List<Iteration> list = new ArrayList<Iteration>();
		list = new ArrayList<Iteration>(iterations.values());
		return list;
	}

}
