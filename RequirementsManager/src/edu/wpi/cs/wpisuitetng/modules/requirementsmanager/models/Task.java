package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models;

public class Task {
	private boolean completed;
	
	public Task(){
		completed  = false;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	

}
