/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Mitchell Caisse
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.filter;

import java.awt.BorderLayout;

import javax.swing.JPanel;

/** The view for viewing and creating filters 
 * 
 * @author Mitchell
 *
 */

public class FilterView extends JPanel {

	private CreateFilterView createFilterView;
	
	public FilterView() {
		setLayout(new BorderLayout());
		createFilterView = new CreateFilterView();
		add(createFilterView, BorderLayout.CENTER);

	}
	
}
