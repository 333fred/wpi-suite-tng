/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Fredric Silberberg
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.Event;

/**
 * Implementation of the abstract Changeset type, that can be
 * displayed as an Event
 */

public class RequirementChangeset extends Changeset implements Event {
	
	/**
	 * Returns the RequirementChangset from the given JSON-encoded string
	 * 
	 * @param content
	 *            the JSON-encoded requirementchangeset
	 * @return the requirement changeset
	 */
	public static RequirementChangeset fromJSON(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, RequirementChangeset.class);
	}
	
	/**
	 * Returns the RequirementChangset array from the given JSON-encoded string
	 * 
	 * @param content
	 *            the JSON-encoded requirementchangeset array
	 * @return the requirement changeset array
	 */
	public static RequirementChangeset[] fromJSONArray(final String content) {
		final Gson parser = new Gson();
		return parser.fromJson(content, RequirementChangeset[].class);
	}
	
	/**
	 * Create a requirement changeset with null user and the current time
	 */
	public RequirementChangeset() {
	}
	
	/**
	 * Create a requirement changeset with the given user and the current time
	 * 
	 * @param u
	 *            the creator
	 */
	public RequirementChangeset(final User u) {
		super(u);
	}
	
	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContent() {
		// Check for every change in field that could be in this changeset, and
		// add it to the return string
		String content = "";
		
		// Start by checking to see if this the creation changeset. If so, then
		// just say we created the requirement and return
		if (changes.get("creation") != null) {
			content += "Created Requirement<br></html>";
			return content;
		}
		
		// Otherwise, check all changes that we track and record them in the
		// log, returning at the end
		if (changes.get("name") != null) {
			final FieldChange<?> change = changes.get("name");
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
			final int oldIteration = ((Double) changes.get("iteration")
					.getOldValue()).intValue();
			final int newIteration = ((Double) changes.get("iteration")
					.getNewValue()).intValue();
			try {
				final String oldName = IterationDatabase.getInstance()
						.get(oldIteration).getName();
				final String newName = IterationDatabase.getInstance()
						.get(newIteration).getName();
				content += oldToNew("Iteration", new FieldChange<String>(
						oldName, newName));
			} catch (final IterationNotFoundException e) {
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
			final List<Object> added = new ArrayList<Object>();
			final List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("users"));
			if (added.size() > 0) {
				content += "Added " + added.size() + " assignee(s)<br>";
				for (final Object o : added) {
					content += "   " + o.toString() + "<br>";
				}
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size() + " assignee(s)<br>";
				for (final Object o : removed) {
					content += "   " + o.toString() + "<br>";
				}
			}
		}
		if (changes.get("subRequirements") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			final List<Object> added = new ArrayList<Object>();
			final List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("subRequirements"));
			if (added.size() > 0) {
				content += "Added " + added.size()
						+ " Sub-Requirement(s)<br><I>";
				for (final Object o : added) {
					final Integer i = (int) Math.round((Double) o);
					content += "&nbsp;&nbsp;&nbsp;";
					try {
						final Requirement tempReq = RequirementDatabase
								.getInstance().get(i);
						content += tempReq.getName() + "<br>";
					} catch (final RequirementNotFoundException e) {
						content += "Unknown requirement with ID "
								+ i.toString() + "<br>";
						e.printStackTrace();
					}
				}
				content += "</I>";
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size()
						+ " Sub-Requirement(s)<br><I>";
				for (final Object o : removed) {
					final Integer i = (int) Math.round((Double) o);
					content += "&nbsp;&nbsp;&nbsp;";
					try {
						final Requirement tempReq = RequirementDatabase
								.getInstance().get(i);
						content += tempReq.getName() + "<br>";
					} catch (final RequirementNotFoundException e) {
						content += "Unknown requirement with ID "
								+ i.toString() + "<br>";
						e.printStackTrace();
					}
				}
				content += "</I>";
			}
		}
		if (changes.get("pUID") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			final List<Object> added = new ArrayList<Object>();
			final List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("pUID"));
			if (added.size() > 0) {
				content += "Added " + added.size()
						+ " Parent Requirement(s)<br><I>";
				for (final Object o : added) {
					final Integer i = (int) Math.round((Double) o);
					content += "&nbsp;&nbsp;&nbsp;";
					try {
						final Requirement tempReq = RequirementDatabase
								.getInstance().get(i);
						content += tempReq.getName() + "<br>";
					} catch (final RequirementNotFoundException e) {
						content += "Unknown requirement with ID "
								+ i.toString() + "<br>";
						e.printStackTrace();
					}
					
				}
				content += "</I>";
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size()
						+ " Parent Requirement(s)<br><I>";
				for (final Object o : removed) {
					final Integer i = (int) Math.round((Double) o);
					content += "&nbsp;&nbsp;&nbsp;";
					try {
						final Requirement tempReq = RequirementDatabase
								.getInstance().get(i);
						content += tempReq.getName() + "<br>";
					} catch (final RequirementNotFoundException e) {
						content += "Unknown requirement with ID "
								+ i.toString() + "<br>";
						e.printStackTrace();
					}
				}
				content += "</I>";
			}
		}
		if (changes.get("notes") != null) {
			// Use oldToNewList to get the list of additions and removals from
			// the change
			final List<Object> added = new ArrayList<Object>();
			final List<Object> removed = new ArrayList<Object>();
			oldToNewList(added, removed, changes.get("notes"));
			if (added.size() > 0) {
				content += "Added " + added.size() + " note<br>";
			}
			if (removed.size() > 0) {
				content += "Removed " + removed.size() + " note(s)<br>";
			}
		}
		if (changes.get("tasks") != null) {
			final List<Object> added = new ArrayList<Object>();
			final List<Object> removed = new ArrayList<Object>();
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
		if (changes.get("aTests") != null) {
			final List<Object> added = new ArrayList<Object>();
			final List<Object> removed = new ArrayList<Object>();
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
		
		return content;
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
	
	@Override
	public Boolean identify(final Object o) {
		return equals(o);
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
	private String oldToNew(final String type, final FieldChange<?> change) {
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
	@SuppressWarnings ("unchecked")
	private void oldToNewList(final List<Object> added,
			final List<Object> removed, final FieldChange<?> change) {
		final List<Object> oldList = (List<Object>) change.getOldValue();
		final List<Object> newList = (List<Object>) change.getNewValue();
		for (final Object oldObj : oldList) {
			boolean detected = false;
			for (final Object newObj : newList) {
				if (oldObj.equals(newObj)) {
					// In this case, we've confirmed that the new list
					// of object has the given object from
					// the old list, so break and set the detected
					// variable to true
					detected = true;
					break;
				}
			}
			// If we didn't detect the old object in the new list,
			// increase the count
			if (!detected) {
				removed.add(oldObj);
			}
		}
		// Now check for newly added objects
		for (final Object newObj : newList) {
			boolean detected = false;
			for (final Object oldObj : oldList) {
				if (newObj.equals(oldObj)) {
					// In this case, we've confirmed that the old list
					// of objects has the given object from
					// the old list, so break and set the detected
					// variable to true
					detected = true;
					break;
				}
			}
			// If we didn't detect the new object in the old list,
			// increase the count
			if (!detected) {
				added.add(newObj);
			}
		}
	}
	
	@Override
	public void save() {
	}
	
	@Override
	public String toJSON() {
		return new Gson().toJson(this, RequirementChangeset.class);
	}
	
}
