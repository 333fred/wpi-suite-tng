package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.defecttracker.defect.DefectPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.models.Requirement;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.DetailPanel;


/**
 * @author Steven Kordell
 *
 */
public class DetailView extends JPanel {

	public JScrollPane detailScrollPanel;
	
	public DetailView(Requirement requirement){
		DetailPanel detailPanel = new DetailPanel(requirement);
		
		this.setLayout(new BorderLayout());
		detailScrollPanel = new JScrollPane(detailPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			
		// Prevent content of scroll pane from smearing (credit: https://gist.github.com/303464)
		detailScrollPanel.getVerticalScrollBar().addAdjustmentListener(new java.awt.event.AdjustmentListener(){
			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent ae){
				//SwingUtilities.invokeLater(new Runnable(){
				//	public void run(){
						detailScrollPanel.repaint();
				//	}
				//});
			}
		});
		
		add(detailScrollPanel,BorderLayout.CENTER);
	}
}