/**
 * Plant Class - A plat is a superclass that provides an enum for all of the plant objects.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */


public abstract class Plant extends Entity {

    /**
     * The plants and their statistics that are needed when acting.
     */
    enum PlantStats {

        GRASS(3, 0.3, 8, 0, 6, 21, 15),
        TREE(10, 0.025, 2, 1, 6, 21, 50);

        private int breedingAge; // The minimum age a plant needs to be to be able to breed
        private double breedingProbability; // The default probability a plant propagates
        private double badEnvironmentBreedingProbability; // The probability of a plant for propagating when in the wrong environment.
        private int maxLitterSize; // The maximum amount of new seeds produced.
        private int level; // The level the plant is stored at in the map
        private int timeActivityStart; // The time this plant starts its activity. Uses 24 hour format
        private int timeActivityEnd; // The time this plant ends its activity. Uses 24 hour format
        private int maxAge; // The maximum age a plant can reach. It dies afterwards

        /**
         * Create the parameters for a type of plant. Can then be used in the new species to retrieve the values.
         * The superclasses are created to then automatically include the new species in all processes.
         *
         * @param breedingAge The minimum age a plant needs to be to be able to breed
         * @param breedingProb The default probability a plant propagates
         * @param litterSize The probability of a plant for propagating when in the wrong environment.
         * @param level The maximum amount of new seeds produced.
         * @param timeStart The time this plant starts its activity. Uses 24 hour format
         * @param timeEnd The time this plant ends its activity. Uses 24 hour format
         * @param maxAge The maximum age a plant can reach. It dies afterwards
         */
        PlantStats(int breedingAge, double breedingProb, int litterSize, int level, int timeStart, int timeEnd, int maxAge) {
            this.breedingAge = breedingAge;
            this.breedingProbability = breedingProb;
            this.maxLitterSize = litterSize;
            this.level = level;
            this.timeActivityStart = timeStart;
            this.timeActivityEnd = timeEnd;
            this.maxAge = maxAge;
            this.badEnvironmentBreedingProbability  = breedingProbability * 0.1; // 90 percent lower
        }

        /**
         * @return The breeding age.
         */
        public int getBreedingAge() {
            return breedingAge;
        }

        /**
         * @return The breeding probability.
         */
        public double getBreeding_Probability() {return breedingProbability;}

        /**
         * @return The max litter size.
         */
        public int getMaxLitterSize() {return maxLitterSize;}

        /**
         * @return The level the entity moves at.
         */
        public int getLevel() {return level;}

        /**
         * @return The time the plant starts its activity at.
         */
        public int getTimeActivityStart() {return timeActivityStart;}

        /**
         * @return The time the plant ends its activity at.
         */
        public int getTimeActivityEnd() {return timeActivityEnd;}

        /**
         * @return The max age the plant can turn.
         */
        public int getMaxAge() { return maxAge; }

        /**
         * @return Get the probability of breeding when not in preferred environment.
         */
        public double getBadEnvironmentBreedingProbability() { return badEnvironmentBreedingProbability; }

    }

    /**
     * Create a new plant
     * @param field The field the plant is put into
     * @param location The location in the field.
     */
    public Plant(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * @return The max age.
     */
    protected abstract int getMaxAge();
}
