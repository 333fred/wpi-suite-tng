package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JCalendar;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;

public class IterationView extends FocusableTab {
	
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
	
	/**Padding constants */
	
	private final int VERTICAL_PADDING = 10;
	private final int HORIZONTAL_PADDING = 20;
	
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
		
		
		if (status == Status.CREATE) {
			butSave = new JButton("Create");
		}
		else {
			butSave = new JButton("Save");
		}
		
		
		butCancel = new JButton("Cancel");
		
		txtName = new JTextField();
		
		calStartDate = new JCalendar();
		calEndDate = new JCalendar();
		
		SpringLayout layout = new SpringLayout();
		
		
		layout.putConstraint(SpringLayout.WEST, labName, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labName, VERTICAL_PADDING, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, txtName, HORIZONTAL_PADDING, SpringLayout.EAST, labName);
		layout.putConstraint(SpringLayout.NORTH, txtName, VERTICAL_PADDING, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, labStartDate, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labStartDate, VERTICAL_PADDING, SpringLayout.SOUTH, labName);
		
	
		layout.putConstraint(SpringLayout.NORTH, labEndDate, 0, SpringLayout.NORTH, labStartDate); // allign them, no padding
		
		
		layout.putConstraint(SpringLayout.NORTH, calStartDate, VERTICAL_PADDING, SpringLayout.SOUTH, labStartDate);
		layout.putConstraint(SpringLayout.WEST, calStartDate, 0, SpringLayout.WEST, labStartDate);
		
		layout.putConstraint(SpringLayout.NORTH, calEndDate, VERTICAL_PADDING, SpringLayout.SOUTH, labEndDate);
		layout.putConstraint(SpringLayout.WEST, calEndDate, HORIZONTAL_PADDING, SpringLayout.EAST, calStartDate);
		
		layout.putConstraint(SpringLayout.WEST, labEndDate, 0, SpringLayout.WEST, calEndDate);
		
		layout.putConstraint(SpringLayout.EAST, txtName, 0, SpringLayout.EAST, calEndDate);
		

		layout.putConstraint(SpringLayout.WEST, butSave, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, butSave, VERTICAL_PADDING, SpringLayout.SOUTH, calEndDate);
		
		layout.putConstraint(SpringLayout.WEST, butCancel, HORIZONTAL_PADDING, SpringLayout.EAST, butSave);
		layout.putConstraint(SpringLayout.NORTH, butCancel, 0, SpringLayout.NORTH, butSave);
		
		setLayout(layout);
		
		//add all the components
		
		add(labName);
		add(txtName);
		
		add(labStartDate);
		add(labEndDate);
		
		
		add(calStartDate);
		add(calEndDate);
		
		add(butSave);
		add(butCancel);
		
	}

	
	
}
