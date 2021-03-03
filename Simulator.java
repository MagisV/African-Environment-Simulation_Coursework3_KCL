import java.util.*;
import java.awt.Color;

/**
 * Class Simulator - Provides the frame for a simulation. Initializes a newly created map with animals according to a
 * certain probability. A loop lets all entities act at each step.
 A
 * @author David J. Barnes and Michael Kölling edited by Valentin Magis and Barnabas Szalai
 * @version 2021-03-02
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.

    private static final int DEFAULT_WIDTH = 150;   // The default width for the grid.
    private static final int DEFAULT_DEPTH = 100;  // The default depth of the grid.

    private static final double LION_CREATION_PROBABILITY = 0.02;
    private static final double GAZELLE_CREATION_PROBABILITY = 0.06;
    private static final double EAGLE_CREATION_PROBABILITY = 0.005;
    private static final double GRASS_CREATION_PROBABILITY = 0.1;
    private static final double TREE_CREATION_PROBABILITY = 0.01;
    private static final double FROG_CREATION_PROBABILITY = 0.02;
    private static final double SNAKE_CREATION_PROBABILITY = 0.06;

    private static final int DAY_LENGTH = 15;

    private List<Entity> entities;   // List of animals in the field.
    private Stack<List<Entity>> previousEntities;
    private Stack<List<Entity>> nextEntities;
    private Field field;  // The current state of the field.
    private int step;  // The current step of the simulation.
    private SimulatorView view;  // A graphical view of the simulation.
    private ControlPanel control;

    private MapView mapView;

    private static final Time clock = new Time(DAY_LENGTH); // A clock imitating time. Animals behave differently at different times.


    /**
     * Construct a simulation field with default size.
     * @param steps
     */
    public Simulator(int steps)
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH, steps);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width, int step)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = 50;//DEFAULT_DEPTH;
            width = 50;//DEFAULT_WIDTH;
        }

        previousEntities = new Stack<>();
        nextEntities = new Stack<>();
        
        entities = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        mapView = new MapView(depth, width);
        control = new ControlPanel();
        this.step = step;

        setColors(view);
        // Setup a valid starting point.
        reset();
        mapView.setVisible(false);
    }

    /**
     * Toggle showing the map.
     * @return If the map is showing.
     */
    public boolean switchMapShowing() {
        mapView.setVisible(!mapView.isVisible());
       return !mapView.isVisible();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        int tempStep = this.step+1;
        while(tempStep <= numSteps && view.isViable(field) && !control.getPaused()) {
            simulateOneStep();
            tempStep++;
        }
    }

    /**
     * @return The current step of the simulation.
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets the current environment to be highlighted. Called from the control panel.
     * @param environment The environment to be highlighted.
     */
    public void setCurrentEnvironmentInspection(String environment) {
        mapView.setCurrentEnvironment(environment);
        mapView.showStatus(field);
    }

    /**
     * Load the previous step of the map.
     */
    public void loadPreviousStep() {
        System.out.println("loaded previous");
        step--;
        System.out.println(step);
        field.loadPrevious();
        nextEntities.push(entities);
        entities = previousEntities.pop();
        view.showStatus(step, field);
        mapView.showStatus(field);
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each entity.
     */
    public void simulateOneStep()
    {
        if(field.isNext()) {
            step++;
            System.out.println("isNext");
            field.loadNext();
            previousEntities.push(entities);
            entities = nextEntities.pop();
            view.showStatus(step, field);
            mapView.showStatus(field);
        } else {
            step++;
            // Provide space for newborn animals.
            List<Entity> newEntities = new ArrayList<>();
            // Let all entities act.
            for (Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
                Entity entity = it.next();
                entity.act(newEntities, clock.getCurrentTime(step));
                if (!entity.isAlive()) {
                    it.remove();
                }
            }

            // Add the newly born foxes and rabbits to the main lists.
            entities.addAll(newEntities);
            previousEntities.push(entities);
            view.showStatus(step, field);
            mapView.showStatus(field);

            //place current items in the stack
            field.savePrev();
        }
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        entities.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
        mapView.showStatus(field);
    }


    /**
     * Set the colors for each animal type.
     * @param view The SimulatorView the colors refer to.
     */
    private void setColors(SimulatorView view)
    {
        view.setColor(Gazelle.class, Color.ORANGE);
        view.setColor(Lion.class, Color.BLUE);
        view.setColor(Eagle.class, Color.RED);
        view.setColor(Snake.class, Color.PINK);

        Color frogGreen = new Color(0, 255, 64);
        view.setColor(Frog.class, frogGreen);

        Color brown = new Color(90, 60, 35);
        view.setColor(Tree.class, brown);

        Color green = new Color(108, 154, 29);
        view.setColor(Grass.class, green);

    }
    
    /**
     * Randomly populate the field with entities.
     */
    private void populate()
    {
        field.clear();
        EntityLevelMapper entityLevelMap = new EntityLevelMapper();
        for (int row = 0; row < field.getDepth(); row++)
        {
            for (int col = 0; col < field.getWidth(); col++)
            {
                Class newEntityClass = getRandomEntity();
                if (newEntityClass != null) {
                    Location location = new Location(row, col, entityLevelMap.getEntityLevel(newEntityClass)); //the entityLevelMap takes the class of the entity and returns the level it should be stored int.
                    Entity newEntity = null;
                    if (newEntityClass == Grass.class)
                        newEntity = new Grass(true, field, location);
                    else if (newEntityClass == Tree.class)
                        newEntity = new Tree(true, field, location);
                    else if (newEntityClass == Frog.class)
                        newEntity = new Frog(field, location);
                    else if (newEntityClass == Gazelle.class)
                        newEntity = new Gazelle(field, location);
                    else if (newEntityClass == Lion.class)
                        newEntity = new Lion(field, location);
                    else if (newEntityClass == Eagle.class)
                        newEntity = new Eagle(field, location);
                    else if (newEntityClass == Snake.class)
                        newEntity = new Snake(field, location);
                    if (newEntity != null)
                        entities.add(newEntity);
                }
            }
        }
    }

    /**
     * Randomly selects an entity type. Used in populate() to place animals in the map.
     * @return The selected entity type.
     */
    private Class getRandomEntity()
    {
        Random rand = Randomizer.getRandom();
        if (rand.nextDouble() <= GRASS_CREATION_PROBABILITY)
            return Grass.class;
        else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY)
            return Tree.class;
        else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY + LION_CREATION_PROBABILITY)
            return Lion.class;
        else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY + LION_CREATION_PROBABILITY + GAZELLE_CREATION_PROBABILITY)
            return Gazelle.class;
        else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY + LION_CREATION_PROBABILITY + GAZELLE_CREATION_PROBABILITY + FROG_CREATION_PROBABILITY)
            return Frog.class;
        else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY + LION_CREATION_PROBABILITY + GAZELLE_CREATION_PROBABILITY + FROG_CREATION_PROBABILITY + EAGLE_CREATION_PROBABILITY)
            return Eagle.class;
        else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY + GRASS_CREATION_PROBABILITY + LION_CREATION_PROBABILITY + GAZELLE_CREATION_PROBABILITY + FROG_CREATION_PROBABILITY + EAGLE_CREATION_PROBABILITY+SNAKE_CREATION_PROBABILITY)
            return Snake.class;
        else return null; //No entity at this field
    }
}
