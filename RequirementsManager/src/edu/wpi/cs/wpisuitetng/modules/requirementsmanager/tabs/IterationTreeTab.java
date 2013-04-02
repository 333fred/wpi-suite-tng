package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.tabs;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.IterationTreeView;

/** A tab wrapper that automatically wraps an iteration tree view around the given
 * tab component
 * 
 * @author Mitchell
 *
 */

public class IterationTreeTab extends JPanel implements TabFocusListener {

	/** The JPanel that will be displayed to the right of the iterationVIew */
	private JPanel tabComponent;
	
	/** The split pane used separate the tree view and main tab component */
	private JSplitPane splitPane;
	
	/** The IterationTreeView that will be displayed on the left */
	private IterationTreeView iterationTreeView;
	
	/** the focus listener for when a tab has gained focus */
	private TabFocusListener tabFocusListener;
	
	/** Creates a new IterationTreeTab, that displays the given component
	 * 
	 * @param tabComponent The component to display in the tab
	 */
	
	public IterationTreeTab(JPanel tabComponent) {
		this(tabComponent, null);
	}
	
	/** Creates a new IterationTreeTab, that displays the given component, and tabFocusListener
	 * 
	 * @param tabComponent The component to display in the tab
	 * @param tabFocusListener The tab focus listener that will be called when this tab gains focus
	 */
	
	public IterationTreeTab(JPanel tabComponent, TabFocusListener tabFocusListener) {
		this.tabComponent = tabComponent;		
		//initialize the components
		iterationTreeView = new IterationTreeView();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,	iterationTreeView, tabComponent);
		splitPane.setResizeWeight(0.1);
		
		//create the layout for the tab
		GridLayout mainLayout = new GridLayout(0, 1);
		setLayout(mainLayout);
		
		//add the components
		add(splitPane);
		
	}
	
	/** Called when this tab gains focus, If this was created with a tabFocusListener,
	 *  the onGainFocus method is also called on that listener
	 * 
	 */
	
	public void onGainedFocus() {
		if (tabFocusListener != null) {
			//notify the focus listener that we have gained focus
			tabFocusListener.onGainedFocus();
		}
		//refresh the iteration tree
		iterationTreeView.refresh();
	}
	
	
}
