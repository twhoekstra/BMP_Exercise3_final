package nanocourse;
import java.awt.Color;

/**
 * Final Exercise Biomolecular Programming
 * Differentiation of tissues during development
 * 
 * Cell is a subclass of Floater. It can diffuse just like a floater, but also
 * divide, grow and die. It can also transition between states and sense 
 * Morphogens.
 * 
 * @author Thijn Hoekstra
 */
public class Cell extends Floater {
    // Parameters
    int deathChance = 10000; // The probability of dying is 1/"deathChance"
    int growthStep = 2; // How much the cell can grow at once
    int growthChance = 100;// The probability of growing is 1/"growthChance"
    int releaseChance = 200;
    
    int threshold1to2 = 20; // Morphogen concentrations to change type
    int threshold2to3 = 7;
    int maxNeighbours = 2;
    
    // Stores what type of cell this cell is.
    int type; 
    /*
     * 0 = original,
     * 1 = blue cells, high morphogen concentration
     * 2 = white cells, intermediate morphogen concentration
     * 3 = red cells, low morphogen concentration
     * 
     * Each type has its own color, which is stored a corresponding index in the
     * following array:
     */
    Color[] typeColors = new Color[]{
        Color.WHITE, 
        Color.BLUE, 
        Color.PINK, 
        Color.RED};
    
    // Whether or not the cell has to sense the morphogen concentration.
    boolean doesMorphSense;
    Floater[] morphogens;
    /**
     * Creates an alive cell.
     * @param id a unique integer used for identification.
     * @param speed how fast the floater diffuses.
     * @param minRad minimum radius of the floater.
     * @param maxRad maximum radius of the floater. Used for creating randomly-
     * sized floaters. Also used for division.
     * @param spawnFrame bounds in which to spawn the floater at a random 
     * location.
     */
    public Cell(int id, double speed, int minRad, int maxRad, Frame spawnFrame){
        super(id, speed, minRad, maxRad, spawnFrame);
        
        this.type = 0; // These cells are "original"
        super.color = typeColors[type];
    }
    
    /**
     * Creates a non-existing cell.
     * @param id a unique integer used for identification.
     * @param speed how fast the floater diffuses.
     */
    public Cell(int id, double speed){
        super(id, speed);
    }
    
    /**
     * The state machine determines the activity of the cell. A cell can either
     * do nothing, divide, die or release a morphogen.
     * @return Returns 0 for no activity, 1 for division, 2 for death and 3 for 
     * morphogen release
     */
    public int stateMachine(){
            if(!exists){
                return 0;
            }
        
            // Check if the cell is going to die
            if (rnd.nextInt(deathChance) == 0){
                // The main program will handle killing this cell
                return 2; // Pass information to main program
            }
            
            // Morphogen sensing, only do this if the cell is not an original cell
            if(doesMorphSense && type != 0){
                // Store the local concentration
                int concentration = checkConcentration(morphogens, 100, false);
                
                // Now decide whether to convert into another type
                int oldType = type;
                switch (oldType){
                    case 1:
                        if (concentration < threshold1to2){ 
                            // Transition from type 1 to 2
                            type = 2;
                            color = typeColors[2]; // Set color
                        }
                        break;
                    case 2:
                        if (concentration < threshold2to3){ 
                            // Transition from type 2 to 3
                            type = 3;
                            color = typeColors[3]; // Set color
                        }
                        break;
                }
            }
            
            // If the cell is an original cell it can release morphogens
            else if (type == 0){
                if (rnd.nextInt(releaseChance) == 0){
                //Main program will handle the final release
                return 3; // Pass information to main program
                }
            } 
            
            // Check if the cell is going to grow
            if (rnd.nextInt(growthChance) == 0 && radius < maxRad){ // Growth cap
                radius = radius + growthStep; // Bring about growth
            }
            
            /* 
             * If the cell is large enough (enough cytosol) it will check if it
             * has the space to divide. If so, it'll follow through.
             */
            if (radius >= maxRad){
                if (checkConcentration(neighbors, 5, true) < maxNeighbours){
                    //Main program will handle the final division
                    return 1; // Pass information to main program
                } 
            }
            
        // The cell can also do nothing instead.
        return 0; // Pass information to main program
    }
    /**
     * Enables the cell to sense the morphogen concentration in its environment.
     * @param morphogens an array of morphogense to use for sensing.
     */
    public void setMorphSense(Floater[] morphogens){
        doesMorphSense = true;
        this.morphogens = morphogens;
    }
    
    /**
     * Discloses the type of cell
     * @return the cell's type
     */
    public int getType(){
        return type;
    }
    
    /**
     * Makes the cell divide. This decreases the radius of the cell by a factor
     * ~0.7, which corresponds to the cell sharing half of its surface area with
     * its daughter cell
     */
    public void divide(){
        radius = (int)( (double) radius * 0.7071);
    }
    
    /**
     * Turns a non-existing cell into an existing one. Used for duplication of
     * cells.
     * @param type what type the new cell should be.
     * @param radius the radius of the new cell.
     * @param maxRad the maximum radius the new cell can achieve. Used for
     * division.
     * @param x the X position of the new cell.
     * @param y the Y position of the new cell.
     */
    public void animate(int type, int radius, int maxRad, double x, double y){
        this.radius =  radius;
        this.x = x;
        this.y = y;
        this.maxRad = maxRad;
        exists = true;
        this.type = type;
    }
    
    /**
     * Checks the concentration of floaters within a circular area of a chosen
     * radius around the cell.
     * @param floaters what array of floaters to analyze.
     * @param radius the radius around the cell in which to check.
     * @param selfIncluded whether or not to exclude ID's that match the cell.
     * @return the number of floaters in the area of choice.
     */
    public int checkConcentration(Floater[] floaters, int radius, boolean selfIncluded){
        int n_floaters = floaters.length;
        int count = 0; // Tally of floaters that meet the criteria
        double distance;
        
        for (int i = 0; i < n_floaters; i++){
            if ((i == id && selfIncluded) || !floaters[i].doesExist()){
                continue; // Selectively exclude floaters
            }
            distance = distanceToFloater(floaters[i]);
            if(distance < radius){
                count++; // Add to tally for each floater that meets criteria
            }
        }
        return count;
    }
    
    /**
     * Sets the color of the cell to that of a specific type.
     * @param type the type whose color we want to give the cell.
     */
    public void setColor(int type){
        color = typeColors[type];
    }
    
    public void setColor(Color color){
        this.color = color;
    }
}
