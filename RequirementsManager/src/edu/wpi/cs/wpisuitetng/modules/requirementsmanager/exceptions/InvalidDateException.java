package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions;

import java.util.Date;

public class InvalidDateException extends Exception {
	
	Date invalidDate;
	
	public InvalidDateException(Date invalidDate){
		super();
		this.invalidDate = invalidDate;
	}

}
