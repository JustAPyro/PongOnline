import javafx.application.Application;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Simple driver class is entry point for the program
 * extends Application so we can use JFX throughout the program
 *
 * Bugs: None known
 * @author Luke Hanna (Github.com/JustAPyro)
 * @date Feb 16, 2021
 * @version 1.0
 */
public class Driver extends Application
{
    public static void main(String[] args) throws UnknownHostException
    {
        launch(); // Our main method calls the launch function which instantiates a Application and calls start()
    }

    /**
     * This function is called when the application starts- By default it launches a client
     *
     * @param primaryStage is the stage that is provided on Application Launch in JFX
     * @Override
     */
    public void start(Stage primaryStage) throws UnknownHostException
    {
        // For Testing purposes the start function creates a server and then a client

        new Client(primaryStage, InetAddress.getByName(null), 9875);
    }

}
