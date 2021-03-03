/**
 * Class Time - keeps track of day & night
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class Time {

    public final double DAY_LENGTH;

    /**
     * Create a clock for the given time length.
     * @param dayLength The length of the day.
     */
    public Time(int dayLength)
    {
        DAY_LENGTH = dayLength;
    }


    /**
     * Calculates the current time.
     * @param currentTicks The current ticks of the simulation. Time depends on the ticks.
     * @return The current time. A number between 0 and 24;
     */
    public int getCurrentTime(int currentTicks)
    {
        //This function mimics the flow of time. Depending on the day length, it returns an hour value on the 24h clock.
        return (int) ((24/DAY_LENGTH) * currentTicks) % 24;
    }
}
