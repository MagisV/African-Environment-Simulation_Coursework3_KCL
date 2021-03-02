import java.util.HashMap;
import java.util.List;

/**
 * This class collects and provides some statistical data on the state 
 * of a field. It is flexible: it will create and maintain a counter 
 * for any class of object that is found within the field.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class FieldStats
{
    // Counters for each type of entity (fox, rabbit, etc.) in the simulation.
    private HashMap<Class, Counter> counters;
    // Whether the counters are currently up to date.
    private boolean countsValid;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats()
    {
        // Set up a collection for counters for each type of animal that
        // we might find
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Get details of what is in the field.
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Field field)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            //generateCounts(field);

        }
        for(Class key : counters.keySet()) {
            //System.out.println("asd :" + counters.get(key));
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Get details of what is in the field.
     * @return A string describing what is in the field.
     */
    public String getPopulationDetails(Field field, String environment)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            //generateCounts(field);

        }
        for(Class key : counters.keySet()) {
            //System.out.println("asd :" + counters.get(key));
            Counter info = counters.get(key);
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getEnvironmentCount(environment));
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
    /**
     * Invalidate the current set of statistics; reset all 
     * counts to zero.
     */
    public void reset()
    {
        countsValid = false;
        for(Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of animal.
     * @param animalClass The class of animal to increment.
     */
    public void incrementCount(Class animalClass, String environment)
    {
        Counter count = counters.get(animalClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(animalClass.getName());
            counters.put(animalClass, count);
        }
        count.increment(environment);
    }

    /**
     * Decrement the count for one class of animal.
     * @param animalClass The class of animal to increment.
     */
    public void decrementCount(Class animalClass, Entity entity)
    {
        Counter count = counters.get(animalClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(animalClass.getName());
            counters.put(animalClass, count);
        }
        if(entity.getLocation().getCol() < entity.getField().getWidth()/3) {
            count.increment("Savanna");

        } else if(entity.getLocation().getCol() < (entity.getField().getWidth()/3)*2 && entity.getLocation().getCol() > entity.getField().getWidth()/3) {
            count.increment("Forest");
        } else {
            count.increment("Desert");
        }
    }

    /**
     * Indicate that an animal count has been completed.
     */
    public void countFinished()
    {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        // How many counts are non-zero.
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            if(info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }
    
    /**
     * Generate counts of the number of foxes and rabbits.
     * These are not kept up to date as foxes and rabbits
     * are placed in the field, but only when a request
     * is made for the information.
     * @param field The field to generate the stats for.
     *              //Maybe turn this around?
     */
    public void generateCounts(Field field)
    {
        reset();
        for (int level = 0; level < field.getHeight(); level++) {
            for (int row = 0; row < field.getDepth(); row++) {
                for (int col = 0; col < field.getWidth(); col++) {
                    Entity entity = field.getEntityAt(row, col, level);
                    if (entity != null) {
                        if(col < field.getWidth()/3) {
                            incrementCount(entity.getClass(), "Savanna");
                        } else if(col < (field.getWidth()/3)*2 && col > field.getWidth()/3) {
                            incrementCount(entity.getClass(), "Forest");
                        } else {
                            incrementCount(entity.getClass(), "Desert");
                        }
                    }
                }
            }
        }
        countsValid = true;
    }

    public void setCountsValid(boolean isValid)
    {
        countsValid = isValid;
    }


    public void updateCounter(List<Entity> list) {
        for (Entity entity : list)
        {
            incrementCount(entity.getClass(), "");
        }
    }
}