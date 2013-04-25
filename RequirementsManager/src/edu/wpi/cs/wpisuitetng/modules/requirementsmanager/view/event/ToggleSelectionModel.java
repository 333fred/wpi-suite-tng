/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	@author Steve Kordell
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view.event;

import javax.swing.DefaultListSelectionModel;

/**
 * The toggleselectionmodel is responsible for 
 */

public class ToggleSelectionModel extends DefaultListSelectionModel {
	
	private static final long serialVersionUID = 1L;
	
	boolean gestureStarted = false;
	
	@Override
	public void setSelectionInterval(final int index0, final int index1) {
		// if shift
		if (!gestureStarted) {
			if (isSelectedIndex(index0)) {
				super.removeSelectionInterval(index0, index1);
			} else {
				super.addSelectionInterval(index0, index1);
			}
		}
		gestureStarted = true;
	}
	
	@Override
	public void setValueIsAdjusting(final boolean isAdjusting) {
		if (!isAdjusting) {
			gestureStarted = false;
		}
	}
	
}
