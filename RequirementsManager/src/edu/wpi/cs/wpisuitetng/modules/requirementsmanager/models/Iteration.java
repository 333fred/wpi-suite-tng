/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * @author Jason Whitehouse
 * Skeleton class for iteration. This will be made into a model in the future, but for
 * now it is just a dummy for allowing assignment.
 * 
 * Because this is not a full implementation, validation of dates is not required
 */
public class Iteration extends AbstractModel{
	private Date startDate;
	private Date endDate;
	private int idNum;
	
	
	/**
	 * Constructor
	 * @param start Start date of the iteration
	 * @param end End date of the iteration. Must be later than the
	 * @param num ID num of the iteration
	 */
	public Iteration(Date start, Date end, int num){
		startDate = start;
		endDate = end;
		idNum = num;
	}
	
	//TODO implement model methods
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIdNum() {
		return idNum;
	}

	public void setIdNum(int idNum) {
		this.idNum = idNum;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
