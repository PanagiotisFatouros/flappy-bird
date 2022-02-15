import bagel.*;
import bagel.util.*;
import java.lang.Math;
/**
 * This abstract class defines all the behaviour of the weapons rock and bomb.
 * It is a component to the game
 */
public abstract class Weapon extends Component {
    // Set to 3 instaed of 5 so that the game is more playable
    private static final double startSpeed = -3;
    private static final Vector2 startVelocity = new Vector2(1, 0);
    private static final double shotSpeed = 5;
    private boolean pickedUp = false;
    private Image image;
    private int shootingRange;
    private double distanceTraveled;
    private boolean isShot;
    private boolean targetsMetal;

    /**
     * This method creates a new weapon and is called from the bomb/rock class
     * @param Image the image to be rendered for the weapon
     * @param Vector2 the start position of the weapon
     * @param int the distance which the weapon can travel after being shot
     * @param boolean a boolean on whether or not the weapon can destroy metal
     */
    public Weapon(Image image, Vector2 position, int shootingRange, 
            boolean targetsMetal) {
        super(startVelocity, position, 
                startSpeed * Math.pow(ShadowFlap.getSpeedIncreasePerTimescale(),
                                        ShadowFlap.getTimescale() - 1));
        this.image = image;
        this.shootingRange = shootingRange;
        this.targetsMetal = targetsMetal;
    } 

    /**
     * Returns the shooting range of the weapon
     * @return int shootingRange, shooting range of the weapon
     */
    public int getShootingRange() {
        return shootingRange;
    }

    /**
     * Returns a boolean value of whether or not this weapon has been shot by
     * the bird
     * @return boolean isShot
     */
    public boolean isShot() {
        return isShot;
    }

    /**
     * This method is used by the bird class to pick up this particular weapon
     * All the inputs will be so the weapon now travels with the bird on its
     * beak.
     * @param Vector2 the velocity which it should travel at now to stay
     * with the bird
     * @param double the speed which it should travel at now to stay with the bird
     */
    public void pickUp(Vector2 velocity, double speed, Vector2 position) {
        this.pickedUp = true;
        this.setVelocity(velocity);
        this.setSpeed(speed);
        this.setPosition(position);
    }

    /**
     * This method "kills" the weapon meaning it removes it from the game
     */
    @Override
    public void die () {
        ShadowFlap.getWeapons().addToRemove(this);
    }

    /**
     * This method draws the weapon at its current position
     */
    @Override
    public void draw () {
        image.draw(this.getPosition().x, this.getPosition().y);
    }

    /**
     * This method is used by the bird and shoots the weapon as described by the
     * project specificaiton.
     */
    public void shoot () {
        if (pickedUp == true) {
            isShot = true;
            this.setSpeed(shotSpeed);
            this.setVelocity(startVelocity);
            pickedUp = false;    
        }
        // The distance travelled while shot cannot exceed the shooting range
        // of the weapon
        distanceTraveled += Math.abs(this.getSpeed());
        if (distanceTraveled >= shootingRange) {
           this.die();
        }
    }

    /**
     * This method gets a rectangle surrounding the Component
     * @return Rectangle, rectangle surrounding the Component
     */
    public Rectangle getWeaponRectangle() {
        return image.getBoundingBoxAt(this.getPosition().asPoint());
    }

    /**
     * This method determins if the weapon has colided with a pipe, note this does
     * not mean the weapon destroyed the pipe
     * @param Pipe the pipe to check for a collision with
     * @return boolean, a boolean value on if it colided
     */
    public boolean hitsPipe(Pipe pipe) {
        // Checks to see if a rectangle arround the image of the weapon
        // intersects either of the top or bottom rectangles of the pipe
        if ((image.getBoundingBoxAt(
            this.getPosition().asPoint()).intersects(pipe.getPipeRectangles()[0]) 
            || image.getBoundingBoxAt(
            this.getPosition().asPoint()).intersects(pipe.getPipeRectangles()[1]))) {
            return true;
        } return false;
    }
    
    /**
     * Determins if this weapon destroys the pipe or breaks
     * @param pipe the pipe to check
     * @return boolean, if it destroys it or not
     */
    public abstract boolean destroysPipe(Pipe pipe);

    /**
     * This method determins if the weapon has destroyed any of the pipes currently
     * active in the game, if it has then it "kills" the pipe
     * @param Pipes the pipes in the game
     */
    public void destroyedPipe(Pipes pipes) {
        for (Pipe pipe: pipes.getComponents()) {
            if (this.isShot() && this.hitsPipe(pipe)) {
                // If the weapon has the ability to destroy the pipe, destroy
                // the pie, weapon and increment the score of the player
                if (this.destroysPipe(pipe) || this.targetsMetal) {
                    ShadowFlap.incrementScore();
                    pipe.die();
                    this.die();
                    this.isShot = false;
                } 
                // If the weapon hits but cannot destory the pipe, the weapon
                // gets destroyed
                else {
                    this.isShot = false;
                    this.die();
                }
            }
        }
    }
} 
