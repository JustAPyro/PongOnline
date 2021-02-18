import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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

    // GUI Elements initalized at class level for access
    private Stage window; // This is the actual window for the game
    private Scene scene; // This is the area INSIDE the window that holds the game
    private GraphicsContext gc; // This is the *brush* that we use to draw things
    private int width = 900, height = 700; // Width and height of the window

    // Some networking variables
    private Socket socket; // This is the socket used to establish the TCP connection
    private ObjectOutputStream outStream;   // IO Stream out
    private ObjectInputStream inStream;     // IO stream in

    private Player player = new Player(true, height/2, 0);
    private Player p2;

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
            socket = new Socket(inetAddress, port);

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
                player.update();

                /*
                try {
                    //outStream.writeObject(player);
                    //p2 = (Player) inStream.readObject();

                } catch (IOException e) {
                    e.printStackTrace();
                }
    */
                //p2.draw(gc);
                player.draw(gc);

            }
        };
        timer.start();

        StackPane layout = new StackPane(); // Create a layout to show how things are set up in the window
        layout.getChildren().add(gameCanvas); // Add the game canvas to our new layout
        scene = new Scene(layout); // Now apply our layout (Canvas included) to our scene

        // If the key is pressed adjust the player values
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W)
                player.setUpPressed(true);
            if (event.getCode() == KeyCode.S)
                player.setDownPressed(true);
        });

        // If the key is released adjust the player values
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.W)
                player.setUpPressed(false);
            if (event.getCode() == KeyCode.S)
                player.setDownPressed(false);
        });

        // Apply the updated scene to the window and set to show
        window.setScene(scene);
        window.show();


    }

}
