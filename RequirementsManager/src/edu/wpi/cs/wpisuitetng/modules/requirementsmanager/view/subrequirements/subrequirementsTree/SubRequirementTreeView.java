package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.subrequirements.subrequirementsTree;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.exceptions.RequirementNotFoundException;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.IDatabaseListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.localdatabase.RequirementDatabase;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
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

	private MainTabController tabController;

	/** List of all the iterations currently being displayed */
	List<Requirement> requirements;

	private boolean firstPaint;

	public SubRequirementTreeView(MainTabController tabController) {
		super(new BorderLayout());
		this.tabController = tabController;

		requirementsController = new RequirementsController();

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
		updateTreeView();
	}

	public void getRequirementsFromServer() {
		RetrieveAllRequirementsRequestObserver observer = new RetrieveAllRequirementsRequestObserver(
				this);
		requirementsController.getAll(observer);
	}

	public void updateTreeView() {
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

		}
	}

	public static String getExpansionState(JTree tree, int row) {
		/*
		 * TreePath rowPath = tree.getPathForRow(row); StringBuffer buf = new
		 * StringBuffer(); int rowCount = tree.getRowCount(); for (int i = row;
		 * i < rowCount; i++) { TreePath path = tree.getPathForRow(i); if (i ==
		 * row || isDescendant(path, rowPath)) { if (tree.isExpanded(path))
		 * buf.append("," + String.valueOf(i - row)); } else break; } return
		 * buf.toString();
		 */
		return null;
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
		this.requirements = Arrays.asList(requirements);
		this.updateTreeView();
	}

	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		this.updateTreeView();
	}

	@Override
	public boolean shouldRemove() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	@Override
	public void receivedData(Iteration[] iterations) {
		update();
	}

}
