/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;

/**
 * Maintains a local database of iterations
 * 
 * @author Fredric
 */
public class IterationDatabase {

	private static Map<Integer, Iteration> iterations;
	private static boolean initialized;

	/**
	 * Initializes the database for the first time
	 */
	private static synchronized void init() {
		if (initialized) {
			return;
		} else {
			initialized = true;
			iterations = new HashMap<Integer, Iteration>();
		}
	}

	/**
	 * Sets the iteration to the given map
	 * 
	 * @param iterations
	 */
	public static synchronized void setIterations(
			Map<Integer, Iteration> iterations) {
		init();
		IterationDatabase.iterations = iterations;
	}

	/**
	 * Sets the iterations to the given list. This removes everything in the map
	 * and adds only things in the list
	 * 
	 * @param iterations
	 *            the iterations to add
	 */
	public static synchronized void setIterations(List<Iteration> iterations) {
		init();
		System.out.println("Set iterations called!!!");
		IterationDatabase.iterations = new HashMap<Integer, Iteration>();
		for (Iteration i : iterations) {
			IterationDatabase.iterations.put(i.getId(), i);
		}
	}

	/**
	 * Adds the given iterations to the map. The difference between this and set
	 * iterations is that this doesn't erase all iterations, only adds/updates
	 * the given list
	 * 
	 * @param iterations the iterations to add/update
	 */
	public static synchronized void addIterations(List<Iteration> iterations) {
		init();
		for (Iteration i : iterations) {
			IterationDatabase.iterations.put(i.getId(), i);
		}
	}
	
	/**
	 * Adds or updates a specific iteration
	 * @param i the iteration to add/update
	 */
	public static synchronized void addIteration(Iteration i){
		init();
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
	 */
	public static synchronized Iteration getIteration(int id)
			throws IterationNotFoundException {
		init();
		if (iterations.get(id) != null) {
			return iterations.get(id);
		} else {
			throw new IterationNotFoundException(id);
		}
	}

	/**
	 * Gets all the iterations in the local database
	 * 
	 * @return all the current arrays
	 */
	public static synchronized List<Iteration> getAllIterations() {
		init();
		List<Iteration> list = new ArrayList<Iteration>();
		list = Arrays.asList(iterations.values().toArray(new Iteration[0]));
		return list;
	}

}
