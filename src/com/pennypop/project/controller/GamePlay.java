package com.pennypop.project.controller;

import com.pennypop.project.object.Cell;
import com.pennypop.project.object.GameSettings;

/*
 * The gameplay logic class that contains all the implementation
 * of how to play connect 4. It determines the victory, checks the turn
 * and adds disc pieces to the board.
 */

public class GamePlay {
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;
	
	public static final int ONGOING = 0;
	public static final int VICTORY = 1;
	public static final int DRAW = 2;
	
	private int rows;
	private int cols;
	private int numOfDiscs;
	private int turn;
	private int winCond;
	private int winner;
	public int state;
	private GameSettings settings;
	
	private Cell[][] board;
	
	//Constructor to initialize and create back-end board
	public GamePlay() {
		settings = GameSettings.get();
		rows = settings.getRows();
		cols = settings.getColumns();
		winCond = settings.getWinCondition();
		numOfDiscs = rows * cols;
		turn = PLAYER_1;
		winner = 0;
		state = ONGOING;
		
		board = new Cell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Cell cell = new Cell(rows, cols);
				board[i][j] = cell;
			}
		}
	}
	
	//Add to the back-end board and see if there's a winner
	public void addDisc(int player, int row, int column) {
		board[row][column].setPlayer(player);
		numOfDiscs--;
		
		if (checkWin(row, column)) {
			winner = turn;
			return;
		}
		
		if (checkDraw()) {
			return;
		}
		
		turn++;
		
	}
	
	//Determine whose turn it is
	public int checkTurn() {
		if (turn > GameSettings.get().getPlayers()) {
			turn = PLAYER_1;
		}
		
		return turn;
	}
	
	//Check if there's any winning combinations
	public boolean checkWin(int row, int col) {
		if (checkVerticalMatch(row, col)
				|| checkHorizontalMatch(row, col)
				|| checkDiagonalRightMatch(row, col)
				|| checkDiagonalLeftMatch(row, col)) {
			state = VICTORY;
			return true;
		}
		
		return false;
	}
	
	//Check matches from top to bottom: '|'
	private boolean checkVerticalMatch(int row, int col) {
		int count = 1;
		
		for (int i = row - 1; i >= 0; i--) {
			if (board[i][col].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
			
			count++;
			
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
		
		return false;
	}
	
	//Check matches from left to right: '-'
	private boolean checkHorizontalMatch(int row, int col) {
		int count = 1;
		
		//Check left half
		for (int i = col - 1; i >= 0; i--) {
			if (board[row][i].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
			
			count++;
			
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
		
		//Check right half
		for (int i = col + 1; i < this.cols; i++) {
			if (board[row][i].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
			
			count++;
			
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
		
		return false;
	}
	
	//Check matches diagonally to the right: '/'
	private boolean checkDiagonalRightMatch(int row, int col) {
		//Check top half
		int count = 1;
		for (int i = 1; row+i < this.rows && col+i < this.cols; i++) {
			if (board[row + i][col + i].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
			
			count++;
			
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
		
		//Check bottom half
		for (int i = 1; row-i >= 0 && col-i >= 0; i++) {
			if (board[row - i][col - i].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
			
			count++;
			
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
		
		return false;
	}
	
	//Check matches diagonally to the left: '\'
	private boolean checkDiagonalLeftMatch(int row, int col) {
		//Check bottom half
		int count = 1;
		for (int i = 1; row-i >= 0 && col+i < this.cols; i++) {
			if (board[row - i][col + i].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
				
			count++;
				
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
			
		//Check top half
		for (int i = 1; row+i > this.rows && col-i <= 0; i++) {
			if (board[row + i][col - i].getPlayer() != board[row][col].getPlayer()) {
				break;
			}
				
			count++;
				
			//No need to check anymore if there is victory
			if (checkCond(count)) {
				return true;
			}
		}
			
		return false;
	}
	
	//Check if the win condition has been achieved
	private boolean checkCond(int matches) {
		if (matches >= winCond) {
			return true;
		}
		
		return false;
	}
	
	//Determine if there has been a draw
	public boolean checkDraw() {
		if (numOfDiscs == 0) {
			state = DRAW;
			return true;
		}
		
		return false;
	}
	
	public Cell[][] getBoard() {
		return board;
	}
	
	public int getState() {
		return state;
	}
	
	public int getWinner() {
		return winner;
	}

}
