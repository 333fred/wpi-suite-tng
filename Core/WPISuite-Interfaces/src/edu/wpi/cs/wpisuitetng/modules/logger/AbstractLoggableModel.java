package edu.wpi.cs.wpisuitetng.modules.logger;

import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

public abstract class AbstractLoggableModel extends AbstractModel implements
		LoggableModel {

	protected Date lastModifiedDate;

	/**
	 * {@inheritDoc}
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLastModifiedDate(Date date) {
		this.lastModifiedDate = date;
	}

}
