package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.logger;

import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * 
 * @author Fredric
 */

public class Logger {

	/**
	 * Record for an event
	 * 
	 * @author Fredric
	 */
	public class Event {
		private Object oldVal;
		private Object newVal;
		private EventType type;
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
		String log;

		/*
		 * Timestamp and userstamp the log, and insert a newline It will look
		 * like this: On (DATE) (USERNAME) changed: (CHANGES)
		 */
		Date date = new Date();
		log = "On " + date.toString() + "\n" + s.getUsername() + " changed:\n";

		String type = null;

		// Loop through all events, and add them to the log based on
		// what type of event occurred.
		for (Event event : events) {
			switch (event.type) {
			case DESC_CHANGE:
				// We will add the whole change to the log, since it would take
				// up a lot of space. This is the only one that we don't fall
				// through
				log.concat("\tUpdated the Description\n");
				break;
			case NOTE_CHANGE:
				Note note = (Note) event.newVal;
				log.concat("\tAdded note: " + note.getNote() + "\n");
				break;
			case SUB_CHANGE:
				// TODO: Determine how to display the changes
				break;
			case ITER_CHANGE:
				// TODO: Once we implement iterations, we can determine how to
				// log
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
				log.concat(type + event.oldVal.toString() + " to " + event.newVal.toString() + "\n");
			}
		}
	}

}
