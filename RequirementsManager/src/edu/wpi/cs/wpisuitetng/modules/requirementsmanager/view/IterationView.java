package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.toedter.calendar.JCalendar;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.FocusableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;

/** View for creating or editing a iteration
 * 
 * @author Mitchell, Matt
 *
 */

public class IterationView extends FocusableTab implements ActionListener {
	
	/** Status enum, whether created or edited */
	private enum Status {
		CREATE,
		EDIT
	}
	
	/** Controller for adding an iteration */
	private AddIterationController addIterationController;
	
	/** The maintab controller */
	private MainTabController mainTabController;
	
	/** The iteration object this view will be displaying / creating */
	private Iteration iteration;
	
	/** The status enum, whether editing or creating */
	private Status status;
	
	/** Swing components */
	private JLabel labName;
	private JLabel labStartDate;
	private JLabel labEndDate;
	
	/** Error message components */
	private JLabel labErrorMessage;
	private JLabel labNameError;
	private JLabel labCalendarError;
	
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
	
	private boolean saveEnabled;
	
	public IterationView(Iteration iteration, MainTabController mainTabController) {
		this(iteration, Status.EDIT, mainTabController);
	}
	
	public IterationView(MainTabController mainTabController) {
		this(new Iteration(), Status.CREATE, mainTabController);		
	}
	
	private IterationView(Iteration iteration, Status status, MainTabController mainTabController) {
		this.iteration = iteration;
		this.status = status;
		this.mainTabController = mainTabController;
		
		this.saveEnabled = false;
		
		//initilize the add iteration controller
		addIterationController = new AddIterationController(this);
		// initlaize JComponents
		
		labName = new JLabel("Name:");
		labStartDate = new JLabel("Starting Date:");
		labEndDate = new JLabel("Ending Date:");
		labErrorMessage = new JLabel(" ");
		
		labNameError = new JLabel(" ");
		labCalendarError = new JLabel(" ");
		
		labNameError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		labCalendarError.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		
		butSave = new JButton();
		butSave.setAction(new SaveAction());
		butSave.setEnabled(saveEnabled);
		
		if (status == Status.CREATE) {
			butSave.setText("Create");
		}
		else {
			butSave.setText("Save");
		}	
		
		
		butCancel = new JButton("Cancel");
		butCancel.setAction(new CancelAction());
		
		txtName = new JTextField();
		
		calStartDate = new JCalendar();
		calEndDate = new JCalendar();
		
		// populate fields, if editing
		if (status == Status.EDIT) {
			txtName.setText(iteration.getName());
			calStartDate.setDate(iteration.getStartDate());
			calEndDate.setDate(iteration.getEndDate());			
		}
		
		SpringLayout layout = new SpringLayout();
		
		
		layout.putConstraint(SpringLayout.WEST, labName, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labName, VERTICAL_PADDING, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, labNameError, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labNameError, VERTICAL_PADDING, SpringLayout.SOUTH, labName);
		
		layout.putConstraint(SpringLayout.WEST, txtName, HORIZONTAL_PADDING, SpringLayout.EAST, labName);
		layout.putConstraint(SpringLayout.NORTH, txtName, VERTICAL_PADDING, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, labStartDate, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labStartDate, VERTICAL_PADDING, SpringLayout.SOUTH, labNameError);
		
	
		layout.putConstraint(SpringLayout.NORTH, labEndDate, 0, SpringLayout.NORTH, labStartDate); // allign them, no padding
		
		
		layout.putConstraint(SpringLayout.NORTH, calStartDate, VERTICAL_PADDING, SpringLayout.SOUTH, labStartDate);
		layout.putConstraint(SpringLayout.WEST, calStartDate, 0, SpringLayout.WEST, labStartDate);
		
		layout.putConstraint(SpringLayout.NORTH, calEndDate, VERTICAL_PADDING, SpringLayout.SOUTH, labEndDate);
		layout.putConstraint(SpringLayout.WEST, calEndDate, HORIZONTAL_PADDING, SpringLayout.EAST, calStartDate);
		
		layout.putConstraint(SpringLayout.WEST, labCalendarError, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, labCalendarError, VERTICAL_PADDING, SpringLayout.SOUTH, calStartDate);
		
		layout.putConstraint(SpringLayout.WEST, labEndDate, 0, SpringLayout.WEST, calEndDate);
		
		layout.putConstraint(SpringLayout.EAST, txtName, 0, SpringLayout.EAST, calEndDate);
		

		layout.putConstraint(SpringLayout.WEST, butSave, HORIZONTAL_PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, butSave, VERTICAL_PADDING, SpringLayout.SOUTH, labCalendarError);
		
		
		layout.putConstraint(SpringLayout.WEST, butCancel, HORIZONTAL_PADDING, SpringLayout.EAST, butSave);
		layout.putConstraint(SpringLayout.NORTH, butCancel, 0, SpringLayout.NORTH, butSave);
		
		layout.putConstraint(SpringLayout.WEST, labErrorMessage, HORIZONTAL_PADDING, SpringLayout.EAST, butCancel);
		layout.putConstraint(SpringLayout.NORTH, labErrorMessage, 0, SpringLayout.NORTH, butCancel);
		
		
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
		
		add(labErrorMessage);
		add(labNameError);
		add(labCalendarError);
		

	}

	/** Action to save the given Iteration
	 * 
	 * @author Mitchell, Matt
	 *
	 */
	
	private class SaveAction extends AbstractAction {
		
		public SaveAction() {
			
		}
		
		public void actionPerformed(ActionEvent e) {
			//pull the values from the fields
			String name = txtName.getText();
			System.out.println("NAMEEE?!!!!!!!! |" + name + "|");
			Date startDate = calStartDate.getDate();
			Date endDate = calEndDate.getDate();
			
			if (status == Status.CREATE) {
				Iteration toAdd = new Iteration(name, startDate, endDate);
				addIterationController.addIteration(toAdd);
			}
			
		}
		
	}
	
	/** Class for the cancel action of the cancel button
	 *  Closes the current tab
	 * 
	 * @author Mitchell, Matt
	 *
	 */
	
	private class CancelAction extends AbstractAction {
		
		public CancelAction() {
			super("Cancel");
		}
		
		
		public void actionPerformed(ActionEvent e) {
			mainTabController.closeCurrentTab();			
		}		
	}
	
	
	public MainTabController getMainTabController() {
		return mainTabController;
	}
	
	/** Displays an error message when there is an issue with saving
	 *  
	 * @param error The error to display
	 */
	
	public void displaySaveError(String error) {
		//check for error
		if (txtName.getText().trim().isEmpty()) {
			labNameError.setText("**Name connot be blank**");
		}
		else {
			labNameError.setText(" ");
		}
		
		if (calStartDate.getDate().after(calEndDate.getDate())) {
			labCalendarError.setText("**Start date must be before End date**");
		}
		else {
			labCalendarError.setText(" ");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent)e.getSource();
		
		//if the save was disabled, enable it
		if (!saveEnabled) {
			saveEnabled = true;
		}
		
		if (source.equals(txtName)) {
			
		}
		else if (source.equals(calEndDate)) {
			
		}
		else {
			
		}
	}
	
	
}
