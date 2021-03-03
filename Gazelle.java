import java.util.List;

/**
 * A simple model of a gazelle.
 * gazelles age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Gazelle extends Animal
{
    // Characteristics shared by all gazelles (class variables).
    private static final int BREEDING_AGE = AnimalStats.GAZELLE.getBreedingAge();  // The age at which a gazelle can start to breed.
    private static final int MAX_LITTER_SIZE = AnimalStats.GAZELLE.getMaxLitterSize();     // The maximum number of births.

    private static final int MAX_ENERGY = AnimalStats.GAZELLE.getMaxEnergy();
    private static final int MAX_AGE = AnimalStats.GAZELLE.getMaxAge();

    private final double BREEDING_PROBABILITY = AnimalStats.GAZELLE.getBreeding_Probability(); // The likelihood of a gazelle breeding.
    private final double BAD_ENVIRONMENT_BREEDING_PROBABILITY = AnimalStats.GAZELLE.getBadEnvironmentBreedingProbability();

    private final int TIME_ACTIVITY_START = AnimalStats.GAZELLE.getTimeActivityStart();
    private final int TIME_ACTIVITY_END = AnimalStats.GAZELLE.getTimeActivityEnd();

    /**
     * Create a new born Frog at location in field.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     * @param size      The size of the parent. Might be mutated.
     * @param scent     The scent of the parent. Might be mutated.
     */
    public Gazelle(Field field, Location location, double size, double scent) {
        super(field, location);
        age = 0;
        energyLevel = MAX_ENERGY;
        this.size = getMutationValue(size);
        this.scent = getMutationValue(scent);
        currentBreedingProbability = BREEDING_PROBABILITY;
    }

    /**
     * Create a new default Frog at location in field.
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Gazelle(Field field, Location location)
    {
        super(field, location);

        age = rand.nextInt(MAX_AGE);
        energyLevel = rand.nextInt(MAX_ENERGY);
        availableForCoitus = rand.nextBoolean();
        size = AnimalStats.LION.getDefaultSize();
        scent = AnimalStats.LION.getDefaultScent();
        currentBreedingProbability = BREEDING_PROBABILITY;
    }
    
    /**
     * This is what the gazelle does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param newGazelles A list to return newly born gazelles.
     * @param currentTime
     */
    public void act(List<Entity> newGazelles, int currentTime)
    {
        super.act(newGazelles, currentTime);
        if (isAwake(currentTime)) {
            if (isAlive()) {
                if (sex && foundMate(8)) {
                    giveBirth(newGazelles);
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
     * Updates the gazelle's foodValue, depending on its age.
     */
    protected void updateFoodValue()
    {
        foodValue = (int) Math.round(2*Math.log(Math.pow(age, 2))); //log grows fast in the beginning and slower at later stages
    }

    /**
     * Returns the max energy of this species. Animals cant eat if they have reached their max energy.
     * @return The maximal energy
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
