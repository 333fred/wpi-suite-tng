package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger;

import java.util.Date;
import java.util.List;

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
	 * @param req the new requirement
	 * @param s the session of the requirement
	 */
	public static void logCreation(Requirement req, Session s){
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

		// Log of all events
		String logMsg = "";

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
				break;
			case NOTE_CHANGE:
				// TODO: Need to loop through the notes, find all different, and
				// make a log of each change
				break;
			case SUB_CHANGE:
				// TODO: Determine how to display the changes
				logMsg += ("Changed Subrequirements<br>");
				break;
			case ITER_CHANGE:
				// TODO: Once we implement iterations, we can determine how to
				// log
				logMsg += ("Changed Iteration<br>");
				break;
			case RELEASE_CHANGE:
				// TODO: Implement Releases, then we can log them
				break;
			case PARENT_CHANGE:
				if (type == null) {
					type = "Parent Requirement: ";
				}
			case NAME_CHANGE:
				if (type == null) {
					type = "Name: ";
					logMsg += type +  "<b>\"" + event.oldVal.toString() + "\"</b>" + " to <b>\""
							+ event.newVal.toString() + "\"</b><br>";
					break;
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
			case ASSIGN_CHANGE:
				if (type == null) {
					type = "Assign: ";
				}
				logMsg += type +  "<b>" + event.oldVal.toString() + "</b>" + " to <b>"
						+ event.newVal.toString() + "</b><br>";
			default:
				break;
			}
		}

		// Now we need to actually log the event
		Log log = new Log(logMsg, s.getUser());
		req.addLog(log);
	}

}
