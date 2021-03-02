import java.util.List;

/**
 * A grass is a plant. It is eaten by the gazelle.
 *
 * @author David J. Barnes and Michael Kölling, edited by Valentin Magis and Barnabas Szalai
 * @version 2021-03-02
 */

public class Grass extends Plant {


    private static final int MAX_AGE = PlantStats.GRASS.getMaxAge();
    private final double BREEDING_PROBABILITY = PlantStats.GRASS.getBreeding_Probability(); // The likelihood of a gazelle breeding.
    private final double BAD_ENVIRONMENT_BREEDING_PROBABILITY = PlantStats.GRASS.getBadEnvironmentBreedingProbability();
    private final int MAX_LITTER_SIZE = PlantStats.GRASS.getMaxLitterSize();
    private final int BREEDING_AGE = PlantStats.GRASS.getBreedingAge();
    private final int TIME_ACTIVITY_START = PlantStats.GRASS.getTimeActivityStart();
    private final int TIME_ACTIVITY_END = PlantStats.GRASS.getTimeActivityEnd();

    /**
     * The constructor method of the class, which initializes its instance variables and calls the constructor of
     * the superclass.
     * @param randomAge a boolean whether the age should be randomly determined
     * @param field the field to which the grass is to be placed
     * @param location the location where the grass is to be placed
     */

    public Grass(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        updateFoodValue();
        age = 0;
        currentBreedingProbability = BREEDING_PROBABILITY;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    /**
     * Calls the act method of the superclass. Also calls makes the grass propagate if it is alive after the act.
     * @param newGrass
     * @param currentTime The current time
     */
    @Override
    public void act(List<Entity> newGrass, int currentTime) {
        super.act(newGrass, currentTime);
        if (isAwake(currentTime)) {
            if (isAlive()) {
                propagate(newGrass);
            }
        }
    }

    /**
     * Updates the grass's nutrition value, depending on its age.
     */
    protected void updateFoodValue()
    {
        foodValue = (int) Math.round(2*Math.log(age)); //log grows fast in the beginning and slower at later stages, mimics the growth of real grass.
    }

    /**
     * Returns the nutrition value that the animal receives when it eats the grass
     * @return the food value
     */
    public int getFoodValue()
    {
        return foodValue;
    }



    /**
     * Check whether or not this grass can propagate at this step
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void propagate(List<Entity> newRabbits)
    {
        // New seeds are put into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Grass seed = new Grass(false, field, loc);
            newRabbits.add(seed);
        }
    }

    /**
     * Returns the max age of this species. If reached, the entity dies.
     * @return The maximum age.
     */
    protected int getMaxAge()
    {
        return MAX_AGE;
    }

    /**
     * Returns the default breeding probability of this species in bad environments.
     * @return The bad default breeding probability.
     */
    public double getBadEnvironmentBreedingProbability() { return BAD_ENVIRONMENT_BREEDING_PROBABILITY; }

    /**
     * Returns the default breeding probability of this species in preferred environments.
     * @return The good default breeding probability.
     */
    public double getBreedingProbability() { return BREEDING_PROBABILITY; }

    /**
     * Returns the maximum litter size of this entity.
     * @return The maximum litter size.
     */
    public int getMaxLitterSize() { return MAX_LITTER_SIZE; }

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
