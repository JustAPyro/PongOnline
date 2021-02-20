import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts a server listening thread and listens for any connections, starting a new server thread for each incoming connection
 *
 * Bugs: None known
 * @author Luke Hanna (Github.com/JustAPyro)
 * @date Feb 14, 2021
 * @version 1.0
 */
public class ServerListener
{
    private ServerSocket listener;      // This will be the server socket used to listen for incoming connecitons
    private Socket connectionSocket;    // This is the socket that will be generated when a connection is formed

    private Player[] players = new Player[2]; // Player Array

    private int index; // Global variable dec
    private ServerThread p1Thread; // Thread for player 1
    private ServerThread p2Thread; // Thread for player 2

    /**
     * Main constructor initializes a server listener.
     *
     * @param port The port on which the server listener will listen for incoming connection
     */
    public ServerListener(int port)
    {
        try
        {
            // Creates a ServerSocket on the given port
            listener = new ServerSocket(port);
        }
        catch (IOException e) // Error Creating server socket
        {
            // Notify the user and abort the server program
            System.out.println("Could not create Server Socket, program must be aborted.");
            return;
        }

        // This loop runs continuously checking for incoming connections
        while (true)
        {
            try
            {
                // Let the user know we're now listening
                System.out.println("Server Launched successfully, listening for connections...");

                // Listen for connection, this blocks the thread
                connectionSocket = listener.accept();

                // If the player 1 thread is either unassigned or inactive
                if (p1Thread == null || p1Thread.isActive() != true)
                {
                    // Create a new Server thread with new socket, and a new player to assign to it
                    p1Thread = new ServerThread(connectionSocket, players, 0);
                    players[0] = new Player(true, 350, 0);
                    p1Thread.start();
                }
                // If player 2 thread is unassigned or inactive
                else if (p2Thread == null || p2Thread.isActive() != true)
                {
                    // Assign a new server thread based on socket and create new player 2
                    p2Thread = new ServerThread(connectionSocket, players, 1);
                    players[1] = new Player(false, 350, 1);
                    p2Thread.start();
                }
                else // Otherwise, we're already at max users,
                {
                    // So we notify the user and do nothing
                    System.out.println("Already at max players! Please wait!");
                }

                // Connection Initialized, inform the user
                System.out.println("Connection Formed successfully!");

                // Return to the top of the while loop, continue listening for new connections
            }
            catch (IOException e) // Error connecting to given client
            {
                // Notify the user
                System.out.println("Connection attempted but could not be established, returning to listening");
            }
        }
    }

    // For testing this is the main method that launches the server
    public static void main(String[] args)
    {
        new ServerListener(9875); // Launch server on port 9875
    }


}
