package nanocourse;

import java.awt.Color;

/**
 * Final Exercise Biomolecular Programming Differentiation of tissues during
 * development
 *
 * Morphogen is a subclass of Floater. It can diffuse just like a floater, but
 * can also decay and be sensed by Cells.
 *
 * @author Thijn Hoekstra
 */
public class Morphogen extends Floater {

    int decayChance = 500;  // The probability of decaying is 1/"decayChance"

    /**
     * Creates a non-existing morphogen, ready to be used when a cell produces a
     * morphogen.
     *
     * @param id a unique integer used for identification.
     * @param speed how fast the morphogen diffuses.
     * @param radius the size of the morphogen
     */
    public Morphogen(int id, double speed, int radius) {
        super(id, speed);
        super.radius = radius;
        super.color = Color.GREEN;
    }

    /**
     * The state machine determines what the morphogen does. Apart from
     * diffusing, the morphogen can only decay. This is handled by the state
     * machine.
     *
     * @return if the morphogen is going to decay
     */
    public boolean stateMachine() {
        if (exists) {
            if (rnd.nextInt(decayChance) == 1) {
                //Main program will handle killing this cell
                return true;
            }
        }
        return false;
    }

    /**
     * Calling this method activates a non-existing morphogen and transforms it
     * into an existing one.
     * @param x the X position of the new morphogen.
     * @param y the Y position of the new morphogen. These are used to spawn a
     * morphogen beneath the cell.
     */
    public void animate(double x, double y) { 
        this.x = x;
        this.y = y;
        exists = true;
    }
}
