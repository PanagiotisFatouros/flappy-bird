import bagel.*;
import bagel.util.*;

/**
 * Code for SWEN20003 Project 2, Semester 2, 2021 This implements the Shadow
 * Flap game design
 * 
 * Please filling your name below
 * 
 * @author: Panagiotis Fatouros (1170920)
 */
public class ShadowFlap extends AbstractGame {
    private static final int windowLength = 1024;
    private static final int windowHeight = 768;
    private final double gravity = 0.4;
    private final double speedWhileFlying = -6;
    private final int fontSize = 48;
    private final int scoreEndsLevel0 = 10;
    private final int scoreEndsLevel1 = 30;
    private static final int finalScoreVerticalAdjustment = 75;
    private static final int pressToShootVerticalAdjustment = 68;
    private static int countFrames = 0;
    private static int level;
    private static lifeBar lives;
    private static boolean displayLevelUp = false;
    private static int timescale = 1;
    private static final double speedIncreasePerTimescale = 1.5;
    private Image background;
    private static Bird bird;
    private static Pipes pipes;
    private static Weapons weapons;
    private Font font;
    private boolean inGame = false;
    private boolean finishedGame = false;
    private boolean lost = false;
    private static int score = 0;
    private static double distanceSinceLastPipe;
    private static final int maxTimeScale = 5;
    private static final int minTimeScale = 1;
    private static final Point scoreTextPosition = new Point(100, 100);
    public static final double weaponSpawningChance = 0.01;
    public static final int distanceBetweenConsecutivePipes = 300;
    private static final int levelUpScreenDuration = 150;

    /**
     * Returns the current frame count of the game.
     * 
     * @return int, the current frame count
     */
    public static int getCountFrames() {
        return countFrames;
    }

    /**
     * Returns how much the speed should increase by when increasing timescale by 1
     * 
     * @return double, the increase required
     */
    public static double getSpeedIncreasePerTimescale() {
        return speedIncreasePerTimescale;
    }

    /**
     * Sets the count of the frames to a given value
     * 
     * @param int the value to set count frames to
     */
    public static void setCountFrames(int countFrames) {
        ShadowFlap.countFrames = countFrames;
    }

    /**
     * Returns the instance of Weapons in game.
     * 
     * @return Weapons, the instance of Weapons
     */
    public static Weapons getWeapons() {
        return weapons;
    }

    /**
     * Returns the bird in the game.
     * 
     * @return Bird, bird of the game
     */
    public static Bird getBird() {
        return bird;
    }

    /**
     * Returns the instance of Pipes in game.
     * 
     * @return Pipes, the instance of Pipes
     */
    public static Pipes getPipes() {
        return pipes;
    }

    /**
     * Increments the game score by 1
     */
    public static void incrementScore() {
        score += 1;
    }

    /**
     * Returns the current level the game is at
     * 
     * @return int, the current level
     */
    public static int getLevel() {
        return level;
    }

    /**
     * The current timescale of the game from 1-5
     * 
     * @return int, the current timescale
     */
    public static int getTimescale() {
        return timescale;
    }

    /**
     * Returns the lifebar of the game
     * @return lifeBar, the lifebar of the game
     */
    public static lifeBar getLives() {
        return lives;
    }

    /**
     * This method prints a given text with it's center at the point x, y
     * 
     * @param String the text to be printed
     * @param double the x-coordinate of the center for the text
     * @param double the y-coordinate of the center for the text
     */
    private void printTextCentered(String text, double x, double y) {
        font.drawString(text, x - font.getWidth(text) / 2, y + fontSize / 4);
    }

    /**
     * Creates a new instance of the Shadow Flap game
     */
    public ShadowFlap() {
        super(windowLength, windowHeight, "Flappy Bird is Back!");
        background = new Image("res/level-0/background.png");
        bird = new Bird();
        pipes = new Pipes();
        weapons = new Weapons();
        font = new Font("res/font/slkscr.ttf", fontSize);
        level = 0;
    }

