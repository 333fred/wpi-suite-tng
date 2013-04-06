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
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.TabFocusListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.OpenRequirementTabAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.RefreshAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions.ViewRequirementAction;

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
public class RequirementTableView extends JPanel implements TabFocusListener,
		IToolbarGroupProvider, IDatabaseListener, IReceivedAllRequirementNotifier, IRetreivedAllIterationsNotifier {
	
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

	private JTable table;

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
		retreiveAllIterationsController = new RetrieveAllIterationsController(this);
		retreiveAllRequirementsController = new RetrieveAllRequirementsController(this);
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

		this.table = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};

		JScrollPane scrollPane = new JScrollPane(this.table);
		this.table.setFillsViewportHeight(true);
		this.table.getColumnModel().removeColumn(this.table.getColumnModel().getColumn(0));
		
		// Add to this list of the column does not need equal size
		String shortCols = "Estimate|Effort";
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			if (this.table.getColumnName(i).matches(shortCols)) {
				this.table.getColumnModel().getColumn(i).setPreferredWidth(12);
			}
		}
	/*	this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int colWidth = this.table.getColumnModel().getColumn(0).getPreferredWidth();
		int numCol = this.table.getColumnCount();
		for (int i = 0; i < numCol; i++) {
			this.table.getColumnModel().getColumn(i).setMinWidth(colWidth*2);
		}*/
	//	this.table.setPreferredSize(new Dimension(1100, 200));
		this.table.setAutoCreateRowSorter(true);

		add(scrollPane);

		// Add double click event listener
		this.table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					onDoubleClick(row);
				}
			}
		});
		
		this.table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					viewRequirement();
				}
				event.consume();
			}
		});

	}

	public static RequirementTableView getInstance(MainTabController tabController) {
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
		SpringLayout layout  = new SpringLayout();
		content.setLayout(layout);
		content.setOpaque(false);		
		
		butView = new JButton("Edit Requirement");
		butRefresh = new JButton("Refresh");

		butRefresh.setAction(new RefreshAction(this));
		butView.setAction(new ViewRequirementAction(this));		
		
		layout.putConstraint(SpringLayout.NORTH, butView, 5, SpringLayout.NORTH, content);
		layout.putConstraint(SpringLayout.WEST, butView, 16, SpringLayout.WEST, content);
		
		layout.putConstraint(SpringLayout.NORTH, butRefresh, 5, SpringLayout.SOUTH, butView);
		layout.putConstraint(SpringLayout.WEST, butRefresh, 16, SpringLayout.WEST, content);
		layout.putConstraint(SpringLayout.EAST, butRefresh, 0, SpringLayout.EAST, butView);
		
		// create and add the buttons that will be displayed
		content.add(butView);
		content.add(butRefresh);
		
		toolbarView = new ToolbarGroupView("Requirements", content);
		// set the width of the group so it is not too long
		toolbarView.setPreferredWidth((int) (butView.getPreferredSize().getWidth() + 40));
		
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
			row.addElement(requirements[i].getType().equals(Type.BLANK) ? "" : requirements[i].getType().toString().substring(0,1).concat(requirements[i].getType().toString().substring(1).toLowerCase()).replaceAll("_s", " S").replaceAll("_f", " F"));
			row.addElement(requirements[i].getPriority().equals(Priority.BLANK) ? "" : requirements[i].getPriority().toString().substring(0,1).concat(requirements[i].getPriority().toString().substring(1).toLowerCase()));
			row.addElement(requirements[i].getStatus().equals(Status.BLANK) ? "" : requirements[i].getStatus().toString().substring(0,1).concat(requirements[i].getStatus().toString().substring(1).toLowerCase()).replaceAll("_p", " P"));
			try {
				row.addElement(IterationDatabase.getInstance().getIteration(requirements[i].getIteration()).getName());
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
		retreiveAllIterationsController.getAll();;
	}

	/**
	 * Updates the list view according to the values in the Requirements Array
	 * 
	 * @throws RequirementNotFoundException
	 * 
	 * */

	private void updateListView() {
		// update the string array
		parseRequirements();
		// set the list values of the requirementsList to the values in the
		// String aray
		// requirementsList.setListData(listValues.toArray(new String[0]));
		// invalidate the list so it is forced to be redrawn
		// this.table.invalidate();
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
		getRequirementsFromServer();
		getIterationsFromServer();
		tabController.refreshIterationTree();
	}

	/**
	 * Open a new tab containing a view of the selected requirement in the list
	 * view TODO: Possibly implement view requirement mode
	 */

	public void viewRequirement() {
		for (int i : this.table.getSelectedRows()) {
			viewRequirement(i);
			//TODO: Slight problem: when opening multiple requirements at the same time,because of the server respond speed, the iterations won't open the tabs necessarily in the order they appear in the table
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
	//	this.requirements = RequirementDatabase.getInstance().getAllRequirements().toArray(requirements);
	//	updateListView();
	}

	@Override
	public boolean shouldRemove() {
		//this listener should persist
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

}
