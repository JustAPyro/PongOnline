import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

/**
 * Simple player class that gets passed around from server to client
 *
 * Bugs: None known
 * @author Luke Hanna (Github.com/JustAPyro)
 * @date Feb 18, 2021
 * @version 1.0
 */
public class Player implements Serializable
{

    // Variables based on if keys are up or down
    private boolean upPressed = false;
    private boolean downPressed = false;

    private int playerID; // This is the unique identifier for the player
    private int vel = 2; // How much the position is incremented by
    private boolean left; // left or right player
    private int y; // Vertical position

    private int height = 250; // Height of paddle
    private int width = 25; // Width of the paddle


    /**
     * Default constructor assigns a paddle to left or right and sets the height.
     *
     * @param left can be "left" or "right" depending on which side the players paddle is on
     * @param y the vertical position of the paddle
     * @param playerID unique identifier for the player
     */
    public Player(boolean left, int y, int playerID)
    {
        this.left = left;
        this.y = y;
        this.playerID = playerID;
    }

    /**
     * Setter method for upPressed, which manages which key the player is pressing
     *
     * @param upPressed True if the player is holding the up key (W)
     */
    public void setUpPressed(boolean upPressed)
    {
        this.upPressed = upPressed; // Apply the new state
    }

    /**
     * Setter method for downPressed, which manages which key the player is pressed
     *
     * @param downPressed True if the player is holding the down key (S)
     */
    public void setDownPressed(boolean downPressed)
    {
        this.downPressed = downPressed; // Apply the new state
    }

    /**
     * Returns the index of the player
     *
     * @return playerID
     */
    public int getIndex()
    {
        return playerID;
    }

    /**
     * This is called every game frame and is where we do all game logic relating to player
     */
    public void update()
    {
        if (upPressed)
            y -= vel;
        if (downPressed)
            y += vel;
    }

    /**
     * This draws the current Player
     *
     * @param gc the graphics context that draws everything
     */
    public void draw(GraphicsContext gc)
    {
        int x; // X draw value
        if (left == true) // If this is a left paddle
            x = 75; // Set the x position to the left side of the screen
        else // Otherwise
            x = 900 - 75; // Set to the right side of the screen

        // Draw the paddle rect
        gc.fillRect(x-width/2, y-height/2, width, height);
    }

}
