/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Fredric
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.logging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.logger.Changeset;
import edu.wpi.cs.wpisuitetng.modules.logger.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * Implementation of the abstract Changeset type, that can be displayed as an
 * Event
 * 
 * @author Fredric
 */

public class RequirementChangeset extends Changeset implements Event {

	/**
	 * Create a requirement changeset with null user and the current time
	 */
	public RequirementChangeset() {
		super();
	}

	/**
	 * Create a requirement changeset with the given user and the current time
	 * 
	 * @param u
	 *            the creator
	 */
	public RequirementChangeset(User u) {
		super(u);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return "<html><font size=4><b>" + user.getName()
				+ " on<font size=.25></b> "
				+ new SimpleDateFormat("MM/dd/yy hh:mm a").format(date)
				+ "</html>";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContent() {
		// Check for every change in field that could be in this changeset, and
		// add it to the return string
		String content = "<html>";

		// Start by checking to see if this the creation changeset. If so, then
		// just say we created the requirement and return
		if (changes.get("creation") != null) {
			content += "Created Requirement<br></html>";
			return content;
		}

		// Otherwise, check all changes that we track and record them in the
		// log, returning at the end
		if (changes.get("name") != null) {
			FieldChange<?> change = changes.get("name");
			// Name is specially formatted, so we don't use the oldToNew method
			content += "Name: " + "<b>\"" + change.getOldValue().toString()
					+ "\"</b>" + " to <b>\"" + change.getNewValue().toString()
					+ "\"</b><br>";
		}
		if (changes.get("description") != null) {
			content += "Updated the Description<br>";
		}
		if (changes.get("type") != null) {
			// Get the default string for an old and new value
			content += oldToNew("Type", changes.get("type"));
		}
		if (changes.get("priority") != null) {
			// Get the default string for an old and new value
			content += oldToNew("Priority", changes.get("priority"));
		}
		if (changes.get("status") != null) {
			// Get the default string for an old and new value
			content += oldToNew("Status", changes.get("status"));
		}
		if (changes.get("releaseNum") != null) {
			// Get the default string for an old and new value
			content += oldToNew("Release Number", changes.get("releaseNum"));
		}
		if (changes.get("iteration") != null) {
			// Get the default string for an old and new value
			// content += oldToNew("Iteration", changes.get("iteration"));
			int oldIteration = ((Double) changes.get("iteration").getOldValue())
					.intValue();
			int newIteration = ((Double) changes.get("iteration").getNewValue())
					.intValue();
			try {
				String oldName = IterationDatabase.getInstance()
						.getIteration(oldIteration).getName();
				String newName = IterationDatabase.getInstance()
						.getIteration(newIteration).getName();
				content += oldToNew("Iteration", new FieldChange<String>(
						oldName, newName));
			} catch (IterationNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (changes.get("estimate") != null) {
			// Get the default string for an old and new value
			content += oldToNew("Estimate", changes.get("estimate"));
		}
		if (changes.get("effort") != null) {
			// Get the default string for an old and new value
			content += oldToNew("Effort", changes.get("effort"));
		}
		if (changes.get("users") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			List<Object> added = new ArrayList<Object>();
			List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("users"));
			if (added.size() > 0) {
				content += "Added " + added.size() + " assignee(s)<br>";
				for (Object o : added) {
					content += "   " + o.toString() + "<br>";
				}
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size() + " assignee(s)<br>";
				for (Object o : removed) {
					content += "   " + o.toString() + "<br>";
				}
			}
		}
		if (changes.get("subRequirements") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			List<Object> added = new ArrayList<Object>();
			List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("subRequirements"));
			if (added.size() > 0) {
				content += "Added " + added.size() + " Sub-Requirement(s)<br>";
				for (Object o : added) {
					// TODO: Make it look up the correct name, instead of just
					// the ID
					Integer u = (Integer) o;
					content += u.toString() + "<br>";
				}
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size()
						+ " Sub-Requirement(s)<br>";
				for (Object o : removed) {
					// TODO: Make it look up the correct name, instead of just
					// the ID
					Integer u = (Integer) o;
					content += u.toString() + "<br>";
				}
			}
		}
		if (changes.get("pUID") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			List<Object> added = new ArrayList<Object>();
			List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("pUID"));
			if (added.size() > 0) {
				content += "Added " + added.size()
						+ " Parent Requirement(s)<br>";
				for (Object o : added) {
					// TODO: Make it look up the correct name, instead of just
					// the ID
					Integer u = (Integer) o;
					content += u.toString() + "<br>";
				}
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size()
						+ " Parent Requirement(s)<br>";
				for (Object o : removed) {
					// TODO: Make it look up the correct name, instead of just
					// the ID
					Integer u = (Integer) o;
					content += u.toString() + "<br>";
				}
			}
		}
		if (changes.get("notes") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			List<Object> added = new ArrayList<Object>();
			List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("notes"));
			if (added.size() > 0) {
				content += "Added " + added.size() + " note<br>";
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size() + " note(s)<br>";
			}
		}
		if (changes.get("tasks") != null) {
			List<Object> added = new ArrayList<Object>();
			List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("tasks"));
			int modified = added.size();
			if (removed.size() > 0) {
				content += "Modified " + removed.size() + " task(s)<br>";
				modified -= removed.size();
			}
			if (modified > 0) {
				content += "Added " + modified + " task(s)<br>";
			}			
		}
		if(changes.get("aTests") != null){
			List<Object> added = new ArrayList<Object>();
			List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("aTests"));
			int modified = added.size();
			if (removed.size() > 0) {
				content += "Modified " + removed.size() + " tests(s)<br>";
				modified -= removed.size();
			}
			if (modified > 0) {
				content += "Added " + modified + " tests(s)<br>";
			}	
		}

		content += "</html>";

		return content;
	}

	/**
	 * Takes a given field change, such a enum to enum or string to string, and
	 * returns a formatted string showing the description with the given type
	 * 
	 * @param type
	 *            the type change
	 * @param change
	 *            the FieldChange with the old and new values
	 * @return the formatted change description
	 */
	private String oldToNew(String type, FieldChange<?> change) {
		return type + " <b>" + change.getOldValue().toString() + "</b>"
				+ " to <b>" + change.getNewValue().toString() + "</b><br>";
	}

	/**
	 * Takes two List<Object> variables, loops through a given change, and adds
	 * all added and removed variables to the correct list
	 * 
	 * @param added
	 *            the list to be populated with added variables
	 * @param removed
	 *            the list to be populated with removed variables
	 * @param change
	 *            the FieldChange with the old and new Lists
	 */
	private void oldToNewList(List<Object> added, List<Object> removed,
			FieldChange<?> change) {
		List<Object> oldList = (List<Object>) change.getOldValue();
		List<Object> newList = (List<Object>) change.getNewValue();
		for (Object oldObj : oldList) {
			boolean detected = false;
			for (Object newObj : newList) {
				if (oldObj.equals(newObj)) {
					// In this case, we've confirmed that the new list
					// of requirements has the given requirement from
					// the old list, so break and set the detected
					// variable to true
					detected = true;
					break;
				}
			}
			// If we didn't detect the old requirement in the new list,
			// increase the count
			if (!detected) {
				removed.add(oldObj);
			}
		}
		// Now check for newly added requirements
		for (Object newObj : newList) {
			boolean detected = false;
			for (Object oldObj : oldList) {
				if (newObj.equals(oldObj)) {
					// In this case, we've confirmed that the old list
					// of requirements has the given requirement from
					// the old list, so break and set the detected
					// variable to true
					detected = true;
					break;
				}
			}
			// If we didn't detect the new requirement in the oldF list,
			// increase the count
			if (!detected) {
				added.add(newObj);
			}
		}
	}

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
		return new Gson().toJson(this, RequirementChangeset.class);
	}

	public static RequirementChangeset fromJSON(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, RequirementChangeset.class);
	}

	public static RequirementChangeset[] fromJSONArray(String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, RequirementChangeset[].class);
	}

	@Override
	public Boolean identify(Object o) {
		return this.equals(o);
	}

}
