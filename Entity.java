import java.util.List;
import java.util.Random;


/**
 * Class Entity - An entity is a living thing that is represented in the simulator. All animals and plants indirectly inherit from this class.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public abstract class Entity {

    protected boolean alive; // Whether the entity is alive or not.
    protected Field field;   // The entity's field.
    protected Location location; // The entity's position in the field.
    protected double currentBreedingProbability; // The current breeding probability of the entity. Changes if it is not in its preferred environment.

    protected static final Random rand = Randomizer.getRandom(); // A randomizer shared by all entities, used for propagating.
    protected int foodValue; // How much energy another entity receives by eating this entity.
    protected int age; // The entities age

    protected static final FoodChain foodChain = new FoodChain(); // Stores the food sources of each entity, if it has any.
    private static EntityEnvironmentMapper entityEnvironmentMapper; // A map of the preferred environments of the each entity type.


    /**
     * Create a new entity at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Entity(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        entityEnvironmentMapper = new EntityEnvironmentMapper();
    }

    /**
     * Make this entity act - that is: make it do
     * whatever it wants/needs to do.
     * @param newEntities A list to receive newly created entities.
     * @param currentTime The current time
     */
    public void act(List<Entity> newEntities, int currentTime)
    {
        incrementAge();
        updateFoodValue();
        updateEnvironment();
    }

    /**
     * Check whether the entity is alive or not.
     * @return true if the entity is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the entity is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the entity's location.
     * @return The entity's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * Place the entity at the new location in the given field.
     * @param newLocation The entity's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the entity's field.
     * @return The entity's field.
     */
    protected Field getField()
    {
        return field;
    }

    /**
     * Update the entities foodValue
     */
    protected abstract void updateFoodValue();

    /**
     * Return the foodValue of this entity.
     * @return
     */
    protected int getFoodValue()
    {
        return foodValue;
    }

    /**
     * Increment the age of this entity.
     */
    protected void incrementAge()
    {
        age++;
        if (age > this.getMaxAge())
        {
            setDead();
        }
    }

    /**
     * Returns if this entity can be eaten. True by default, overwritten if further conditions apply to being eatable.
     * @return if the entity is eatable.
     */
    protected boolean isEatable()
    {
        return true;
    }

    /**
     * Returns the maximum age of an entity.
     * @return The max age of an entity
     */
    protected abstract int getMaxAge();

    /**
     * Returns the breeding probability when the entity is at the environment which is not its preferred one
     * @return the breeding probability of the bad environment
     */
    protected abstract double getBadEnvironmentBreedingProbability();

    /**
     * Returns the breeding probability of an entity
     * @return the breeding probability of an entity
     */
    protected abstract double getBreedingProbability();

    /**
     * Returns the maximum number of newborns an entity can produce
     * @return the maximum litter size
     */
    protected abstract int getMaxLitterSize();

    /**
     * Returns the age at which the entity can start generating offsprings
     * @return the breeding age of the entity
     */
    protected abstract int getBreedingAge();

    /**
     * Returns the time when the animal starts to act
     * @return the start of the activity time
     */
    protected abstract int getTimeActivityStart();

    protected abstract int getTimeActivityEnd();

    /**
     * Return if the entity is in its preferred environment. Some animals have a preferred environment where they can live better.
     * @return If the entity is in its preferred environment.
     */
    protected void updateEnvironment()
    {
        if (isAlive()) { // The animal is still alive
            String environment = entityEnvironmentMapper.getPreferredEnvironment(this);
            if (environment != null) { // There is a preference entry for this animal in the
                if (field.getCurrentEnvironment(this.getLocation()).equals(environment)) // The preferred environment is equal to the current environment the entity is in
                    currentBreedingProbability = getBreedingProbability();
                else
                    currentBreedingProbability = getBadEnvironmentBreedingProbability();
            }
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can propagate.
     * @return The number of seeds (may be zero).
     */
    protected int breed()
    {
        int newEntities = 0;
        if(canBreed() && rand.nextDouble() <= currentBreedingProbability) {
            newEntities = rand.nextInt(getMaxLitterSize()) + 1;
        }
        if (newEntities > 0 && this.getClass() != Grass.class)
        {
            System.out.println(this.getClass() + ": " + newEntities);
        }
        return newEntities;
    }

    /**
     * A frog can breed if it has reached the breeding age.
     * @return true if the frog can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= getBreedingAge();
    }

    /**
     * Return if this entity is awake.
     * @param currentTime The current time.
     * @return If this entity is awake
     */
    protected boolean isAwake(int currentTime)
    {
        return getTimeActivityStart() <= currentTime && getTimeActivityEnd() >= currentTime;
    }



}
