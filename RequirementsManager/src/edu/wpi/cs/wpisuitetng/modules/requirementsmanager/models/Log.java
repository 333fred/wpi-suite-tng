package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * Simple class to hold a log and any associated metadata
 * 
 * @author Fredric
 */

public class Log implements Event {

	private String log;
	private Date date;
	private User creator;
	
	/**
	 * Create a new Log with everything specified
	 * 
	 * @param log
	 *            The content of the log
	 * @param date
	 *            The date the log was created
	 * @param creator
	 *            The user that created the log
	 */
	public Log(String log, Date date, User creator) {
		this.log = log;
		this.date = date;
		this.creator = creator;
	}

	/**
	 * Create a new log. The date and time is set to the current system date
	 * and time.
	 * 
	 * @param log
	 *            The content of the log
	 * @param creator
	 *            The creator of the log
	 */
	public Log(String log, User creator) {
		this.log = log;
		this.creator = creator;
		this.date = new Date();
	}

	/**
	 * Create a blank log. The date will be recorded, but it can always be
	 * changed with setDate
	 */
	public Log() {
		this.date = new Date();
	}

	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + getCreator().getName() + " on<font size=.25></b> " + new SimpleDateFormat("MM/dd/yy hh:mm a").format(getDate()) + "</html>";	
	}

	@Override
	public String getContent() {
		return "<html><i>" + getLog() + "</i></html>";
	}
	
}
