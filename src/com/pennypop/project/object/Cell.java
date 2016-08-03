package com.pennypop.project.object;

/*
 * Object class for the cells (connect4 table is made up of an accumulation
 * of cells). This contains the data for it including the location
 * in both the screen and the board. It also contains info on the piece it currently hold.
 */

public class Cell {
	private int row, col, player;
	private float x, y;
	
	public Cell(int row, int col) {
		this.row = row;
		this.col = col;
		player = 0;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	
	public void setPlayer(int player) {
		this.player = player;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public void setCoords(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
