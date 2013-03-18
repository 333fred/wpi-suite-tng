/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import java.awt.*;
import javax.swing.*;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.defectevents.model.DefectEventListModel;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes.NoteList;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
/**
 * @author Alex Woodyard
 *
 */
public class DetailView extends JPanel{

	private JPanel top, bottom, left, right;
	private JButton ok;
	private JTextField name;
	private JLabel nameLabel;
	
	/** The data model for the list */
	protected DefaultListModel noteList;
	protected JList notes;
	private Requirement model;
	
	public DetailView(DetailView detailView, Requirement model){
		this.model = model;
		
		noteList = new DefaultListModel();
		notes = new JList(noteList);
		//JScrollPane notePane = new JScrollPane(notes);
		
		top = new JPanel();
		bottom = new JPanel();
		//next two lines are temporary fillers for the time being.  
		// They can be removed once the other two "modules" are complete
		left = new JPanel();
		right = new JPanel();
		ok = new JButton("OK");
		nameLabel = new JLabel("Name: ");
		name = new JTextField();
		
		setLayout(new BorderLayout()); //set to use outside edges as basis for sub panels.
		
		//add the name label and text box to the top panel
		top.setLayout(new GridLayout(1,2));
		//Grid layout: use a grid for the base layout for the panel.
		top.add(nameLabel);
		top.add(name);
		
		//add the ok button to the bottom panel
		bottom.add(ok);
				
		
		//uncomment these lines when ready to add the notes module and the module
		// that has the description, status, etc
		//left = new DescriptionPanel();
		//right = new NotesPanel();
		
		//add all the sub panels to the main panel.
		add(top, BorderLayout.NORTH);
		add(bottom,BorderLayout.SOUTH);
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);
		
		
		add(notes); //This is for displaying the notes
	}
	
	public DefaultListModel getNoteList() {
		return noteList;
	}
	
	public Requirement getModel() {
		return this.model; //TODO: Make sure the Requirement member is in here
	}
}
