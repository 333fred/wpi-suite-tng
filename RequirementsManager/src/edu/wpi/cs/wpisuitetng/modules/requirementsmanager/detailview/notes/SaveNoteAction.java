package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action that calls {@link edu.wpi.cs.wpisuitetng.modules.RequirementsManager.notes.SaveNoteController#savenote()}
 */
@SuppressWarnings("serial")
public class SaveNoteAction extends AbstractAction {
	public final SaveNoteController controller;

	/**
	 * Construct the action
	 * @param controller the controller to trigger
	 */
	public SaveNoteAction(SaveNoteController controller) {
		super("Add note");
		this.controller = controller;
	}
	
	/*
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.savenote();
	}

}
