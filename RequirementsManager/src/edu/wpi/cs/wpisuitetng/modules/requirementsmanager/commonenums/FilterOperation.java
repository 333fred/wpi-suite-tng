package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

public enum FilterOperation {

	LESS_THAN("<"), LESS_THAN_EQUAL("<="), EQUAL("Equal"), NOT_EQUAL(
			"Not equal"), GREATER_THAN_EQUAL(">="), GREATER_THAN(">"), OCCURS_BETWEEN(
			"Occurs between"), OCCURS_AFTER("Occurs after"), OCCURS_BEFORE(
			"Occurs before"), CONTAINS("Contains"), STARTS_WITH("Starts with");

	private String name;

	private FilterOperation(String name) {
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
	
	public static FilterOperation getFromString(String str) {
		for (FilterOperation operation: values()) {
			if (str.equals(operation.toString())) {
				return operation;
			}
		}
		
		return null;
	}

}
