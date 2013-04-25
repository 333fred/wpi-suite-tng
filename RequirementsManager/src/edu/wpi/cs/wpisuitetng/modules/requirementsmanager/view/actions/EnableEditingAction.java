/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 * 	@author Alex Chen
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementTableView;

/**
 * The action for editing a requirement
 */
public class EnableEditingAction extends AbstractAction {
	
	private final RequirementTableView tableView;
	TableRowSorter<TableModel> sorter;
	
	/**
	 * Constructor for EnableEditingAction
	 * 
	 * @param tableView
	 */
	public EnableEditingAction(final RequirementTableView tableView,
			final TableRowSorter<TableModel> sorter) {
		super("Enable Editing");
		this.tableView = tableView;
		this.sorter = sorter;
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		
		// TODO Auto-generated method stub
		if (tableView.isEditable()) {
			tableView.setEditable(false);
			tableView.displayEditInformation("");
			tableView.changeButtonStatus();
			tableView.getTable().setRowSorter(sorter);
			tableView.getTable().clearUpdated();
			tableView.refresh();
		} else {
			final List<SortKey> sortKeys = new ArrayList<SortKey>(tableView
					.getTable().getRowSorter().getSortKeys());
			
			// Turn off row sorting (it breaks editing right now)
			tableView.getTable().setRowSorter(null);
			
			final Comparator<String> PriorityComparator = new Comparator<String>() {
				
				@Override
				public int compare(String s1, String s2) {
					if (s1.trim().equals("")) {
						s1 = "BLANK";
					}
					if (s2.trim().equals("")) {
						s2 = "BLANK";
					}
					final String upper1 = s1.toUpperCase();
					final String upper2 = s2.toUpperCase();
					final Priority p1 = Priority.valueOf(upper1);
					final Priority p2 = Priority.valueOf(upper2);
					return p1.compareTo(p2);
				}
			};
			
			final Comparator<String> IterationStringComparator = new Comparator<String>() {
				
				@Override
				public int compare(final String s1, final String s2) {
					final IterationDatabase Idb = IterationDatabase
							.getInstance();
					final Iteration Iteration1 = Idb.getIteration(s1);
					final Iteration Iteration2 = Idb.getIteration(s2);
					
					if (Iteration1.getStartDate().before(
							Iteration2.getStartDate())) {
						return -1; // first argument is less, or before second
					} else if (Iteration1.getStartDate().after(
							Iteration2.getStartDate())) {
						return 1; // first iteration is more, or after second
					}
					return 0; // dates are equal
				}
			};
			
			final Comparator<String> numberComparator = new Comparator<String>() {
				
				@Override
				public int compare(final String s1, final String s2) {
					final int Estimate1 = Integer.parseInt(s1);
					final int Estimate2 = Integer.parseInt(s2);
					
					if (Estimate1 < Estimate2) {
						return -1;
					} else if (Estimate1 > Estimate2) {
						return 1;
					} else {
						return 0;
					}
				}
			};
			
			final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
					tableView.getTable().getModel());
			/*
			 * for (int i = 0; i < this.table.getColumnCount(); i++) { if
			 * (this.table.getColumnName(i).equals("Priority")) {
			 * sorter.setComparator(i, comparator); } }
			 */
			// TODO: find a better way to get the the appropriate columns (for
			// loop
			// was failing for me for no reason)
			sorter.setComparator(3, PriorityComparator);
			sorter.setComparator(5, IterationStringComparator);
			sorter.setComparator(6, numberComparator);
			sorter.setComparator(7, numberComparator);
			tableView.getTable().setRowSorter(sorter);
			tableView.getTable().getRowSorter().setSortKeys(sortKeys);
			
			// set isEditable to true
			tableView.setEditable(true);
			tableView.changeButtonStatus();
			tableView.displayEditInformation("Editing Enabled");
		}
	}
	
}
