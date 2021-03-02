import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */

// Example of how to get a value out of an enum: AnimalStats.EAGLE.getBreedingAge() -->  returns 5
public abstract class Animal extends Entity
{

    enum AnimalStats {

        EAGLE(0, 0.35, 10, 3, 6, 21, 4, 3, 50, 50, 3),
        FROG(1, 0.82,  6, 2, 6, 21, 1, 1, 50, 5, 1),
        GAZELLE(5, 0.08, 2, 2, 6, 21, 2, 1, 25, 20, 1),
        LION(15, 0,  2, 2, 6, 21, 5, 3, 50, 50, 2),
        SNAKE(5, 0.8, 5, 6, 6, 21, 5, 3, 50, 50, 1);


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

        public int getBreedingAge() {
            return breedingAge;
        }
        public double getBreeding_Probability() { return breedingProbability; }
        public int getMaxLitterSize() { return maxLitterSize; }
        public int getLevel() { return level; }
        public int getTimeActivityStart() { return timeActivityStart; }
        public int getTimeActivityEnd() { return timeActivityEnd; }
        public double getDefaultScent() { return defaultScent; }
        public double getDefaultSize() { return defaultSize; }
        public int getMaxEnergy() { return maxEnergy; }
        public int getMaxAge() { return maxAge; }
        public int getMovingRadius() { return movingRadius; }
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

    @Override
    public void act(List<Entity> newEntities, int currentTime) {
        super.act(newEntities, currentTime);
        if(isAwake(currentTime)) {
            incrementHunger();
        }
        else
            energyLevel++; // Animal is sleeping and regathering energy
    }


    protected abstract int getMaxEnergy();
    protected abstract int getMaxAge();

    protected void incrementHunger()
    {
       // energyLevel -= 2 * size + scent;
        if (energyLevel <= 0)
        {
            setDead();
        }
    }

    //Method for searching for a partner
    //Animals can propagate every 2nd time they meet another animal. If an animal meets another animal and that animal
    //can't propagate, that animal will be able to propagate when it meets another animal the next time.
    public boolean foundMate(int radius) {
        boolean found = false;
        Field field = getField();
        List<Location> nearby = field.nearbyLocations(getLocation(), radius);
        Iterator<Location> it = nearby.iterator();
        while(it.hasNext() && !found) {
            Location where = it.next();
            Object animal = field.getEntityAt(where);

            if(animal != null && animal.getClass() == this.getClass()) {
                //System.out.println(animal.getClass() + ", " + this.getClass());
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

            if (entity != null
                    && foodSources.contains(entity.getClass()) // Check if the found entity can be eaten by the entity searching for food
                    && entity.isAlive()  // Check if the found entity is still alive (has not been eaten by another entity already)
                    && entity.isEatable() // and if it is eatable. E.g. Trees are only eatable when young.
                    && field.isFree(location) // Check if there is no other entity occupying this location.
             )
            { // Needs to be able to eat plants as well
                if (getMaxEnergy() > energyLevel + entity.getFoodValue() // Eating this entity would not exceed the max energy
                        && ((entity instanceof Animal && wonFight(this, (Animal) entity)) // The searching animal found another animal (not a plant) and was able to catch it.
                        || entity instanceof Plant)) // Or the animal found a plant and eats it.
                {
                    energyLevel += entity.getFoodValue();
                    entity.setDead();
                    return new Location(where.getRow(), where.getCol(), this.getLocation().getLevel()); // The location this animal will move to. The level might differ
                }
            }
        }
        return null;
    }



    private boolean wonFight(Animal predator, Animal prey)
    {
        if (prey.getSize() < predator.getSize() * rand.nextDouble()) // Generates a double within the range of 0 and the predators size. If the generated number is within in preys size, the prey wins the fight and is not eaten
            return true;
        else
            return false;
    }

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
        //double newValue = value;
        double mutation = rand.nextDouble();
        if (mutation <= MUTATION_PROBABILITY) {
            if (rand.nextBoolean()) {
                //newValue+=0.3;
                //if (newValue >= 5.6)
                //System.out.println("PLUS  " + value + " to " + newValue);
                value += 0.3;
            }
            else
               // newValue-=0.3;
            //if (newValue <= 2.6)
               // System.out.println("MINUS "  + value + " to " + newValue);
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
        // New lions are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Lion young = new Lion(field, loc, size, scent); // Pass on the genes. Might mutate (in the constructor)
            newEntities.add(young);
        }
    }
}




