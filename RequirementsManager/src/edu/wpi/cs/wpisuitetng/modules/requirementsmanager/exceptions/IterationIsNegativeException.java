package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

public class IterationIsNegativeException extends Exception {
	int id;

	public IterationIsNegativeException(int id) {
		super();
		this.id = id;
	}
	
}
