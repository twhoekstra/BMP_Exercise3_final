package nanocourse;

import java.awt.Color;

/**
 * Final Exercise Biomolecular Programming Differentiation of tissues during
 * development
 *
 * The floater class is a circular object that can be displayed on screen.
 * A floater can diffuse across space at a certain speed, staying inside set
 * bounds and colliding with other floaters while doing so.
 *
 * @author Thijn Hoekstra
 */
public class Floater {
    
    double x; // X position
    double y; // Y position
    int id; // Unique ID
    boolean exists; // Whether or not the floater exists
    double speed; // Diffusion speed
    int radius; // Size
    Color color; // Color
    
    // Minimum and maximum radius for use with random sizing
    int minRad;
    int maxRad;
    
    // Random number generator
    ExtRandom rnd;
    
    // Parameters for what to collide with
    boolean doesFloaterCollide;
    boolean doesFrameCollide;
    Floater[] neighbors;
    Frame frame;

    /**
     * General constructor for floaters that exist.
     * @param id a unique integer used for identification.
     * @param speed how fast the floater diffuses.
     * @param minRad minimum radius of the floater. Used for creating randomly-
     * sized floaters.
     * @param maxRad maximum radius of the floater.
     * @param spawnFrame bounds in which to randomly spawn the floater.
     */
    public Floater(int id, double speed, int minRad, int maxRad, Frame spawnFrame) { 
        this.rnd = new ExtRandom(); // Initializing RNG
        this.exists = true; // The floater will exist
        this.doesFloaterCollide = false; // Collision is off by default
        this.doesFrameCollide = false;
        this.id = id;
        this.speed = speed;
        this.radius = maxRad; //Spawn the smallest size
        this.maxRad = maxRad;
        this.minRad = minRad;
        this.x = rnd.randXInFrame(spawnFrame, radius);
        this.y = rnd.randYInFrame(spawnFrame, radius);
    }

    /**
     * Constructor used for non-existing floaters.
     * @param id a unique integer used for identification.
     * @param speed how fast the floater diffuses.
     */
    public Floater(int id, double speed) {
        this.rnd = new ExtRandom(); // Initializing RNG
        this.exists = false; // The floater won't exist
        this.doesFloaterCollide = false; // Collision is off by default
        this.doesFrameCollide = false;
        this.id = id;
        this.speed = speed;
    }

    /**
     * Transitions the floater from existing to non-existing.
     */
    public void kill() { // 
        exists = false;
    }

    /**
     * For debugging purposes. Prints useful information.
     */
    public void printInfo() {
        System.out.println("This is a floater with the folliwing parameters:\n"
                + "Id: \t\t" + id + "\n"
                + "Radius: \t" + radius + "\n"
                + "Exists: \t" + exists + "\n"
                + "Speed: \t\t" + speed + "\n"
                + "Collide0: \t" + doesFrameCollide + "\n"
                + "Collide1: \t" + doesFloaterCollide + "\n"
                + "X: \t" + x + "\n"
                + "Y: \t" + y + "\n"
        );
    }

    /**
     * Returns whether the floater exists.
     * @return whether or not the floater exists.
     */
    public boolean doesExist() {
        return exists;
    }

    /**
     * Initializes collision with other floaters.
     * @param neighbors an array of floaters to collide with. May contain itself.
     */
    public void setFloaterCollision(Floater[] neighbors) {
        doesFloaterCollide = true;
        this.neighbors = neighbors;
    }

    /**
     * Initializes collision with a boundary.
     * @param frame the boundary to collide with.
     */
    public void setFrameCollision(Frame frame) {
        doesFrameCollide = true;
        this.frame = frame;
    }

    /**
     * Checks how much overlap there is with neighboring floaters.
     * @param floaters an array of floaters for which to check overlap. May 
     * include itself.
     * @return the total amount of overlap.
     */
    public double checkTotalOverlap(Floater[] floaters) {
        int n_floaters = floaters.length;
        double overlap = 0;
        double distance;
        for (int i = 0; i < n_floaters; i++) {
            // Iterate through cells in the array
            if (i == id || !floaters[i].doesExist()) {
                // Skip if the floater to be checked has a matching ID
                continue; 
            }
            distance = distanceToFloater(floaters[i]); // Calculate distance
            if (distance < 0) {
                /*
                 * If the two cells do overlap. Add the distance with which they
                 * overlap to a sum.
                 */
                overlap = overlap - distance;
            }
        }
        return overlap;
    }

