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

                // Connection found! Start new server thread to handle this connection
                ServerThread serverThread = new ServerThread(connectionSocket);

                // Start the newly created server thread to handle the connection
                serverThread.start();

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
