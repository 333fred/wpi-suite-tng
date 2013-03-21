/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.MakeNotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.SaveNoteAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.SaveNoteController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.note.noteCellRenderer;
/**
 * @author Swagasaurus
 *
 */
public class DetailView extends JPanel{
	
	/** For Notes */
	protected DefaultListModel noteList;
	protected JList notes;
	
	//Textfields
	JTextArea textName;
	JTextArea textDescription;

	//combo boxes
	JComboBox comboBoxType;
	JComboBox comboBoxStatus;
	JComboBox comboBoxPriority;
	
	private Requirement requirement;
	
	private static final int VERTICAL_PADDING = 10;
	private static final int VERTICAL_CLOSE = -5;
	private static final int VERTICAL_FAR = 20;
	private static final int HORIZONTAL_PADDING = 20;
	
	public DetailView(Requirement requirement){
		this.requirement = requirement;
		
		noteList = new DefaultListModel();
		notes = new JList(noteList);
		
		notes.setCellRenderer(new noteCellRenderer());
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		JLabel lblName = new JLabel("Name:");
		add(lblName);
		
		JLabel lblDescription = new JLabel("Description:");
		add(lblDescription);
		
		JLabel lblType = new JLabel("Type:");
		add(lblType);
		
		JLabel lblStatus = new JLabel("Status:");
		add(lblStatus);
		
		JLabel lblPriority = new JLabel("Priority:");
		add(lblPriority);

		textName = new JTextArea(1,40);
		textName.setLineWrap(false);
		textName.setBorder((new JTextField()).getBorder());
		add(textName);
		
	    textDescription = new JTextArea(4,40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
		add(textDescription);
				
		String[] availableTypes = { "", "Epic", "Theme", "User Story", "Non-functional","Scenario"};
		comboBoxType = new JComboBox(availableTypes);
		comboBoxType.setPrototypeDisplayValue("Non-functional");
		add(comboBoxType);
		
		String[] availableStatuses = {"","New","In Progress","Open","Complete","Deleted"};
		comboBoxStatus = new JComboBox(availableStatuses);
		comboBoxStatus.setPrototypeDisplayValue("Non-functional");
		add(comboBoxStatus);
		
		String[] availablePriorities = {"","High","Medium","Low"};
		comboBoxPriority = new JComboBox(availablePriorities);
		comboBoxPriority.setPrototypeDisplayValue("Non-functional");
		add(comboBoxPriority);
		
		JButton btnSave = new JButton("Save Requirement");
		btnSave.setAction(new SaveRequirementAction(requirement,this));
		add(btnSave);
		
		//create note panel
		//noteList = new DefaultListModel();
		//notes = new JList(noteList);
		MakeNotePanel makeNotePanel = new MakeNotePanel(this.requirement, this); 
		//JScrollPane noteScrollPanel = new JScrollPane(notes);
		
		add(notes); //This is for displaying the notes
		add(makeNotePanel);

		//Align left edges of objects
	    layout.putConstraint(SpringLayout.WEST, lblName, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblDescription, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblType, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblStatus, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblPriority, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, textName, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, textDescription, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, comboBoxType, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, comboBoxStatus, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, comboBoxPriority, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, btnSave, HORIZONTAL_PADDING, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, notes, HORIZONTAL_PADDING, SpringLayout.WEST, this);	     
	    layout.putConstraint(SpringLayout.WEST, makeNotePanel, HORIZONTAL_PADDING, SpringLayout.WEST, this);	    
	    
	    //Align Right edges of objects
	    layout.putConstraint(SpringLayout.EAST, makeNotePanel, 450, SpringLayout.WEST, this);
	    
	    //Align North Edges of Objects
	    layout.putConstraint(SpringLayout.NORTH, lblName, VERTICAL_PADDING, SpringLayout.NORTH, this);	  
	    layout.putConstraint(SpringLayout.NORTH, textName, VERTICAL_PADDING+VERTICAL_CLOSE, SpringLayout.SOUTH, lblName);	    
	    layout.putConstraint(SpringLayout.NORTH, lblDescription, VERTICAL_PADDING, SpringLayout.SOUTH, textName);
	    layout.putConstraint(SpringLayout.NORTH, textDescription, VERTICAL_PADDING+VERTICAL_CLOSE, SpringLayout.SOUTH, lblDescription);	    
	    layout.putConstraint(SpringLayout.NORTH, lblType, VERTICAL_PADDING, SpringLayout.SOUTH, textDescription);	
	    layout.putConstraint(SpringLayout.NORTH, comboBoxType, VERTICAL_PADDING+VERTICAL_CLOSE, SpringLayout.SOUTH, lblType);	 	    
	    layout.putConstraint(SpringLayout.NORTH, lblStatus, VERTICAL_PADDING, SpringLayout.SOUTH, comboBoxType);
	    layout.putConstraint(SpringLayout.NORTH, comboBoxStatus, VERTICAL_PADDING+VERTICAL_CLOSE, SpringLayout.SOUTH, lblStatus);	    
	    layout.putConstraint(SpringLayout.NORTH, lblPriority, VERTICAL_PADDING, SpringLayout.SOUTH, comboBoxStatus);	
	    layout.putConstraint(SpringLayout.NORTH, comboBoxPriority, VERTICAL_PADDING+VERTICAL_CLOSE, SpringLayout.SOUTH, lblPriority); 
	    layout.putConstraint(SpringLayout.NORTH, notes, VERTICAL_PADDING+VERTICAL_FAR, SpringLayout.SOUTH, comboBoxPriority);	    
	    layout.putConstraint(SpringLayout.NORTH, makeNotePanel, VERTICAL_PADDING, SpringLayout.SOUTH, notes);
	    layout.putConstraint(SpringLayout.NORTH, btnSave, VERTICAL_PADDING, SpringLayout.SOUTH, makeNotePanel);	  
	}
	
	public DefaultListModel getNoteList() {
		return noteList;
	}
}
