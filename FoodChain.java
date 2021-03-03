import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class FoodChain - This class contains a HashMap representing the food each entity eats.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class FoodChain {

    private HashMap<Class, ArrayList<Class>> foodChain;
    private EntityLevelMapper entityLevelMap = new EntityLevelMapper();
    /**
     * Create a HashMap storing the food chain relationships.
     */
    public FoodChain()
    {
        foodChain = new HashMap<>();
        initialiseFoodChain();;
    }

    /**
     * Places the relations in the food chain into the hashmap.
     */
    private void initialiseFoodChain()
    {
        foodChain.put(Tree.class, new ArrayList<>());
        foodChain.put(Grass.class, new ArrayList<>());
        foodChain.put(Frog.class, new ArrayList<>());

        foodChain.put(Gazelle.class, new ArrayList<>(Arrays.asList(Grass.class, Tree.class)));
        foodChain.put(Lion.class, new ArrayList<>(Arrays.asList(Gazelle.class)));
        foodChain.put(Eagle.class, new ArrayList<>(Arrays.asList(Gazelle.class, Frog.class)));
        foodChain.put(Snake.class, new ArrayList<>(Arrays.asList(Frog.class)));
    }

    /**
     * Get the food sources of the specified animal
     * @param entityClass The class of the entity.
     * @return An ArrayList of the types of entities the given entity can eat.
     */
    public ArrayList<Class> getFoodSources(Class entityClass)
    {
        return foodChain.get(entityClass);
    }

    /**
     * Returns a list of the levels the food sources of an entity are in
     * @param entityClass The class of the entity
     * @return The levels of the food sources of the entity
     */
    public ArrayList<Integer> getFoodSourceLevels(Class entityClass)
    {
        ArrayList<Class> currentFoods = foodChain.get(entityClass);
        ArrayList<Integer> levels = new ArrayList<>();
        for (int i = 0; i < currentFoods.size(); i++)
        {
            levels.add(entityLevelMap.getEntityLevel(currentFoods.get(i)));
        }
        return levels;
    }

}
