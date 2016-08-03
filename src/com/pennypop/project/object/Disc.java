package com.pennypop.project.object;

import com.badlogic.gdx.graphics.Texture;

/*
 * Object class that contains the data for the playing discs
 * Also finds and sets the position of the discs
 */
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pennypop.project.screens.GameScreen;

public class Disc {
	private Image disc;
	private float x, y, width, height;
	private int row, col;
	
	public Disc(Texture texture) {
		disc = new Image(texture);
		disc.setSize(disc.getWidth() * GameScreen.IMG_PADDING, 
				disc.getHeight() * GameScreen.IMG_PADDING);
		
		width = disc.getWidth();
		height = disc.getHeight();
		
		//initialize: -1 = error
		x = y = row = col = -1;
	}

	public float getX() {
		return x;
	}

	//Finds the location of the column clicked and then locate the first empty row
	public boolean setPosition(float clickX, Cell[][] board) {
		for (int i = 0; i < GameSettings.get().getColumns(); i++) {
			if (clickX > board[0][i].getX() && clickX < board[0][i].getX()+width){
				for (int j = 0; j < GameSettings.get().getRows(); j++) {
					if (board[j][i].getPlayer() == 0) {
						col = i;
						row = j;
						x = board[j][i].getX();
						y = board[j][i].getY();
						disc.setPosition(x, y);

						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//For AI to locate the empty row to place on the board
	public void setPosition(int col, Cell[][] board) {
		for (int i = 0; i < GameSettings.get().getRows(); i++) {
			if (board[i][col].getPlayer() == 0) {
				this.col = col;
				row = i;
				x = board[i][col].getX();
				y = board[i][col].getY();
				disc.setPosition(x, y);
				
				return;
			}
		}
	}

	public float getY() {
		return y;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Image getDisc() {
		return disc;
	}

	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
