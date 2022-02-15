import bagel.*;
import bagel.util.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class implements the behvaiour for all the pipes in the game using
 * the delegation concept of Component oriented design.
 * This class stores an ArrayList of all the current active pipes in the game.
 */
public class Pipes extends Components<Pipe> {
    private static final int startXPosition = Window.getWidth();
    // The closest to the top the middle of the pipes can be
    private static final int highestStartYPosition = 184;
    // The closest to the bottom the middle of the pipes can be
    private static final int lowestStartYPosition = 401;
    private static int[] levelZeroInitialYPositions = {184, 384, 584};
    private static final double steelPipeFrequency = 0.5;
    
                    
    /**
     * Creates a new Pipes instance
     */
    public Pipes () {
        super();
    }

    /**
     * Spawns a new pipe at an allowed position which are determined according 
     * to the current level of the game.
     */
    @Override
    public void spawnComponents() {
        Pipe pipe;
        Vector2 startPosition;
        // Pick random startY position of the possible in level 1
        if (ShadowFlap.getLevel() == 1) {
            startPosition = new Vector2(startXPosition, 
            (int)(Math.random() * lowestStartYPosition + highestStartYPosition));
            
            if (Math.random() <= steelPipeFrequency) {
                pipe = new SteelPipe(startPosition);
            } else {
                pipe = new PlasticPipe(startPosition);
            }
        }
        // Pick random startY position of the possible in level 0 
        else {
            startPosition = new Vector2(startXPosition, levelZeroInitialYPositions[
                    new Random().nextInt(levelZeroInitialYPositions.length)]);
            pipe = new PlasticPipe(startPosition);
        }
        this.getComponents().add(pipe);
    }

    /**
     * Returns an ArrayList of rectangles (2 for each pipe, one for top one for 
     * bottom) surrounding each of the pipes currently active in the game.
     * @return ArrayList<Rectange[]> rectangles for the pipes
     */
    public ArrayList<Rectangle[]> getPipesRectangles() {
        ArrayList<Rectangle[]> rectangles = new ArrayList<Rectangle[]>();
        for (Pipe pipe: this.getComponents()) {
            rectangles.add(pipe.getPipeRectangles());
        }
        return rectangles;
    }

    /**
     * Returns an ArrayList of rectangles (2 for each flame, one for top one for 
     * bottom) surrounding each of the flames currently active in the game.
     * @return ArrayList<Rectange[]> rectangles for the flames
     */
    public ArrayList<Rectangle[]> getFlameRectangles() {
        ArrayList<Rectangle[]> rectangles = new ArrayList<Rectangle[]>();
        for (Pipe pipe: this.getComponents()) {
            if (pipe instanceof SteelPipe) {
                rectangles.add(((SteelPipe) pipe).getFlameRectangles());
            }
        }
        return rectangles;
    }
}
