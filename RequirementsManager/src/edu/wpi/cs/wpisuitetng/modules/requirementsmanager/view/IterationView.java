package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IterationView extends JPanel {

	
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
	
	private JButton butSave;
	private JButton butCancel;
	
	private JTextField txtName;

	
	public IterationView(Iteration iteration) {
		this(iteration, Status.EDIT);
	}
	
	public IterationView() {
		this(new Iteration(), Status.CREATE);		
	}
	
	private IterationView(Iteration iteration, Status status) {
		this.iteration = iteration;
		this.status = status;
		
		
		
	}

	
	
}