    /**
     * Remove all the pipes and weapons in the game
     */
    private void removeAllComponents() {
        pipes.removeAllComponents();
        weapons.removeAllComponents();
    }

    /**
     * Move all the components active in the game
     */
    private void moveAll() {
        pipes.move();
        weapons.move();
        bird.move();
        bird.accelerate(gravity);
        try {
            bird.getWeapon().accelerate(gravity);
        } catch (Exception NullPointerException) {
            // Do nothing the bird has not picked up a weapon
        }
    }

    /**
     * Remove all the pipes and weapons which have died in the game
     */
    private void removeRequiredComponents() {
        weapons.removeRequired();
        pipes.removeRequired();
    }

    /**
     * Draw everything active in the game while it is being played
     */
    private void drawAll() {
        bird.draw();
        pipes.draw();
        weapons.draw();
        lives.draw();
        font.drawString("SCORE: " + score, scoreTextPosition.x, scoreTextPosition.y);
    }

    /**
     * Move and draw everything active in the game while it is being played
     * @param Input input from the player
     */
    private void moveAndDraw(Input input) {
        if (input.isDown(Keys.SPACE)) {
            // Fly upwards at speed of 6 pixels per frame
            bird.setSpeed(speedWhileFlying);
            try {
                bird.getWeapon().setSpeed(speedWhileFlying);
            } catch (Exception NullPointerException) {
                // Do nothing
            }
        }
        // Remove any pipes and weapons that are required to be removed
        removeRequiredComponents();
        // Move everything
        moveAll();
        // Draw everything
        drawAll();
    }

    /**
     * Spawn a new pipe in the game 
     */
    private void spawnPipes() {
        pipes.spawnComponents();
        distanceSinceLastPipe = 0;
    }

    /**
     * Spawn a new weapon into the game
     */
    private void spawnWeapons() {
        // Spawn a new weapon with given chance in level 1
        if (Math.random() < weaponSpawningChance && level == 1) {
            weapons.spawnComponents();
        }

        // Remove any weapons that are now overlapping with pipes
        for (Weapon weapon : weapons.getComponents()) {
            for (Pipe pipe : ShadowFlap.getPipes().getComponents()) {
                if (weapon.hitsPipe(pipe)) {
                    weapons.addToRemove(weapon);
                }
            }
        }
    }

    /**
     * Checks interactions between components.
     * Namely picking up weapons, weapon hitting a pipe, bird being
     * out of bounds or if it has passed a pipe
     */
    private void interactionChecks() {
        bird.pickUpWeapon(weapons);
        weapons.destroyedPipe(pipes);
        if (bird.hasCollided(pipes) || bird.outOfBounds()) {
            bird.die();

            if (lives.getNumLives() == 0) {
                lost = true;
                inGame = false;
            } else if (bird.outOfBounds()) {
                bird.respawn();
            }
        }

        if (bird.passedPipe(pipes) && !lost) {
            score += 1;
        }
        // Again remove any required weapons and pipes
        removeRequiredComponents();
    }

