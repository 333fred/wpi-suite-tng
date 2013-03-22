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
public class RequirementTreeView extends JPanel implements IReceivedAllRequirementNotifier{

	private JTree reqTree;
	private DefaultMutableTreeNode top;
	
	RetrieveAllRequirementsController retreiveAllRequirementsController;
	
	RequirementTreeView() {
		super(new BorderLayout());		
		
		this.retreiveAllRequirementsController = new RetrieveAllRequirementsController(this);
		
		this.top = new DefaultMutableTreeNode("Requirements");
		//this.createNodes(top);
		this.reqTree = new JTree(top);	
		JScrollPane treeView = new JScrollPane(reqTree);	
		this.add(treeView,BorderLayout.CENTER);
		
		/*
		//TODO: Make Work
		//QuickFix, works, but not optimal
		ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
		Runnable task = new Runnable() {
		    public void run() {
		    	System.out.println("[TREE] Invoking this");
		    	getRequirementsFromServer();
		    	
		    }
		  };
		  worker.schedule(task, 5, TimeUnit.SECONDS);
		  */
	}


	@Override
	public void receivedData(Requirement[] requirements) {
		DefaultMutableTreeNode req = null;
        
		this.top.removeAllChildren();
		
		for(int i = 0; i < requirements.length; i++){
			// produce a summary String for the list
			req = new DefaultMutableTreeNode(requirements[i].toListString());
			this.top.add(req);
		}
		
		this.reqTree.expandRow(0);
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
	
}
