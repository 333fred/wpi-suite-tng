package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.charts;

/**
 * String and iteger pair for use in AbstractRequirementStatistics, stores the
 * String value to display in the char
 * a long with its integer value.
 * 
 * User to keep the order of the values, as using toString on Iteration might
 * not be the most conveint.
 */

public class StringIntegerPair {
	
	private String string;
	private Integer value;
	
	/**
	 * Creates a new StringIntegerPair with the given string and value pair
	 * 
	 * @param string
	 *            the string to hold
	 * @param value
	 *            the integer to hold
	 */
	public StringIntegerPair(String string, Integer value) {
		this.string = string;
		this.value = value;
	}
	
	/**
	 * @return the string value of this pair
	 */
	
	public String getString() {
		return string;
	}
	
	/**
	 * @return the integer value of this pair
	 */
	
	public Integer getInterger() {
		return value;
	}
}
