import java.awt.Color;

/**
 * Provide a counter for a participant in the simulation.
 * This includes an identifying string and a count of how
 * many participants of this type currently exist within 
 * the simulation. It also stores a count of entities for each environment of the field.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class Counter
{
    // A name for this type of simulation participant
    private String name;
    // How many of this type exist in the simulation.
    private int count;
    // How many of this type exist in the savanna environment
    private int savannaCount;
    // How many of this type exist in the forest environment
    private int forestCount;
    // How many of this type exist in the desert environment
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

    /**
     * Returns an integer value of the number of the total
     * @param environment
     * @return
     */

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

    /**
     * Increment the current count by one for both the total and the current environment
     * @param environment the environment whose population is to be incremented.
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
