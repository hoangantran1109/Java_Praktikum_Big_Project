package View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class Main
 * This class implements the clients.
 * All clients connect to one server.
 *
 * @author Hoang An Tran, Simon Wiethase, Tobias Celik, Julian Wiemer
 */
public class Main extends Application {
    public static String host = "localhost";
    public static int port = 7070;
    public static boolean isKi = false;

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/FXML/loginGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/resources/Icons/lockereLilienRobot.png")));
        stage.setTitle("RoboRally - Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        //create a thread in order to read message from server continuously
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /**
     * Main Method, it also handles the args
     * -h ... defines the adress of the Server
     * -p ... defines the port
     * -u ki  defines the Client as an AI
     * if it is an AI, an Automatic LogIn Procedure is Done
     * else, the normal LogIn Window is shown
     *
     * @param args String Array of Arguments
     */
    public static void main(String[] args) {
        int argsLength = args.length;
        switch (argsLength) {
            case 2 -> parseArguments(args, 0);
            case 4 -> {
                parseArguments(args, 0);
                parseArguments(args, 2);
            }
            case 6 -> {
                parseArguments(args, 0);
                parseArguments(args, 2);
                parseArguments(args, 4);
            }
            default -> {
                host = "localhost";
                port = 7070;
                isKi = false;
            }
        }
        if (isKi) {
            try {
                new AIAutoLogin();
            } catch (IOException ignored) {
            }
        } else {
            launch(args);
        }
    }

    /**
     * gets the Arguments and sets the properties
     *
     * @param args   Arguments as String Array
     * @param values The Index in the Argument Array the query should look at
     */
    private static void parseArguments(String[] args, int values) {
        switch (args[values]) {
            case "-h" -> host = args[values + 1];
            case "-p" -> {
                try {
                    port = Integer.parseInt(args[values + 1]);
                } catch (Exception e) {
                    port = 7070;
                }
            }
            case "-u" -> isKi = args[values + 1].equalsIgnoreCase("ki");
        }
    }
}
