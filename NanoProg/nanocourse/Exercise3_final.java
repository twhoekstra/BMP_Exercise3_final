package nanocourse;

import java.awt.Color;
import nano.*;

public class Exercise3_final {

/**
 * Final Exercise Biomolecular Programming
 * Differentiation of tissues during development
 * 
 * @author Thijn Hoekstra
 */
    
    public Exercise3_final() {
        // Start of code
        
        // Grahpical settings
        int xSize = 640; // Horizontal size of the grahpical area
        int ySize = 360; // Vertical size of the grahpical area
        int delay = 5; // Delay between drawing new frames in milliseconds
        Canvas screen = new Canvas(xSize, ySize, 0, 0);
        ExtPen pen = new ExtPen(screen);
        ExtRandom rnd = new ExtRandom();
        
        // On-screen objects settings
        int max_cells = 5000; // Maximum number of cells
        int n_cells = 50; // Initial amount of cells
        int max_morphogens = 10000; // Maximum number of morphogens
        int n_morphogens = 0; // Initial amount of morphogens

        // Defining the area where all the particles can move
        Frame frame = new Frame(15, 15, xSize - 15, ySize - 15);
        // Defining the area where the initial cells will be placed
        Frame spawn = new Frame(frame.getX0(), frame.getY0(), frame.getX0() + 25,
                frame.getY1());

        // Initializing arrays that contain the cells and the morphogens
        Cell[] cells = new Cell[max_cells];
        Morphogen[] morphogens = new Morphogen[max_morphogens];

        /* 
         * Partially filling the cells array with cells that exist,
         * the rest of the space if filled with "non-existing" cells
         * Note that each cell is given a unique identifier (ID) "i", which is
         * passed as the first parameter. This will come in handy later on. 
         */
        for (int i = 0; i < max_cells; i++) {
            cells[i] = i < n_cells
                    ? new Cell(i,1 , 6, 8 + rnd.nextInt(4), spawn)
                    : // Existing cells
                    new Cell(i, 2);  // "Non-existing" cells with ID and speed 2

            // Tell the cells that they can't leave the defined border
            cells[i].setFrameCollision(frame);

            /* 
             * Tell the cells what array they are stored in in order to 
             * prevent overlaps
             */
            cells[i].setFloaterCollision(cells);
            /*
             * Tell the cells what array the morphogens are stored in so that
             * they can check them
             */
            cells[i].setMorphSense(morphogens);
        }

        /* 
         * Filling the morphogens array with "non-existing" morphogens which will
         * be activated when necessary.
         * Note that each morphogen is given a unique identifier "i" (ID), which 
         * is passed as the first parameter. This will come in handy later on.
         */
        for (int i = 0; i < max_morphogens; i++) {
            // "Non-existing" cells
            morphogens[i] = new Morphogen(i,10, 2); //ID, speed and radius

            /*
             * This is where the core loop of the program starts. Note that 
             * morphogens can't collide among themselves. This behaviour wouldn't
             * make the program a lot more interesting as the morphogens are very
             * small.
             */
            morphogens[i].setFrameCollision(frame);
        }

        /*
         * This is where the core loop of the program starts
         */
        while (true) {

            // Make all the cells move, first the cells
            for (int i = 0; i < n_cells; i++) {
                cells[i].advancedDiffuse();
            }
            // Then the morphogens
            for (int i = 0; i < n_morphogens; i++) {
                morphogens[i].advancedDiffuse();
            }

            /*
             * Now we have each cell that exists do something, like divide, die 
             * or release a morphogen
             */
            double old_n_cells = n_cells; //Paramater to retain for-loop length
            int state;

            for (int i = 0; i < old_n_cells; i++) {

                // Run the state machine and get back what the cell wants to do
                state = cells[i].stateMachine();

                switch (state) {
                    case 1: // Cell wants to divide

                        //Check if we still have space in our array for a new cell
                        if (n_cells < max_cells) {
                            cells[i].divide(); //Make the original cell divide

                            /*
                             * Now we have to make the daughter cell. This process 
                             * varies if the mother cell is an original cell
                             */
                            if (cells[i].getType() == 0) { // For original cells
                                // Making a new cell with state 1
                                cells[n_cells].animate(
                                        1,
                                        cells[i].getRadius(),
                                        cells[i].getMaxRad(),
                                        cells[i].getX(),
                                        cells[i].getY());
                                cells[n_cells].setColor(1);
                            } else { // For any other cell
                                // Making a new identical cell
                                cells[n_cells].animate(
                                        cells[i].getType(), 
                                        cells[i].getRadius(), 
                                        cells[i].getMaxRad(), 
                                        cells[i].getX(), 
                                        cells[i].getY());
                                cells[n_cells].setColor(cells[i].getColor());
                            }
                            n_cells++; // Keeping track of the new cells
                        }
                        break;
                        
                    case 2: // Cell is going to die
                        /*
                         * Putting the last alive cell in the array in the in the 
                         * location that is vacated by the dying cell
                         */
                        cells[i].animate(
                                cells[n_cells - 1].getType(), 
                                cells[n_cells - 1].getRadius(), 
                                cells[n_cells - 1].getMaxRad(), 
                                cells[n_cells - 1].getX(), 
                                cells[n_cells - 1].getY()); 
                        cells[i].setColor(cells[n_cells - 1].getColor());
                        
                        /*
                         * Now we've swapped the two cells we can kill a cell
                         */
                        cells[n_cells - 1].kill();
                        
                        n_cells--; // Keeping track of the dying cells
                        break;
                        
                    case 3: // Cell wants to release a morphogen
                        if (n_morphogens < max_morphogens) { // Check if possible
                            morphogens[n_morphogens].animate( // Make morphogen
                                    cells[i].getX(), // Place at cell
                                    cells[i].getY());
                            n_morphogens++; // Keep track of new morphogens
                        }
                        break;
                        
                    default:
                        break;
                }
            }

            /*
             * Each morhpogen has a chance of decaying.
             */
            for (int i = 0; i < n_morphogens; i++) {
                // Run the state machine and check if the morphogen is decaying
                if (morphogens[i].stateMachine()) {
                    /*
                     * Same swap as seen in handling cell death.
                     */
                    morphogens[i].animate(
                            morphogens[n_morphogens - 1].getX(), 
                            morphogens[n_morphogens - 1].getY());
                    morphogens[n_morphogens - 1].kill();
                    n_morphogens--; // Keep track of removed morphogens
                }
            }
            
            /*
             * All the objects on screen have had the ability to do something,
             * e.g. diffuse or divide. Now it is time to draw the results. Note
             * that we draw the morphogens first as this will overlay the cells
             * when drawn later. This looks nicer.
             */
            for (int i = 0; i < n_morphogens; i++) {
                pen.drawFloater(morphogens[i]); // Draw every morphogen
            }
            
            for (int i = 0; i < n_cells; i++) {
                pen.drawFloater(cells[i]); // Draw every cell
            }

            pen.drawFrame(frame, Color.yellow); // Draw the boundaries

            
            /*
             * This order of operations for refreshing seems to work best
             */
            screen.update(); // Update the screen
            screen.pause(delay); // Wait. This holds the frame for a while
            screen.clear(); // Clear the screen
        }

        // End of code
    }

    public static void main(String[] args) {
        Exercise3_final e = new Exercise3_final();
    }
}
