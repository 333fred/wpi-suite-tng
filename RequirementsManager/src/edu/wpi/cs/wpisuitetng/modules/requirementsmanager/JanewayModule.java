/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.text.html.ListView;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.MainView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.RequirementListView;

/**
 * @author Fredric
 *
 */
public class JanewayModule implements IJanewayModule {

	//temporarilly does nothing to get rid of the NullPointer
	List<JanewayTabModel> tabs = new ArrayList<JanewayTabModel>();
	
	public JanewayModule(){
		
		Requirement req = new Requirement();
		DetailView detView = new DetailView(req);
		RequirementListView listView = new RequirementListView();
		//RequirementDetailViewGui reqtab = new RequirementDetailViewGui(detView, req);
		
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), new JPanel(), detView);
		JanewayTabModel tab2 = new JanewayTabModel(getName(), new ImageIcon(), new JPanel(), listView);
		
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
