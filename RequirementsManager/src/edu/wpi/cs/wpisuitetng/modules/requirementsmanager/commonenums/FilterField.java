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
	
	/** Return the enum that the given string represents
	 * 
	 * @param str String to parse
	 * @return The enum value, or null if it doesnt exist 
	 */
	
	public static FilterField getFromString(String str) {
		for (FilterField field: values()) {
			if (str.equals(field.toString())) {
				return field;
			}
		}
		
		return null;
	}
}