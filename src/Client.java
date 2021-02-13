import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

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

    // GUI Elements initialized at class level for access
    private Stage window; // This is the actual window for the game
    private Scene scene; // This is the area INSIDE the window that holds the game
    private GraphicsContext gc; // This is the *brush* that we use to draw things
    private int width = 900, height = 700; // Width and height of the window

    // Some networking variables
    private Socket socket; // This is the socket used to establish the TCP connection
    private ObjectOutputStream outStream;   // IO Stream out
    private ObjectInputStream inStream;     // IO stream in

    // Player variables we're drawing from
    private Player player = new Player(true, height/2, 0);
    private Player p2 = new Player(false, height/2, 1);

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

        // Creates the graphics context to draw on the canvas
        gc = gameCanvas.getGraphicsContext2D();

        try
        {
            // Create a new socket for TCP communication
            socket = new Socket("96.252.107.167", port);

            // Set IO streams from new socket
            inStream = new ObjectInputStream(socket.getInputStream());
            outStream = new ObjectOutputStream(socket.getOutputStream());

            // Send client initialization request
            outStream.writeObject("init");
            Player startingPlayer = (Player) inStream.readObject();
            player = startingPlayer;
        }
        catch (IOException | ClassNotFoundException e)  // If there's an error with the IO
        {
            e.printStackTrace();

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error establishing IO connection with server");
            alert.show();
        }

        // This timer is used for animation and holds at 60 FPS when possible
        AnimationTimer timer = new AnimationTimer() {
            @Override // This gets called every frame of the game by timer
            public void handle(long timeStamp)
            {

                // Clear the screen before redrawing
                gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

                // Update the main player
                player.update();

                try
                {
                    outStream.writeObject(player);  // Write the updated player
                    outStream.reset();              // Reset the output stream so it disregards previously sent objects
                    p2 = (Player) inStream.readObject(); // Read the object sent as response (The opposite player)

                }
                catch (IOException e) { // If there was an error reading/writing to the IO stream

                    // Notify the user and print the stack trace
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Error reading or writing from IO streams");
                    alert.show();
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e) { // If there was an error recognizing the recieved class

                    // Notify the user and print the stack trace
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Error recognizing server sent objects.");
                    alert.show();
                    e.printStackTrace();
                }

                if (p2 != null) // p2 will be a null value recieved from server until a second client connects
                    p2.draw(gc);        // Draw the second player (Updated from server)
                player.draw(gc);    // Draw the first player (Updated from local data)

            }
        };
        timer.start(); // Start the animation timer

        StackPane layout = new StackPane();   // Create a layout to show how things are set up in the window
        layout.getChildren().add(gameCanvas); // Add the game canvas to our new layout
        scene = new Scene(layout);            // Now apply our layout (Canvas included) to our scene

        // If the key is pressed adjust the player values
        scene.setOnKeyPressed(event -> {        // For each event
            if (event.getCode() == KeyCode.W)   // If key pressed is W
                player.setUpPressed(true);      // Set player up-pressed to true
            if (event.getCode() == KeyCode.S)   // If key pressed is S
                player.setDownPressed(true);    // Sey player down-pressed to true
        });

        // If the key is released adjust the player values
        scene.setOnKeyReleased(event -> {       // For event received
            if (event.getCode() == KeyCode.W)   // if key released is W
                player.setUpPressed(false);     // Set player up-pressed to false
            if (event.getCode() == KeyCode.S)   // if key released is S
                player.setDownPressed(false);   // Set player down-pressed to false
        });

        // Apply the updated scene to the window and set to show
        window.setScene(scene);
        window.show();

    }

}
