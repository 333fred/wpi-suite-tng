package edu.wpi.cs.wpisuitetng.modules.logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public abstract class Changeset {

	protected Map<String, FieldChange<?>> changes;
	protected Date date;
	protected User user;

	/**
	 * Create a new changeset, with the current time and null creator
	 */
	public Changeset() {
		this.changes = new HashMap<String, FieldChange<?>>();
		this.date = new Date();
		this.user = null;
	}

	/**
	 * Create a new changeset, with the current date and the specified user
	 * 
	 * @param u the creator
	 */
	public Changeset(User u) {
		this();
		this.user = u;
	}

	/**
	 * @return the map of field names to changes (Assignee -> (Bob, Joe))
	 */
	public Map<String, FieldChange<?>> getChanges() {
		return this.changes;
	}

	/**
	 * @param changes
	 *            the changes to set
	 */
	public void setChanges(Map<String, FieldChange<?>> changes) {
		this.changes = changes;
	}

	/**
	 * @return The Date when this event happened
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date
	 *            The Date of the Event to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return The User responsible for this event
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            The User responsible for the event to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