    /**
     * Check all the inputs from the player to change game controls except that
     * which involves the movement of the bird (flying).
     * @param Input input from the player
     */
    private void inputChecks(Input input) {
        // Decrease timescale if possible and input was pressed
        if (input.wasPressed(Keys.K) && timescale > minTimeScale) {
            timescale -= 1;
            pipes.changeTimescale(1 / speedIncreasePerTimescale);
            weapons.changeTimescale(1 / speedIncreasePerTimescale);
        }
        // Increase timescale if possible and input was pressed
        else if (input.wasPressed(Keys.L) && timescale < maxTimeScale) {
            timescale += 1;
            pipes.changeTimescale(speedIncreasePerTimescale);
            weapons.changeTimescale(speedIncreasePerTimescale);
        }
        // Shoot weapon for bird
        if (input.wasPressed(Keys.S)) {
            bird.shoot();
        }
        // Close game
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    /**
     * Everything to be done if the game is lost
     */
    private void gameLostUpdates() {
        // Keeping moving the bird until it hits the ground
        if (bird.getPosition().y < windowHeight) {
            bird.accelerate(gravity);
            bird.move();
        }

        // Draw the bird and pipes
        bird.draw();
        pipes.draw();

        // Print messages
        printTextCentered("GAME OVER!", windowLength / 2, windowHeight / 2);
        printTextCentered("FINAL SCORE: " + score, windowLength / 2, 
                                windowHeight / 2 + finalScoreVerticalAdjustment);
    }

    /**
     * Everything to be done when the game is won.
     */
    private void printEndScreen() {
        // Print end screen
        printTextCentered("CONTRATULATIONS!", windowLength / 2, windowHeight / 2);
        printTextCentered("FINAL SCORE: " + score, windowLength / 2, 
                                windowHeight / 2 + finalScoreVerticalAdjustment);
    }

    /**
     * Levels the game up from 0 to 1, from here the game will display
     * the level up screen
     */
    private void levelUp() {
        displayLevelUp = true;
        countFrames = 0;
        bird.levelUp();
        level = 1;
        removeAllComponents();
        inGame = false;
    }

    /**
     * Upon pressing space the game will start
     */
    private void startGame() {
        inGame = true;
        lives = new lifeBar(level);
        countFrames = 0;
        distanceSinceLastPipe = 0;
        pipes.spawnComponents();
    }

    /**
     * Messages to be displayed while waiting t
     */
    private void startScreenMessages() {
        // Print required messages
        printTextCentered("PRESS SPACE TO START", windowLength / 2, windowHeight / 2);
        if (level == 1) {
            printTextCentered("PRESS 'S' TO SHOOT", windowLength / 2,
                    windowHeight / 2 + pressToShootVerticalAdjustment);
        }
    }

    /**
     * Everything to be done during the level up screen.
     */
    private void levelUpScreen() {
        printTextCentered("LEVEL-UP!", windowLength / 2, 
        windowHeight / 2);
        printTextCentered("SCORE: " + score, windowLength / 2, 
        windowHeight / 2 + finalScoreVerticalAdjustment);
        // Once the duration for which the screen should be printed is over.
        // We change the background and set the boolean variable which says to
        // display this to false.
        if (countFrames == levelUpScreenDuration) {
            displayLevelUp = false;
            // Update background once we level up
            background = new Image("res/level-1/background.png");
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update of the game. Possibly using an input.
     * Allows the game to exit when the escape key is pressed.
     * @param Input the input from the user
     */
    @Override
    public void update(Input input) {
        background.draw(windowLength / 2, windowHeight / 2);
        countFrames += 1;

        // Display the level up screen
        if (displayLevelUp) {
            levelUpScreen();
        }

        // If we are waiting to start
        if (!inGame && !finishedGame && !lost && !displayLevelUp) {
            startScreenMessages();
            
            // Start the game upon pressing space
            if (input.wasPressed(Keys.SPACE)) {
                startGame();
            }
        }

        // Checks for passing level 0
        if (inGame && level == 0 && score == scoreEndsLevel0) {
            levelUp();
        }

        // Checks for passing level 1
        if (inGame && level == 1 && score == scoreEndsLevel1) {
            finishedGame = true;
        }

        if (inGame && !finishedGame) {
            if (distanceSinceLastPipe >= distanceBetweenConsecutivePipes) {
                spawnPipes();
            }
            if (level == 1) {
                spawnWeapons();
            }
            moveAndDraw(input);
        
            interactionChecks();
            distanceSinceLastPipe += Math.abs(pipes.getComponents().get(0).getSpeed());  
        } 

        // If the game was lost
        else if (lost) {
            gameLostUpdates();
        } 

        // If the game was finished succesfully
        else if (finishedGame) {
            printEndScreen();
        }

        inputChecks(input);
    }
}
