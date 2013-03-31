package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

public class IterationNotFoundException extends Exception {
	private long id;

	public IterationNotFoundException(long id2) {
		this.id = id2;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
}
