/**
 * Class Environment - A class that keeps track of the borders of each environment.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */

public class Environment{

    private String name;
    private int startCol;
    private int endCol;


    /**
     * A constructor method that initializes the instance variables of the class.
     * @param name the name of the environment
     * @param startCol the start width position of the environment
     * @param endCol the end width position of the environment
     */
    public Environment(String name, int startCol, int endCol) {
        this.name = name;
        this.startCol = startCol;
        this.endCol = endCol;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the left border of the environment
     * @return the start col
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * Returns the right border of the environment
     * @return the end col
     */
    public int getEndCol() {
        return endCol;
    }
}
