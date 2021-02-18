import javafx.scene.canvas.GraphicsContext;

/**
 * Simple player class that gets passed around from server to client
 *
 * Bugs: None known
 * @author Luke Hanna (Github.com/JustAPyro)
 * @date Feb 18, 2021
 * @version 1.0
 */
public class Player
{

    boolean left; // left or right player
    int y;

    int height = 350; // Height of paddle
    int width = 40; // Width of the paddle

    /**
     * Default constructor assigns a paddle to left or right and sets the height.
     *
     * @param left can be "left" or "right" depending on which side the players paddle is on
     * @param height the vertical position of the paddle
     */
    public Player(boolean left, int height)
    {
        this.left = left;
        this.height = height;
    }

    /**
     * This draws the current Player
     *
     * @param gc the graphics context that draws everything
     */
    public void draw(GraphicsContext gc)
    {
        int x;
        if (left == true)
            x = 75;
        else
            x = 900 - 75;

        gc.fillRect(x, y, width, height);
    }

}
