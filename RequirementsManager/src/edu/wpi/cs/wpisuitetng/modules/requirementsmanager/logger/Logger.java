package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.LogManager;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Log;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * 
 * @author Fredric
 */

public class Logger {

	// Singleton instance of the logger
	private static Logger logger;

	public static Logger getInstance() {
		if (logger == null) {
			logger = new Logger();
		}
		return logger;
	}

	/**
	 * Record for an event
	 * 
	 * @author Fredric
	 */
	public class Event {
		private Object oldVal;
		private Object newVal;
		private EventType type;

		public Event() {
		}

		public Event(Object oldVal, Object newVal, EventType type) {
			this.oldVal = oldVal;
			this.newVal = newVal;
			this.type = type;
		}
	}

	/**
	 * Type of event record
	 * 
	 * @author Fredric
	 */
	public enum EventType {
		NAME_CHANGE, DESC_CHANGE, TYPE_CHANGE, STATUS_CHANGE, PRIORITY_CHANGE, RELEASE_CHANGE, ITER_CHANGE, EFFORT_CHANGE, ASSIGN_CHANGE, SUB_CHANGE, PARENT_CHANGE, NOTE_CHANGE
	}

	/**
	 * Logs the creation of a requirement
	 * 
	 * @param req
	 *            the new requirement
	 * @param s
	 *            the session of the requirement
	 */
	public static void logCreation(Requirement req, Session s) {
		Log log = new Log("Created requirement<br>", s.getUser());
		req.addLog(log);
	}

	/**
	 * Static method that processes a list of changes, and adds them to a
	 * requirement, timestamping them
	 * 
	 * @param req
	 *            The requirement to log
	 * @param events
	 *            The events to log
	 */
	public static void logEvents(Requirement req, List<Event> events, Session s) {
		
		// Check for no events occurring
		if(events == null){
			return;
		}
		
		// Log of all events
		String logMsg = "";
		boolean logged = false;

		// Loop through all events, and add them to the log based on
		// what type of event occurred.
		for (Event event : events) {
			String type = null;
			System.out.println("Event type " + event.type.toString());
			switch (event.type) {
			case DESC_CHANGE:
				// We will add the whole change to the log, since it would take
				// up a lot of space. This is the only one that we don't fall
				// through
				logMsg += ("Updated the Description<br>");
				logged = true;
				break;
			case NOTE_CHANGE:
				// Set type
				if (type == null) {
					type = "Note(s)";
				}
			case SUB_CHANGE:
				// Set type
				if (type == null) {
					type = "Sub-Requirement(s)";
				}
			case ASSIGN_CHANGE:
				// Set type
				if (type == null) {
					type = "Assignee(s)";
				}
				// Loop through each list of objects, and find all
				// deletions, additions, and report them all. This way, if we
				// delete two and add two, it won't report deleting/adding 0
				// objects. This is a general case loop, that supports looping
				// through all of our lists and reporting the number of
				// deletions and additions to any of them
				int deletedCount = 0;
				int addedCount = 0;
				List<Object> oldList = (List<Object>) event.oldVal;
				List<Object> newList = (List<Object>) event.newVal;
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
						deletedCount++;
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
						addedCount++;
					}
				}
				// Put added and deleted in the log based on whether or not they
				// exist
				if (addedCount > 0) {
					logMsg += "Added " + addedCount + " " + type + "<br>";
					System.out.println(logMsg);
				}
				if (deletedCount > 0) {
					logMsg += "Removed " + deletedCount + " " + type + "<br>";
				}
				logged = true;
				break;
			case ITER_CHANGE:
				// TODO: Once we implement iterations, we can determine how to
				// log
				logMsg += ("Changed Iteration<br>");
				logged = true;
				break;
			case RELEASE_CHANGE:
				// TODO: Implement Releases, then we can log them
				logged = true;
				break;
			case NAME_CHANGE:
				logMsg += "Name: " + "<b>\"" + event.oldVal.toString()
						+ "\"</b>" + " to <b>\"" + event.newVal.toString()
						+ "\"</b><br>";
				logged = true;
				break;
			case PARENT_CHANGE:
				if (type == null) {
					type = "Parent Requirement: ";
				}
			case TYPE_CHANGE:
				if (type == null) {
					type = "Type: ";
				}
			case STATUS_CHANGE:
				if (type == null) {
					type = "Status: ";
				}
			case PRIORITY_CHANGE:
				if (type == null) {
					type = "Priority: ";
				}
			case EFFORT_CHANGE:
				if (type == null) {
					type = "Effort: ";
				}
				logMsg += type + "<b>" + event.oldVal.toString() + "</b>"
						+ " to <b>" + event.newVal.toString() + "</b><br>";
				logged = true;
			default:
				break;
			}
		}
		if (logged) {
			Log log = new Log(logMsg, s.getUser());
			req.addLog(log);
		}
	}
}
