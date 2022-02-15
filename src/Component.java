import bagel.util.*;
import java.lang.Math;

/**
 * This abstract class dictates the behaviour of all moving components in the 
 * game.
 */
public abstract class Component {
    private final double maxSpeed = 10;
    private Vector2 velocity;
    private Vector2 position;
    private double speed;

    /**
     * Creates a new component
     * @param Vector2 the velocity which the component has
     * @param Vector2 the position at which it starts
     * @param double the speed which the component has
     */
    public Component(Vector2 velocity, Vector2 position, double speed) {
        this.velocity = velocity;
        this.position = position;
        this.speed = speed;
    }

    /**
     * Returns the current position of the component
     * @return Vector2, the current position of the compoennt
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the current position of the component to a new position
     * @param Vector2 the new position of the component
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Returns the velocity of the component
     * @return Vector2, the current velocity of the object
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * Returns the current speed of the object
     * @return double, the current speed of the object
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the current velocity of the component to a new velocity
     * @param Vector2 the new velocity to set it to
     */
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * Sets the current speed of the component to a new speed
     * @param double the new speed to set it to
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * This method is implemented by each component and describes how it "dies"
     * and what its "death" means
     */
    public abstract void die();

    /**
     * Moves the object from it's current position according to its velocity and
     * speed. If the component moves past the left of the screen it "dies" and
     * is removed from the game.
     */
    public void move() {
        if (this.position.x < 0) {
           this.die();
        } 
        this.position = this.position.add(this.velocity.mul(this.speed));
    }

    /**
     * Accelerates the component according to the given acceleration.
     * @param double the magnitude of the acceleration
     */
    public void accelerate(double acceleration) {
        this.speed = Math.min(this.speed + acceleration, maxSpeed);
    }

    /**
     * This method changes the timescale of the component by the given rate.
     * It changes the timescale as described by the project specificaiton which
     * is changing the speed at which the components move to change the speed of
     * the game.
     * @param double the rate at which to change the current speed
     */
    public void changeTimescale (double rate) {
        this.speed *= rate;
    }

    /**
     * This method is to draw each component and is implented indivdually by
     * each component type. 
     */
    public abstract void draw();
} 