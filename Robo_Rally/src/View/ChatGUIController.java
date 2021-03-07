package View;


import ClientServer.Client;
import GameLogic.Player;
import MessageObjects.Message;
import MessageObjects.MessageAdapter;
import MessageObjects.SendChat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * modifies the Chat GUI, sends messages according to user input
 * @author Tobias Celik, Hoang An Tran
 */
public class ChatGUIController implements Initializable {
    @FXML
    public TextField chatField;
    public TextArea chatArea;
    public Label userName;
    public TextArea userList;
    public Button readyToPlay;
    public Button chooseAMap;
    public AnchorPane chatPane;
    public GridPane chatGridPane;
    private Client client;
    private Updater updater;
    public boolean isReady = false;
    private static final Logger logger = LogManager.getLogger(ChatGUIController.class);
    Gson gson;

    /**
     * executed when the send button is pressed
     * sends message of type send chat to the client out stream
     */
    public void setSendButton() {

        if (updater != null && client.connected) {
            if (!chatField.getText().equals("")) {
                if (!(chatField.getText().startsWith("@"))) {
                    Message publicMessage = new SendChat(chatField.getText(), -1);
                    client.out.println(gson.toJson(publicMessage));
                    logger.info(gson.toJson(publicMessage));
                    chatField.setText("");
                } else {
                    String msg = chatField.getText();
                    String[] splitMessage = msg.split(" ");
                    HashSet<String> recipients = new HashSet<>();
                    StringBuilder text = new StringBuilder();
                    for (String part : splitMessage) {
                        if (part.charAt(0) == '@') {
                            recipients.add(part.substring(1));
                        } else {
                            text.append(part).append(" ");
                        }
                    }
                    if (recipients.size() == 0) {
                        Message publicMessage = new SendChat(text.toString(), -1);
                        client.out.println(gson.toJson(publicMessage));
                        logger.info(gson.toJson(publicMessage));
                    } else {
                        boolean sentPM = false;
                        List<Integer> recepientID = new ArrayList<>(Collections.singletonList(Client.getInstance().getPlayer().getId()));
                        for (String recipient : recipients) {
                            int userID = getUserid(recipient);
                            if (userID == -1) {
                                chatArea.appendText("User " + recipient + " does not exist. Please choose a valid username.\n");
                            } else {
                                if (userID == -2) {
                                    chatArea.appendText("There are some Players with this name!\nYou can also tag them with their ID\n");
                                } else {
                                    if (!recepientID.contains(userID)) {
                                        sentPM = sendPrivateMessage(text, recipient, userID);
                                        recepientID.add(userID);
                                    }
                                }
                            }
                        }
                        if (sentPM) {
                            chatArea.appendText("PM: " + text + "\n");
                        }
                    }
                }
            } else {
                chatArea.appendText("Message is Empty\n");
            }
        } else {
            Platform.runLater(() -> chatArea.appendText("Wait until a safe connection is established\n" +
                                                        "Or better: Close this window"));
        }
        chatField.setText("");
    }

    /**
     * used when a message contains the correct characters to indicate a private message
     * @param text the text that will be contained in the message to send
     * @param recipient to provide feedback to the user
     * @param userID the user to send the message to, which will be included in the message
     * @return boolean true after the message was sent
     */
    private boolean sendPrivateMessage(StringBuilder text, String recipient, int userID) {
        Message privateMessage = new SendChat("PM: " + text, userID);
        client.out.println(gson.toJson(privateMessage));
        logger.info(gson.toJson(privateMessage));
        chatArea.appendText("You sent a private message to " + recipient + "\n");
        return true;
    }


    /**
     * get UserID from playerName
     * @param recipient the Recipient
     * @return Integer userID or -2 to indicate a too large playerID number or -1 to indicate a missing playerID
     */
    public Integer getUserid(String recipient) {
        try {
            return Integer.parseInt(recipient);
        } catch (Exception k) {
            List<Integer> playerIDs = new ArrayList<>();
            for (Player p : Client.getInstance().playerList) {
                if (p.getName().equals(recipient)) {
                    playerIDs.add(p.getId());
                }
            }
            if (playerIDs.size() > 1) {
                return -2;
            }
            if (playerIDs.size() == 0) {
                return -1;
            }
            return playerIDs.get(0);
        }
    }

    /**
     * sends message of type send chat when the exit button is pressed and closes the window
     */
    public void setExitButton() {
        if (client.connected) {
            Message exitMessage = new SendChat("bye", -1);
            client.out.println(gson.toJson(exitMessage));
            logger.info(gson.toJson(exitMessage));
        }
        System.exit(0);
    }

    /**
     * initializes the GSON builder for message creation and assignes variables for later use
     * @param url default argument needed to override
     * @param resourceBundle default argument needed to override
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatPane.autosize();
        chatGridPane.autosize();
        chatGridPane.scaleYProperty().bind(chatPane.heightProperty().divide(615));
        chatGridPane.scaleXProperty().bind(chatPane.widthProperty().divide(700));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();

        client = Client.getInstance();
        String playerName = client.getPlayer().getName();
        userList.appendText(playerName + "\n");
        userName.setText(playerName);
        updater = new Updater(chatArea, userList);
        updater.start();
        updater.buildUserList();
        new Thread(() -> chatField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                setSendButton();
            }
        })).start();


        Platform.runLater(() -> chooseAMap.visibleProperty().bind(Client.getInstance().firstReadyPlayer));
    }

    /**
     * sends messages according to whether the player is ready or not
     */
    public void readyToPlayButtonPressed() {
        if (!isReady) {
            ChatGuiContext.getInstance().sendReadyMessage(true);
            isReady = true;
            Platform.runLater(() -> readyToPlay.setText("Not Ready"));
        } else {
            ChatGuiContext.getInstance().sendReadyMessage(false);
            isReady = false;
            Platform.runLater(() -> readyToPlay.setText("Ready"));
        }
    }


    /**
     * creates an instance of the the map selection GUI and modifies it
     */
    public void chooseAMapAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/FXML/selectMapGUI.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 180, 180);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("MapSelector");
        stage.getIcons().add(Client.getInstance().roboIcon);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Client.getInstance().chatStage);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
