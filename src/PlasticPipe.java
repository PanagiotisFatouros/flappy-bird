import bagel.*;
import bagel.util.*;

/**
 * This class defines the unique (from steel) parts and behaviour of plastic 
 * pipes.
 */
public class PlasticPipe extends Pipe {
    private static final Image image = new Image("res/level/plasticPipe.png");

    /**
     * Creates a new steel pipe
     * @param position the position it should spawn at
     */
    public PlasticPipe(Vector2 position) {
        super(position, image);
    }
}
