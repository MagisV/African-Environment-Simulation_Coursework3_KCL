import java.util.HashMap;
import java.util.HashSet;

/**
 * Helperclass EntityLevelMapper - This class specifies at what level of the map each entity type is moving at.
 */
public class EntityLevelMapper {

    private static HashMap<Class, Integer> entityLevelMap;

    public EntityLevelMapper() {
        entityLevelMap = new HashMap<>();
        initializeMap();
    }

    /**
     * Initialize the level map. This implies at what level in the map the animal moves and indirectly also at what other levels this entity may look for food.
     * Levels are received from the enums in Animal and Plant.
     */
    private void initializeMap() {
        entityLevelMap.put(Eagle.class, Animal.AnimalStats.EAGLE.getLevel());
        entityLevelMap.put(Frog.class, Animal.AnimalStats.FROG.getLevel());
        entityLevelMap.put(Gazelle.class, Animal.AnimalStats.GAZELLE.getLevel());
        entityLevelMap.put(Lion.class, Animal.AnimalStats.LION.getLevel());
        entityLevelMap.put(Snake.class, Animal.AnimalStats.SNAKE.getLevel());
        entityLevelMap.put(Tree.class, Plant.PlantStats.TREE.getLevel());
        entityLevelMap.put(Grass.class, Plant.PlantStats.GRASS.getLevel());
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
