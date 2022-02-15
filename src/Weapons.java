import bagel.*;
import bagel.util.*;
import java.util.ArrayList;

/**
 * This class implements the behvaiour for all the weapons in the game using
 * the delegation concept of Component oriented design.
 * This class stores an ArrayList of all the current active weapons in the game.
 */
public class Weapons extends Components<Weapon>{
    private static final int startXPosition = Window.getWidth();
    // The closest to the top the weapon can be
    private static final int highestStartYPosition = 100;
    // The closest to the bottom the weapon can be
    private static final int lowestStartYPosition = 500;
    private static final double bombFrequency = 0.5;

    /**
     * Creates a new Weapons instance
     */
    public Weapons () {
        super();
    }

    /**
     * Spawns a new weapon at an allowed position, this weapon is ensured to not
     * overlap with pipes and if it does overlap with pipes it is removed 
     * immediately.
     */
    @Override
    public void spawnComponents() {
        Weapon weapon;
        // Spawn a new weapon with a equal chance of it being a bomb or a rock
        if (ShadowFlap.getLevel() == 1 && Math.random() <= bombFrequency) {
            weapon = new Bomb(new Vector2(startXPosition, 
            (int)(Math.random() * lowestStartYPosition + highestStartYPosition)));
        } else {
            weapon = new Rock(new Vector2(startXPosition,
            (int)(Math.random() * lowestStartYPosition + highestStartYPosition)));
        }
        // Add this to the list of weapons we have
        this.getComponents().add(weapon);    
    }
    
    /**
     * Checks if any weapons have been shot and destoyed a pipe
     * @param Pipes the pipes to check if have been destroyed.
     */
    public void destroyedPipe(Pipes pipes) {
        for (Weapon weapon: this.getComponents()) {  
            // Check only if the weapon has been shot if it destroyed a pipe          
            if (weapon.isShot()) {
                weapon.destroyedPipe(pipes);
            }
        }
    } 

    /**
     * Returns an ArrayList of rectangles surrounding each of the weapons
     * currently active in the game.
     * @return ArrayList<Rectange> rectangles for the weapons
     */
    public ArrayList<Rectangle> getWeaponsRectangles() {
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Weapon weapon: this.getComponents()) {
            rectangles.add(weapon.getWeaponRectangle());
        }
        return rectangles;
    }

    /**
     * Moves all the currently active weapons in the game according to how they
     * are individually described to move in the "Weapon" class.
     */ 
    @Override
    public void move() {
        for (Weapon weapon: this.getComponents()) {
            if (weapon.isShot()) {
                weapon.shoot();
                weapon.destroyedPipe(ShadowFlap.getPipes());
            }
           weapon.move();
        }
    }
}
