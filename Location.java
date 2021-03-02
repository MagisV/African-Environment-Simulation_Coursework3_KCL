/**
 * Represent a location in a rectangular grid.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Location
{
    // Row and column positions.
    private int row;
    private int col;
    private int level;

    private String environment;

    /**
     * Represent a row and column.
     * @param row The row.
     * @param col The column.
     */
    public Location(int row, int col, int level)
    {
        this.row = row;
        this.col = col;
        this.level = level;
    }
    
    /**
     * Implement content equality.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol() && level == other.getLevel();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form row,column, level
     * @return A string representation of the location.
     */
    public String toString()
    {
        return row + "," + col + "," + level;
    }
    
    /**
     * Use the top 16 bits for the row value and the bottom for
     * the column. Except for very big grids, this should give a
     * unique hash code for each (row, col) pair.
     * @return A hashcode for the location.
     */
    public int hashCode()
    {
        return (row << 16) + col;
    }
    
    /**
     * Returns the row of the location on the field.
     * @return The row.
     */
    public int getRow()
    {
        return row;
    }
    
    /**
     * Returns the col of the location on the field.
     * @return The column.
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Returns the level at which the location resides.
     * @return the level of the location
     */
    public int getLevel() { return level; }

}
