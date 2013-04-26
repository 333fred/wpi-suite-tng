/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Priority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Status;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.commonenums.Type;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IterationDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Task;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RowCol;

/**
 * @author Alex
 * 
 */
public class RequirementsTable extends JTable {

	private static final long serialVersionUID = 1L;
	/** list of edited rows for saving */
	private boolean[] editedRows;
	/** List of edited row/column pairs for highlighting */
	private ArrayList<RowCol> editedRowColumns;
	private final RequirementTableView view;

	/**
	 * @param rowData
	 * @param columnNames
	 */
	public RequirementsTable(final Vector rowData, final Vector columnNames,
			final RequirementTableView view) {
		super(rowData, columnNames);
		this.view = view;
	}

	public void clearUpdated() {
		editedRows = new boolean[super.getRowCount()];
		editedRowColumns = new ArrayList<RowCol>();
	};

	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		//Make sure that editedRows is initialized and of the proper size
		if (editedRows == null) {
			editedRows = new boolean[super.getRowCount()];
		} else if (editedRows.length < super.getRowCount()) {
			final boolean[] temp = new boolean[super.getRowCount()];
			editedRows = temp;
		}

		if (editedRowColumns == null)
			editedRowColumns = new ArrayList<RowCol>();

		//gray our non-changeable elements
		if(!isCellEditable(row, column) && view.isEditable()){
			final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setBackground(Color.lightGray);
			return renderer;
		}
		
