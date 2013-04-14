package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums;

public enum FilterOperation {

	LESS_THAN("<"), LESS_THAN_EQUAL("<="), EQUAL("Equals"), NOT_EQUAL(
			"Not equals"), GREATER_THAN_EQUAL(">="), GREATER_THAN(">"), OCCURS_BETWEEN(
			"Occurs between"), OCCURS_AFTER("Occurs after"), OCCURS_BEFORE(
			"Occurs before");

	private String name;

	private FilterOperation(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

}
