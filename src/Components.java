import java.util.ArrayList;

/**
 * This generic class implements the behvaiour for all the component in the 
 * game using the delegation concept of obaject oriented design.
 * This class stores an ArrayList of all the current active component in the game.
 */
public abstract class Components<T extends Component> {
    private ArrayList<T> Components;
    private ArrayList<T> toRemove;

    /**
     * Creates a new Components instance
     */ 
    public Components () {
        this.Components = new ArrayList<T>();
        this.toRemove = new ArrayList<T>();
    }
    
    /**
     * Returns the Array List of component being stored in this instance
     * @return ArrayList<T> component being stored
     */
    public ArrayList<T> getComponents() {
        return Components;
    }

    /**
     * Abstract method implemented by all subclasses which dictates how 
     * component are spawned.
     */
    public abstract void spawnComponents();

    /**
     * This method adds the input component to list array list of components
     * which are to be removed from the game
     * @param T the component to be removed
     */
    public void addToRemove(T Component) {
        this.toRemove.add(Component);
    }

    /**
     * Remove the components in the "toRemove" array list from the game
     */
    public void removeRequired() {
        for (Component Component: toRemove) {
            Components.remove(Component);
        }
        Components.trimToSize();
        this.toRemove = new ArrayList<T>();
    }

    /**
     * Removes a specific component from the game 
     * @param T the component to remove
     */
    public void removeComponent(T Component) {
        Components.remove(Component);
        Components.trimToSize();
    }

    /**
     * Remove all the components in the game stored in this instance
     */
    public void removeAllComponents() {
        this.Components = new ArrayList<T>();
    }

    /**
     * This method changes the timescale of the components by the given rate.
     * It changes the timescale as described by the project specificaiton which
     * is changing the speed at which the components move to change the speed of
     * the game.
     * @param double the rate at which to change the current speed
     */
    public void changeTimescale (double rate) {
        for (T Component: Components) {
            Component.changeTimescale(rate);
        }
    }

    /**
     * Moves all the components as described in the component class
     */
    public void move() {
        for (T Component: Components) {
            Component.move();
        }
    }

    /**
     * Accelerates the components according to the given acceleration.
     * @param double the magnitude of the acceleration
     */
    public void accelerate(double acceleration) {
        for (T Component: Components) {
            Component.accelerate(acceleration);
        }
    }

    /**
     * Draws each component as described in the component class.
     */
    public void draw() {
        for (T Component: Components) {
            Component.draw();
        }  
    }
}

