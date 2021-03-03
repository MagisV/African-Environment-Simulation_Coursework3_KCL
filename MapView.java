import java.awt.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the map grid.
 * Its displays the different environments on the map using three different colors. A particular environment can be
 * selected using the buttons on the ControlPanel, and a particular environment on the view will be colored.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class MapView extends JFrame
{
    // Color used for objects that have no defined color.

    private final String STEP_PREFIX = "";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population, infoLabel;
    private FieldView fieldView;

    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    private String currentEnvironment;

    /**
     * Create a view of the given width and height.
     * @param height The sim height.
     * @param width  The simulation's width.
     */
    public MapView(int height, int width)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();

        setTitle("Map Environment Viewer");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        infoLabel = new JLabel("  ", JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        currentEnvironment = "Savanna";

        setLocation(500, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();

        JPanel infoPane = new JPanel(new BorderLayout());
        infoPane.add(stepLabel, BorderLayout.WEST);
        infoPane.add(infoLabel, BorderLayout.CENTER);
        contents.add(infoPane, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * A setter method for the environment instance variable.
     * @param environment The environment to be set.
     */
    public void setCurrentEnvironment(String environment) {
        currentEnvironment = environment;
    }

    /**
     * Displays the environments using different colors.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(Field field)
    {

        stepLabel.setText("");
        stats.reset();

        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(col <= field.getSavannaEnvironment().getEndCol()) {
                    if(!"Savanna".equals(currentEnvironment)) {
                        fieldView.drawMark(col, row, Color.gray);
                    } else {
                        fieldView.drawMark(col, row, Color.YELLOW);
                    }
                } else if(col > field.getSavannaEnvironment().getEndCol() && col <= field.getForestEnvironment().getEndCol()) {
                    if(!"Forest".equals(currentEnvironment)) {
                        fieldView.drawMark(col, row, Color.gray);
                    } else {
                        fieldView.drawMark(col, row, Color.GREEN);
                    }
                } else {
                    if(!"Desert".equals(currentEnvironment)) {
                        fieldView.drawMark(col, row, Color.gray);
                    } else {
                        fieldView.drawMark(col, row, Color.ORANGE);
                    }
                }
            }
        }
        stats.generateCounts(field);
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field, currentEnvironment));
        fieldView.repaint();
    }

    /**
     * Provide a graphical view of a rectangular field. This is
     * a nested class (a class defined inside a class) which
     * defines a custom component for the user interface. This
     * component displays the field.
     * This is rather advanced GUI stuff - you can ignore this
     * for your project if you like.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }
}