		//Highlight changed elements
		for (RowCol map : editedRowColumns) {
			if (map.getRow() == convertRowIndexToModel(row) && map.getCol() == convertColumnIndexToModel(column)) {
				final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setBackground(Color.yellow);
				return renderer;
			}
		}
		return super.getCellRenderer(row, column);

	}

	public boolean[] getEditedRows() {
		return editedRows;
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		// All columns can be editable in certain circumstances
		boolean statusEditable = false;
		final String status = (String) super.getModel().getValueAt(
				convertRowIndexToModel(row), 4);
		if (super.convertColumnIndexToModel(column) == 4)
			statusEditable = true;
		else if (super.convertColumnIndexToModel(column) == 6) {
			statusEditable = !status.equals("In Progress")
					&& !status.equals("Deleted") && !status.equals("Complete");
		} else if (super.convertColumnIndexToModel(column) == 7) {
			statusEditable = status.equals("Complete");
		} else if (super.convertColumnIndexToModel(column) != 5) {
			statusEditable = !status.equals("Deleted")
					&& !status.equals("Complete");
		}

		return (view.isEditable() && statusEditable);
	}

	@Override
	/**
	 * Overrides the cell editor to allow use of combo boxes for Status, Priority, and Iteration fields
	 */
	public TableCellEditor getCellEditor(final int row, final int column) {
		if (convertColumnIndexToModel(column) == 2) {
			// final String[] items1 = { "None", "Epic", "Theme", "User Story",
			// "Non Functional", "Scenario" };
			final JComboBox comboBox1 = new JComboBox();
			for (final Type t : Type.values()) {
				if (t == Type.BLANK) {
					comboBox1.addItem("");
				} else {
					comboBox1.addItem(t.toString());
				}
			}
			final DefaultCellEditor dce1 = new DefaultCellEditor(comboBox1);
			return dce1;
		} else if (convertColumnIndexToModel(column) == 3) {
			// final String[] items1 = { "None", "Low", "Medium", "High" };
			final JComboBox comboBox1 = new JComboBox();

			for (final Priority t : Priority.values()) {
				if (t == Priority.BLANK) {
					comboBox1.addItem("");
				} else {
					comboBox1.addItem(t.toString());
				}
			}
			final DefaultCellEditor dce1 = new DefaultCellEditor(comboBox1);
			return dce1;
		} else if (convertColumnIndexToModel(column) == 4) {
			final JComboBox comboBox1 = getAvailableStatusOptions(row);
			final DefaultCellEditor dce1 = new DefaultCellEditor(comboBox1);
			return dce1;
		} else {
			return super.getCellEditor(row, column);
		}
	}

	private String[] getIterations(int row) {
		//Get iterations from the database
		List<Iteration> iterationList = IterationDatabase.getInstance()
				.getAll();

		Requirement requirement = null;

		//Now get the current requirement
		try {
			requirement = RequirementDatabase.getInstance().get(
					Integer.parseInt((String) view.getTable().getModel()
							.getValueAt(convertRowIndexToModel(row), 0)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RequirementNotFoundException e) {
			e.printStackTrace();
		}

		iterationList = Iteration.sortIterations(iterationList);
		int availableIterationNum = 0;
		int currentAvailableIterationIndex = 0;
		final Date currentDate = new Date();
		for (final Iteration iteration : iterationList) {
			// if the current date is before the end date of the iteration, or
			// the iteration is this requirement's current iteration or is the
			// backlog
			if (((currentDate.compareTo(iteration.getEndDate()) <= 0)
					|| (requirement.getIteration() == iteration.getId()) || (iteration
					.getId() == -1)) && (iteration.getId() != -2)) {
				// increment the number of available iterations
				availableIterationNum++;
			}

		}
		final String[] availableIterations = new String[availableIterationNum];
		for (final Iteration iteration : iterationList) {
			// if the current date is before the end date of the iteration,
			// or the iteration is this requirement's current iteration,
			// or it is the backlog, add it to the list
			if (((currentDate.compareTo(iteration.getEndDate()) <= 0)
					|| (requirement.getIteration() == iteration.getId()) || (iteration
					.getId() == -1)) && (iteration.getId() != -2)) {
				availableIterations[currentAvailableIterationIndex] = iteration
						.getName();
				currentAvailableIterationIndex++;
			}
		}
		return availableIterations;
	}

	public JComboBox getAvailableStatusOptions(int row) {
		JComboBox comboBoxStatus = new JComboBox();
		Requirement req = null;

		try {
			req = RequirementDatabase.getInstance().get(
					Integer.parseInt((String) view.getTable().getModel()
							.getValueAt(convertRowIndexToModel(row), 0)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RequirementNotFoundException e) {
			e.printStackTrace();
		}

		comboBoxStatus.removeAllItems();
		for (final Status t : Status.values()) {
			comboBoxStatus.addItem(t.toString());
		}
		comboBoxStatus.removeItem("None");
		boolean hasComplete = true;
		for (final Task aTask : req.getTasks()) {
			if (!aTask.isCompleted()) {
				hasComplete = false;
			}
		}
		if (!hasComplete) {
			comboBoxStatus.removeItem(Status.COMPLETE.toString());
		}

		if (req.getStatus() == Status.IN_PROGRESS) {
			// In Progress: In Progress, Complete, Deleted
			comboBoxStatus.removeItem(Status.NEW.toString());
			comboBoxStatus.removeItem(Status.OPEN.toString());
			if (!req.subReqsCompleted()) {
				comboBoxStatus.removeItem(Status.COMPLETE.toString());
			}

			if ((req.getSubRequirements().size() > 0) || !req.tasksCompleted()) {
				comboBoxStatus.removeItem(Status.DELETED.toString());
			}
		} else if ((req.getSubRequirements().size() > 0)
				|| !req.tasksCompleted()) {
			comboBoxStatus.removeItem(Status.DELETED.toString());
		}
		if (req.getStatus() == Status.NEW) {
			// New: New, Deleted
			comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
			comboBoxStatus.removeItem(Status.OPEN.toString());
			if (hasComplete) {
				comboBoxStatus.removeItem(Status.COMPLETE.toString());
			}
		}
		if (req.getStatus() == Status.OPEN) {
			// Open: Open, Deleted
			comboBoxStatus.removeItem(Status.NEW.toString());
			comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
			if (hasComplete) {
				comboBoxStatus.removeItem(Status.COMPLETE.toString());
			}
		}
		if (req.getStatus() == Status.COMPLETE) {
			// Complete: Open, Complete, Deleted
			comboBoxStatus.removeItem(Status.NEW.toString());
			comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
		}
		if (req.getStatus() == Status.DELETED) {
			// Deleted: Open, Deleted, Complete
			comboBoxStatus.removeItem(Status.NEW.toString());
			comboBoxStatus.removeItem(Status.IN_PROGRESS.toString());
			if (hasComplete) {
				comboBoxStatus.removeItem(Status.COMPLETE.toString());
			}
		}

		return comboBoxStatus;
	}

	@Override
	public void setValueAt(final Object value, final int row, final int col) {
		if (!view.isEditable()) {
			return;
		}
		if (editedRows == null) {
			editedRows = new boolean[super.getRowCount()];
		} else if (editedRows.length < super.getRowCount()) {
			final boolean[] temp = new boolean[super.getRowCount()];
			editedRows = temp;
		}

		// The estimate column should only accept non-negative integers
		try {

			if (super.convertColumnIndexToModel(col) == 6) {
				final int i = Integer.parseInt((String) value);
				if ((i < 0)
						|| (i == Integer.parseInt((String) super.getValueAt(
								row, col)))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(Integer.toString(i), row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 7) {
				final int i = Integer.parseInt((String) value);
				if ((i < 0)
						|| (i == Integer.parseInt((String) super.getValueAt(
								row, col)))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(Integer.toString(i), row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 8) {

				final String i = (String) value;
				if ((i.length() < 0)
						|| (i.equals((String) super.getValueAt(row, col)))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(i, row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 1) {

				final String i = (String) value;
				if ((i.length() < 0) || (i.length() > 100)
						|| (i.equals((String) super.getValueAt(row, col)))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(i, row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 2) {

				final String i = (String) value;
				if (i.equals((String) super.getValueAt(row, col))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(i, row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 3) {

				final String i = (String) value;
				if (i.equals((String) super.getValueAt(row, col))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(i, row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 4) {

				final String i = (String) value;
				if (i.equals((String) super.getValueAt(row, col))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(i, row, col);
				}

			} else if (super.convertColumnIndexToModel(col) == 5) {

				final String i = (String) value;
				if (i.equals((String) super.getValueAt(row, col))) {
					return;
				} else {
					// we save the parsed int to removed leading 0s
					editedRows[convertRowIndexToModel(row)] = true;
					editedRowColumns.add(new RowCol(convertRowIndexToModel(row), convertColumnIndexToModel(col)));
					selectionModel.removeSelectionInterval(
							convertRowIndexToModel(row),
							convertRowIndexToModel(row));
					super.setValueAt(i, row, col);
				}

			} else {
				selectionModel.removeSelectionInterval(
						convertRowIndexToModel(row),
						convertRowIndexToModel(row));
				super.getCellEditor().stopCellEditing();
				super.setValueAt(value, row, col);
			}
		} catch (final NumberFormatException e) {
			return;
		}
	}
}
