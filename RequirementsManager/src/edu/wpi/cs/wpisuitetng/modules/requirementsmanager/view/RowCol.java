/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

/**
 * Simple class to hold row and column numbers
 */

public class RowCol {
	
	private int row;
	private int col;
	
	RowCol(int a, int b) {
		row = a;
		col = b;
	}
	
	/**
	 * @return the row number being held
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * @return the column number being held
	 */
	public int getCol() {
		return col;
	}
	
}