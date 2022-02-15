import bagel.*;
import bagel.util.*;

/**
 * This class defines the behvaiour of the bird in the game.
 */
public class Bird extends Component {
    private static final Vector2 startVelocity = new Vector2(0, 1);
    private static final Vector2 startPosition = new Vector2(200, 350);
    private static final int startSpeed = 0;
    private static final int framesBetweenWingsUp = 10;
    private Weapon weapon = null;
    private static final Image wingsUpImageLvl0 =
                                    new Image("res/level-0/birdWingUp.png");
    private static final Image wingsDownImageLvl0 =
                                    new Image("res/level-0/birdWingDown.png");

    private static final Image wingsUpImageLvl1 =
                                    new Image("res/level-1/birdWingUp.png");
    private static final Image wingsDownImageLvl1 =
                                    new Image("res/level-1/birdWingDown.png");
    private static Image wingsUpImage;
    private static Image wingsDownImage;
    private Rectangle rectangle;

    /**
     * Creates a new bird
     */
    public Bird () {
        super(startVelocity, startPosition, startSpeed);
        wingsUpImage = wingsUpImageLvl0;
        wingsDownImage = wingsDownImageLvl0;
    }

    /**
     * Implements the "death" behaviour of pipes which is that it looses a life
     * and then is removed from the game if its lives are out. It also drops
     * any weapon it is holding when it dies.
     */
    @Override
    public void die () {
        // Remove the weapon if it has one
        try {
            ShadowFlap.getWeapons().addToRemove(this.weapon);
        } catch (Exception NullPointerException) {
            // Do nothing
        }
        this.weapon = null;
        // Loose a life
        ShadowFlap.getLives().die();
    }

    /**
     * Respawns the bird by resetting it's speed and position back to the
     * initial.
     */
    public void respawn () {
        this.setSpeed(startSpeed);
        this.setPosition(startPosition);
    }

    /**
     * Returns the weapon it is holding if it is holding one.
     * @return Weapon, held weapon.
     */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Changes the images of the bird to be the images for the level above what
     * it is currently.
     */
    public void levelUp() {
        wingsDownImage = wingsDownImageLvl1;
        wingsUpImage = wingsUpImageLvl1;
        this.respawn();
    }

    /**
     * Draws the bird at it's current position, every time between a specified
     * number of frames the bird is drawn flapping its wings.
     */
    @Override
    public void draw() {
        // Every given number of frames the bird should be drawn with its
        // wings up to seem like it is flapping its wings
        if (ShadowFlap.getCountFrames() % framesBetweenWingsUp == 0) {
            wingsUpImage.draw(this.getPosition().x, this.getPosition().y);
        }
        // Otherwise it is drawn without flapping its wings
        else {
            wingsDownImage.draw(this.getPosition().x, this.getPosition().y);
        }
    }

    /**
     * Determins and returns whether the bird is out of bounds of the game
     * @return boolean, the out of bounds status
     */
    public boolean outOfBounds() {
        return this.getPosition().y < 0  ||
                                    this.getPosition().y > Window.getHeight();
    }

    /**
     * Shoots it's weapon if it is currently holding one. The shooting behaviour
     * is defined in the weapon class.
     */
    public void shoot() {
        if (weapon != null) {
            weapon.shoot();
            weapon = null;
        }
    }

    /**
     * Determins whether the bird has colided with any of the pipes in the game.
     * The pipe is removed if it has.
     * @param Pipes the pipes in the game
     * @return boolean, whether it has colided with a pipe
     */
    public boolean hasCollided(Pipes pipes) {
        boolean intersectsPipes = false, intersectsFlames = false;
        rectangle = this.getBirdRectange();

        // For each of the pipes check if the bird has hit the pipes or
        // the flames (if they are active)
        for (Pipe pipe: pipes.getComponents()) {
            intersectsPipes = rectangle.intersects(pipe.getPipeRectangles()[0])
                           || rectangle.intersects(pipe.getPipeRectangles()[1]);

            if (pipe instanceof SteelPipe && ShadowFlap.getCountFrames() %
                SteelPipe.getFramesBetweenFlameSpawn() < 
                SteelPipe.getFlameDuration()) {
                intersectsFlames = 
                            rectangle.intersects(((SteelPipe) pipe).getFlameRectangles()[0])
                        ||  rectangle.intersects(((SteelPipe) pipe).getFlameRectangles()[1]);
            }

            // If it has hit, the pipe dies
            if (intersectsPipes || intersectsFlames) {
                pipe.die();
                return true;
            }
        }
        return intersectsPipes || intersectsFlames;
    }

    /**
     * Returns a rectangle surrounding the bird.
     * @return Rectangle
     */
    public Rectangle getBirdRectange() {
        return wingsDownImage.getBoundingBoxAt(this.getPosition().asPoint());
    }

    /**
     * This method allows the bird to  "pick up" any weapon in the game it is
     * currently touching if it does not already have a weapon and that weapon
     * has not already been shot
     * @param Weapons all the weapons in the game
     */
    public void pickUpWeapon(Weapons weapons) {
        // If we have a weapon we can't pick up another
        if (weapon != null) {
            return;
        }
        // Try to pick up all active weapons
        for (Weapon weapon: weapons.getComponents()) {
            if (weapon.getWeaponRectangle().intersects(this.getBirdRectange())
                    && !weapon.isShot()) {
                this.weapon = weapon;
                weapon.pickUp(this.getVelocity(), this.getSpeed(), 
                                this.getBirdRectange().topRight().asVector());
            }
        }
    }

    /**
     * This method checks to see if the bird has passed any more of the current
     * pipes in the game.
     * @param Pipes the pipes in the game
     * @return boolean. If it passed a pipe.
     */
    public boolean passedPipe(Pipes pipes) {
        for (Pipe pipe: pipes.getComponents()) {
            // Check to see if we are passed the right side of the pipe and that
            // we have not already passed it
            if (pipe.getTopRightCornerBottomRectangle().x <= this.getPosition().x
                && !pipe.isPassed()) {
                pipe.pass();
                return true;
            }
        }
        return false;
    }
}