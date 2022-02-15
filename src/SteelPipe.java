import bagel.*;
import bagel.util.*;

/**
 * This class defines the unique (from plastic) parts and behvaiour of steel 
 * pipes.
 */
public class SteelPipe extends Pipe {
    private static final Image image = new Image("res/level-1/steelPipe.png");
    private static final Image flames = new Image("res/level-1/flame.png");
    private static final int framesBetweenFlameSpawn = 20;
    private static final int flameDuration = 3;
    private Rectangle flameRectangles[];

    /**
     * Creates a new steel pipe
     * @param position the position it should spawn at
     */
    public SteelPipe(Vector2 position) {
        super(position, image);
        this.flameRectangles = new Rectangle[2];
    }

    /**
     * Returns the number of frames between which flames spawn on a steel pipe
     * @return int, the frames between the flames spawning
     */
    public static int getFramesBetweenFlameSpawn() {
        return framesBetweenFlameSpawn;
    }

    /**
     * Returns the number of frames for which flames are displayed
     * @return int, flame duration
     */
    public static int getFlameDuration() {
        return flameDuration;
    }

    /**
     * This method gets 2 rectangles surrounding the flames on the top and 
     * bottom of the steel pipe
     * @return Rectangle[], index 0 is the top flame, index 1 is the bottom
     * flame
     */
    public Rectangle[] getFlameRectangles() {
        Point p1 = new Point(this.getPosition().x, this.getPosition().y - 
                            (this.getGap() - flames.getHeight()) / 2);
        Point p2 = new Point(this.getPosition().x, this.getPosition().y + 
                            (this.getGap() - flames.getHeight()) / 2);
        flameRectangles[0] = flames.getBoundingBoxAt(p1);
        flameRectangles[1] = flames.getBoundingBoxAt(p2);
        return flameRectangles;
    }

    /**
     * This method draws flames every given number frames for some length
     * defined in the constants framesBetweenFlameSpawn and flameDuration
     * such that they look nice when spawning but the game is still playable.
     * They are drawn directly ontop of the pipes, I did not
     * place a gap between the pipe and flame as it makes the smaller pipe
     * gaps impossible to fit through when flames are active 
    */
    public void drawFlames() {
        if (ShadowFlap.getCountFrames() % framesBetweenFlameSpawn < 
            flameDuration) {
            // Render the center of the flames ontop of the pipes
            flames.draw(this.getPosition().x, this.getPosition().y - 
                        (this.getGap()- flames.getHeight()) / 2);
            // Render the flame on the pipe pipe flipped so it is the right
            // way up            
            flames.draw(this.getPosition().x, this.getPosition().y + 
            (this.getGap() - flames.getHeight()) / 2, 
            this.getDrawingOptions().setRotation(this.getRotation()));
        }
    }
}
 