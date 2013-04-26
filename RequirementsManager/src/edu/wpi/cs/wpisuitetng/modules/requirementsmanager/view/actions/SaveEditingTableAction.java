/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Alex Gorowara
 ********************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.IterationNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.UpdateRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.ISaveNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * 
 * 
 */
@SuppressWarnings ("serial")
public class SaveEditingTableAction extends AbstractAction implements
		ISaveNotifier {
	
	private final RequirementTableView tableView;
	TableRowSorter<TableModel> sorter;
	
	/**
	 * Creates a new save editing action with the given view and row sorter
	 * 
	 * @param tableView
	 *            the view for this action
	 * @param sorter
	 *            the row sorter
	 */
	public SaveEditingTableAction(final RequirementTableView tableView,
			final TableRowSorter<TableModel> sorter) {
		super("Save Changes");
		this.tableView = tableView;
		this.sorter = sorter;
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		final RequirementsController saveController = new RequirementsController();
		final IterationController iterationController = new IterationController();
		final RequirementDatabase rdb = RequirementDatabase.getInstance();
		
		final boolean[] changedRows = tableView.getTable().getEditedRows();
		
		// if the user is still currently editing a cell, and they try to save
		if (tableView.getTable().getCellEditor() != null) {
			// stop editing first before saving
			tableView.getTable().getCellEditor().stopCellEditing();
		}
		
		for (int i = 0; i < changedRows.length; i++) {
			if (changedRows[i]) {
				final int id = Integer.parseInt((String) tableView.getTable()
						.getModel().getValueAt(i, 0));
				final String newName = (String) tableView.getTable()
						.getModel().getValueAt(i, 1);
				
				String iterationName = (String)tableView.getTable().getModel().getValueAt(i, 5);				
				Iteration newIteration = IterationDatabase.getInstance().getIteration(
						iterationName);
				
				final int newEstimate = Integer.parseInt((String) tableView
						.getTable().getModel().getValueAt(i, 6));
				final int newEffort = Integer.parseInt((String) tableView
						.getTable().getModel().getValueAt(i, 7));
				final String newRelease = (String) tableView.getTable()
						.getModel().getValueAt(i, 8);	
				try {
					Requirement reqToChange = rdb.get(id);
					UpdateRequirementRequestObserver observer = new UpdateRequirementRequestObserver(
							this);
					Requirement parentReq = null;
					Iteration anIteration = null;
					
					reqToChange.setEstimate(newEstimate);
					reqToChange.setName(newName);
					reqToChange.setEffort(newEffort);
					reqToChange.setReleaseNum(newRelease);
					

//					try {
//						anIteration = IterationDatabase.getInstance().get(
//								reqToChange.getIteration());
//						anIteration.removeRequirement(reqToChange.getrUID());
//						final UpdateIterationRequestObserver observer2 = new UpdateIterationRequestObserver(
//								this);
//						iterationController.save(anIteration, observer2);
//					} catch (final IterationNotFoundException f) {
//						f.printStackTrace();
//					}
//					
//					newIteration.addRequirement(reqToChange.getrUID());
//					final UpdateIterationRequestObserver observer2 = new UpdateIterationRequestObserver(
//							this);
//					iterationController.save(newIteration, observer2);
//					
//					reqToChange.setIteration(newIteration.getId());
					
					
					try {
						reqToChange.setType(Type.valueOf(((String)tableView.getTable().getModel().getValueAt(i, 2)).toUpperCase().replaceAll(" ", "_").replaceAll("-", "_")));
					} catch (final IllegalArgumentException except) {
						//We use "" instead of "None"
						reqToChange.setType(Type.BLANK);
					}
					
					try {
						reqToChange.setPriority(Priority.valueOf(((String)tableView.getTable().getModel().getValueAt(i, 3)).toUpperCase().replaceAll(" ", "_")));
					} catch (final IllegalArgumentException except) {
						reqToChange.setPriority(Priority.BLANK);
					}
					
					try {
					reqToChange.setStatus(Status.valueOf(((String)tableView.getTable().getModel().getValueAt(i, 4)).toUpperCase().replaceAll(" ", "_")));
					} catch (final IllegalArgumentException except) {
						reqToChange.setStatus(Status.BLANK);
					}
					
					
					if(reqToChange.getStatus().equals(Status.DELETED)&&reqToChange.getpUID().size()>0){
						parentReq = RequirementDatabase.getInstance().get(reqToChange.getpUID().get(0));
						parentReq.removeSubRequirement(reqToChange.getrUID());
						reqToChange.removePUID(parentReq.getrUID());
						saveController.save(parentReq, observer);
					}
					
					if(reqToChange.getStatus().equals(Status.DELETED)){
						try {
							anIteration = IterationDatabase.getInstance().get(reqToChange.getIteration());
							anIteration.removeRequirement(reqToChange.getrUID());
							final UpdateIterationRequestObserver observerDelete = new UpdateIterationRequestObserver(
									this);
							iterationController.save(anIteration, observerDelete);
						} catch (IterationNotFoundException e1) {
							e1.printStackTrace();
						}
						
						anIteration = IterationDatabase.getInstance().getIteration("Deleted");						
						reqToChange.setIteration(anIteration.getId());
						anIteration.addRequirement(reqToChange.getrUID());
						final UpdateIterationRequestObserver observer3 = new UpdateIterationRequestObserver(this);
						iterationController.save(anIteration, observer3);
					}
					
					saveController.save(reqToChange, observer);
				} catch (final RequirementNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		if (tableView.isEditable()) {
			tableView.setEditable(false);
			tableView.displayEditInformation("");
			tableView.changeButtonStatus();
			tableView.getTable().clearUpdated();
		}
	}
	
	@Override
	public void fail(final Exception exception) {
		tableView.displayEditInformation("Unable to complete request: "
				+ exception.getMessage());
		tableView.refresh();
	}
	
	@Override
	public void responseError(final int statusCode, final String statusMessage) {
		tableView.displayEditInformation("Received " + statusCode
				+ " error from server: " + statusMessage);
		tableView.refresh();
	}
	
	@Override
	public void responseSuccess() {
		// Update all tabs of their total estimates and sub requirement panel
		MainTabController tabController = this.tableView.getController();
		for (int i = 0; i < tabController.getTabView().getTabCount(); i++) {
			if (tabController.getTabView().getComponentAt(i) instanceof DetailPanel) {
				final DetailPanel detailPanel = (((DetailPanel) tabController
						.getTabView().getComponentAt(i)));
				detailPanel.updateTotalEstimate();
				detailPanel.updateSubReqTab();
				
				try {
					detailPanel
							.determineAvailableStatusOptions(RequirementDatabase
									.getInstance().get(
											detailPanel.getRequirement()
													.getrUID()));
				} catch (final RequirementNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
