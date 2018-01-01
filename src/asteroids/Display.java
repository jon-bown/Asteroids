package asteroids;

import javax.swing.*;
import java.awt.*;
import static asteroids.Constants.*;

/**
 * Defines the top-level appearance of an Asteroids game.
 */
@SuppressWarnings("serial")
public class Display extends JFrame
{
    // The area where the action takes place
    private Screen screen;
    private JLabel lives;
    private JLabel level;
    private JLabel score;
    private JLabel nukes, drones, mines;

    /**
     * Lays out the game and creates the controller
     */
    public Display (Controller controller)
    {
        // Title at the top
        setTitle(TITLE);

        // Default behavior on closing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main playing area and the controller
        screen = new Screen(controller);

        setLegend("Lives: " + controller.getLives());
        
        // This panel contains the screen to prevent the screen from being
        // resized
        JPanel screenPanel = new JPanel();
        screenPanel.setLayout(new GridBagLayout());
        screenPanel.add(screen);

        // This panel contains buttons and labels
        JPanel controls = new JPanel();

        // The button that starts the game
        JButton startGame = new JButton(START_LABEL);
        controls.add(startGame);
  
        //Start the blank J Label to be used for the changing variables of the game
        lives = new JLabel();  
        level = new JLabel();
        score = new JLabel();
        nukes = new JLabel();
        drones = new JLabel();
        mines = new JLabel();
        
        JPanel gameInfo = new JPanel();
        // this was made to add space into the gridLayout for the button. 
        JPanel startSpace1 = new JPanel ();
        JPanel startSpace2 = new JPanel ();
        startSpace1.add(nukes);
        startSpace2.add(drones);
     
        // added to evenly space the grid for start button
        JPanel startSide = new JPanel();
        startSide.setLayout(new GridLayout());
        startSide.add(startSpace1);
        startSide.add(startGame);
        startSide.add(startSpace2);
        
        controls.add(startSide);
        controls.add(gameInfo);
        gameInfo.add(mines);
        gameInfo.add(lives);
        gameInfo.add(level);
        gameInfo.add(score);
        

        
      

        // Organize everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(screenPanel, "Center");
        mainPanel.add(controls, "North");
        
        setContentPane(mainPanel);
        pack();

        // Connect the controller to the start button
        startGame.addActionListener(controller);
    }
    
    /**
     * Called when it is time to update the screen display. This is what drives
     * the animation.
     */
    public void refresh ()
    {
        screen.repaint();
    }
    /**
     * Sets the foreground of the game
     */
    public void setColor(Color c){
    	screen.setForeground(c);
    }
    
    /**
     * Sets the large legend
     */
    public void setLegend (String s)
    {
        screen.setLegend(s);
    }
/**
 * Methods that set the labels on the start panel
 * @param l
 */
    public void setLivesLabel (int l)
    {
        lives.setText("Lives: " + l);
    }

    public void setScoreLabel (int s)
    {
        score.setText("Score: " + s);
    }

    public void setLevelLabel (int n)
    {
        level.setText("Level: " + n);
    }

    public void setNukesLabel(int n){
    	nukes.setText("Nukes: (N)" + " " + n);
    }
    public void setDronesLabel(int n){
    	drones.setText("Drones: (G)" + " " + n);
    }
    public void setMinesLabel(int n){
    	mines.setText("Mines(M):" + " " + n);
    }
}
