public abstract class Plant extends Entity {
    //Biome biome;

    enum PlantStats {

        GRASS(3, 0.3, 8, 0, 6, 21, 15),
        TREE(20, 0.03, 2, 1, 6, 21, 50);

        private int breedingAge;
        private double breedingProbability;
        private double badEnvironmentBreedingProbability;
        private int maxLitterSize;
        private int level;
        private int timeActivityStart;
        private int timeActivityEnd;
        private int maxAge;

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

        public int getBreedingAge() {
            return breedingAge;
        }
        public double getBreeding_Probability() {return breedingProbability;}
        public int getMaxLitterSize() {return maxLitterSize;}
        public int getLevel() {return level;}
        public int getTimeActivityStart() {return timeActivityStart;}
        public int getTimeActivityEnd() {return timeActivityEnd;}
        public int getMaxAge() { return maxAge; }
        public double getBadEnvironmentBreedingProbability() { return badEnvironmentBreedingProbability; }

    }

    public Plant(Field field, Location location)
    {
        super(field, location);
    }
    protected abstract int getMaxAge();
}
