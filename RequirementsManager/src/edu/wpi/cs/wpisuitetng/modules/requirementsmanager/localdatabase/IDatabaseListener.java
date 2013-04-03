package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase;

public interface IDatabaseListener {

	/**
	 * Called when a change is detected in the database, to be used for updating
	 * the UI
	 */
	public void update();

	/**
	 * @return whether or not the listener should be removed
	 */
	public boolean shouldRemove();

}
