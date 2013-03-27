package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Model class to hold a list of user names, only for querying the server
 * 
 * @author Fredric
 */

public class StringListModel implements Model {

	List<String> users;

	/**
	 * @return the users
	 */
	public List<String> getUsers() {
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<String> users) {
		this.users = users;
	}

	/**
	 * General constructor, given some list of users
	 * 
	 * @param users
	 *            the users to hold
	 */
	public StringListModel(List<String> users) {
		this.users = users;
	}

	/**
	 * Standard constructor, initializes the list of users to an array list
	 */
	public StringListModel() {
		this.users = new ArrayList<String>();
	}

	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void save() {
		// Unnecessary function for this model
	}

	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void delete() {
		// Unnecessary function for this model
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toJSON() {
		return new Gson().toJson(this, StringListModel.class);
	}

	/**
	 * Converts a given StringListModel from JSON to an actual object
	 * 
	 * @param content
	 *            The JSON encoded object
	 * @return the instance of the object
	 */
	public static StringListModel fromJson(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, StringListModel.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean identify(Object o) {
		return this.equals(o);
	}

	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public Permission getPermission(User u) {
		return null;
	}

	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void setPermission(Permission p, User u) {
		// TODO Auto-generated method stub

	}

	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public Project getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not implemented in this model, unnecessary for querying the database
	 */
	@Override
	public void setProject(Project p) {
		// TODO Auto-generated method stub

	}

}
