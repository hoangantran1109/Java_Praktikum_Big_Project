package View;

import ClientServer.Client;
import GameLogic.Player;
import MessageObjects.ActiveCard;
import MessageObjects.Message;
import MessageObjects.MessageAdapter;
import MessageObjects.MsgNames;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Class Updater
 * This class defines Updater/ReaderTask with Server
 *
 * @author Hoang An Tran, Simon Wiethase, Tobias Celik
 */

public class Updater extends Thread {

    private final Client client;

    @FXML
    private static TextArea chatArea;
    private static TextArea userList;

    private String playerName;
    private String[] cards = {""};
    private final HashMap<Integer, Boolean> finishedPlayers = new HashMap<>();
    private int round = 0;
    private static final Logger logger = LogManager.getLogger(Updater.class);

    Gson gson;

    /**
     * Updater()
     * Instantiates a new Updater.
     *
     * @param chatArea chatArea of the Player which will be receive Servers message.
     * @param userList the ChatUsers list
     */
    public Updater(TextArea chatArea, TextArea userList) {
        this.client = Client.getInstance();
        Updater.userList = userList;
        Updater.chatArea = chatArea;
    }

    /**
     * The String input of the Scanner is in JSON-Format. In order to get the content of it we format it into a JSONMessage.
     * The type of the JSONMessage is equal to the instruction we are getting from the server.
     * Its MessageAdapter then is used to differentiate
     * between different  cases like we would normally do with instructions themselves.
     * The body of the message then contains various content.
     * For more info check out the attributes of the
     *
     * @link MessageObjects/MessageBody class.
     */
    public void run() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();

