/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveRequirementByIDController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.InvalidDateException;

/**
 * Skeleton class for iteration. This will be made into a model in the future,
 * but for now it is just a dummy for allowing assignment.
 * 
 * Because this is not a full implementation, validation of dates is not
 * required
 * 
 * @author Jason Whitehouse
 */
public class Iteration extends AbstractModel {

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
		this.name = "";
		this.startDate = null;
		this.endDate = null;
		this.id = -1;
		this.requirements = new ArrayList<Integer>();
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
	public Iteration(String name, Date startDate, Date endDate, int id) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		requirements = new ArrayList<Integer>();
	}

	/**
	 * Creates a Iteration with all given inputs
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
	public Iteration(String name, Date startDate, Date endDate, int id,
			List<Integer> reqs) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.id = id;
		this.requirements = reqs;
	}

	// TODO implement model methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates a JSON string representing this class
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, Iteration.class);
	}

	/**
	 * Converts a JSON encoded iteration to a instantiated iteration
	 * 
	 * @param content
	 *            the JSON encoded iteration
	 * @return the instantiated iteration
	 */
	public static Iteration fromJSON(String content) {
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
	public static Iteration[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, Iteration[].class);
	}

	/**
	 * Adds a given requirement to this iteration
	 * 
	 * @param rUID
	 *            the new requirement
	 */
	public void addRequirement(int rUID) {
		// TODO: Validate that the requirement actually exists, probably through
		// a local map
		this.requirements.add(rUID);
	}

	/**
	 * Removes a given requirement from this iteration
	 * 
	 * @param rUID
	 *            the removed requirement
	 */
	public void removeRequirement(int rUID) {
		for (Integer id : this.requirements) {
			if (id == rUID) {
				this.requirements.remove(id);
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof Iteration) {
			Iteration i = (Iteration) o;
			if (i.getId() == this.id) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date of the iteration. Note that the start date must be
	 * after the end date
	 * 
	 * @param startDate
	 *            the startDate to set
	 * @throws InvalidDateException
	 *             if the start date is after the end date
	 */
	public void setStartDate(Date startDate) throws InvalidDateException {
		if (startDate.compareTo(endDate) < 0) {
			this.startDate = startDate;
		} else {
			throw new InvalidDateException(startDate);
		}
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date of the iteration. Note that this must be after the
	 * start date
	 * 
	 * @param endDate
	 *            the endDate to set
	 * @throws InvalidDateException
	 *             if the end date is before the start date
	 */
	public void setEndDate(Date endDate) throws InvalidDateException {
		if (endDate.compareTo(startDate) > 0) {
			this.endDate = endDate;
		} else {
			throw new InvalidDateException(endDate);
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the requirements
	 */
	public List<Integer> getRequirements() {
		return requirements;
	}

	/**
	 * @param requirements
	 *            the requirements to set
	 */
	public void setRequirements(List<Integer> requirements) {
		this.requirements = requirements;
	}
}
