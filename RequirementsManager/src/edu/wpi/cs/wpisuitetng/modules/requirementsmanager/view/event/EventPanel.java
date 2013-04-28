/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasaurus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    @author Steve Kordell
 *******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The EventPanel class displays the individual event panels
 */
@SuppressWarnings ("serial")
public class EventPanel extends JPanel {
	
	protected JLabel title;
	protected JLabel content;
	
	private final Event event;
	
	/**
	 * The note panel is the panel that is used to create and display notes
	 * 
	 * @param event
	 *            the note that is displayed
	 */
	public EventPanel(final Event event) {
		this.event = event;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		title = new JLabel(this.event.getTitle());
		title.setFont(title.getFont().deriveFont(9));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		content = new JLabel();
		content.setText("<html><BODY>" + this.event.getContent()
				+ "</BODY></HTML>");
		content.setFont(content.getFont().deriveFont(9));
		content.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createCompoundBorder(
						BorderFactory.createEmptyBorder(5, 0, 5, 0),
						BorderFactory.createLineBorder(Color.black, 1)),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		this.add(title);
		this.add(content);
	}
	
	/**
	 * Creates a new EventPanel with the given event and max width of the panel
	 * 
	 * @param event
	 *            the event to handle
	 * @param maxWidth
	 *            the max with of the Panel
	 */
	public EventPanel(final Event event, final int maxWidth) {
		this.event = event;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		title = new JLabel(this.event.getTitle());
		title.setFont(title.getFont().deriveFont(9));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray));
		content = new JLabel();
		content.setText("<html><BODY><TABLE WIDTH=" + (maxWidth - 30)
				+ "><TR><TD>" + this.event.getContent()
				+ "</TD></TR></TABLE></BODY></HTML>");
		content.setFont(content.getFont().deriveFont(9));
		content.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createCompoundBorder(
						BorderFactory.createEmptyBorder(5, 0, 5, 0),
						BorderFactory.createLineBorder(Color.black, 1)),
				BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		this.add(title);
		this.add(content);
	}
	
}
