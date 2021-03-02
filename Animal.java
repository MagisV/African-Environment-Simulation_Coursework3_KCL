import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * @author David J. Barnes and Michael Kölling, edited by Valentin Magis and Barnabas Szalai
 * @version 2021-03-02
 */
public abstract class Animal extends Entity
{
    /**
     * The animals and their statistics that can be used.
     */
    enum AnimalStats {

        EAGLE(0, 0.35, 10, 3, 6, 21, 4, 3, 50, 50, 3),
        FROG(1, 0.82,  6, 2, 6, 21, 1, 1, 50, 5, 1),
        GAZELLE(5, 0.08, 2, 2, 6, 21, 2, 1, 25, 20, 1),
        LION(15, 0,  2, 2, 6, 21, 5, 3, 50, 50, 2),
        SNAKE(5, 0.8, 5, 2, 6, 21, 5, 3, 50, 50, 1);


        private final int breedingAge;
        private final double breedingProbability;
        private final double badEnvironmentBreedingProbability;
        private final int maxLitterSize;
        private final int level;
        private final int timeActivityStart;
        private final int timeActivityEnd;
        private final double defaultSize;
        private final double defaultScent;
        private final int maxEnergy;
        private final int maxAge;
        private final int movingRadius;


        /**
         * Create a new animal type
         * @param breedingAge The minimum age of the animal to breed.
         * @param breedingProbability The probability with which this animal produces offspring
         * @param maxLitterSize The maximum litter size of one breeding process
         * @param level The level at which the animal moves in the map
         * @param timeActivityStart The time the animal starts its activity
         * @param timeActivityEnd The time the animal ends its activity
         * @param defaultSize The default size of the animal.
         * @param defaultScent The default scent of the animal.
         * @param maxEnergy The maximum energy this animal can have
         * @param maxAge The maximum age this animal can reach. If reached, the animal dies.
         * @param movingRadius The amount of fields the animal can move with one step.
         */
        AnimalStats(int breedingAge, double breedingProbability, int maxLitterSize, int level, int timeActivityStart,
                    int timeActivityEnd, double defaultSize, double defaultScent, int maxEnergy, int maxAge, int movingRadius) {
            this.breedingAge = breedingAge;
            this.breedingProbability = breedingProbability;
            this.maxLitterSize = maxLitterSize;
            this.level = level;
            this.timeActivityStart = timeActivityStart;
            this.timeActivityEnd = timeActivityEnd;
            this.defaultSize = defaultSize;
            this.defaultScent = defaultScent;
            this.maxEnergy = maxEnergy;
            this.maxAge = maxAge;
            this.movingRadius = movingRadius;
            this.badEnvironmentBreedingProbability  = breedingProbability * 0.1; // 90 percent lower
        }

        /**
         * Return the breeding age of the animal
         * @return the breeding age
         */
        public int getBreedingAge() {
            return breedingAge;
        }

        /**
         * Returns the breeding probability of the animal
         * @return  the breeding probability
         */
        public double getBreeding_Probability() { return breedingProbability; }

        /**
         * Returns the maximum number of babies that a particular female animal can produce.
         * @return the max litter size
         */
        public int getMaxLitterSize() { return maxLitterSize; }

        /**
         * Returns the level where the animal resides
         * @return the level of the animal
         */
        public int getLevel() { return level; }

        /**
         * Returns the start time from which the animal starts to act
         * @return the time of the start of its activity
         */
        public int getTimeActivityStart() { return timeActivityStart; }

        /**
         * Returns the time until the animal acts
         * @return the time of the activity end
         */
        public int getTimeActivityEnd() { return timeActivityEnd; }

        /**
         * Returns the default scent of each animal when it is born
         * @return the default scent of the animal
         */
        public double getDefaultScent() { return defaultScent; }

        /**
         * Returns the default size of the animal when it is born
         * @return the default size of the animal
         */
        public double getDefaultSize() { return defaultSize; }

        /**
         * Returns the maximum energy that an animal can reach in its lifetime
         * @return the maximum energy of an animal
         */
        public int getMaxEnergy() { return maxEnergy; }

        /**
         * Returns the maximum age at which the animal dies automatically
         * @return the maximum age of an animal
         */
        public int getMaxAge() { return maxAge; }

        /**
         * Returns the radius at which the animal can move at a time
         * @return the moving radius of an animal
         */
        public int getMovingRadius() { return movingRadius; }

        /**
         * Returns the breeding probability of the animal when it is located in an environment that is not its preferred
         *  one
         * @return the breeding probability of an animal at the bad environment
         */
        public double getBadEnvironmentBreedingProbability() { return badEnvironmentBreedingProbability; }
    }

    //The sex of the animal. True is male.
    protected boolean sex;
    protected boolean availableForCoitus;
    protected double energyLevel; // The energy an animal currently has. // We should make a max energy

    // Mutation
    protected double size; // The size of the animal. Impacts competition and energy consumption.
    protected double scent; // The scent of the animal. Impacts how far it can look for food and energy consumption.
    private final static double MUTATION_PROBABILITY = 0.01;

    /**
     * Create a new animal at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field, location);
        this.sex = rand.nextBoolean();
        this.availableForCoitus = false;
    }

    /**
     * Calls the act method in entity. It also increments either the hunger or the energy level of the animal.
     * @param newEntities A list to receive newly born animals.
     * @param currentTime the current time of the day.
     */
    @Override
    public void act(List<Entity> newEntities, int currentTime) {
        super.act(newEntities, currentTime);
        if(isAwake(currentTime)) {
            incrementHunger();
        }
        else
            energyLevel++; // Animal is sleeping and regathering energy
    }

