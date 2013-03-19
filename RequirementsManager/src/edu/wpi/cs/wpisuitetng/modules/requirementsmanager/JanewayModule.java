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
		MainView main = new MainView(detView,req);
		
		
		JanewayTabModel tab1 = new JanewayTabModel(getName(), new ImageIcon(), new JPanel(), main);
		
		tabs.add(tab1);
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
