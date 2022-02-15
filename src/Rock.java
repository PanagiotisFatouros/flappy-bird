import bagel.*;
import bagel.util.*;

public class Rock extends Weapon {
    private static final Image image = new Image("res/level-1/rock.png");
    private static final int shootingRange = 25;
    private static final boolean targetsMetal = false;

    /**
     * This creates a rock object
     * @param Vector2 the position the rock should start at
     */
    public Rock(Vector2 position) {
        super(image, position, shootingRange, targetsMetal);
    }

    /**
     * Determins if this weapon destroys the pipe or breaks
     * @param pipe the pipe to check
     * @return boolean, if it destroys it or not, rocks can only destroy plastic
     * pipes
     */
    @Override
    public boolean destroysPipe(Pipe pipe) {
        return pipe instanceof PlasticPipe;
    }
}
 