Connect 4 Project for an Interview
===

This project is a Connect 4 game with a "player vs player" and "player vs maxmining AI" mode. 
AI is enabled on default.

The first screen is the menu screen where it has 3 buttons:
- SFX Button: Plays the button click sound
- API Button: Displays the weather for San Francisco
- Game: Launches the Connect 4 game

**Implementation Details:**

This project has 2 screens: MainScreen and GameScreen

MainScreen is the first screen that popsup and contains the button to display the weather information from Open Weather Map (API data extraction and parsing is implemented at OpenWeatherMapAPI.java).

GameScreen is the second screen where Connect 4 is played. Pressing "Game" on MainScreen switches over to this screen. 
Clicking a column will drop a disc to the first empty row on that column. 
It has an "AI" button on the right where clicking it will turn off the AI. AI is defaulted ON for each game.

Game logic/controls are implemented at GamePlay.java and the information for the game is held by Disc.java (for game pieces), Cell.java (for the connect 4 table), and GameSettings.java (contains the settings for the game). GameSettings.java's value can be changed to change how the connect 4 game works (more rows/columns, etc).

AI is implemented in AI.java which uses an algorithm where it tries to get the highest profit while minimizing loss. It uses two rounds to find the profits: first to find the maximum points from the possible moves and then subtracts points from anticipating the opponents move in the next round as a result of a possible move the AI made.

Assets for the entire program are handled by Assets.java which uses AssetManager to load and get the assets.

**Screenshots:**

![Main Screen](https://raw.githubusercontent.com/allan3723/Connect-4/master/screenshots/MainScreen.png)
![Game Screen](https://raw.githubusercontent.com/allan3723/Connect-4/master/screenshots/GameScreen.png)
