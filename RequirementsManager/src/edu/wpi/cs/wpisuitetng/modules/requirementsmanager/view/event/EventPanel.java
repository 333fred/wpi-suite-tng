/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    spkordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The EventPanel class displays the individual event panels
 * 
 * @author spkordell
 */
@SuppressWarnings("serial")
public class EventPanel extends JPanel {

	protected JLabel title;
	protected JLabel content;
	
	private Event event;
	private int wrapWidth;
	
	private static final int WRAP_MIN = 100;

	/**
	 * The note panel is the panel that is used to create and display notes
	 * 
	 * @param note
	 *            the note that is displayed
	 */
	public EventPanel(Event event) {
		this.event = event;	
		if (this.wrapWidth < WRAP_MIN) {
			this.wrapWidth = WRAP_MIN;
		}
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		title = new JLabel(event.getTitle());
		title.setFont(title.getFont().deriveFont(9));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		content = new JLabel("<html><BODY><TABLE WIDTH="+(this.wrapWidth-30)+"><TR><TD>" + event.getContent() +"</TD></TR></TABLE></BODY></HTML>");
		content.setFont(content.getFont().deriveFont(9));
		content.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0),BorderFactory.createLineBorder(Color.black, 1)),BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		this.add(title);
		this.add(content);
	}
		
	public void paint (Graphics g) {
		if (this.wrapWidth < WRAP_MIN) {
			this.wrapWidth = WRAP_MIN;
		}
		content.setText("<html><BODY><TABLE WIDTH="+(this.wrapWidth-30)+"><TR><TD>" + event.getContent() +"</TD></TR></TABLE></BODY></HTML>");
		this.content.setPreferredSize(null);
		this.setPreferredSize(null);
		this.content.setSize(new Dimension(this.wrapWidth-30,this.content.getPreferredSize().height));

		this.setSize(new Dimension(this.wrapWidth,this.content.getPreferredSize().height + this.title.getPreferredSize().height + 22));
		this.setPreferredSize(this.getSize());
		//this.setPreferredSize(new Dimension(this.wrapWidth,this.title.getPreferredSize().height+this.content.getPreferredSize().height));
		super.paint(g);
	}
	
	public void setWrapWidth(int wrapWidth) {
		this.wrapWidth = wrapWidth;
	}
	
/*	public JLabel getnoteField() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public JLabel getNoteList() {
		// TODO Auto-generated method stub
		return null;
	}*/
}
