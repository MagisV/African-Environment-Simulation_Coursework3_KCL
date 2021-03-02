import java.util.HashMap;

public class EntityEnvironmentMapper {

    private HashMap<Class, String> entityEnvironmentMap;

    public EntityEnvironmentMapper()
    {
        entityEnvironmentMap = new HashMap<>();
        initializeMap();
    }

    //ENUM?
    private void initializeMap()
    {
        entityEnvironmentMap.put(Eagle.class, "Desert");
        entityEnvironmentMap.put(Tree.class, "Forest");
        //entityEnvironmentMap.put(Snake.class, "Forest");
        entityEnvironmentMap.put(Gazelle.class, "Savanna");
        entityEnvironmentMap.put(Lion.class, "Savanna");
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
