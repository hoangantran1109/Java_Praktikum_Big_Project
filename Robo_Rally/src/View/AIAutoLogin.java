package View;

import ClientServer.Client;
import GameLogic.Player;
import MessageObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


/**
 * Class AIAutoLogin
 * This class automatically logs an AI
 *
 * @author Tobias Celik, based on the work of Julian Wiemer and Hoang An Tran in LoginGUIController
 * @see LoginGUIController
 */

public class AIAutoLogin {

    private Gson gson;
    private static final Logger logger = LogManager.getLogger(View.AIAutoLogin.class);
    private final Client client = Client.getInstance();
    private int playerID;
    private final String PROTOCOL = "1.0";
    private boolean ready = false, initalAsAI = false;
    private Player player;

    /**
     * AIAutoLogin()
     * This Constructor sets up the communication with the Server
     * If everything is fine, the Automatic Login Procedure will start
     * Errors will be intercepted and shown to the user
     */
    public AIAutoLogin() throws IOException {
        boolean everythingOK = true;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();

        if (Main.isKi) {
            initalAsAI = true;
        }
        if (client.getStatus() != null && client.getStatus().getMessageType().equals(MsgNames.error)) {
            logger.error("No connection!");
            WarnDialog.main(new String[]{"No Connection", "No Connection", "Server not reachable:\n" + Main.host + ":" + Main.port, "1"});
            everythingOK = false;
        }
        if (everythingOK && client.in.hasNext()) {
            String msg = client.in.nextLine();
            logger.info(msg);
            Message message = gson.fromJson(msg, Message.class);
            if (message.getMessageType().equals(MsgNames.helloClient)) {
                Message helloServer = new HelloServer(PROTOCOL, "LockereLilien", false);
                String helloServerJson = gson.toJson(helloServer);
                client.out.println(helloServerJson);
            }
        }
        if (everythingOK && client.in.hasNext()) {
            String msg = client.in.nextLine();
            logger.info(msg);
            Message message = gson.fromJson(msg, Message.class);
            if (message.getMessageType().equals(MsgNames.welcome)) {
                playerID = message.getMessageBody().getId();
            } else {
                logger.error(message.getMessageBody().getError());
                WarnDialog.main(new String[]{"Something went wrong", message.getMessageBody().getError(), message.getMessageBody().getError(), "2"});
                everythingOK = false;
            }
        }
        if (everythingOK && initalAsAI) {
            autoLogin(0);
        }
    }

    /**
     * Automatic LogIn As AI
     * It gets an Robot Id and tries to Log in with it
     * If it is not successful, the robotId will be incremented and the Method will be again called recursively
     * If it is successful, the LaunchChatWindow Class will be started, which launches the ChatGui
     *
     * @param robotId number of the Robot, with which the AI will try to log into the game
     */
    protected void autoLogin(int robotId) {
        String name = "AI_" + playerID;
        Message playerValuesMessage = new PlayerValues(name, robotId);
        playerValuesMessage.getMessageBody().setAI(initalAsAI);
        String playerValuesMessageJson = gson.toJson(playerValuesMessage);
        client.out.println(playerValuesMessageJson);
        logger.info(playerValuesMessageJson);

        if (waitForAnswer()) {
            client.setPlayer(player);
            LaunchChatWindow.main(new String[]{});
        } else {
            robotId++;
            autoLogin(robotId);
        }
    }

    /**
     * In this Method, the Client awaits the answer from the Server
     *
     * @return In the case of an successful Login, the value will be true, else false
     */
    private boolean waitForAnswer() {
        while (!ready) {
            if (client.in.hasNext()) {
                String msg = client.in.nextLine();
                logger.info(msg);
                Message message = gson.fromJson(msg, Message.class);
                if (message.getMessageType().equals(MsgNames.error) && message.getMessageBody().getError().contains("another one.")) {
                    logger.error(message.getMessageBody().getError());
                    return false;
                } else if (message.getMessageType().equals(MsgNames.playerAdded)) {
                    player = message.getMessageBody().getPlayer();
                    if (player.getId() == playerID) {
                        Context.getInstance().setAI(initalAsAI);
                        ready = true;
                    }
                    if (client.playerList.stream().noneMatch(o -> o.getId() == player.getId())) {
                        client.addPlayerToList(player);
                    }
                }
            }
        }
        return true;
    }


    /**
     * Class WarnDialog
     * In Order to start a JavaFX window the Application needs a Class, which Extends its Methods
     * This Class is needed to show the User an alert dialog, when something went wrong
     */
    public static class WarnDialog extends Application {
        static int type;
        static String header, content, title;

        public void start(Stage stage) {
            Alert warnAlert;
            switch (type) {
                case 1 -> warnAlert = new Alert(Alert.AlertType.ERROR);
                case 2 -> warnAlert = new Alert(Alert.AlertType.WARNING);
                default -> warnAlert = new Alert(Alert.AlertType.INFORMATION);
            }
            warnAlert.setTitle(title);
            warnAlert.setHeaderText(header);
            warnAlert.setContentText(content);

            Stage alertStage = (Stage) warnAlert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(Client.class.getResourceAsStream("/resources/Icons/lockereLilienRobot.png")));
            alertStage.setResizable(false);

            warnAlert.showAndWait();
        }

        private static void main(String[] args) {
            title = args[0];
            header = args[1];
            content = args[2];
            try {
                type = Integer.parseInt(args[3]);
            } catch (Exception e) {
                type = 0;
            }
            launch();
        }

    }

    /**
     * Class LaunchChatWindow
     * In Order to start a JavaFX window the Application needs a Class, which Extends its Methods
     * This Class is needed to launch the ChatGui, which is initalized in the Client.
     */
    public static class LaunchChatWindow extends Application {
        Client client = Client.getInstance();

        public void start(Stage stage) throws Exception {
            client.openChatWindow();
        }

        private static void main(String[] args) {
            launch(args);
        }
    }
}