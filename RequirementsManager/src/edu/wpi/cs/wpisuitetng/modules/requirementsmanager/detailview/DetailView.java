/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview;

import java.awt.*;
import javax.swing.*;
/**
 * @author Alex Woodyard
 *
 */
public class DetailView extends JPanel{

	private JPanel top, bottom, left, right;
	private JButton ok;
	private JTextField name;
	private JLabel nameLabel;
	
	
	
	public DetailView(DetailView detailView){
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
		
	}
}
