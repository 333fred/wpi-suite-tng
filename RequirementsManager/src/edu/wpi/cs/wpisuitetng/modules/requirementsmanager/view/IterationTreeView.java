/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.controllers.RetrieveAllIterationsController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;


/**
 * @author spkordell
 *
 */
public class IterationTreeView extends JPanel {

	private JTree tree;
	private DefaultMutableTreeNode top;
	private RetrieveAllIterationsController retrieveAllIterationsController;

	IterationTreeView() {
		super(new BorderLayout());

		this.top = new DefaultMutableTreeNode("Iterations");
		//this.createNodes(top);
		this.tree = new JTree(top);	
		JScrollPane treeView = new JScrollPane(tree);	
		this.add(treeView,BorderLayout.CENTER);
		
		this.retrieveAllIterationsController = new RetrieveAllIterationsController(this);
		
		//Dummy Stuff for now
		
/*		DefaultMutableTreeNode iterationNode = null;
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
		
		*/
		this.tree.expandRow(0);
	}

	public void refresh() {
		System.out.print("Getting Iterations from Server\n");
		this.retrieveAllIterationsController.getAll();
	}
	
	public void refreshIterations(Iteration[] iterations) {
		System.out.print("Refreshing tree\n");
		DefaultMutableTreeNode iterationNode = null;
        
		this.top.removeAllChildren();
		
		for(int i = 0; i < iterations.length; i++){
			iterationNode = new DefaultMutableTreeNode(iterations[i].getName());
			
			for (Integer aReq : iterations[i].getRequirements()) {
				iterationNode.add(new DefaultMutableTreeNode(aReq.toString()));
			}
			
			this.top.add(iterationNode);
		}
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