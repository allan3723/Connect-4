package com.pennypop.project.object;

//Singleton class for settings
//Edit this to change the rules, match condition, rows, columns, etc
	
public class GameSettings {
	private static GameSettings settings;
	
	private int players;
	private int rows;
	private int columns;
	private int winCondition;
	private boolean ai;
	
	public static GameSettings get() {
		if (settings == null) {
			settings = new GameSettings();
		}
		
		return settings;
	}

	private GameSettings() {
		//Default Values
		players = 2;
		rows = 6;
		columns = 7;
		winCondition = 4;
		ai = true;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getWinCondition() {
		return winCondition;
	}

	public void setWinCondition(int winCondition) {
		this.winCondition = winCondition;
	}

	public boolean isAi() {
		return ai;
	}

	public void setAi(boolean ai) {
		this.ai = ai;
	}
}
