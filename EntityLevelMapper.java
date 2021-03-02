import java.util.HashMap;
import java.util.HashSet;

/**
 * Helperclass EntityLevelMapper
 * This class specifies at what level of the map each entity type is moving at.
 *
 * APPLY THIS TO THE POPULATE METHOD
 */
public class EntityLevelMapper {

    private static HashMap<Class, Integer> entityLevelMap;

    public EntityLevelMapper() {
        entityLevelMap = new HashMap<>();
        initializeMap();
    }

    /**
     * Initialize the level map. This implies at what level in the map the animal moves and indirectly also at what other levels this entity ma look for food.
     */
    private void initializeMap() {
        entityLevelMap.put(Eagle.class, 3);
        entityLevelMap.put(Frog.class, 2);
        entityLevelMap.put(Gazelle.class, 2);
        entityLevelMap.put(Lion.class, 2);
        entityLevelMap.put(Snake.class, 2);
        entityLevelMap.put(Tree.class, 1);
        entityLevelMap.put(Grass.class, 0);
    }

    /**
     * Get the level this entity moves at in the map.
     *
     * @param entityClass The desired entity
     * @return Its level in the map.
     */
    public int getEntityLevel(Class entityClass) {
        return entityLevelMap.get(entityClass);
    }

    /**
     * Return the levels specified in the entityLevelMap.
     *
     * @return The number of levels specified.
     */
    public int getAmountOfLevels() {
        HashSet set = new HashSet(entityLevelMap.values());
        return set.size();
    }
}
