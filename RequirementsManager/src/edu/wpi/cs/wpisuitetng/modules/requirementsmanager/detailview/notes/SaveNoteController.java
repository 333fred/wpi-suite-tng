package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes;

import javax.swing.JOptionPane;

//import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.DetailView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;


/**
 * This controller handles saving defect notes to the server
 */
public class SaveNoteController {

	private final NotePanel view;
	private final Requirement model;
	private final DetailView parentView;

	/**
	 * Construct the controller
	 * @param view the NewnotePanel containing the note field
	 * @param model the Defect model being noteed on
	 * @param parentView the DefectPanel displaying the defect
	 */
	public SaveNoteController(NotePanel view, Requirement model, DetailView parentView) {
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
			this.model.addNote(new Note(noteText));
			//TODO: Display the note in the main frame
			JOptionPane.showMessageDialog(parentView, "You're note: "+noteText, "You Added A Note! Oh Boy!", JOptionPane.OK_OPTION);
			parentView.getNoteList().addElement(noteText);
		}
	}
}
