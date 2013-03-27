/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event.EventCellRenderer;


/**
 * Panel containing a note for a requirement
 * @author Zac Chupka, Maddie Burris
 */
public class DetailNoteView extends JPanel{
	/** For Notes */
	protected DefaultListModel noteList;
	protected JList notes;
	private Requirement requirement;
	private DetailPanel parentView;
	
	/**
	 * Construct the panel and add layout components
	 * @param requirement the requirement 
	 * @param parentView the parent view
	 */
	public DetailNoteView(Requirement requirement, DetailPanel parentView){
		this.requirement = requirement;
		this.parentView = parentView;
		
		setLayout(new BorderLayout());
		// Set up the note panel
		MakeNotePanel makeNotePanel = new MakeNotePanel(requirement, parentView);

		// Create the note list
		noteList = new DefaultListModel();
		notes = new JList(noteList);
		notes.setCellRenderer(new EventCellRenderer());

		// Add the list to the scroll pane
		JScrollPane noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(notes);
		
		// Set up the frame
		JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		notePane.add(noteScrollPane, BorderLayout.CENTER);
		notePane.add(makeNotePanel, BorderLayout.SOUTH);
		
		add(notePane, BorderLayout.CENTER);
		
		//adds the notes to the list model
		addNotesToList();
	}
	
	private void addNotesToList() {
		noteList.clear();
		
		//add the notes to the list model.
		for (Note aNote : requirement.getNotes()) {
			this.noteList.addElement(aNote);
		}
	}
	
	/**
	 * A function to get the list of notes added
	 * @return the list of notes
	 */
	public DefaultListModel getNoteList() {
		return noteList;
	}
	
	/** Updates the requirement that is being displayed
	 * 
	 * @param newRequirement The updated version of the requirement
	 */
	
	public void updateRequirement(Requirement newRequirement) {
		this.requirement = newRequirement;
		
		//updates the notes list
		addNotesToList();
	}
	
}
