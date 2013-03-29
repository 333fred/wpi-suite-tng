package edu.wpi.cs.wpisuitetng.modules.logger;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.Model;

public interface LoggableModel extends Model {

	/**
	 * @param date
	 *            the new last modified date
	 */
	public void setLastModifiedDate(Date date);

	/**
	 * @return the last modified date
	 */
	public Date getLastModifiedDate();

}
