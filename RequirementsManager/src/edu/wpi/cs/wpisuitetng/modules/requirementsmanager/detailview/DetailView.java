/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.notes.NotePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
/**
 * @author Swagasaurus
 *
 */
public class DetailView extends JPanel{
	
	/** For Notes */
//	public DefaultListModel noteList;
	//protected JList notes;
	
	Requirement requirement;
	
	private static final int VERTICAL_PADDING = 10;
	private static final int VERTICAL_CLOSE = -5;
	
	public DetailView(Requirement requirement){
		this.requirement = requirement;
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		
		JLabel lblName = new JLabel("Name:");
		//lblName.setBounds(12, 50, 55, 16);
		add(lblName);
		
		JLabel lblDescription = new JLabel("Description:");
		//lblDescription.setBounds(12, 80, 77, 16);
		add(lblDescription);
		
		JLabel lblType = new JLabel("Type:");
		//lblType.setBounds(12, 250, 55, 16);
		add(lblType);
		
		JLabel lblStatus = new JLabel("Status:");
		//lblStatus.setBounds(12, 300, 55, 16);
		add(lblStatus);
		
		JLabel lblPriority = new JLabel("Priority:");
		//lblPriority.setBounds(12, 350, 55, 16);
		add(lblPriority);

		JTextArea textName = new JTextArea(1,40);
		textName.setLineWrap(false);
		textName.setBorder((new JTextField()).getBorder());
		//textField.setBounds(110, 50, 200, 20);
		//textName.setColumns(10);
		add(textName);
		
		JTextArea textDescription = new JTextArea(4,40);
		textDescription.setLineWrap(true);
		textDescription.setWrapStyleWord(true);
		textDescription.setBorder((new JTextField()).getBorder());
	//	textDescription.setBounds(110, 80, 200, 130);
		add(textDescription);
				
		JComboBox comboBoxType = new JComboBox();
		//comboBoxType.setBounds(110, 250, 100, 20);
		add(comboBoxType);
		
		JComboBox comboBoxStatus = new JComboBox();
		//comboBoxStatus.setBounds(110, 300, 100, 20);
		add(comboBoxStatus);
		
		JComboBox comboBoxPriority = new JComboBox();
		//comboBoxPriority.setBounds(110, 350, 100, 20);
		add(comboBoxPriority);
		
		JButton btnSave = new JButton("Save Requirement");
		//btnOk.setBounds(410, 296, 200, 25);
		add(btnSave);

		//create note panel
		//noteList = new DefaultListModel();
		//notes = new JList(noteList);
		NotePanel notePanel = new NotePanel(this.requirement, this); 
		//JScrollPane noteScrollPanel = new JScrollPane(notes);
		
		//add(this.notes); //This is for displaying the notes
		add(notePanel);
		
		//Align left edges of objects
	    layout.putConstraint(SpringLayout.WEST, lblName, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblDescription, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblType, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblStatus, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, lblPriority, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, textName, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, textDescription, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, comboBoxType, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, comboBoxStatus, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, comboBoxPriority, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, this);
	    layout.putConstraint(SpringLayout.WEST, notePanel, 0, SpringLayout.WEST, this);	    
	    
	    //Align Right edges of objects
	    layout.putConstraint(SpringLayout.EAST, notePanel, 450, SpringLayout.WEST, this);
	    
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
	    layout.putConstraint(SpringLayout.NORTH, notePanel, VERTICAL_PADDING+20, SpringLayout.SOUTH, comboBoxPriority);	 
	    layout.putConstraint(SpringLayout.NORTH, btnSave, VERTICAL_PADDING, SpringLayout.SOUTH, notePanel);	     
	}
}
