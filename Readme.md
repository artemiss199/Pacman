# PAC-MAN OOP Project
**Author:** Roghaye Saadabadi (Student ID: 404463707)
**Repository:** [https://github.com/artemiss199/Pacman](https://github.com/artemiss199/Pacman)

This repository contains an Object-Oriented Programming implementation of the classic Pac-Man arcade game.

## How to Run the Project
* You can run the project by easily cloning the repository.
* After cloning, execute the `main.java` file to start the game.

## Development Environment
I highly recommend you to use my setup to avoid any problem in running the project however running this project on other versions shouldn't make so much difference
* **Java Version:** OpenJDK 25.0.1
* **IDE:** IntelliJ IDEA 2025.2

## Overview & Features
The game architecture is built using several core classes: logic.Game, logic.Pacman, logic.Ghost, logic.Maze, logic.Pellet, and logic.ScoreManager. It includes a fully functional main menu that handles starting the game, exiting, selecting the game mode (difficulty), and selecting the level.

### Controls & Gameplay
* **Movement:** You can both control the logic.Pacman using WASD keys or arrow keys.
* **Collision Rules:** logic.Pacman and ghosts cannot bypass walls or the border defined in the map. You would stop moving on collision with walls, and ghosts choose a new direction on collision with walls. Only moving in allowed directions is permitted.
* **Pausing:** You can also pause the game using the escape key.
* **Ending:** You should either collide with a normal ghost (not frightened) to lose, or collect all beans to win.

### Scoring System
* **Movement Penalty:** Each key press would lead in one negative score (-1 point).
* **Collecting Beans:** Collecting beans using logic.Pacman would lead in bean deletion and adds 10 points to the score.
* **Victory Bonus:** Adding 500 scores on collecting all beans.
* **Eating Ghosts:** You can receive 200 scores by eating a frightened ghost. Ghosts would jump back to their spawn point immediately after being eaten.
* **Persistence:** We store the highest score of the player in a `highscore.txt` file in the main project folder. The score is shown during the game.

## Advanced logic.Ghost AI
The game utilizes advanced tracking algorithms to control the enemies.
* They can move wisely or randomly based on the difficulty level.
* We use the A* algorithm with a bit of inaccuracy and mis-prediction (dumb acting) based on the difficulty level.
* If you want to use 100% accuracy, just put the difficulty on Hard in the main menu. On Hard Mode, they use the A* algorithm and they are nearly unbeatable.
* You can find the fully explained implementation in the `logic.Ghost.java` file with explanation on the code.

## Custom Level Design
You can define your own custom map in the `levels` folder. Levels are defined using ASCII characters. You can have as many ghosts as you want by placing the corresponding letters in the text file:
* `#` = wall
* `M` = logic.Pacman Spawn Point
* `.` = Normal Bean
* `O` = Super logic.Pellet (printed as apple fruit)
* `P` = Pinky logic.Ghost
* `I` = Cyan Blue logic.Ghost
* `C` = Orange logic.Ghost
* `B` = Red logic.Ghost (Blinky)

*(Note: Power pellets are represented as apples in the game; you can eat them and make ghosts frightened.)*

## Audio & Assets
* **Graphics:** All graphical assets can be found in the `Assets` folder under the main directory of the project.
* **Sound Effects (SFX):** All sound effects can be found in the `Assets/sfx` folder. We have sfx for the main menu, game start, logic.Pacman eating beans, logic.Pacman eating a ghost, and losing.