    /**
     * Get the maximum energy of the animal
     * @return The max energy.
     */
    protected abstract int getMaxEnergy();

    /**
     * Get the maximum age of the animal
     * @return The maximum age.
     */
    protected abstract int getMaxAge();

    /**
     * Increment the hunger of the animal.
     * Relates to the size and scent of the animal. This is the disadvantage the counterpart to size and scent.
     */
    protected void incrementHunger()
    {
        energyLevel -= Math.round((size + scent) / 2);
        if (energyLevel <= 0)
        {
            setDead();
        }
    }

    /**
     * A method for searching for a partner. Animals propagate every 2nd time they meet another animal.If an animal
     * meets another animal and that animal can't propagate, that animal will be able to propagate when it meets
     * another animal the next time.
     * @param radius the radius at which the animal can look for a mate.
     * @return whether an available mate in the given radius was found.
     */
    public boolean foundMate(int radius) {
        boolean found = false;
        Field field = getField();
        List<Location> nearby = field.nearbyLocations(getLocation(), radius);
        Iterator<Location> it = nearby.iterator();
        while(it.hasNext() && !found) {
            Location where = it.next();
            Object animal = field.getEntityAt(where);

            if(animal != null && animal.getClass() == this.getClass()) {
                Animal mate = (Animal) animal;
                if(mate.availableForCoitus) {
                    found = true;
                    mate.availableForCoitus = false;
                } else {
                    mate.availableForCoitus = true;
                }
            }
        }
        return found;
    }

    /**
     * //Maybe refactor this into multiple mehtods?
     * Look for food sources in adjacent locations.
     * Only the first found food source is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = new ArrayList<>();
        adjacent.addAll(field.adjacentLocations(getLocation(), foodChain.getFoodSourceLevels(this.getClass()), (int) Math.round(scent))); //Get the adjacent locations of the levels the current animal eats at and with the scent range this animal has. (E.g. Gazelles look for food at level 0 and 1)
        ArrayList<Class> foodSources = foodChain.getFoodSources(this.getClass()); // Get the entities this animal eats.

        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Entity entity = field.getEntityAt(where);

            if (canEat(entity, foodSources)) {
                energyLevel += entity.getFoodValue();
                entity.setDead();
                return new Location(where.getRow(), where.getCol(), this.getLocation().getLevel()); // The location this animal will move to. The level might differ
            }

        }
        return null;
    }

    /**
     * The conditions that must be fulfilled for one entity eating another entity.
     * @param entity The eating entity
     * @param foodSources The eating entities food sources.
     * @return If this entity can eat the other entity.
     */
    private boolean canEat(Entity entity, ArrayList<Class> foodSources)
    {
        return entity != null
                && foodSources.contains(entity.getClass()) // Check if the found entity can be eaten by the entity searching for food
                && entity.isAlive()  // Check if the found entity is still alive (has not been eaten by another entity already)
                && entity.isEatable() // and if it is eatable. E.g. Trees are only eatable when young.
                && field.isFree(location) // Check if there is no other entity occupying this location.
                && getMaxEnergy() > energyLevel + entity.getFoodValue() // Eating this entity would not exceed the max energy
                && ((entity instanceof Animal && wonFight(this, (Animal) entity)) // The searching animal found another animal (not a plant) and was able to catch it.
                || entity instanceof Plant); // Or the found entity was a plant
    }

    /**
     * A function to simulate a fight between a predator and a prey. Returns true when the predator wins.
     * @param predator the predator that is chasing the prey
     * @param prey the prey that the predator wants to eat
     * @return a boolean whether the predator successfully faught the prey
     */
    private boolean wonFight(Animal predator, Animal prey)
    {
        if (prey.getSize() < predator.getSize() * rand.nextDouble()) // Generates a double within the range of 0 and the predators size. If the generated number is within in preys size, the prey wins the fight and is not eaten
            return true;
        else
            return false;
    }

    /**
     * Returns the size of the animal object
     * @return the size of the animal
     */
    private double getSize()
    {
        return size;
    }

    /**
     * Slightly increases or decreases a given value by a certain probability.
     * @param value The value to be changed
     * @return If changed, the new value, otherwise the given value.
     */
    protected double getMutationValue(double value)
    {
        if (rand.nextDouble() <= MUTATION_PROBABILITY) {
            if (rand.nextBoolean())
                value += 0.3;
            else
                value -= 0.3;
        }
        return value;
    }

    /**
     * Check whether or not this entity is to propagate at this step.
     * New births will be made into free adjacent locations.
     * @param newEntities A list to return newly born entities.
     */
    protected void giveBirth(List<Entity> newEntities)
    {
        // New animals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            createAnimal(field, loc, size, scent, newEntities);
        }
    }

    /**
     * Create a new animal
     * @param field The field to put the entitiy in
     * @param loc The location of the new entity
     * @param size The size passed on from the parent.
     * @param scent The scent passed on from the parent
     * @param newEntities A list to add the newly created animal to.
     */
    private void createAnimal(Field field, Location loc, double size, double scent, List<Entity> newEntities)
    {
        Entity young;
        if (this.getClass() == Frog.class)
            young = new Frog(field, loc, size, scent);
        else if (this.getClass() == Gazelle.class)
            young = new Gazelle(field, loc, size, scent);
        else if (this.getClass() == Lion.class)
            young = new Lion(field, loc, size, scent);
        else if (this.getClass() == Eagle.class)
            young = new Eagle(field, loc, size, scent);
        else
            young = new Snake(field, loc, size, scent);
        newEntities.add(young);
    }
}




