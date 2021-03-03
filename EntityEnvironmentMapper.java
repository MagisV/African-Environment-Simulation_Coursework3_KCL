import java.util.HashMap;

/**
 * Class EntityEnvironmentMapper - Maps entities to their preferred environment. They have a better chance of propagating there.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class EntityEnvironmentMapper {

    private HashMap<Class, String> entityEnvironmentMap;


    /**
     * Create an EntityEnvironmentMapper. Initialized automatically.
     */
    public EntityEnvironmentMapper()
    {
        entityEnvironmentMap = new HashMap<>();
        initializeMap();
    }

    /**
     *  Initialize the preferred environments of entities. Not all entities have a preferred environment.
     */
    private void initializeMap()
    {
        entityEnvironmentMap.put(Eagle.class, "Desert");
        entityEnvironmentMap.put(Tree.class, "Forest");
        entityEnvironmentMap.put(Snake.class, "Forest");
        entityEnvironmentMap.put(Gazelle.class, "Savanna");
        entityEnvironmentMap.put(Lion.class, "Savanna");
        entityEnvironmentMap.put(Grass.class, "Savanna");
    }

    /**
     * Return the environment this entity usually lives in.
     * @param entity The entity
     * @return The entities preferred environment. Returns null if there is no preference.
     */
    public String getPreferredEnvironment(Entity entity)
    {
        return entityEnvironmentMap.get(entity.getClass());
    }
}
