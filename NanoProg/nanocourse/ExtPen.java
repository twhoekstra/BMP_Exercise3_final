package nanocourse;
import java.awt.Color;
import nano.Pen;
import nano.Canvas;

/**
 * Final Exercise Biomolecular Programming
 * Differentiation of tissues during development
 * 
 * The ExtPen or Extended Pen class is a subclass of Pen and features methods to
 * draw Floaters and Frames.
 * 
 * @author Thijn Hoekstra
 */
public class ExtPen extends Pen {
    /**
     * Creates an extended pen. Identical to "regular" pen constructor.
     * @param canvas which canvas to place the pen on.
     */
    public ExtPen(Canvas canvas) {
        super(canvas);
    }
    
    /**
     * Takes a floater and draws it on the canvas the pen is set to.
     * @param floater which floater to draw.
     */
    public void drawFloater(Floater floater){
        drawCircle(
                (int) floater.getX(), 
                (int) floater.getY(), 
                (int) floater.getRadius(), 
                floater.getColor(), 
                true);
    }
    
    /**
     * Takes a frame and draws it on the canvas the pen is set to.
     * @param frame which frame to draw.
     * @param color what color to draw this frame in.
     */
    public void drawFrame(Frame frame, Color color){
        drawRectangle(
                (int) frame.getX0(), 
                (int) frame.getY0(),
                (int) (frame.getX1() - frame.getX0()),
                (int) (frame.getY1() - frame.getY0()), 
                color, true);
    }
}
