import java.util.List;

/**
 * Class Snake - The snake is an animal that movies on the ground. It is a predator and eats only frogs.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class Snake extends Animal {
    // Characteristics shared by all snakes.
    private static final int BREEDING_AGE = AnimalStats.SNAKE.getBreedingAge();  // The age at which a snake can start to breed.
    private static final int MAX_LITTER_SIZE = AnimalStats.SNAKE.getMaxLitterSize();     // The maximum number of births.

    private static final int MAX_ENERGY = AnimalStats.SNAKE.getMaxEnergy();
    private static final int MAX_AGE = AnimalStats.SNAKE.getMaxAge();

    private final double BREEDING_PROBABILITY = AnimalStats.SNAKE.getBreeding_Probability();
    private final double BAD_ENVIRONMENT_BREEDING_PROBABILITY = AnimalStats.SNAKE.getBadEnvironmentBreedingProbability();
    private final int TIME_ACTIVITY_START = AnimalStats.SNAKE.getTimeActivityStart();
    private final int TIME_ACTIVITY_END = AnimalStats.SNAKE.getTimeActivityEnd();

    /**
     * Create a new born Snake at location in field.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param size      The size of the parent. Might be mutated.
     * @param scent     The scent of the parent. Might be mutated.
     */

    public Snake(Field field, Location location, double size, double scent) {
        super(field, location);
        age = 0;
        energyLevel = MAX_ENERGY;
        this.size = getMutationValue(size);
        this.scent = getMutationValue(scent);
        currentBreedingProbability = BREEDING_PROBABILITY;
    }

    /**
     * Create a new default Snake at location in field.
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Snake(Field field, Location location)
    {
        super(field, location);
        age = rand.nextInt(MAX_AGE);
        energyLevel = rand.nextInt(MAX_ENERGY);
        availableForCoitus = rand.nextBoolean();
        size = AnimalStats.SNAKE.getDefaultSize();
        scent = AnimalStats.SNAKE.getDefaultScent();
        currentBreedingProbability = BREEDING_PROBABILITY;
    }

    /**
     * This is what the snake does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * //@param field The field currently occupied.
     * @param newSnakes A list to return newly born snakes.
     * @param currentTime The current time
     */
    public void act(List<Entity> newSnakes, int currentTime)
    {
        super.act(newSnakes, currentTime);
        if (isAwake(currentTime)) { // The snake is active
            if (isAlive()) {
                if (sex && foundMate((int) scent)) {
                    giveBirth(newSnakes);
                }
                // Move towards a source of food if found.
                Location newLocation = findFood();
                if (newLocation == null) {
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
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
     * Updates the snake's nutrition value, depending on its age.
     */
    protected void updateFoodValue()
    {
        foodValue = (int) Math.round(3*Math.log(Math.pow(age, 3))); //log grows fast in the beginning and slower at later stages
    }

    /**
     * Returns the max energy of this species. Animals cant eat if they have reached their max energy.
     * @return The maximum energy
     */
    protected int getMaxEnergy()
    {
        return MAX_ENERGY;
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
