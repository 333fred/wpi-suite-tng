package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.Color;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.janeway.config.Configuration;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.SaveRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;


/**
 * This controller handles saving requirement notes to the server
 */
public class SaveNoteController {

	private final MakeNotePanel view;
	private final Requirement model;
	private final DetailPanel parentView;

	/**
	 * Construct the controller
	 * @param view the MakeNotePanel containing the note field
	 * @param model the requirement to which notes are being added
	 * @param parentView the DetailPanel displaying the current requirement
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
			view.getnoteField().requestFocusInWindow();
			
			//We want to save the note to the server immediately, but only if the requirement hasn't been just created
			if (model.getName().length() > 0) {
				SaveRequirementController controller = new SaveRequirementController(this.parentView);
				controller.SaveRequirement(model,false);
			}
			
			
		}
	}
}
