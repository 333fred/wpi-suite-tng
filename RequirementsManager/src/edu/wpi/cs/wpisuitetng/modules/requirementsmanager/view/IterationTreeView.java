/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.IReceivedAllRequirementNotifier;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllRequirementsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;

/**
 * @author spkordell
 *
 */
public class IterationTreeView extends JPanel {

	private JTree tree;
	private DefaultMutableTreeNode top;

	IterationTreeView() {
		super(new BorderLayout());

		this.top = new DefaultMutableTreeNode("Iterations");
		//this.createNodes(top);
		this.tree = new JTree(top);	
		JScrollPane treeView = new JScrollPane(tree);	
		this.add(treeView,BorderLayout.CENTER);
		
		
		//Dummy Stuff for now
		DefaultMutableTreeNode iterationNode = null;
		iterationNode = new DefaultMutableTreeNode("Iteration 1");
		iterationNode.add(new DefaultMutableTreeNode("Requirement a"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement b"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement c"));
		this.top.add(iterationNode);
		iterationNode = new DefaultMutableTreeNode("Iteration 2");
		iterationNode.add(new DefaultMutableTreeNode("Requirement d"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement e"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement f"));
		this.top.add(iterationNode);
		iterationNode = new DefaultMutableTreeNode("Iteration 3");
		iterationNode.add(new DefaultMutableTreeNode("Requirement g"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement h"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement i"));
		this.top.add(iterationNode);
		iterationNode = new DefaultMutableTreeNode("Backlog");
		iterationNode.add(new DefaultMutableTreeNode("Requirement j"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement k"));
		iterationNode.add(new DefaultMutableTreeNode("Requirement l"));
		this.top.add(iterationNode);
		
		this.tree.expandRow(0);
	}

/*
	@Override
	public void receivedData(Requirement[] requirements) {
		DefaultMutableTreeNode req = null;
        
		this.top.removeAllChildren();

		for(int i = 0; i < requirements.length; i++){
			// produce a summary String for the list
			req = new DefaultMutableTreeNode(requirements[i].toListString());
			this.top.add(req);
		}

		this.tree.expandRow(0);
		//this.reqTree.repaint();
	}



	@Override
	public void errorReceivingData(String RetrieveAllRequirementsRequestObserver) {
		// TODO Auto-generated method stub

	}

	private void getRequirementsFromServer() {
		System.out.println("[TREE] We are getting requirements from the server");
		retreiveAllRequirementsController.getAll();
	}

	public void refresh() {
		this.getRequirementsFromServer();
	}
*/
}