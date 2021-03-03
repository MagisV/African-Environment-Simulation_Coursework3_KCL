import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class Control Panel - A control panel that allows for the manipulation of the simulation. It allows for stopping
 * the simulation and running it by a specified step at a time.
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 2021-03-02
 */
public class ControlPanel extends JFrame implements ActionListener{

    private static Simulator simulator;
    private static int steps;


    /**
     * The main method of the project. Calls the method to start the application and creates a new instance of the
     * Simulator class.
     */
    public static void main(String[] arg) {
        simulator = new Simulator(steps);
        startSimulation();
    }

    /**
     * Starts the initial simulator.
     */
    public static void startSimulation() {
        simulator.runLongSimulation();
    }


    private final JButton nextButton;
    private final JButton pauseButton;
    private final JButton previousButton;
    private final JTextField enteredSpeed;
    private final JButton submitSpeed;
    private final JLabel feedbackMessage;

    private final JButton showMapButton;
    private final JButton savannaDataButton;
    private final JButton forestDataButton;
    private final JButton desertDataButton;

    private boolean paused;

    /**
     * The constructor methods which creates the frame, panel, buttons, labels and textFields on the control panel.
     *
     */
    public ControlPanel() {

        paused = false;

        JPanel panel = new JPanel();
        steps = 0;

        JFrame frame = new JFrame();
        frame.setTitle("Control Panel");
        frame.setSize(600, 250);
        frame.setLocation(200, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(panel);

        panel.setLayout(null);

        showMapButton = new JButton("Show Environment Map");
        showMapButton.setBounds(20, 120, 200, 20);
        showMapButton.addActionListener(this);
        panel.add(showMapButton);

        savannaDataButton = new JButton("Show Savanna Statistics");
        savannaDataButton.setBounds(300, 90, 200, 20);
        savannaDataButton.addActionListener(this);
        savannaDataButton.setVisible(false);
        savannaDataButton.setForeground(Color.RED);
        panel.add(savannaDataButton);

        forestDataButton = new JButton("Show Forest Statistics");
        forestDataButton.setBounds(300, 120, 200, 20);
        forestDataButton.addActionListener(this);
        forestDataButton.setVisible(false);
        panel.add(forestDataButton);

        desertDataButton = new JButton("Show Desert Statistics");
        desertDataButton.setBounds(300, 150, 200, 20);
        desertDataButton.addActionListener(this);
        desertDataButton.setVisible(false);
        panel.add(desertDataButton);

        pauseButton = new JButton("Switch to manual");
        pauseButton.setBounds(20, 20, 140, 20);
        pauseButton.addActionListener(this);
        panel.add(pauseButton);

        previousButton = new JButton("Previous");
        previousButton.setBounds(20, 20, 80, 20);
        previousButton.addActionListener(this);
        previousButton.setVisible(false);
        panel.add(previousButton);

        nextButton = new JButton("Next");
        nextButton.setBounds(120, 20, 80, 20);
        nextButton.addActionListener(this);
        nextButton.setVisible(false);
        panel.add(nextButton);

        enteredSpeed = new JTextField("speed");
        enteredSpeed.setBounds(220, 20, 80, 20);
        enteredSpeed.addActionListener(this);
        enteredSpeed.setVisible(false);
        panel.add(enteredSpeed);

        submitSpeed = new JButton("Submit");
        submitSpeed.setBounds(300, 20, 80, 20);
        submitSpeed.addActionListener(this);
        submitSpeed.setVisible(false);
        panel.add(submitSpeed);

        feedbackMessage = new JLabel("", JLabel.CENTER);
        feedbackMessage.setForeground(Color.red);
        feedbackMessage.setVisible(true);
        panel.add(feedbackMessage);

        frame.setVisible(true);

    }

    /**
     * This method is called when a button is clicked on the control panel. It manipulates the simulation and the map
     * when specific buttons are clicked.
     * @param e Event created when button is clicked,
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == nextButton) {
            displayMessage("", Color.RED);
            paused = true;
            if(!enteredSpeed.isEnabled()) {
                if (enteredSpeed != null) {
                    int temp;
                    try {
                        temp = Integer.parseInt(enteredSpeed.getText());
                        for(int i=0; i<temp; i++) {
                            simulator.simulateOneStep();
                        }
                    } catch (Exception a) {
                        simulator.simulateOneStep();
                    }
                } else {
                    simulator.simulateOneStep();
                }
            }
        } else if(e.getSource() == pauseButton) {
            displayMessage("", Color.RED);
            if(paused) {
                paused = false;
                pauseButton.setText("Pause");
            } else {
                paused = true;
                pauseButton.setText("Resume");
                nextButton.setVisible(true);
                previousButton.setVisible(true);
                pauseButton.setVisible(false);
                enteredSpeed.setVisible(true);
                submitSpeed.setVisible(true);
            }
        } else if(e.getSource() == previousButton) {
            displayMessage("", Color.RED);
            if(!enteredSpeed.isEnabled()) {
                if(simulator.getStep() > 0)  {
                    if (enteredSpeed != null) {
                        int enteredAmount;
                        try {
                            enteredAmount = Integer.parseInt(enteredSpeed.getText());
                            int index = 0;
                            while(index < enteredAmount && simulator.getStep() != 0) {
                                simulator.loadPreviousStep();
                                index++;
                            }
                        } catch (Exception a) {
                            displayMessage("Please enter a positive integer!", Color.RED);
                        }
                    } else {
                        simulator.loadPreviousStep();
                    }
                } else {
                   displayMessage("You are at the initial step!", Color.RED);
                }
            }
            paused = true;
        } else if (e.getSource() == submitSpeed) {
            if(enteredSpeed.isEnabled()) {
                enteredSpeed.setEnabled(false);
                submitSpeed.setText("Unsubmit");
            } else {
                enteredSpeed.setEnabled(true);
                submitSpeed.setText("Submit");
            }
        } else if (e.getSource() == showMapButton) {
            boolean result = simulator.switchMapShowing();
            if (result) {
                showMapButton.setText("Show Environment Map");
                savannaDataButton.setVisible(false);
                desertDataButton.setVisible(false);
                forestDataButton.setVisible(false);
            } else {
                showMapButton.setText("Hide Environment Map");
                savannaDataButton.setVisible(true);
                desertDataButton.setVisible(true);
                forestDataButton.setVisible(true);
            }

        } else if(e.getSource() == savannaDataButton) {
            simulator.setCurrentEnvironmentInspection("Savanna");
            savannaDataButton.setForeground(Color.RED);
            desertDataButton.setForeground(Color.BLACK);
            forestDataButton.setForeground(Color.BLACK);
        } else if(e.getSource() == forestDataButton) {
            simulator.setCurrentEnvironmentInspection("Forest");
            savannaDataButton.setForeground(Color.BLACK);
            desertDataButton.setForeground(Color.BLACK);
            forestDataButton.setForeground(Color.RED);
        } else if(e.getSource() == desertDataButton) {
            simulator.setCurrentEnvironmentInspection("Desert");
            savannaDataButton.setForeground(Color.BLACK);
            desertDataButton.setForeground(Color.RED);
            forestDataButton.setForeground(Color.BLACK);

        }
    }

    /**
     * Return if the simulation has been paused.
     * @return If the simulation has been paused
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Displays a feedback message on the control panel.
     * @param message the message that is to be displayed.
     * @param color the preferred color of the message text
     */
    private void displayMessage(String message, Color color) {

        feedbackMessage.setText(message);
        feedbackMessage.setForeground(color);
    }
}
