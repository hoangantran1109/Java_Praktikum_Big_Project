package View;

import ClientServer.Client;
import GameLogic.Player;
import MessageObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class Main
 * This class implements the clients.
 * All clients connect to one server.
 *
 * @author Hoang An Tran, Simon Wiethase, Tobias Celik, Julian Wiemer
 */
public class LoginGUIController implements Initializable {

    @FXML
    public TextField userNameInput;
    public Label errorText;
    public ComboBox<String> robotSelector;
    public Button login;
    public CheckBox isAI;
    public Line nameUnderline;
    public Line roboUnderline;

    Gson gson;

    private static final Logger logger = LogManager.getLogger(LoginGUIController.class);
    private final Client client = Client.getInstance();
    private int playerID;
    private final String PROTOCOL = "1.0";
    private boolean ready = false;
    private Player player;

    /**
     * initialize()
     * This function help to check the connection with Server
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorText.setAlignment(Pos.CENTER);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();
        robotSelector.getItems().add("Twonky");
        robotSelector.getItems().add("Smash Bot");
        robotSelector.getItems().add("HulkX90");
        robotSelector.getItems().add("Zoom Bot");
        robotSelector.getItems().add("Hammer Bot");
        robotSelector.getItems().add("Spin Bot");

        if (Main.isKi) {
            isAI.setSelected(true);
        }
        if (client.getStatus() != null && client.getStatus().getMessageType().equals(MsgNames.error)) {
            errorText.setStyle("-fx-text-fill: darkred;");
            logger.error("No connection!");
            errorText.setText("No connection!");
            userNameInput.setDisable(true);
            robotSelector.setDisable(true);
            login.setDisable(true);
            return;

        }

        if (client.in.hasNext()) {
            String msg = client.in.nextLine();
            logger.info(msg);
            Message message = gson.fromJson(msg, Message.class);
            if (message.getMessageType().equals(MsgNames.helloClient)) {
                Message helloServer = new HelloServer(PROTOCOL, "LockereLilien", false);
                String helloServerJson = gson.toJson(helloServer);
                client.out.println(helloServerJson);
            }
        }
        if (client.in.hasNext()) {
            String msg = client.in.nextLine();
            logger.info(msg);
            Message message = gson.fromJson(msg, Message.class);
            if (message.getMessageType().equals(MsgNames.welcome)) {
                errorText.setStyle("-fx-text-fill: darkgreen;");
                errorText.setText("protocol v" + PROTOCOL);
                playerID = message.getMessageBody().getId();

            } else {
                errorText.setStyle("-fx-text-fill: darkred;");
                errorText.setText(message.getMessageBody().getError());
                logger.error(message.getMessageBody().getError());
                userNameInput.setDisable(true);
                robotSelector.setDisable(true);
                login.setDisable(true);
            }
        }
    }

    /**
     * tryPlayerValues()
     * This function help to choose the name and robot for Player
     */
    public void tryPlayerValues() {

        String name = userNameInput.getText();
        String robot = robotSelector.getSelectionModel().getSelectedItem();
        boolean validName = false;
        int robotId = 0;
        if (robot == null) {
            roboUnderline.setStyle("-fx-stroke:red;");
            errorText.setText("Please choose a Robot");
            errorText.setStyle("-fx-text-fill: red;");
            return;
        }
        roboUnderline.setStyle("-fx-stroke:green;");
        if (name.isEmpty()) {
            nameUnderline.setStyle("-fx-stroke:red;");
            errorText.setText("Please choose a Name");
            errorText.setStyle("-fx-text-fill: red;");
            return;
        } else {
            int pos = 0;
            for (Character letter : name.toCharArray()) {
                if (pos == 0 && (letter == '$' || letter == '@')) {
                    nameUnderline.setStyle("-fx-stroke:red;");
                    errorText.setText("Name must not start with '@' or '$'");
                    errorText.setStyle("-fx-text-fill: red;");
                    return;
                }
                if (!Character.isDigit(letter) && letter != ' ') {
                    validName = true;
                    break;
                }
                pos++;
            }
        }
        if (!validName) {
            nameUnderline.setStyle("-fx-stroke:red;");
            errorText.setText("Name must contain one character");
            errorText.setStyle("-fx-text-fill: red;");
            return;
        }
        nameUnderline.setStyle("-fx-stroke:green;");

        robotId = getRobotId(robot, robotId);
        Message playerValuesMessage = new PlayerValues(name, robotId);
        playerValuesMessage.getMessageBody().setAI(isAI.isSelected());
        String playerValuesMessageJson = gson.toJson(playerValuesMessage);
        client.out.println(playerValuesMessageJson);
        logger.info(playerValuesMessageJson);

        while (!ready) {
            if (client.in.hasNext()) {
                String msg = client.in.nextLine();
                logger.info(msg);
                Message message = gson.fromJson(msg, Message.class);
                if (message.getMessageType().equals(MsgNames.error) && message.getMessageBody().getError().contains("another one.")) {
                    errorText.setStyle("-fx-text-fill: #bd2118;");
                    errorText.setMaxWidth(300);
                    errorText.setText(message.getMessageBody().getError());
                    logger.error(message.getMessageBody().getError());
                    if (message.getMessageBody().getError().contains("figure")) {
                        roboUnderline.setStyle("-fx-stroke:red;");
                        robotSelector.getItems().remove(robot);
                        return;
                    }
                    return;
                } else if (message.getMessageType().equals(MsgNames.playerAdded)) {
                    player = message.getMessageBody().getPlayer();
                    if (player.getId() == playerID) {
                        Context.getInstance().setAI(isAI.isSelected());
                        ready = true;
                        client.setPlayer(player);
                        client.openChatWindow();
                        Stage stage = (Stage) login.getScene().getWindow();
                        stage.close();
                    }
                    if (client.playerList.stream().noneMatch(o -> o.getId() == player.getId())) {
                        client.addPlayerToList(player);
                    }

                }
            }
        }
    }

    /**
     * getRobotID()
     * This function gets the id of robots from robots name
     *
     * @param robot   the name of robot
     * @param robotId the id of robot
     * @return the id of robot
     */
    private int getRobotId(String robot, int robotId) {
        switch (robot) {
            case "Twonky" -> robotId = 0;
            case "Smash Bot" -> robotId = 1;
            case "HulkX90" -> robotId = 2;
            case "Zoom Bot" -> robotId = 3;
            case "Hammer Bot" -> robotId = 4;
            case "Spin Bot" -> robotId = 5;
        }
        return robotId;
    }

    /**
     * quit()
     * This function help to exit the game and closes the connections
     *
     * @throws IOException the io exception
     */
    public void quit() throws IOException {
        if (client.socket != null) {
            client.socket.close();
            client.in.close();
            client.out.close();
        }
        System.exit(0);
    }
}