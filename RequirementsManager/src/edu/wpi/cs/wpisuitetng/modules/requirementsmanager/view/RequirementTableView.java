/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Steve Kordell, Mitchell Caisse
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveRequirementByIDController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.UnclosableTabComponent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.EnableEditingAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.RefreshAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.SaveEditingTableAction;

/**
 * RequirementListView is the basic GUI that will display a list of the current
 * Requirements, fetched from the database. It will allow the user to select a
 * given requirement and view/edit it. It also allows users to create new
 * requirements, and refresh the list of requirements
 * 
 * @author Steve, Mitchell
 * 
 */
@SuppressWarnings("serial")
public class RequirementTableView extends Tab implements IToolbarGroupProvider,
		IDatabaseListener, IReceivedAllRequirementNotifier,
		IRetreivedAllIterationsNotifier {

	private static RequirementTableView tv;

	/** The MainTabController that this view is inside of */
	private final MainTabController tabController;

	/** Controller for receiving all requirements from the server */
	private RetrieveAllRequirementsController retreiveAllRequirementsController;

	private RetrieveAllIterationsController retreiveAllIterationsController;

	/** The list of requirements that the view is displaying */
	private Requirement[] requirements;

	/**
	 * The View that will display on the toolbar and contain buttons relating to
	 * this view
	 */
	private ToolbarGroupView toolbarView;

	/** The View and Refresh buttons used on the toolbar */
	private JButton butView;
	private JButton butRefresh;

	/** Flag used to make paint only refresh the requirements once, on load */
	private boolean firstPaint;

	@SuppressWarnings("rawtypes")
	private Vector<Vector> rowData;

	private RequirementsTable table;

	private JButton btnEdit;
	private JButton btnSave;

	private JTextArea textEditInformation;

	private boolean isEditable;
	
	private TableRowSorter<TableModel> sorter;
	
	// TODO: testing only. delete later
	JTextArea FilterDemo;
	JButton ClearFilter;
	JTextArea textFilterInfo;
	
	
	/**
	 * Constructor for a RequirementTableView
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private RequirementTableView(MainTabController tabController) {
		this.tabController = tabController;

		firstPaint = false;
		// register this listener to the Database
		RequirementDatabase.getInstance().registerListener(this);
		retreiveAllIterationsController = new RetrieveAllIterationsController(
				this);
		retreiveAllRequirementsController = new RetrieveAllRequirementsController(
				this);
		// init the toolbar group
		initializeToolbarGroup();

		requirements = new Requirement[0];

		setLayout(new BorderLayout(0, 0));

		Vector<String> columnNames = new Vector<String>();
		columnNames.addElement("ID");
		columnNames.addElement("Name");
		columnNames.addElement("Type");
		columnNames.addElement("Priority");
		columnNames.addElement("Status");
		columnNames.addElement("Iteration");
		columnNames.addElement("Estimate");
		columnNames.addElement("Effort");
		columnNames.addElement("Release Number");

		this.rowData = new Vector<Vector>();

		isEditable = false;

		this.table = new RequirementsTable(rowData, columnNames, this);

		btnEdit = new JButton("Enable Editing");
		btnSave = new JButton("Save Changes");

		textEditInformation = new JTextArea(1, 25);
		textEditInformation.setOpaque(false);
		textEditInformation.setEnabled(false);
		textEditInformation.setDisabledTextColor(Color.BLACK);
		textEditInformation.setLineWrap(true);
		textEditInformation.setWrapStyleWord(true);

		
		// TODO: for testing only. Delete later
		FilterDemo = new JTextArea(1, 15);
		FilterDemo.setOpaque(true);
		FilterDemo.setEnabled(true);
		FilterDemo.setBorder((new JTextField()).getBorder());
		FilterDemo.setDisabledTextColor(Color.BLACK);
		FilterDemo.setLineWrap(true);
		FilterDemo.setWrapStyleWord(true);
		FilterDemo.getDocument().addDocumentListener( new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}
			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}
			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}
		});
		textFilterInfo = new JTextArea(1,20);
		textFilterInfo.setOpaque(false);
		textFilterInfo.setEnabled(false);
		textFilterInfo.setDisabledTextColor(Color.BLACK);
		textFilterInfo.setLineWrap(true);
		textFilterInfo.setWrapStyleWord(true);
		ClearFilter = new JButton("Clear Filter");
		
		
	
		
		
		
		
		// TODO: dynamically change this
		btnSave.setEnabled(false);
		// TODO: Set this button's action

		SpringLayout editPanelLayout = new SpringLayout();
		JPanel editPanel = new JPanel(editPanelLayout);
		editPanel.add(btnEdit);
		editPanel.add(btnSave);
		editPanel.add(FilterDemo);
		editPanel.add(textEditInformation);
		editPanel.add(ClearFilter);
		editPanel.add(textFilterInfo);
		
		editPanel.setPreferredSize(new Dimension(
				btnEdit.getPreferredSize().width,
				btnEdit.getPreferredSize().height
						+ (btnEdit.getPreferredSize().height / 2)
						+ textEditInformation
								.getPreferredScrollableViewportSize().height
						+ 10));

		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnEdit, 0,
				SpringLayout.VERTICAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnSave, 0,
				SpringLayout.VERTICAL_CENTER, editPanel);

		editPanelLayout.putConstraint(SpringLayout.EAST, btnEdit, -5,
				SpringLayout.HORIZONTAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.WEST, btnSave, 5,
				SpringLayout.HORIZONTAL_CENTER, editPanel);

		editPanelLayout.putConstraint(SpringLayout.WEST, textEditInformation,
				0, SpringLayout.WEST, btnEdit);
		editPanelLayout.putConstraint(SpringLayout.NORTH, textEditInformation,
				0, SpringLayout.SOUTH, btnEdit);

		
		
		// TODO: testing only. delete later
		editPanelLayout.putConstraint(SpringLayout.WEST, ClearFilter, 5, SpringLayout.WEST, editPanel);
		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, ClearFilter, 0,
				SpringLayout.VERTICAL_CENTER, editPanel);
		editPanelLayout.putConstraint(SpringLayout.WEST, textFilterInfo, 5, SpringLayout.WEST, editPanel);
		editPanelLayout.putConstraint(SpringLayout.NORTH, textFilterInfo, 0,
				SpringLayout.SOUTH, ClearFilter);
		editPanelLayout.putConstraint(SpringLayout.WEST, FilterDemo, 5, SpringLayout.EAST, ClearFilter);
		editPanelLayout.putConstraint(SpringLayout.VERTICAL_CENTER, FilterDemo, 0,
				SpringLayout.VERTICAL_CENTER, editPanel);
		
		
		
		
		
		JScrollPane scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.getColumnModel().removeColumn(
				this.table.getColumnModel().getColumn(0));

		Comparator<String> PriorityComparator = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				if (s1.trim().equals("")) {
					s1 = "BLANK";
				}
				if (s2.trim().equals("")) {
					s2 = "BLANK";
				}
				String upper1 = s1.toUpperCase();
				String upper2 = s2.toUpperCase();
				Priority p1 = Priority.valueOf(upper1);
				Priority p2 = Priority.valueOf(upper2);
				return p1.compareTo(p2);
			}
		};

		Comparator<String> IterationStringComparator = new Comparator<String>() {
			public int compare(String s1, String s2) {
				IterationDatabase Idb = IterationDatabase.getInstance();
				Iteration Iteration1 = Idb.getIteration(s1);
				Iteration Iteration2 = Idb.getIteration(s2);

				if (Iteration1.getStartDate().before(Iteration2.getStartDate())) {
					return -1; // first argument is less, or before second
				} else if (Iteration1.getStartDate().after(
						Iteration2.getStartDate())) {
					return 1; // first iteration is more, or after second
				}
				return 0; // dates are equal
			}
		};
		
		Comparator<String> numberComparator = new Comparator<String>() {
			public int compare(String s1, String s2) {
				int Estimate1 = Integer.parseInt(s1);
				int Estimate2 = Integer.parseInt(s2);
				
				if (Estimate1 < Estimate2) {
					return -1;
				}
				else if (Estimate1 > Estimate2) {
					return 1;
				}
				else {
					return 0;
				}
			}
		};

		sorter = new TableRowSorter<TableModel>(
				table.getModel());
		/*
		 * for (int i = 0; i < this.table.getColumnCount(); i++) { if
		 * (this.table.getColumnName(i).equals("Priority")) {
		 * sorter.setComparator(i, comparator); } }
		 */
		// TODO: find a better way to get the the appropriate columns (for loop
		// was failing for me for no reason)
		sorter.setComparator(3, PriorityComparator);
		sorter.setComparator(5, IterationStringComparator);
		sorter.setComparator(6, numberComparator);
		sorter.setComparator(7, numberComparator);
		table.setRowSorter(sorter);

		// TODO: MOVE
		btnSave.setAction(new SaveEditingTableAction(this, sorter));
		btnEdit.setAction(new EnableEditingAction(this, sorter));

		
		
		
		// TODO: temporary. remove later
		AbstractAction ClearFilterAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sorter.setRowFilter(null);
				textFilterInfo.setText("");
			}
		};
		ClearFilter.setAction(ClearFilterAction);
		ClearFilter.setText("Clear Filter");
		
		
		
		
		
		
		
		
		// Add to this list of the column does not need equal size
		String shortCols = "Estimate|Effort";
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			if (this.table.getColumnName(i).matches(shortCols)) {
				this.table.getColumnModel().getColumn(i).setPreferredWidth(12);
			}
		}

		add(scrollPane, BorderLayout.CENTER);
		add(editPanel, BorderLayout.SOUTH);

		// Add double click event listener
		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !isEditable) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					onDoubleClick(row);
				}
			}
		});

		this.table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER && !isEditable) {
					viewRequirement();
				}
				event.consume();
			}
		});

	}

	public static RequirementTableView getInstance(
			MainTabController tabController) {
		if (tv == null) {
			tv = new RequirementTableView(tabController);
		}
		return tv;
	}

	public static RequirementTableView getInstance() {
		return tv;
	}

	/*
	 * @author Steve Kordell
	 */
	// TODO: More documentation
	void onDoubleClick(int index) {
		// update to use this function instead
		viewRequirement(index);
	}

	/**
	 * Initializes the toolbar group, and adds the buttons that will be
	 * displayed to it.
	 * 
	 * 
	 */
	private void initializeToolbarGroup() {

		JPanel content = new JPanel();
		SpringLayout layout = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);

		butView = new JButton("Edit Requirement");
		butRefresh = new JButton("Refresh");

		butRefresh.setAction(new RefreshAction(this));
		// butView.setAction(new ViewRequirementAction(this));

		layout.putConstraint(SpringLayout.NORTH, butRefresh, 5,
				SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, butRefresh, 16,
				SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.EAST, butRefresh, -16,
				SpringLayout.EAST, content);

		layout.putConstraint(SpringLayout.NORTH, butView, 5,
				SpringLayout.SOUTH, butRefresh);
		layout.putConstraint(SpringLayout.WEST, butView, 16, SpringLayout.WEST,
				content);
		layout.putConstraint(SpringLayout.EAST, butView, 0, SpringLayout.EAST,
				butRefresh);

		// create and add the buttons that will be displayed
		// content.add(butView);
		content.add(butRefresh);

		toolbarView = new ToolbarGroupView("Refresh", content);
		// set the width of the group so it is not too long
		toolbarView.setPreferredWidth((int) (butView.getPreferredSize()
				.getWidth() + 40));

	}

	/**
	 * Takes a list of requirements, and turns them into a list of Strings that
	 * the View will display
	 * 
	 * @param requirements
	 *            The list of requirements
	 * @return a list of requirements in string form, that can be shown on the
	 *         JList
	 */

	private void parseRequirements() {

		this.rowData.clear();
		for (int i = 0; i < requirements.length; i++) {
			Vector<String> row = new Vector<String>();
			row.addElement(String.valueOf(requirements[i].getrUID()));
			row.addElement(requirements[i].getName());
			row.addElement(requirements[i].getType().equals(Type.BLANK) ? ""
					: requirements[i]
							.getType()
							.toString()
							.substring(0, 1)
							.concat(requirements[i].getType().toString()
									.substring(1).toLowerCase())
							.replaceAll(" s", " S").replaceAll(" f", " F"));
			row.addElement(requirements[i].getPriority().equals(Priority.BLANK) ? ""
					: requirements[i]
							.getPriority()
							.toString()
							.substring(0, 1)
							.concat(requirements[i].getPriority().toString()
									.substring(1).toLowerCase()));
			row.addElement(requirements[i].getStatus().equals(Status.BLANK) ? ""
					: requirements[i]
							.getStatus()
							.toString()
							.substring(0, 1)
							.concat(requirements[i].getStatus().toString()
									.substring(1).toLowerCase())
							.replaceAll(" p", " P"));
			try {
				row.addElement(IterationDatabase.getInstance()
						.getIteration(requirements[i].getIteration()).getName());
			} catch (IterationNotFoundException e) {
				row.addElement("Iteration Not Found");
			}
			row.addElement(String.valueOf(requirements[i].getEstimate()));
			row.addElement(String.valueOf(requirements[i].getEffort()));
			row.addElement((requirements[i].getReleaseNum()));
			this.rowData.add(row);
		}
	}

	/**
	 * Function to retreive the requirements from the server
	 * 
	 * Mostly a place holder function until the backend is implemented The list
	 * view will automaticaly be updated once the request comes back
	 * 
	 */

	private void getRequirementsFromServer() {
		retreiveAllRequirementsController.getAll();
	}

	private void getIterationsFromServer() {
		retreiveAllIterationsController.getAll();
		;
	}

	/**
	 * Updates the list view according to the values in the Requirements Array
	 * 
	 * @throws RequirementNotFoundException
	 * 
	 * */

	private void updateListView() {

		parseRequirements();

		this.table.repaint();
	}

	/**
	 * Inherited from IToolBarGroup Provider, Provides the toolbar buttons used
	 * by this view
	 * 
	 */

	@Override
	public ToolbarGroupView getGroup() {
		return toolbarView;
	}

	/**
	 * Retreives the list of requirements from the server, then updates the List
	 * View acordingly
	 * 
	 */

	public void refresh() {
		// retreive a new copy of requirements, and update the list view
		System.out.println("We are refreshing the table view");

		if (isEditable) {
			Object[] options = { "Save Changes", "Discard Changes", "Cancel" };
			int res = JOptionPane
					.showOptionDialog(
							this,
							"There are unsaved changes, You need to discart them before you can refresh",
							"Requirements: Confirm Refresh",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);

			if (res == 0) {
				btnSave.getAction().actionPerformed(null);
			} else if (res == 1) {
				btnEdit.getAction().actionPerformed(null);
			} else {

			}
		} else {
			getRequirementsFromServer();
			getIterationsFromServer();
		}

		tabController.refreshIterationTree();
		changeButtonStatus();
	}

	/**
	 * Open a new tab containing a view of the selected requirement in the list
	 * view TODO: Possibly implement view requirement mode
	 */

	public void viewRequirement() {
		for (int i : this.table.getSelectedRows()) {
			viewRequirement(i);
			// TODO: Slight problem: when opening multiple requirements at the
			// same time,because of the server respond speed, the iterations
			// won't open the tabs necessarily in the order they appear in the
			// table
		}
	}

	/**
	 * Views a requirement with the given index
	 * 
	 * @param index
	 *            Index of the requirement to view
	 */

	public void viewRequirement(int index) {
		boolean requirementIsOpen = false;

		if (index < 0 || index >= requirements.length) {
			// invalid index
			System.out.println("Invalid index " + index);
		}

		Requirement requirementToFetch = null;

		// convert index to new view index (incase of sorting)
		int newIndex = this.table.convertRowIndexToModel(index);
		// get the rUID of the requirement in the hidden column
		String reqId = (String) this.table.getModel().getValueAt(newIndex, 0);

		// iterate through the requirements
		for (int i = 0; i < requirements.length; i++) {
			// if the rUIDs match
			if (reqId.equals(Integer.toString(requirements[i].getrUID()))) {
				// get this requirement
				requirementToFetch = requirements[i];
				// stop loop
				break;
			}
		}

		// Check to make sure the requirement is not already being
		// displayed. This is assuming that the list view is displayed in
		// the left most tab, index 0
		for (int i = 0; i < this.tabController.getTabView().getTabCount(); i++) {
			if (this.tabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				if (((((DetailPanel) this.tabController.getTabView()
						.getComponentAt(i))).getModel().getrUID()) == (requirementToFetch
						.getrUID())) {
					this.tabController.switchToTab(i);
					requirementIsOpen = true;
				}
			}
		}

		if (!requirementIsOpen) {
			// create the controller for fetching the new requirement
			RetrieveRequirementByIDController retreiveRequirementController = new RetrieveRequirementByIDController(
					new OpenRequirementTabAction(tabController,
							requirementToFetch));

			// get the requirement from the server
			retreiveRequirementController.get(requirementToFetch.getrUID());
		}
	}

	/**
	 * Method that updates the content of the list view when this tab gains
	 * focus
	 * 
	 */

	@Override
	public void onGainedFocus() {
		refresh();
	}

	/**
	 * Retrieve the requirements from the server when the GUI is first painted
	 * 
	 * @param g
	 *            The Graphics object
	 * 
	 */

	@Override
	public void paint(Graphics g) {
		// call super so there is no change to functionality
		super.paint(g);
		// refresh the requirements, the first time this is called
		if (!firstPaint) {
			refresh();
			firstPaint = true;
		}
	}

	@Override
	public void update() {
		// this.requirements =
		// RequirementDatabase.getInstance().getAllRequirements().toArray(requirements);
		// updateListView();
	}

	@Override
	public boolean shouldRemove() {
		// this listener should persist
		return false;
	}

	@Override
	public void receivedData(Requirement[] requirements) {
		this.requirements = requirements;
		updateListView();

	}

	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {

	}

	@Override
	public void receivedData(Iteration[] iterations) {
		updateListView();
	}

	@Override
	public Component getTabComponent(MainTabController tabController) {
		return new UnclosableTabComponent(tabController, "Requirements");
	}

	/**
	 * @return the table
	 */
	public RequirementsTable getTable() {
		return table;
	}

	// writes to hidden panel to inform the user of editing, etc..
	public void displayEditInformation(String text) {
		this.textEditInformation.setText(text);
	}

	// sets the buttons enabled/disabled depending on the isEditable state
	public void changeButtonStatus() {
		if (isEditable) {
			btnEdit.setText("Discard Changes");
			btnSave.setEnabled(true);
		} else {
			btnEdit.setText("Enable Editing");
			btnSave.setEnabled(false);
		}
	}

	/**
	 * @return the isEditable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * @param editable
	 *            the isEditable to set
	 */
	public void setEditable(boolean editable) {
		this.isEditable = editable;
	}
		

	public boolean onLostFocus() {		
		if (isEditable) {
			Object[] options = { "Save Changes", "Discard Changes", "Cancel" };
			int res = JOptionPane
					.showOptionDialog(
							this,
							"There are unsaved changes, either discard them, or save them before continuing",
							"Requirements: Confirm Changes",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[2]);

			if (res == 0) {
				btnSave.getAction().actionPerformed(null);
			} else if (res == 1) {
				btnEdit.getAction().actionPerformed(null);
			} else {
				return false;
			}
				
		}
		return true;

	}
	
	private void newFilter() {
        RowFilter rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(FilterDemo.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
	
	public void IterationFilter(String IterationName) {
        RowFilter rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(IterationName);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }
	
	
	
	
	// writes to hidden panel to inform the user of editing, etc..
	public void displayFilterInformation(String text) {
		this.textFilterInfo.setText(text);
	}

}
