import bagel.*;
import bagel.util.*;
import java.lang.Math;

/**
 * This class defines the behvaiour of all individual pipes in the game as an 
 * abstract class which is inherrited by the specific pipe types.
 */
public abstract class Pipe extends Component {
    private final int gap = 168;
    // Set to 3 instaed of 5 so that the game is more playable
    private static final double startSpeed = -3;
    private final double rotation = Math.PI;
    private static final Vector2 startVelocity = new Vector2(1, 0);
    private boolean passed = false;
    private Image image;
    private DrawOptions drawingOptions;
    private Rectangle rectangles[];
 
    /**
     * Creates a new pipe
     * @param boolean a boolean variable on whether the pipe is steel
     * @param Vector2 the position the middle of the pipes should spawn at
     */
    public Pipe(Vector2 position, Image image) {
        super(startVelocity, position, startSpeed * 
                            Math.pow(ShadowFlap.getSpeedIncreasePerTimescale(), 
                                            ShadowFlap.getTimescale() - 1));
        // Set the images depending on the type of pipe
        this.image = image;
        this.drawingOptions = new DrawOptions();
        this.rectangles = new Rectangle[2];
    }

    /**
     * Returns the gap between the pipes
     * @return int, gap between pipes
     */
    public int getGap() {
        return gap;
    }

    /**
     * Returns the rotion for the items drawn on the bottom
     * @return double, the rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Returns the drawing options object to be able to use for drawing in 
     * other subclasses
     * @return DrawOptions
     */
    public DrawOptions getDrawingOptions() {
        return drawingOptions;
    }

    /**
     * Returns whether or not this pipe has been passed by the bird
     * @return boolean, passed
     */
    public boolean isPassed() {
        return this.passed;
    }

    /**
     * Updates this pipe to being passed by the bird
     */
    public void pass () {
        this.passed = true;
    }

    /**
     * Implements the "death" behaviour of pipes which is that it is removed
     * from the game. Respawn is always false here.
     */
    @Override
    public void die () {
        ShadowFlap.getPipes().addToRemove(this);
    }

    /**
     * Returns the y coordinate the top pipe's center should be drawn at
     * @return double, coordinate to draw at
     */
    private double topPipePosition() {
        return this.getPosition().y - (gap + this.image.getHeight()) / 2;
    }

    /**
     * Returns the y coordinate the bottom pipe's center should be drawn at
     * @return double, coordinate to draw at
     */
    private double bottomPipePosition() {
        return this.getPosition().y + (gap + this.image.getHeight()) / 2;
    }

    /**
     * Retrieves and returns the top right hand corner of the rectangle
     * which surrounds the bottom pipe.
     * @return Point, the top right hand corner of the bottom rectangle
     */
    public Point getTopRightCornerBottomRectangle() {
        Point p = new Point(this.getPosition().x, this.bottomPipePosition());
        return this.image.getBoundingBoxAt(p).topRight();
    }

    /**
     * This method gets 2 rectangles surrounding the top and bottom of the
     * Component
     * @return Rectangle[], index 0 is the top rectangle, index 1 is the bottom
     * rectangle 
     */
    public Rectangle[] getPipeRectangles() {
        Point p1 = new Point(this.getPosition().x, topPipePosition());
        Point p2 = new Point(this.getPosition().x, bottomPipePosition());
        rectangles[0] = this.image.getBoundingBoxAt(p1);
        rectangles[1] = this.image.getBoundingBoxAt(p2);
        return rectangles;
    }

    /**
     * This method draws the pipe at its current position, both the top and
     * bottom. It also draws the flames at the frame interval and for the
     * duration outlined in framesBetweenFlameSpawn and flameDuration 
     * respectively.
     */
    @Override
    public void draw() {
        // Draws the top and bottom pipes so the middle of their gap is at the
        // correct position
        this.image.draw(this.getPosition().x, topPipePosition());

        this.image.draw(this.getPosition().x, bottomPipePosition(), 
                    drawingOptions.setRotation(rotation));  
        
        // If the pipe is steel then draw the flames
        if (this instanceof SteelPipe) {
            ((SteelPipe) this).drawFlames();
        }
    }
}
