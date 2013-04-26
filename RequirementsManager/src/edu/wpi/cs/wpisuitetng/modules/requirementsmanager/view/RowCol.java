package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

public class RowCol {
	private int row;
	private int col;

	RowCol(int a, int b) {
		row = a;
		col = b;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

}