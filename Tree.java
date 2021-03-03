import java.util.List;

/**
 * Class Tree - A tree is an entity in the simulation. It does not move and can grow on any field. Trees especially grow in forests.
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class Tree extends Plant {

    private static final int MAX_AGE = PlantStats.TREE.getMaxAge(); // Stored in here to be used in superclass.
    private static final int MAX_EATABLE_AGE = 3;
    private static final int MAX_LITTER_SIZE = PlantStats.TREE.getMaxLitterSize();

    private final double BREEDING_PROBABILITY = PlantStats.TREE.getBreeding_Probability();
    private final double BAD_ENVIRONMENT_BREEDING_PROBABILITY = PlantStats.TREE.getBadEnvironmentBreedingProbability();

    private final int BREEDING_AGE = PlantStats.TREE.getBreedingAge();

    private final int TIME_ACTIVITY_START = PlantStats.TREE.getTimeActivityStart();
    private final int TIME_ACTIVITY_END = PlantStats.TREE.getTimeActivityEnd();


    /**
     * Create a new tree.
     * @param randomAge If a random age should be generated.
     * @param field The field to put the tree in.
     * @param location The location of the new tree.
     */
    public Tree(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        currentBreedingProbability = BREEDING_PROBABILITY;
        if (randomAge)
        {
            age = rand.nextInt(MAX_AGE);
        }
    }


    /**
     * Calls the act method in the superclass. It also calls the propagate method when the tree is still alive and
     * awake at the end of its act.
     * @param newTrees a list to add newly produced species
     * @param currentTime The current time of the day
     */
    @Override
    public void act(List<Entity> newTrees, int currentTime) {
        super.act(newTrees, currentTime);
        if (isAwake(currentTime)) {
            if (isAlive()) {
                propagate(newTrees);
            }
        }
    }


    /**
     * Check whether or not this tree can propagate at this step
     * New seeds will be put into free adjacent locations.
     * @param newTrees A list to return newly growing seeds.
     */
    private void propagate(List<Entity> newTrees)
    {
        // New seeds are put into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeNearbyLocations(getLocation(), 3); // The tree can propagate in a radius of 3
        int births = generateSeeds();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tree seed = new Tree(false, field, loc);
            newTrees.add(seed);
        }
    }

    /**
     * Generate a number representing the number of seeds,
     * if it can propagate.
     * @return The number of seeds (may be zero).
     */
    private int generateSeeds()
    {
        int newSeeds = 0;
        if(canBreed() && rand.nextDouble() <= currentBreedingProbability) {
            newSeeds = rand.nextInt(PlantStats.TREE.getMaxLitterSize()) + 1;
        }
        return newSeeds;
    }


    /**
     * Updates the tree's nutrition value, depending on its age.
     */
    protected void updateFoodValue()
    {
        foodValue = (int) Math.round(2*Math.log(age)); //log grows fast in the beginning and slower at later stages
    }

    /**
     * Returns if the tree is still small and can be eaten.
     * @return
     */
    @Override
    public boolean isEatable() {
        return age < MAX_EATABLE_AGE;
    }

    /**
     * Returns the max age of this species. If reached, the entity dies.
     * @return The maximum age.
     */
    @Override
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Returns the default breeding probability of this species in bad environments.
     * @return The bad default breeding probability.
     */
    @Override
    public double getBadEnvironmentBreedingProbability() { return BAD_ENVIRONMENT_BREEDING_PROBABILITY; }

    /**
     * Returns the default breeding probability of this species in preferred environments.
     * @return The good default breeding probability.
     */
    @Override
    public double getBreedingProbability() { return BREEDING_PROBABILITY; }

    /**
     * Returns the maximum litter size of the entity.
     * @return The maximum litter size.
     */
    @Override
    protected int getMaxLitterSize() { return MAX_LITTER_SIZE ; }

    /**
     * Returns the minimum breeding age of this entity
     * @return The minimum breeding age.
     */
    @Override
    public int getBreedingAge() { return BREEDING_AGE; }

    /**
     * Return the time this entity type starts its activity
     * @return The activity start time.
     */
    @Override
    public int getTimeActivityStart() { return TIME_ACTIVITY_START; }

    /**
     * Return the time this entity type ends its activity.
     * @return The activity end time.
     */
    @Override
    public int getTimeActivityEnd() { return  TIME_ACTIVITY_END; }
}