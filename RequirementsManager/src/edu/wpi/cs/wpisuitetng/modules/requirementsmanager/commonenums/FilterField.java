package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

/**
 * Enum for the field to filter
 * 
 */
public enum FilterField {
	NAME("Name"), TYPE("Type"), PRIORITY("Priority"), STATUS("Status"), ITERATION(
			"Iteration"), ESTIMATE("Estimate"), EFFORT("Effort"), RELEASE_NUMBER(
			"Release Number");

	private String name;

	private FilterField(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}