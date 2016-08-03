package com.pennypop.project.object;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

//Singleton class to manage and load the assets

public class Assets {
	private static Assets assets;
	
	private static AssetManager manager;
	
	public static Assets get() {
		if (assets == null) {
			assets = new Assets();
		}
		
		return assets;
	}
	
	private Assets() {
		manager = new AssetManager();
	}
	
	public AssetManager getManager() {
		return manager;
	}
	
	//Load all the assets for the main screen
	public void loadMainScreen() {
		manager.load("font.fnt", BitmapFont.class);
		manager.load("main_bg.png", Texture.class);
		manager.load("apiButton.png", Texture.class);
		manager.load("sfxButton.png", Texture.class);
		manager.load("gameButton.png", Texture.class);
		manager.load("button_click.wav", Sound.class);
	}
	
	//Load all the assets for the game screen
	public void loadGameScreen() {
		//GameScreen
		manager.load("game_bg.png", Texture.class);
		manager.load("aiButton_ON.png", Texture.class);
		manager.load("aiButton_OFF.png", Texture.class);
		manager.load("cell.png", Texture.class);
		manager.load("red.png", Texture.class);
		manager.load("yellow.png", Texture.class);
	}
}
