import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.InetAddress;

/**
 * This is the client-side class that handles GUI and game loop
 *
 * Bugs: None known
 * @author Luke Hanna (Github.com/JustAPyro)
 * @date Feb 16, 2021
 * @version 1.0
 */
public class Client
{

    // GUI Elements initalized at class level for access
    private Stage window; // This is the actual window for the game
    private Scene scene; // This is the area INSIDE the window that holds the game
    private int width = 900, height = 700; // Width and height of the window

    /**
     * Main constructor initializes a client-side pong game
     *
     * @param primaryStage is the stage the driver generates for the application
     * @param inetAddress The IP Address for the hosting server
     * @param port The port number the client should attempt to connect to
     */
    public Client(Stage primaryStage, InetAddress inetAddress, int port)
    {
        // Initialize the window/stage
        window = primaryStage;
        window.setTitle("Pong Online");

        // Create a game canvas (We will use this to draw on)
        Canvas gameCanvas = new Canvas(width, height);


        StackPane layout = new StackPane(); // Create a layout to show how things are set up in the window
        layout.getChildren().add(gameCanvas); // Add the game canvas to our new layout
        scene = new Scene(layout); // Now apply our layout (Canvas included) to our scene

        // ----------- Input handling goes here ---------------------

        // Apply the updated scene to the window and set to show
        window.setScene(scene);
        window.show();


    }

}
