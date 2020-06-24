package nanocourse;
import java.util.Random;

/**
 * Final Exercise Biomolecular Programming
 * Differentiation of tissues during development
 * 
 * The ExtRandom or Extended Random class is a subclass of the random class. It
 * has features that allow it to be used in conjunction with the Frame class and 
 * make choosing a random location within a frame easier.
 * 
 * @author Thijn Hoekstra
 */
public class ExtRandom extends Random {
    
    /**
     * Pick a random X coordinate in the frame.
     * @param frame which frame to pick a coordinate in.
     * @return a random X coordinate in the frame.
     */
    public double randXInFrame(Frame frame){
        return frame.x0 + (frame.x1 - frame.x0)*nextDouble();
    }
    
    /**
     * Pick a random X coordinate in the frame and also ensure that all points lie
     * within a margin. Useful for drawing circles within a frame where the
     * points closest to the border place the circle tangent to the edges.
     * @param frame which frame to pick a coordinate in.
     * @param radius the radius of the margins (inside).
     * @return a random X coordinate in the frame.
     */
    public double randXInFrame(Frame frame, int radius){
        return frame.x0 + radius + (frame.x1 - frame.x0 - radius*2)*nextDouble();
    }
    
    /**
     * Pick a random Y coordinate in the frame.
     * @param frame which frame to pick a coordinate in.
     * @return a random Y coordinate in the frame.
     */
    public double randYInFrame(Frame frame){
        return frame.y0 + (frame.y1 - frame.y0)*nextDouble();
    }
    
    /**
     * Pick a random Y coordinate in the frame and also ensure that all points lie
     * within a margin. Useful for drawing circles within a frame where the
     * points closest to the border place the circle tangent to the edges.
     * @param frame which frame to pick a coordinate in.
     * @param radius the radius of the margins (inside).
     * @return a random Y coordinate in the frame.
     */
    public double randYInFrame(Frame frame, int radius){
        return frame.y0 + radius + (frame.y1 - frame.y0 - radius*2)*nextDouble();
    }
}
