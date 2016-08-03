package com.pennypop.project.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pennypop.project.controller.OpenWeatherMapAPI;
import com.pennypop.project.object.Assets;

/**
 * This is where you screen code will go, any UI should be in here
 * 
 * @author Richard Taylor, Allan Cheng
 */
public class MainScreen implements Screen {
	private static final String PENNYPOP_LABEL = "PennyPop";
	private static final String WEATHER_CITY = "San Francisco";
	private static final String WEATHER_COUNTRY = "US";
	private static final String WEATHER_LABEL = "Current Weather";
	private static final int BUTTON_GAP = 10;

	private final BitmapFont font;
	private final Texture sfxTexture, apiTexture, gameTexture, bgTexture;
	private final Sound clickSound;

	private final Screen currentScreen;
	private final Stage stage;
	private final SpriteBatch spriteBatch;
	private AssetManager manager;
	
	private Game game;
	
	public MainScreen(Game game) {
		spriteBatch = new SpriteBatch();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);
		currentScreen = this;
		this.game = game;
		
		manager = Assets.get().getManager();
		
		//Get all the fonts, textures, and sounds that will be used
		font = manager.get("font.fnt", BitmapFont.class);
		bgTexture = manager.get("main_bg.png", Texture.class);
		sfxTexture = manager.get("sfxButton.png", Texture.class);
		apiTexture = manager.get("apiButton.png", Texture.class);
		gameTexture = manager.get("gameButton.png", Texture.class);
		clickSound = manager.get("button_click.wav", Sound.class);
		
		configUI();
	}
	
	//Configure & position the buttons and labels used for UI
	private void configUI() {
		//Set the BG
		Image bg = new Image(bgTexture);
		
		//Configure the "PennyPop" label and position it
		//x = center of left-half screen; y = a bit higher than center
		Label label = configLabel(font, PENNYPOP_LABEL, Color.RED);
		BitmapFont.TextBounds bounds = font.getBounds(PENNYPOP_LABEL);
		label.setPosition(Gdx.graphics.getWidth()/4 - bounds.width/2, 
			Gdx.graphics.getHeight()/2 + label.getHeight());
	
		//Configure the "SFX" button and position it below the label & right of API
		Button sfxButton = configButton(sfxTexture);
		sfxButton.setPosition(Gdx.graphics.getWidth()/4 - 
			((3 * sfxButton.getWidth())/2)- BUTTON_GAP,
			Gdx.graphics.getHeight()/2 - sfxButton.getHeight()/2 - label.getHeight());
	
		//Configure the "API" button and position it below the label & between the 2 buttons
		Button apiButton = configButton(apiTexture);
		apiButton.setPosition(Gdx.graphics.getWidth()/4 - apiButton.getWidth()/2,
			Gdx.graphics.getHeight()/2 - sfxButton.getHeight()/2 - label.getHeight());
	
		//Configure the "Game" button and position it below the label & left of API
		Button gameButton = configButton(gameTexture);
		gameButton.setPosition(Gdx.graphics.getWidth()/4 + gameButton.getWidth()/2 + BUTTON_GAP,
			Gdx.graphics.getHeight()/2 - sfxButton.getHeight()/2 - label.getHeight());
	
		//Add all elements to be drawn
		stage.addActor(bg);
		stage.addActor(label);
		stage.addActor(sfxButton);
		stage.addActor(apiButton);
		stage.addActor(gameButton);
	}
	
	//Displays the weather information from the API
	private void displayWeather() {
		OpenWeatherMapAPI weather = 
				new OpenWeatherMapAPI(WEATHER_CITY, WEATHER_COUNTRY);
		
		//Configure weather information label
		//Position: x = center of right-half screen; y = center
		String weatherString = new String(WEATHER_LABEL + "\n"
				+ WEATHER_CITY + "\n\n"
				+ weather.getWeatherDesc() + "\n"
				+ weather.getTemperature() + " Kelvin\n"
				+ weather.getWindDeg() + " degrees, " +
				+ weather.getWindSpeed() + "mph wind");
		Label weatherLabel = configLabel(font, weatherString, Color.CYAN);
		BitmapFont.TextBounds bounds = font.getMultiLineBounds(weatherString);
		weatherLabel.setPosition((3 * Gdx.graphics.getWidth()/4) - bounds.width/2, 
			Gdx.graphics.getHeight()/2 - bounds.height/2);
		weatherLabel.setAlignment(Align.center);
		
		//Add all elements to be drawn
		stage.addActor(weatherLabel);		
	}

	//Set the button's image & onClick
	private Button configButton(final Texture texture) {
		Button button = new Button(new Image(texture).getDrawable());
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				clickSound.play();
			
				// If API button is clicked, get the weather information in the background
				if (texture == apiTexture) {
					Runnable getWeather = new Runnable() {
						public void run() {
							displayWeather();
						}
					};
					
					new Thread(getWeather).start();
				} else {
					//Game button is clicked so start the game screen
					if (texture == gameTexture) {
						manager.finishLoading();
						Screen gameScreen = new GameScreen(game, currentScreen);
						currentScreen.hide();
						game.setScreen(gameScreen);
					}
				}
			}
		});
	
		return button;
	}
	
	//Set the label's font and color
	private Label configLabel(BitmapFont font, String label, Color color) {
		LabelStyle style = new LabelStyle(font, color);
		return new Label(label, style);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this
	}

}