    /**
     * Check whether or not the floater is colliding with the bounds defined by a
     * frame.
     * @param frame a frame that defines the boundaries of the floater
     * @return whether or not the floater is colliding with the boundary
     */
    public boolean checkFrameCollide(Frame frame) {
        return (x - radius < frame.x0 || 
                x + radius > frame.x1 || 
                y - radius < frame.y0 || 
                y + radius > frame.y1);
    }

    /**
     * Calculates the distance between the floater and another floater.
     * @param floater another floater for which to calculate the distance to.
     * @return the shortest distance between the two floaters. If the two floaters
     * are tangent, this will equal 0.
     */
    public double distanceToFloater(Floater floater) {
        return Math.sqrt(Math.pow(floater.x - x, 2) 
                + Math.pow(floater.y - y, 2)) - radius - floater.radius;
    }

    /**
     * Makes the floater take a random step. If this method is repeated, the
     * floater perform Brownian motion. This method takes into account what
     * collision types the floater has set.
     */
    public void advancedDiffuse() {

        if(!exists){
            return;
        }
        
        // Saving the current location of the floater, useful later on.
        double oldX = x;
        double oldY = y;
        
        // Determining the size of the random step in both the X and Y direction
        double dx = (rnd.nextDouble() - 0.5) * 2 * speed;
        double dy = (rnd.nextDouble() - 0.5) * 2 * speed;

        /* 
         * Random step protocol if the floater needs to stay within a boundary but
         * should ignore other floaters
         */
        if (doesFrameCollide && !doesFloaterCollide) {
            x = x + dx; // Take a random step of size dx in the X direction
            if (checkFrameCollide(frame)) {
                /* 
                 * If this leads to a collision with the boundary, take the same 
                 * step in the other direction
                 */
                x = oldX - dx;
            }
            // Take a random step of size dy in the Y direction, do the same check
            y = y + dy;
            if (checkFrameCollide(frame)) {
                y = oldY - dy;
            }
            
        /* 
         * Random step protocol if the floater needs to stay within a boundary and
         * also should not overlap with other floaters
         */
        } else if (doesFloaterCollide && doesFrameCollide) {
            // Initializing variables for use later on
            double plusDsOverlap;
            double negDsOverlap;
            boolean isFlipped = false;
            
            x = x + dx;  // Take a random step of size dx in the X direction
            
            // Calculate and store the overlap
            plusDsOverlap = checkTotalOverlap(neighbors);
            if (plusDsOverlap > 0) { // Check if there is any overlap at all
                x = oldX - dx; // If so, take the same step the other way
                negDsOverlap = checkTotalOverlap(neighbors); // And check here
                
                // Take the step in the direction that leads to the least overlap
                if (plusDsOverlap < negDsOverlap) {
                    x = oldX + dx; // Original step was best
                } else {
                    isFlipped = true; // Or the step back is best. Keep track
                }
            }
            // Now check whether the step brings the floater out of bounds.
            if (checkFrameCollide(frame)) {
                // If this is the case, go the other way.
                x = isFlipped ? oldX + dx : oldX - dx;
            }
            
            y = y + dy; // Take a random step of size dx in the X direction
            isFlipped = false; // Reset

            // Do the same collision avoidance in the Y direction
            plusDsOverlap = checkTotalOverlap(neighbors);
            if (plusDsOverlap > 0) {
                y = oldY - dy;
                negDsOverlap = checkTotalOverlap(neighbors);

                if (plusDsOverlap < negDsOverlap) {
                    y = oldY + dy;
                } else {
                    isFlipped = true;
                }
            }
            if (checkFrameCollide(frame)) {
                y = isFlipped ? oldY + dy : oldY - dy;
            }

        /* 
         * Random step protocol if the floater doesn't need to stay within a
         * boundary and is allowed to pass through floaters aswell. Implementation
         * not necessary.
         */
        } else if (doesFloaterCollide && !doesFrameCollide) {
            System.out.println("Warning! This feature doesn't work yet. "
                    + "Please also enable frame collision"); // Warning
        }
    }

    /**
     * Returns the floater's X position
     * @return X position
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the floater's Y position.
     * @return Y position.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Returns the floater's speed.
     * @return speed.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns the floater's radius.
     * @return radius.
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Returns the cap set for the floater's radius.
     * @return the maximum radius.
     */
    public int getMaxRad() {
        return maxRad;
    }

    /**
     * Returns the color of the floater.
     * @return the floater's radius.
     */
    public Color getColor() {
        return color;
    }
}
