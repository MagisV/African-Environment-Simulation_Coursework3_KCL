import java.util.List;

/**
 * Class Eagle - The Eagle is an animal moving in the air and can fly over other entities. It is a predator and eats frogs and gazelles.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class Eagle extends Animal {

    private static final int MAX_ENERGY = AnimalStats.EAGLE.getMaxEnergy();
    private static final int MAX_AGE = AnimalStats.EAGLE.getMaxAge();
    private static final int MOVING_RADIUS = AnimalStats.EAGLE.getMovingRadius();
    private final double BREEDING_PROBABILITY = AnimalStats.EAGLE.getBreeding_Probability();
    private final double BAD_ENVIRONMENT_BREEDING_PROBABILITY = AnimalStats.EAGLE.getBadEnvironmentBreedingProbability();
    private static final int MAX_LITTER_SIZE = AnimalStats.EAGLE.getMaxLitterSize();
    private final int BREEDING_AGE = AnimalStats.EAGLE.getBreedingAge();
    private final int TIME_ACTIVITY_START = AnimalStats.EAGLE.getTimeActivityStart();
    private final int TIME_ACTIVITY_END = AnimalStats.EAGLE.getTimeActivityEnd();

    /**
     * Create a new born eagle at location in field.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param size      The size of the parent. Might be mutated.
     * @param scent     The scent of the parent. Might be mutated.
     */
    public Eagle(Field field, Location location, double size, double scent) {
        super(field, location);
        age = 0;
        energyLevel = MAX_ENERGY;
        this.size = getMutationValue(size);
        this.scent = getMutationValue(scent);
        currentBreedingProbability = BREEDING_PROBABILITY;
    }

    /**
     * Create a new default eagle at location in field.
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Eagle(Field field, Location location)
    {
        super(field, location);

        age = rand.nextInt(MAX_AGE);
        energyLevel = rand.nextInt(MAX_ENERGY);
        availableForCoitus = rand.nextBoolean();
        size = AnimalStats.EAGLE.getDefaultSize();
        scent = AnimalStats.EAGLE.getDefaultScent();
        currentBreedingProbability = BREEDING_PROBABILITY;
    }

    /**
     * Executed every time an eagle acts. Searching for mates, updating the environment and looking for food are initiated here.
     * @param newEntities A list of new entities this eagle can add its offspring to.
     * @param currentTime The current time, the eagle acts accordingly.
     */
    @Override
    public void act(List<Entity> newEntities, int currentTime) {
        super.act(newEntities, currentTime);
        if (isAwake(currentTime)) {
            if (isAlive()) {
                if(availableForCoitus) {
                    if (sex && foundMate((int) scent)) {
                        giveBirth(newEntities);
                        availableForCoitus = false;
                    }
                }
                else {
                    availableForCoitus = true;
                }
                // Move towards a source of food if found.
                Location newLocation = findFood();
                if (newLocation == null) {
                    // No food found - try to move to a free location.
                    // Eagles can travel to further than 1 location at each act
                    newLocation = getField().freeNearbyLocation(getLocation(), MOVING_RADIUS);
                }
                // See if it was possible to move.
                if (newLocation != null) {
                    setLocation(newLocation);
                } else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }


    /**
     * Updates the eagle's nutrition value, depending on its age.
     */
    @Override
    protected void updateFoodValue()
    {
        foodValue = (int) Math.round(2*Math.log(Math.pow(age, 3))); //log grows fast in the beginning and slower at later stages
    }

    /**
     * Returns the max energy of this species. Animals cant eat if they have reached their max energy.
     * @return The maximum energy
     */
    @Override
    protected int getMaxEnergy()
    {
        return MAX_ENERGY;
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


