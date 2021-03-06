import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This is a multi-threaded class that handles the connection objects passed to it by the server listener.
 * This class accepts information from the client and passes back updated information for other players
 *
 * Bugs: None known
 * @author Luke Hanna (Github.com/JustAPyro)
 * @date Feb 15, 2021
 * @version 1.0
 */
public class ServerThread extends Thread
{

    private boolean active; // This is used to determine if the thread is listening
    private ObjectOutputStream outStream; // This will be used to write to client
    private ObjectInputStream inStream; // This will be used to read from client
    private Socket connectionSocket; // We save this so we can close it at the end

    // Gameplay information
    Player[] players; // Data for all players
    int playerIndex; // Index of the player currently being served

    /**
     * Main constructor initializes a server thread that will manage one client connection
     *
     * @param connection The TCP connection with the client generated by ServerListener
     * @param players List of all players so that we can update
     * @param playerIndex the index in players of the player this serverThread is serving
     */
    public ServerThread(Socket connection, Player[] players, int playerIndex)
    {
        // Pass the given values to the object
        this.players = players;
        this.playerIndex = playerIndex;

        // Save the connection to close later
        this.connectionSocket = connection;

        try {
            // Set active to true since it was just started
            active = true;

            // Get the IO Data streams from client TCP connection
            outStream = new ObjectOutputStream(connection.getOutputStream());
            inStream = new ObjectInputStream(connection.getInputStream());
        }
        catch (IOException e) // Couldn't get data stream from client connection
        {
            // Inform the user
            System.out.println("Fatal Error: Could not get IO Streams from Client");
            return;
        }
    }

    /**
     * This just returns if the thread is listening or not
     *
     * @return
     */
    public boolean isActive()
    {
        return active;
    }

    /**
     * This is the overridden method that runs on a different thread- This starts running when start() is called by ServerListener
     * @Override
     */
    public void run()
    {
        // This runs an infinite loop managing the TCP connection with the client
        while (active)
        {
            try
            {
                // This is simply placeholder until a client has been scripted that sends objects
                Object readObject = inStream.readObject();

                // if a client requests initialization
                if (readObject instanceof String && ((String) readObject).equals("init"))
                {
                    // Write the player associated with this serverthread to the client, (Mainly informing it of left/right status)
                    outStream.writeObject(players[playerIndex]);
                }

                if (readObject instanceof Player) // If the serverThread recieves a playerObject
                {
                    Player player = (Player) readObject;    // Cast the read object to a Player
                    int index = player.getIndex();          // Get the index of the player

                    players[index] = player; // Update the server player

                    if (index == 0) // If the index was 0
                    {
                        // Pass back index 1
                        outStream.writeObject(players[1]);
                    }
                    if (index == 1) // If the index was 1
                    {
                        // Pass back index 0
                        outStream.writeObject(players[0]);
                    }

                }

            }
            catch (IOException | ClassNotFoundException e) // If there was an error reading the object
            {
                // Inform the user
                System.out.println("Error: Could not read data from client IO stream");

                // Connection was likely lost so stop the listening loop
                active = false;
            }
        }

        System.out.println("Connection broken, closing Server Thread");

        try { // Closing out our IO streams so that we don't have any data leaksz
            inStream.close();
            outStream.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
