/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.DetailView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.MainView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.detailview.RequirementDetailViewGui;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.requirement.view.RequirementListView;

/**
 * @author Fredric
 *
 */
public class JanewayModule implements IJanewayModule {

	List<JanewayTabModel> tabs;
	
	public JanewayModule() {
		tabs = new ArrayList<JanewayTabModel>();
	}

	//temporarilly does nothing to get rid of the NullPointer
	List<JanewayTabModel> tabs = new ArrayList<JanewayTabModel>();
	
	public JanewayModule(){
		
		
		tabs.add(tab1);
	}
	
	/** List of tabs that this module will display */
	private List<JanewayTabModel> tabs;
	
	/** The list view that will display the requirements	 */
	private RequirementListView requirementListView;
	
	/** Creates a new instance of JanewayModule, initializing the tabs to be displayed
	 * 
	 */
	
	public JanewayModule() {
		//initialize the list of tabs, using an array list
		tabs = new ArrayList<JanewayTabModel>();	
		
		//initialize the requirements list view
		requirementListView = new RequirementListView();
		
		//create a new JanewayTabModel, passing in the requirements view, and a new JPanel as the toolbar
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), new JPanel(), requirementListView);
		
		Requirement req = new Requirement();
		DetailView detView = new DetailView(req);
		RequirementDetailViewGui reqtab = new RequirementDetailViewGui(detView, req);
		
		JanewayTabModel tab2 = new JanewayTabModel(getName(), new ImageIcon(), new JPanel(), reqtab);
		
		//add the tabs to the list of tabs
		tabs.add(tab1);
		tabs.add(tab2);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Requirements Manager";
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 * TODO: Implement This
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}
