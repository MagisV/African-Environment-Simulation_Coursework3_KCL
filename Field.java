import java.util.*;

/**
 * Class Field - Represent a rectangular grid of field positions.
 * Each position is able to store a single animal.
 * 
 * @author David J. Barnes and Michael Kölling, edited by Valentin Magis and Barnabas Szalai
 * @version 2021-03-02
 */
public class Field {

    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();

    // The depth and width of the field.
    private final int depth;
    private final int width;
    private final int height;
    // Storage for the animals.
    private final Entity[][][] field;
    private Stack<Entity[][][]> previousFields;
    private Stack<Entity[][][]> nextFields;

    private final Environment savannaEnvironment;
    private final Environment forestEnvironment;
    private final Environment desertEnvironment;


    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
        this.height = new EntityLevelMapper().getAmountOfLevels();
        field = new Entity[depth][width][height]; //a maximum of four entities can be on the same field 8 (x and y): plant, animal on ground and animal in air. Should maybe not be hardcoded.
        previousFields = new Stack<>();
        nextFields = new Stack<>();
        savannaEnvironment = new Environment("Savanna", 0, width/3);
        forestEnvironment = new Environment("Forest", width/3+1, (width/3)*2);
        desertEnvironment = new Environment("Desert", (width/3)*2+1, width-1);
    }

    /**
     * Returns the name of the environment of the location.
     * @param location The location the environment is requested of.
     * @return The name of the environment.
     */
    public String getCurrentEnvironment(Location location) {
        if(location.getCol() <= savannaEnvironment.getEndCol()) {
            return "Savanna";
        } else if(location.getCol() > savannaEnvironment.getEndCol() && location.getCol() < desertEnvironment.getStartCol() ) {
            return "Forest";
        } else {
            return "Desert";
        }
    }

    /**
     * A getter method that returns the savanna environment object
     * @return the savanna environment
     */
    public Environment getSavannaEnvironment() {
        return savannaEnvironment;
    }

    /**
     * A getter method that returns the forest Environment object
     * @return the forest environment
     */
    public Environment getForestEnvironment() {
        return forestEnvironment;
    }
    
    /**
     * Empty the field.
     */
    public void clear()
    {
        for (int level = 0; level < height; level++) {
            for (int row = 0; row < depth; row++) {
                for (int col = 0; col < width; col++) {
                    field[row][col][level] = null;
                }
            }
        }
    }

    /**
     * Pushes the current field into the stack which holds the previously visited fields
     */
    public void savePrev()
    {
        Entity[][][] temp = new Entity[depth][width][height];
        for (int level = 0; level < height; level++) {
            for (int row = 0; row < depth; row++) {
                for (int col = 0; col < width; col++) {
                    temp[row][col][level] = field[row][col][level];
                }
            }
        }
        previousFields.push(temp);
    }

    /**
     * Pushes the current field into the stack that holds the fields of the fields that have been previously visited,
     * but are after the current step
     */
    public void saveNext()
    {
        Entity[][][] temp = new Entity[depth][width][height];
        for (int level = 0; level < height; level++) {
            for (int row = 0; row < depth; row++) {
                for (int col = 0; col < width; col++) {
                    temp[row][col][level] = field[row][col][level];
                }
            }
        }
        nextFields.push(temp);
    }

    /**
     * Pops a field from the stack that holds the previous fields and sets it as the current field. It also saves the
     * current field into the next fields stack
     */
    public void loadPrevious()
    {
        Entity[][][] temp = previousFields.pop();
        saveNext();
        for (int level = 0; level < height; level++) {
            for (int row = 0; row < depth; row++) {
                for (int col = 0; col < width; col++) {
                    field[row][col][level] = temp[row][col][level];
                }
            }
        }
    }

    /**
     * Pops a field from the stack that loads the next fields that have been previously visited, but are further from
     * the current step. It also saves the current field into the stack that holds the previous fields.
     */
    public void loadNext() {
        Entity[][][] temp = nextFields.pop();
        savePrev();
        for (int level = 0; level < height; level++) {
            for (int row = 0; row < depth; row++) {
                for (int col = 0; col < width; col++) {
                    field[row][col][level] = temp[row][col][level];
                }
            }
        }
    }

    /**
     * Checks whether the stack of the next fields is empty. It is empty when the current step is at the maximum
     * it has been since compiling the program.
     * @return If the stack of the next field is empty.
     */
    public boolean isNext() {
        return !(nextFields.isEmpty());
    }

    /**
     * Clear the given location.
     * @param location The location to clear.
     */
    public void clear(Location location)
    {
        field[location.getRow()][location.getCol()][location.getLevel()] = null;
    }
    
    /**
     * Place an animal at the given location.
     * If there is already an animal at the location it will
     * be lost.
     * @param entity The animal to be placed.
     * @param location Where to place the animal.
     */
    public void place(Entity entity, Location location)
    {
        field[location.getRow()][location.getCol()][location.getLevel()] = entity;
    }
    
    /**
     * Return the entity at the given location, if any.
     * @param location Where in the field.
     * @return The entity at the given location, or null if there is none.
     */
    public Entity getEntityAt(Location location)
    {
        return getEntityAt(location.getRow(), location.getCol(), location.getLevel());
    }
    
    /**
     * Return the entity at the given location, if any.
     * @param row The desired row.
     * @param col The desired column.
     * @param level The desired level.
     * @return The entity at the given location, or null if there is none.
     */
    public Entity getEntityAt(int row, int col, int level)
    {
        return field[row][col][level];
    }

    /**
     * Return the entity at the given location (rom, col) and the specified level.
     * @param location the desired location
     * @param level The desired new level.
     * @return The entity at the given location, or null if there is none.
     */
    public Object getObjectAtLevel(Location location, int level)
    {
        return field[location.getRow()][location.getCol()][level];
    }

    /**
     * Return the object with the highest importance at all levels of the same row and column coordinates.
     * E.g. used to draw entities in SimulatorView
     *
     * @param row The desired row
     * @param col The desired column
     * @return The entity with the highest painting hierarchy
     */
    public Object getObjectOnTop(int row, int col)
    {

        if (this.getEntityAt(row, col, 3) != null) //Air animals are at the top and therefore painted first
            return this.getEntityAt(row, col, 3);

        else if (this.getEntityAt(row, col, 1) != null) //If there is no air animal and there is a tree, the tree is painted
            return this.getEntityAt(row, col, 1);

        else if (this.getEntityAt(row, col, 2) != null) //If there is no air animal and there is a ground animal, the ground animal is painted
            return this.getEntityAt(row, col, 2);

        else if (this.getEntityAt(row, col, 0) != null) //If there is nothing above it, the ground plant is painted.
            return this.getEntityAt(row, col, 0);

        else
            return null;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location)
    {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if(location != null) {
            int row = location.getRow();
            int col = location.getCol();
            int level = location.getLevel(); //Does not change
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol, level));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location, ArrayList<Integer> searchLevelList, int radius)
    {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if(location != null) {
                int row = location.getRow();
                int col = location.getCol();
                int level;
                for (int u = 0; u < searchLevelList.size(); u++) {
                    level = searchLevelList.get(u);
                    for (int roffset = -radius; roffset <= radius; roffset += 1) {
                        int nextRow = row + roffset;
                        if (nextRow >= 0 && nextRow < depth) {
                            for (int coffset = -radius; coffset <= radius; coffset++) {
                                int nextCol = col + coffset;
                                // Exclude invalid locations and the original location.
                                if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                                    locations.add(new Location(nextRow, nextCol, level));
                                }
                            }
                        }
                    }
                }
            //}

            // Shuffle the list. Several other methods rely on the list being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for(Location next : adjacent) {
            if (isFree(next))
                free.add(next);
        }
        return free;
    }

    /**
     * Returns if a given location is free depending on its level.
     * This is the core logic of how entities can move/ where they can place offspring.
     * @param location The location checked for being free.
     * @return If the location is free.
     */
    public boolean isFree(Location location)
    {
        switch (location.getLevel()) //The level that the specified field is at //enums would be nice //also a separate method checking for validity instead of having all the conditions here.
        {
            case 0: if(getObjectAtLevel(location, 0) == null && getObjectAtLevel(location, 1) == null) //we are looking at level 0 (grass). Object at level 0 (grass) or level 1 (tree) makes the field occupied
                return true;
                break;
            case 1: if(getObjectAtLevel(location, 1) == null) //&& getObjectAtLevel(next, 0) == null && getObjectAtLevel(next, 2) == null) //we are looking at level 1 (tree). this will only propagate if there is no other tree, grass or ground animal (level 1 or level 2)
                return true;
                break;
            case 2: if(getObjectAtLevel(location, 2) == null && getObjectAtLevel(location, 1) == null) //looking at level 2 (animal) free if there is no other ground animal or tree
                return true;
                break;
            case 3: if(getObjectAtLevel(location, 3) == null) //looking at level 3 (air animal) free if there is no other air animal
                return true;
                break;
        }
        return false;
    }

    /**
     * Returns a list of free locations in the given radius.
     * @param location The location the is being looked from.
     * @param radius The radius in which free locations are searched for
     * @return A list of all free locations within the given radius.
     */
    public List<Location> getFreeNearbyLocations(Location location, int radius) {
        List<Location> free = new LinkedList<>();
        List<Location> nearbyLocations = nearbyLocations(location, radius);
            for(Location next : nearbyLocations) {
                if (isFree(next))
                    free.add(next);
            }
        return free;
    }

    /**
     * Try to find a free location that is adjacent to the
     * given location. If there is none, return null.
     * The returned location will be within the valid bounds
     * of the field.
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location)
    {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if(free.size() > 0) {
            return free.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Returns a list of locations in the given radius.
     * @param location the location from which the nearby locations are checked
     * @param radius the radius of a circle from the original location. Every location inside the circle counts as a
     * nearby location.
     * @return a list of location that are near the entered location
     */
    public List<Location> nearbyLocations(Location location, int radius) {
        assert location != null : "Null location passed to nearbyLocations";
        List<Location> locations = new LinkedList<>();
        if(location != null) {
           // for(int i = 0; i <= radius; i++) {
                int row = location.getRow();
                int col = location.getCol();
                for (int roffset = -radius; roffset <= radius; roffset += 1) {
                    int nextRow = row + roffset;
                    if (nextRow >= 0 && nextRow < depth) {
                        for (int coffset = -radius; coffset <= radius; coffset++) {
                            int nextCol = col + coffset;
                            // Exclude invalid locations and the original location.
                            if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                                locations.add(new Location(nextRow, nextCol, location.getLevel()));
                            }
                        }
                    }
                }
            //}
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Get one free location.
     * @param location The location that is being looked from.
     * @param radius The radius in which to look for a free location
     * @return A randomly selected free location within the radius from the given location.
     */
    public Location freeNearbyLocation(Location location, int radius)
    {
        // The available free locations.
        List<Location> free = getFreeNearbyLocations(location, radius);
        if(free.size() > 0) {
            return free.get(0);
        }
        else {
            return null;
        }
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Return the height of the field
     * @return The height of the field
     */
    public int getHeight() { return height; }
}
