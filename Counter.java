import java.awt.Color;

/**
 * Provide a counter for a participant in the simulation.
 * This includes an identifying string and a count of how
 * many participants of this type currently exist within 
 * the simulation.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Counter
{
    // A name for this type of simulation participant
    private String name;
    // How many of this type exist in the simulation.
    private int count;
    private int savannaCount;
    private int forestCount;
    private int desertCount;

    /**
     * Provide a name for one of the simulation types.
     * @param name  A name, e.g. "Fox".
     */
    public Counter(String name)
    {
        this.name = name;
        count = 0;
    }
    
    /**
     * @return The short description of this type.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The current count for this type.
     */
    public int getCount()
    {
        return count;
    }

    public int getEnvironmentCount(String environment){
        if(environment.equals("Forest")) {
            return forestCount;
        } else if(environment.equals("Savanna")) {
            return savannaCount;
        } else if (environment.equals("Desert")) {
            return desertCount;
        } else {
            return 0;
        }
    }

    public int getBeachCount()
    {
        return savannaCount;
    }

    public int getForestCount()
    {
        return forestCount;
    }

    public int getDesertCount()
    {
        return desertCount;
    }

    /**
     * Increment the current count by one.
     */
    public void increment(String environment)
    {
        if(environment.equals("Forest")) {
            forestCount++;
        } else if (environment.equals("Desert")) {
            desertCount++;
        } else {
            savannaCount++;
        }
        count++;
    }

    /**
     * Decrement the current count by one.
     */
    public void decrement(String environment) {
        if(environment.equals("Forest")) {
            forestCount--;
        } else if (environment.equals("Desert")) {
            desertCount--;
        } else {
            savannaCount--;
        }
        count--;
    }
    
    /**
     * Reset the current count to zero.
     */
    public void reset()
    {
        count = 0;
        forestCount = 0;
        savannaCount = 0;
        desertCount = 0;
    }
}