        while (client != null && client.in != null && client.socket.isConnected()) {
            if (client.in != null && client.in.hasNext()) {
                String msg = client.in.nextLine();
                logger.info(msg);
                Message message = gson.fromJson(msg, Message.class);
                String messageType = message.getMessageType();

                switch (messageType) {

                    case (MsgNames.error):
                        String errorMessage = message.getMessageBody().getError();
                        Platform.runLater(() -> chatArea.appendText(errorMessage + "\n"));
                        Platform.runLater(() -> chatArea.appendText("You can't do anything except closing the window.\n" +
                                                                    "Don't waste your Time! Have a wonderful Day :)"));
                        logger.error(errorMessage);
                        break;

                    case (MsgNames.playerAdded):
                        finishedPlayers.put(message.getMessageBody().getPlayer().getId(), false);
                        Player player = message.getMessageBody().getPlayer();
                        if (client.playerList.stream().noneMatch(cp -> cp.getId() == player.getId())) {
                            Platform.runLater(() -> chatArea.appendText(player.getName() + " entered the game\n"));
                            client.addPlayerToList(player);
                        }
                        buildUserList();
                        break;

                    case (MsgNames.receivedChat):
                        Integer from = message.getMessageBody().getFrom();
                        String sender = "[";
                        if (from == -1) {
                            sender += "Server";
                        } else {
                            try {
                                sender += Client.getInstance().getPlayerFromID(from).getName() + "_" + from;
                            } catch (Exception exception) {
                                sender += "Player already left";
                            }
                        }
                        String textMessage = message.getMessageBody().getMessage();
                        sender += "]: ";
                        String finalSender = sender;
                        Platform.runLater(() -> chatArea.appendText(finalSender + textMessage + "\n"));
                        break;

                    case (MsgNames.connectionUpdate):
                        String action = message.getMessageBody().getAction();
                        boolean connected = message.getMessageBody().isConnected();
                        int playerID1 = message.getMessageBody().getId();
                        try {
                            playerName = client.getPlayerFromID(playerID1).getName();
                            if (action.equals("Remove") && !connected) {
                                if (Client.getInstance().gameWindowOpen) {
                                    Context.getInstance().removePlayer(client.getPlayerFromID(playerID1).getFigure(), playerName);
                                }
                                Platform.runLater(() -> chatArea.appendText(playerName + " left the game\n"));
                                client.removePlayer(playerID1);
                                buildUserList();
                            }
                        } catch (Exception ignored) {
                        }
                        break;

                    case (MsgNames.gameStarted):
                        Context.getInstance().setMessage(message);
                        Platform.runLater(client::openGameWindow);
                        break;

                    case (MsgNames.playerStatus):
                        int playerID = message.getMessageBody().getId();
                        boolean status = message.getMessageBody().isReady();
                        Player playerInClientList = Client.getInstance().getPlayerFromID(playerID);
                        if (playerInClientList != null) {
                            String isReadyQ = " is not ready";
                            if (message.getMessageBody().isReady()) {
                                isReadyQ = " is ready";
                            }
                            if (playerInClientList.isReady() && !status || !playerInClientList.isReady() && status) {
                                String statusMessage = playerInClientList.getName() + isReadyQ;
                                Platform.runLater(() -> chatArea.appendText(statusMessage + "\n"));
                            }
                        }
                        client.setPlayerStatus(playerID, status);
                        buildUserList();
                        break;

                    case (MsgNames.selectMap):
                        Client.getInstance().firstReadyPlayer.set(true);
                        ChatGuiContext.getInstance().availableMaps = Arrays.asList(message.getMessageBody().getAvailableMaps());
                        if (Client.getInstance().getPlayer().isAI()) {
                            ChatGuiContext.getInstance().aiSendsRandomMap(message.getMessageBody().getAvailableMaps());
                        }
                        Platform.runLater(() -> chatArea.appendText("[GAME]: You can select a map\n"));
                        break;

                    case (MsgNames.currentPlayer):
                        //check if it is this or playerIDs
                        int currentPlayer = message.getMessageBody().getId();
                        Context.getInstance().setCurrentPlayer(currentPlayer);
                        break;

                    case (MsgNames.activePhase):
                        int phase = message.getMessageBody().getPhase();
                        if (phase == 2) {
                            round++;
                            Context.getInstance().setRound(round);
                        }
                        Context.getInstance().setPhase(message.getMessageBody().getPhase());
                        break;

                    case (MsgNames.startingPointTaken):
                        int position = message.getMessageBody().getPosition();
                        playerID = message.getMessageBody().getId();
                        Context.getInstance().setInitRobot(position, client.getPlayerFromID(playerID).getFigure());
                        Client.getInstance().addState(position, playerID);
                        break;

                    case (MsgNames.yourCards):
                        cards = message.getMessageBody().getCards();
                        int cardsInPile = message.getMessageBody().getCardsInPile();
                        Context.getInstance().cardInDeck.setVisible(true);
                        Context.getInstance().setCardsInDeck(cardsInPile);
                        for (int i = 0; i < cards.length; i++) {
                            int finalI = i;
                            Platform.runLater(() -> Context.getInstance().setCardInAllCards(cards[finalI], finalI));
                        }
                        for (int i = 0; i < 5; i++) {
                            int finalI = i;
                            Platform.runLater(() -> Context.getInstance().setCardInProgrammingCards("", finalI));
                        }
                        break;

                    case (MsgNames.notYourCards):
                        playerID = message.getMessageBody().getId();
                        if (Client.getInstance().getPlayer().getId() == playerID) {
                            Context.getInstance().cardInDeck.setVisible(true);
                            cardsInPile = message.getMessageBody().getCardsInPile();
                            Context.getInstance().setCardsInDeck(cardsInPile);
                        }
                        break;

                    case (MsgNames.shuffleCoding):
                        playerID = message.getMessageBody().getId();
                        if (client.getPlayer().getId() == playerID) {
                            Context.getInstance().printShuffleMessage();
                        }
                        break;

                    case (MsgNames.cardSelected):
                        playerID = message.getMessageBody().getId();
                        int register = message.getMessageBody().getRegister();
                        Context.getInstance().setPlayerID(playerID);
                        Context.getInstance().setRegister(register);
                        break;

                    case (MsgNames.selectionFinished):
                        int finishedPlayer = message.getMessageBody().getId();
                        String finishedPlayerName = client.getPlayerFromID(finishedPlayer).getName();
                        Context.getInstance().addFinishedPlayer(finishedPlayer, finishedPlayerName);
                        if (finishedPlayer == client.getPlayer().getId()) {
                            Context.getInstance().setSelectionFinished(true);
                        }
                        finishedPlayers.put(finishedPlayer, true);
                        break;

                    case (MsgNames.timerStarted):
                        Context.getInstance().setTimerRunning(true);
                        break;

                    case (MsgNames.discardHand):
                        playerID = message.getMessageBody().getId();
                        if (Client.getInstance().getPlayer().getId() == playerID) {
                            for (int i = 0; i < 9; i++) {
                                Context.getInstance().setCardInAllCards("", i);
                            }
                        }
                        break;

                    case (MsgNames.cardsYouGotNow):
                        if (!Context.getInstance().isTimerRunning()) {
                            String[] cardsYouGotNow = message.getMessageBody().getCards();
                            for (int i = 0; i < cardsYouGotNow.length; i++) {
                                Context.getInstance().setCardInProgrammingCards(cardsYouGotNow[i], i);
                            }
                        }
                        break;

                    case (MsgNames.cardPlayed):
                        Context.getInstance().cardsPlayedConfirmed = true;
                        break;

                    case (MsgNames.timerEnded):
                        Context.getInstance().setTimerRunning(false);
                        break;

                    case (MsgNames.currentCards):
                        List<ActiveCard> activeCards = message.getMessageBody().getActiveCards();
                        Context.getInstance().handleCurrentCards(activeCards);
                        break;

                    case (MsgNames.movement):
                        playerID = message.getMessageBody().getId();
                        position = message.getMessageBody().getTo();
                        logger.info(playerID + " : " + position);
                        if (client.getPlayerFromID(playerID) != null) {
                            Context.getInstance().setRobot(position, client.getPlayerFromID(playerID).getFigure());
                        }
                        break;

                    case (MsgNames.drawDamage):
                        playerID = message.getMessageBody().getId();
                        playerName = Client.getInstance().getPlayerFromID(playerID).getName();
                        cards = message.getMessageBody().getCards();
                        if (Client.getInstance().getPlayer().getId() == playerID) {
                            for (int i = 0; i < cards.length; i++) {
                                int finalI = i;
                                Platform.runLater(() -> Context.getInstance().printGameMessage(playerName + " got a " + cards[finalI] + " Card.", Color.RED));
                            }
                        }
                        break;

                    case (MsgNames.pickDamage):
                        Platform.runLater(() -> Context.getInstance().damageCards.setGridLinesVisible(true));
                        Platform.runLater(() -> Context.getInstance().damageCards.setDisable(false));
                        Context.getInstance().setCount(message.getMessageBody().getCount());
                        cards = new String[]{"Worm", "Virus", "Trojan Horse", "SPAM"};
                        for (int i = 0; i < cards.length; i++) {
                            int finalI = i;
                            Platform.runLater(() -> Context.getInstance().setCardInDamageCards(cards[finalI], finalI));
                        }
                        Context.getInstance().setDamageSelectable(true);
                        break;
                    case (MsgNames.playerShooting):
                        break;
                    case (MsgNames.reboot):
                        playerID = message.getMessageBody().getId();
                        playerName = client.getPlayerFromID(playerID).getName();
                        Platform.runLater(() -> Context.getInstance().printGameMessage(playerName + " reboot.", Color.GREEN));
                        break;

                    case (MsgNames.playerTurning):
                        playerID = message.getMessageBody().getId();
                        String direction = message.getMessageBody().getDirection();
                        int figure = client.getPlayerFromID(playerID).getFigure();
                        Context.getInstance().setRobotWithOrientation(figure, direction);
                        break;

                    case (MsgNames.energy):
                        playerID = message.getMessageBody().getId();
                        Context.getInstance().pickedUpCube(playerID, message.getMessageBody().getCount(), false);
                        logger.info(playerName + " got one Energy Cube.");
                        break;

                    case (MsgNames.checkpointReached):
                        playerID = message.getMessageBody().getId();
                        int number = message.getMessageBody().getNumber();
                        playerName = Client.getInstance().getPlayerFromID(playerID).getName();
                        Platform.runLater(() -> Context.getInstance().printGameMessage(playerName + " has reached " + number + " checkpoints."));
                        if (Client.getInstance().getPlayer().getId() == playerID) {
                            Platform.runLater(() -> Context.getInstance().checkpointGridPane.setGridLinesVisible(true));
                            Platform.runLater(() -> Context.getInstance().checkpointGridPane.setDisable(false));
                            Platform.runLater(() -> Context.getInstance().getCheckpointsReached().setVisible(true));
                            Platform.runLater(() -> Context.getInstance().setCheckPoint(number));
                        }
                        logger.info(playerName + " has reached " + number + " checkpoints.");
                        break;

                    case (MsgNames.gameWon):
                        playerID = message.getMessageBody().getId();
                        if (!Context.getInstance().gameIsOver) {
                            Platform.runLater(() -> handleGameWon(playerID));
                            logger.info(playerID + " won the Game!");
                        }
                        Platform.runLater(() -> Client.getInstance().gameStage.close());
                        Context.getInstance().gameIsOver = true;
                        break;

                    default:
                        Platform.runLater(() -> chatArea.appendText("ERROR: MessageType: " + messageType + " is not covered!"));
                        logger.error("ERROR: MessageType: " + messageType + " is not covered!");
                }
            } else {
                Platform.runLater(() -> chatArea.appendText("[GAME]: Connection Lost"));
                Platform.runLater(() -> Context.getInstance().printGameMessage("[GAME]: Connection Lost", Color.DARKRED));
                break;
            }
        }
        Platform.runLater(() -> {
            Alert connectionLost = new Alert(Alert.AlertType.ERROR);
            connectionLost.setTitle("Connection Lost");
            connectionLost.setHeaderText("Connection Lost");
            connectionLost.setContentText("""
                    The Connection to the Server is lost.
                    Clicking Ok will close the program.
                    See you next time :)""");

            Stage alertStage = (Stage) connectionLost.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(Client.getInstance().roboIcon);
            alertStage.setResizable(false);

            Optional<ButtonType> result = connectionLost.showAndWait();
            if (result.isEmpty()) {
                Platform.runLater(() -> connectionLost.setContentText("[GAME]: Please close the window with OK"));
            }
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    Platform.exit();
                    System.exit(-1);
                    Client.getInstance().closeEverything();
                }
            }
        });
    }


    /**
     * buildUserList()
     * This function adds ChatUsers in ChatList
     */
    public void buildUserList() {
        Platform.runLater(() -> userList.setText(""));
        for (Player player : client.playerList) {
            if (player.getId() != Client.getInstance().getPlayer().getId()) {
                String isReadyQ = " [not ready]\n";
                if (player.isReady()) {
                    isReadyQ = " [ready]\n";
                }
                String finalIsReadyQ = isReadyQ;
                Platform.runLater(() -> userList.appendText(player.getName() + ": " + player.getId() + finalIsReadyQ));
            }
        }
    }

    /**
     * handleGameWon()
     * This function informs which player won or defeated the game
     * According to the ID of the player who will be received the GameWon or GameOver message
     *
     * @param playerID playerID of Player
     */
    public void handleGameWon(int playerID) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String title = "GAME ";
        String message;
        if (playerID == Client.getInstance().getPlayer().getId()) {
            title += "WON :)";
            message = "  Here is your bachelor's degree!\n";
        } else {
            title += "OVER :(";
            message = "  Don't worry! Take one beer und try again.\n";
        }
        message += "\n  All windows will be closed by clicking on OK";
        alert.setTitle(title);
        Text messageText = new Text(message);
        messageText.setStyle("-fx-font: 14 arial;");
        messageText.setWrappingWidth(400);
        alert.getDialogPane().setContent(messageText);

        Text titleMessage = new Text(title);
        titleMessage.setStyle("-fx-font:20 arial");
        alert.getDialogPane().setHeaderText(titleMessage.getText());

        ImageView icon = new ImageView(Client.getInstance().roboIcon);
        icon.setFitHeight(80);
        icon.setFitWidth(80);
        alert.setGraphic(icon);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(Client.getInstance().roboIcon);
        alertStage.setResizable(false);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) {
            Platform.runLater(() -> alert.setContentText("Please Close the window with OK"));
        }
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(-1);
                Client.getInstance().closeEverything();
            }
        }
    }
}