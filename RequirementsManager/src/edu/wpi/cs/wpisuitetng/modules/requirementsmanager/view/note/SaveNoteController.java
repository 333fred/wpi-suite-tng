package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.config.Configuration;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;

/**
 * This controller handles saving defect notes to the server
 */
public class SaveNoteController {

	private final MakeNotePanel view;
	private final Requirement model;
	private final DetailPanel parentView;

	/**
	 * Construct the controller
	 * @param view the NewnotePanel containing the note field
	 * @param model the Defect model being note on
	 * @param parentView the DefectPanel displaying the defect
	 */
	public SaveNoteController(MakeNotePanel view, Requirement model, DetailPanel parentView) {
		this.view = view; 
		this.model = model;
		this.parentView = parentView;
	}
	
	/**
	 * Save a note to the server
	 */
	public void savenote() {
		final String noteText = view.getnoteField().getText();
		if (noteText.length() > 0) {
			this.model.addNote(new Note(noteText, ConfigManager.getConfig().getUserName()));
			parentView.getNoteList().addElement(this.model.getNotes().get(this.model.getNotes().size()-1));
			view.getnoteField().setText("");
		}
	}
}
