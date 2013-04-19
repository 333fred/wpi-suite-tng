package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.RetrieveAllRequirementsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.observers.notifiers.IRetreivedAllIterationsNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.TreeTransferHandler;

public class SubRequirementTreeView extends JPanel implements
		IDatabaseListener, IReceivedAllRequirementNotifier,
		IRetreivedAllIterationsNotifier {
	private JTree tree;
	private JScrollPane treeView;
	private DefaultMutableTreeNode top;
	private RequirementsController requirementsController;
	private IterationController iterationController;

	private MainTabController tabController;

	/** List of all the iterations currently being displayed */
	List<Requirement> requirements;

	private boolean firstPaint;

	public SubRequirementTreeView(MainTabController tabController) {
		super(new BorderLayout());
		this.tabController = tabController;

		requirementsController = new RequirementsController();
		iterationController = new IterationController();
		firstPaint = true;

		this.top = new DefaultMutableTreeNode("Requirements");
		this.tree = new JTree(top);
		this.tree.setEditable(false);
		this.tree.setDragEnabled(false);
		// this.tree.setDropMode(DropMode.ON);

		this.tree.setTransferHandler(new TreeTransferHandler(tabController));
		this.tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		treeView = new JScrollPane(tree);
		this.add(treeView, BorderLayout.CENTER);

		RequirementDatabase.getInstance().registerListener(this);

		requirements = RequirementDatabase.getInstance().getAllRequirements();
		updateTreeView();
	}

	public void getRequirementsFromServer() {
		RetrieveAllRequirementsRequestObserver observer = new RetrieveAllRequirementsRequestObserver(
				this);
		requirementsController.getAll(observer);
	}

	public void updateTreeView() {
		String eState = getExpansionState(this.tree, 0);
		DefaultMutableTreeNode requirementNode = null;
		DefaultMutableTreeNode subRequirementNode = null;
		Requirement tempReq = null;
		this.top.removeAllChildren();

		if (requirements != null) {
			for (Requirement anReq : requirements) {

				if (anReq.getpUID().size() == 0) {
					requirementNode = new DefaultMutableTreeNode(anReq);

					for (Integer aReq : anReq.getSubRequirements()) {
						try {
							tempReq = RequirementDatabase.getInstance()
									.getRequirement(aReq);
							subRequirementNode = new DefaultMutableTreeNode(
									tempReq);
							requirementNode.add(subRequirementNode);
							updateTreeNodes(tempReq, subRequirementNode);
						} catch (RequirementNotFoundException e) {
							System.out
									.println("Requirement not found: SubRequirementTreeView:372");
							requirementNode = new DefaultMutableTreeNode("POOP");
						}
					}
					this.top.add(requirementNode);
				}
			}

			((DefaultTreeModel) this.tree.getModel())
					.nodeStructureChanged(this.top);
			DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) this.tree
					.getCellRenderer();
			renderer.setLeafIcon(null);
			this.tree.setCellRenderer(renderer);
			restoreExpanstionState(this.tree, 0, eState);

		}
	}

	public static void restoreExpanstionState(JTree tree, int row,
			String expansionState) {
		StringTokenizer stok = new StringTokenizer(expansionState, ",");
		while (stok.hasMoreTokens()) {
			int token = row + Integer.parseInt(stok.nextToken());
			tree.expandRow(token);
		}
	}

	public static String getExpansionState(JTree tree, int row) {
		TreePath rowPath = tree.getPathForRow(row);
		StringBuffer buf = new StringBuffer();
		int rowCount = tree.getRowCount();
		for (int i = row; i < rowCount; i++) {
			TreePath path = tree.getPathForRow(i);
			if (i == row || isDescendant(path, rowPath)) {
				if (tree.isExpanded(path))
					buf.append("," + String.valueOf(i - row));
			} else
				break;
		}
		return buf.toString();
	}

	public static boolean isDescendant(TreePath path1, TreePath path2) {
		int count1 = path1.getPathCount();
		int count2 = path2.getPathCount();
		if (count1 <= count2)
			return false;
		while (count1 != count2) {
			path1 = path1.getParentPath();
			count1--;
		}
		return path1.equals(path2);
	}

	public void updateTreeNodes(Requirement anReq, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode subRequirementNode = null;
		Requirement tempReq = null;

		for (Integer aReq : anReq.getSubRequirements()) {
			try {
				tempReq = RequirementDatabase.getInstance()
						.getRequirement(aReq);
				subRequirementNode = new DefaultMutableTreeNode(tempReq);
				node.add(subRequirementNode);
				updateTreeNodes(tempReq, subRequirementNode);
			} catch (RequirementNotFoundException e) {
				System.out
						.println("GRRR Requirement not found: SubRequirementTreeView:372");
			}
		}
	}

	@Override
	public void receivedData(Requirement[] requirements) {
		// this.requirements = Arrays.asList(requirements);
		refresh();
	}

	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {

	}

	@Override
	public void update() {
	}

	public void refresh() {
		this.requirements = RequirementDatabase.getInstance()
				.getAllRequirements();
		updateTreeView();
	}

	@Override
	public boolean shouldRemove() {
		return false;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (firstPaint) {
			firstPaint = false;
			getRequirementsFromServer();
			getIterationsFromServer();
		}
	}

	private void getIterationsFromServer() {
		RetrieveAllIterationsRequestObserver observer = new RetrieveAllIterationsRequestObserver(
				this);
		iterationController.getAll(observer);
	}

	@Override
	public void receivedData(Iteration[] iterations) {
		updateTreeView();
	}
}
