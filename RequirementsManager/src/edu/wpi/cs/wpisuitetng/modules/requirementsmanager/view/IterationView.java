package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JCalendar;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;

public class IterationView extends FocusableTab {

	
	//temp sub class
	public static class Iteration {	
	}
	
	private enum Status {
		CREATE,
		EDIT
	}
	
	/** The iteration object this view will be displaying / creating */
	private Iteration iteration;
	
	/** The status enum, whether editing or creating */
	private Status status;
	
	/** Swing components */
	private JLabel labName;
	private JLabel labStartDate;
	private JLabel labEndDate;
	
	/** Buttons for saving and canceling iteration */
	
	private JButton butSave;
	private JButton butCancel;
	
	/** Textfield for entering the name of the iteration */
	private JTextField txtName;
	
	/** The JCalendars for selecting dates */
	private JCalendar calStartDate;
	private JCalendar calEndDate;
	

	
	public IterationView(Iteration iteration) {
		this(iteration, Status.EDIT);
	}
	
	public IterationView() {
		this(new Iteration(), Status.CREATE);		
	}
	
	private IterationView(Iteration iteration, Status status) {
		this.iteration = iteration;
		this.status = status;
		
		// initlaize JComponents
		
		labName = new JLabel("Name:");
		labStartDate = new JLabel("Starting Date:");
		labEndDate = new JLabel("Ending Date:");
		
		butSave = new JButton("Save");
		butCancel = new JButton("Cancel");
		
		txtName = new JTextField();
		
		calStartDate = new JCalendar();
		calEndDate = new JCalendar();
		
		SpringLayout layout = new SpringLayout();
		
		
		//setLayout(layout);
		
		//add all the components
		/*
		add(labName);
		add(labStartDate);
		add(labEndDate);
		*/
		
		add(calStartDate);
		add(calEndDate);
	}

	
	
}
