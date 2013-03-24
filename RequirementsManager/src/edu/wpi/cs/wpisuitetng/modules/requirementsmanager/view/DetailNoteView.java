/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Note;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.MakeNotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.noteCellRenderer;


/**
 * @author Zac Chupka, Maddie Burris
 *
 */
public class DetailNoteView extends JPanel{
	/** For Notes */
	protected DefaultListModel noteList;
	protected JList notes;
	private Requirement requirement;
	private DetailPanel parentView;
	
	
	public DetailNoteView(Requirement requirement, DetailPanel parentView){
		this.requirement = requirement;
		this.parentView = parentView;
		
		setLayout(new BorderLayout());
		// Set up the note panel
		MakeNotePanel makeNotePanel = new MakeNotePanel(requirement, parentView);

		// Create the note list
		noteList = new DefaultListModel();
		notes = new JList(noteList);
		notes.setCellRenderer(new noteCellRenderer());

		// Add the list to the scroll pane
		JScrollPane noteScrollPane = new JScrollPane();
		noteScrollPane.getViewport().add(notes);
		
		// Set up the frame
		JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		notePane.add(noteScrollPane, BorderLayout.CENTER);
		notePane.add(makeNotePanel, BorderLayout.SOUTH);
		
		add(notePane, BorderLayout.CENTER);
		
		
		for (Note aNote : requirement.getNotes()) {
			this.noteList.addElement(aNote);
		}
	}
	
	public DefaultListModel getNoteList() {
		return noteList;
	}
	
}
