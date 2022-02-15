import bagel.*;
import bagel.util.Point;

/**
 * This class implements the life bar which is the line of hearts at the top
 * of the screen as described by the project specification.
 */
public class lifeBar {
    private static final int numLivesLevel0 = 3; // Change to large val to test
    private static final int numLivesLevel1 = 6;
    private static final Point initialHeartPosition = new Point(100, 15);
    private static final int spaceBetweenHearts = 50;
    private int numLives;
    private int totalHearts;
    private static final Image fullHeart = new Image("res/level/fullLife.png");
    private static final Image emptyHeart = new Image("res/level/noLife.png");

    /**
     * This method creates a new lifeBar. The number of lives depends on the level
     * of the game.
     * @param int level of the game
     */
    public lifeBar(int level) {
        if (level == 0) {
            this.numLives = numLivesLevel0;
            this.totalHearts = numLivesLevel0;
        } else { 
            this.numLives = numLivesLevel1;
            this.totalHearts = numLivesLevel1;
        }
    }

    /**
     * This method gets the number of lives remaining
     * @return int numLives, which stores the number of lives remaining
     */
    public int getNumLives() {
        return numLives;
    }

    /**
     * This method implements the death behaviour which simply takes away a life.
     */
    public void die() {
        numLives -= 1;
    }

    /** 
     * This method draws the life bar at it's correct position.
     */
    public void draw() {
        int heartNumber;
        // Draw full hearts for the number of lives we have
        for (heartNumber = 0; heartNumber < numLives; heartNumber ++) {
            fullHeart.draw(initialHeartPosition.x + spaceBetweenHearts * heartNumber, 
                        initialHeartPosition.y);
        }
        // Draw the lives we have lost as empty hearts
        for (; heartNumber < totalHearts; heartNumber ++) {
            emptyHeart.draw(initialHeartPosition.x + spaceBetweenHearts * heartNumber, 
                        initialHeartPosition.y);
        }
    }

} 
