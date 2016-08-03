package com.pennypop.project.screens;

import com.badlogic.gdx.Game;
/*
 * This is where the code for the Connect 4 screen goes.
 * It handles all the drawing and UI for the screen
 */
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
import com.pennypop.project.controller.AI;
import com.pennypop.project.controller.GamePlay;
import com.pennypop.project.object.Assets;
import com.pennypop.project.object.Disc;
import com.pennypop.project.object.GameSettings;

public class GameScreen implements Screen{
	//The space between the bottom of the screen & the board
	public static final int FLOOR_SPACING = 85;
	//Adjust how big the board and pieces are
	public static final float IMG_PADDING = 1.2f;
	
	private static final String CONNECT4_LABEL = "Connect\nFour";
	
	private final Stage stage;
	private final SpriteBatch spriteBatch;
	private AssetManager manager;
	
	private Game game;
	private Screen parent;
	
	private final BitmapFont font;
	private final Texture yellowTexture, redTexture, cellTexture, bgTexture, 
		aiONTexture, aiOFFTexture;
	private final Sound sound;
	
	private AI ai;
	
	public GameScreen(final Game game, final Screen parent) {
		spriteBatch = new SpriteBatch();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);
		manager = Assets.get().getManager();
		
		//Reference to the MainScreen to switch back later
		this.game = game;
		this.parent = parent;

		//Get the assets
		font = manager.get("font.fnt", BitmapFont.class);
		bgTexture = manager.get("game_bg.png", Texture.class);
		aiONTexture = manager.get("aiButton_ON.png", Texture.class);
		aiOFFTexture = manager.get("aiButton_OFF.png", Texture.class);
		yellowTexture = manager.get("yellow.png", Texture.class);
		redTexture = manager.get("red.png", Texture.class);
		cellTexture = manager.get("cell.png", Texture.class);
		sound = manager.get("button_click.wav", Sound.class);
		
		//Setup the UI and background
		GameSettings.get().setAi(true);
		setupScreen();
		
		//Once everything is setup, draw and configure the board
		setupBoard();
		
	}
	
	//Background and buttons
	public void setupScreen() {
		//Setup the background
		Image bg = new Image(bgTexture);
		
		//Create a "Connect 4" label
		LabelStyle style = new LabelStyle(font, Color.BLACK);
		Label c4Label = new Label(CONNECT4_LABEL, style);
		c4Label.setAlignment(Align.center);
		BitmapFont.TextBounds bounds = font.getMultiLineBounds(CONNECT4_LABEL);
		c4Label.setPosition(Gdx.graphics.getWidth()/6 - bounds.width/2, 
			3 * Gdx.graphics.getHeight()/4 - bounds.height);

				
		//Setup the AI button: white = on, black = off
		Image image = new Image(aiONTexture);
		Button aiButton = new Button(image.getDrawable(), image.getDrawable(),
				new Image(aiOFFTexture).getDrawable());
		aiButton.setPosition(5 * Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight()/2);
		aiButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				sound.play();
				GameSettings.get().setAi(!GameSettings.get().isAi());
			};
		});
		
		//Add everything to the stage
		stage.addActor(bg);
		stage.addActor(c4Label);
		stage.addActor(aiButton);
	}
	
	public void setupBoard() {
		final GameSettings settings = GameSettings.get();
		final GamePlay board = new GamePlay();
		ai = new AI(board.getBoard());
		
		//Draw the board (which is an accumulation of cells)
		for (int i = 0; i < settings.getColumns(); i++) {
			for (int j = 0; j < settings.getRows(); j++) {
				//Initialize + increase the size of the cell
				Image cell = new Image(cellTexture);
				cell.setSize(cell.getWidth() * IMG_PADDING, cell.getHeight() * IMG_PADDING);
						
				//Center the board
				float x = Gdx.graphics.getWidth()/2 - cell.getWidth()/2
						- (cell.getWidth() * (settings.getColumns()/2))
						+ (cell.getWidth()*i);
				float y = cell.getHeight() * j + FLOOR_SPACING;
				cell.setPosition(x, y);
						
				//Add the coordinates to the table
				board.getBoard()[j][i].setCoords(x, y);
						
				//Add it to be drawn
				stage.addActor(cell);
			}
		}
				
		//Setup the onClick events to track where to position the discs
		stage.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
						
				//if the game has not ended yet, draw the disc at the spot clicked
				if (board.getState() == GamePlay.ONGOING) {
					Disc disc;
					switch(board.checkTurn()) {
						case GamePlay.PLAYER_1:
						disc = new Disc(redTexture);
							break;
						default:
							disc = new Disc(yellowTexture);
							break;
					}
							
					//add a piece to the board if there is room
					if (disc.setPosition(x, board.getBoard())) {
						board.addDisc(board.checkTurn(), disc.getRow(), disc.getCol());
						stage.addActor(disc.getDisc());
								
						//if AI is on & player 2's turn, AI moves
						if (settings.isAi() && board.checkTurn() == GamePlay.PLAYER_2) {
							disc = new Disc(yellowTexture);
							disc.setPosition(ai.findBestMove(), board.getBoard());
							board.addDisc(GamePlay.PLAYER_2, disc.getRow(), disc.getCol());
							stage.addActor(disc.getDisc());
						}
								
						//Display results if match end
						if (board.getState() != GamePlay.ONGOING) {
							String status = new String();
									
							if (board.getState() == GamePlay.VICTORY) {
								if (settings.isAi() == true && 
									board.getWinner() == GamePlay.PLAYER_2) {
									status = "AI wins\nClick to continue";
								} else {
									status = "Player " + board.getWinner() + " wins\n"
											+ "Click to continue";
								}
							} else {
								status = "Draw\nClick to continue";
							}
									
							//initialize the status label and position it it
							//on top & centered horizontally
							LabelStyle style = new LabelStyle(font, Color.RED);
							Label statusLabel = new Label(status, style);
							statusLabel.setAlignment(Align.center);
							BitmapFont.TextBounds bounds = font.getMultiLineBounds(status);
							statusLabel.setPosition(Gdx.graphics.getWidth()/2 - bounds.width/2, 
								Gdx.graphics.getHeight() - 3 * statusLabel.getHeight()/2);

							stage.addActor(statusLabel);
						}
					}
				} else {
					//Game over so switch back to MainScreen
					game.setScreen(parent);
				}
			}
		});
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float dt) {
		stage.act(dt);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

}
