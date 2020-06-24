package nanocourse;

/**
 * Final Exercise Biomolecular Programming
 * Differentiation of tissues during development
 * 
 * The Frame object is a rectangle in space. It can be displayed on screen but it
 * can also be used as a bounding box for floaters.
 * 
 * @author Thijn Hoekstra
 */
public class Frame {
    double x0;
    double y0;
    double x1;
    double y1;
    
    /**
     * Creates a rectangular frame between two points with coordinates (X0, Y0)
     * and (X1, Y1).
     * @param x0_start the X coordinate of the first point.
     * @param y0_start the Y coordinate of the first point.
     * @param x1_start the X coordinate of the second point.
     * @param y1_start the Y coordinate of the second point.
     */
    public Frame(double x0_start, double y0_start, double x1_start, double y1_start){
        //Note that x0 < x1 and y0 > y1
        this.x0 = x0_start;
        this.y0 = y0_start;
        this.x1 = x1_start;
        this.y1 = y1_start;
        
        // Some warnings
        if (x0 > x1){
            System.out.println("Warning. A frame constructed with first x input "
                    + "larger than the second input might lead to irregular "
                    + "behaviour when frame is used for collision detection");
        }
        if (x0 > x1){
            System.out.println("Warning. A frame constructed with first y input "
                    + "larger than the second input might lead to irregular "
                    + "behaviour when frame is used for collision detection");
        }
    }
    
    /**
     * Gives the X coordinate of the center of the frame.
     * @return X coordinate of the center of the frame.
     */
    public double centerX(){
        return (x0+x1)/2;
    }
    
    /**
     * Gives the X coordinate of the center of the frame.
     * @return X coordinate of the center of the frame.
     */
    public double centerY(){
        return (y0+y1)/2;
    }
    
    /**
     * Gives back the X coordinate of the first point defining the frame.
     * @return the X coordinate of the first point.
     */
    public double getX0(){
        return x0;
    }
    
    /**
     * Gives back the X coordinate of the second point defining the frame.
     * @return the X coordinate of the second point.
     */
    public double getX1(){
        return x1;
    }
    
    /**
     * Gives back the Y coordinate of the first point defining the frame.
     * @return the Y coordinate of the first point.
     */
    public double getY0(){
        return y0;
    }
    
    /**
     * Gives back the Y coordinate of the second point defining the frame.
     * @return the Y coordinate of the second point.
     */
    public double getY1(){
        return y1;
    }
}

