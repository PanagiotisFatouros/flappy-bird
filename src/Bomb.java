import bagel.*;
import bagel.util.*;
/**
 * This class is used to represent the weapon type bomb
 */
public class Bomb extends Weapon {
    private static final Image image = new Image("res/level-1/bomb.png");
    private static final int shootingRange = 50;
    private static final boolean targetsMetal = true;

    /**
     * This creates a bomb object
     * @param Vector2 the position the bomb should start at
     */
    public Bomb(Vector2 position) {
        super(image, position, shootingRange, targetsMetal);
    }
    /**
     * Determins if this weapon destroys the pipe or breaks
     * @param pipe the pipe to check
     * @return boolean, bombs destroy plastic and steel pipe so if it is either
     * of these it will destroy it
     */
    @Override
    public boolean destroysPipe(Pipe pipe) {
        return pipe instanceof PlasticPipe || pipe instanceof SteelPipe;
    }
}

