package View;

import ClientServer.Client;
import MessageObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.*;


/**
 * Class Context
 * <p>
 * This Class is a Singleton, which is used as a shared platform between the Updater and the GameGui Controller
 *
 * @author Tobias Celik, Hoang An Tran, Simon Wiethase
 */
public class Context {

    @FXML
    public TextFlow gameMessagesDisplay;
    public GridPane allCards;
    public GridPane programmingCards;
    public GridPane damageCards;
    public GridPane checkpointGridPane;
    public Label energyCubes;
    public Label checkpointsReached;
    public Label cardInDeck;
    public TextArea playItTextArea;

    public int stage = 0;

    protected final Map<Integer, int[]> robotPositions = new HashMap<>();
    protected final Map<Integer, Integer> robotStartPoints = new HashMap<>(), currentRoboPosition = new HashMap<>();
    protected final Map<Integer, Double> robotOrientation = new HashMap<>();
    protected final Map<Integer, Boolean> robotChanged = new HashMap<>();
    protected final List<int[]> possibleStartPositions = new ArrayList<>();
    protected boolean  gameIsOver = false, cardsPlayedConfirmed = false;
    protected String currentPhase = "Current Phase", whatTodo = "", playItDialog, selectedMap;
    protected HashMap<Integer, Boolean> finishedPlayers = new HashMap<>();
    protected int currentPlayer = -1;
    protected RoboImage robotImageDB;
    protected Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();
    protected List<Gson> cardsMessagesQueue = new ArrayList<>();
    protected List<String> cardsMessagesTextQueue = new ArrayList<>(), powerUpQueue = new ArrayList<>();
    protected List<Message> movementMessageQueue = new ArrayList<>();
    protected List<ActiveCard> activeCards = new ArrayList<>();
    protected BooleanProperty activatePlayIT = new SimpleBooleanProperty(false), selectCardActive = new SimpleBooleanProperty(false);

    private GridPane gameBoard;
    private int currentCheckpoint = 1;
    private boolean isAI = false;
    private CardsBuilder cardsBuilder;
    private String currentPlayerName;
    private int phase = 0, round = 1, cardsInDeck = 0;
    private boolean timerRunning = false;
    private boolean isSelectionFinished = false;
    private int register, playerID;
    private Message message;
    private final List<String> selectDamageCards = new ArrayList<>();
    private int count = 0;
    private AI aiInstance;
    private Boolean damageSelectable = false;
    private final Map<Integer, String> neutralLasersOnMap = new HashMap<>();
    private final Map<Integer, String> endLasersOnMap = new HashMap<>();
    private final static Context instance = new Context();
    private static final Logger logger = LogManager.getLogger(Context.class);

    public static Context getInstance() {
        return instance;
    }

    /**
     * Prints a Message on the GameMessage Window in the GameGui and you can choose the Color
     *
     * @param gameMessage: String text of the Message
     * @param color:       Color of the Message
     */
    public void printGameMessage(String gameMessage, Color color) {
        Text message = new Text(gameMessage + "\n");
        message.setFill(color);
        Platform.runLater(() -> gameMessagesDisplay.getChildren().add(message));
    }

    /**
     * Fast Access, this time the Message will be sent in Blueviolet
     *
     * @param gameMessage: String text of the Message
     */
    public void printGameMessage(String gameMessage) {
        printGameMessage(gameMessage, Color.BLUEVIOLET);
    }

    public void printShuffleMessage() {
        printGameMessage("Carddeck was shuffled and refilled", Color.BLUE);
    }

    public void initCardsBuilder(GridPane allCards, GridPane programmingCards, GridPane damageCards) {
        cardsBuilder = new CardsBuilder(allCards, programmingCards, damageCards);
    }

    /**
     * Sets the current Phase of the Game, informs the player off the current phase and dis- or enables certain functions
     *
     * @param phase: int Value of the phase
     */
    public void setPhase(int phase) {
        this.phase = phase;
        switch (phase) {
            case 1 -> this.currentPhase = "Upgrade Phase";
            case 2 -> {
                finishedPlayers.clear();
                stage = 1;
                selectCardActive.set(true);
                this.currentPhase = "Round: " + this.round + " - Programming Phase";
                this.currentPlayerName = Client.getInstance().getPlayer().getName();
                whatToDoFunctions();
            }
            case 3 -> {
                stage = 2;
                this.timerRunning = false;
                this.currentPhase = "Round: " + this.round + " - Activation Phase";
                this.whatTodo = "Enjoy";
            }
            default -> this.currentPhase = "Starting Phase";
        }
        if (isAI && this.aiInstance != null) {
            aiInstance.notifyAI();
        }

    }

    /**
     * When a player finishes his card selection,
     * the other players get a notification, his Carddeck will be disabled, and the Timer will be started
     * finished players are stored in a Hashmap, with their ID as key, and a boolean value of their status as value
     *
     * @param id:   id of the finished player
     * @param name: name of the finished player
     */
    public void addFinishedPlayer(int id, String name) {
        Context.getInstance().printGameMessage(name + " is done!", Color.GREEN);
        if (Client.getInstance().getPlayer().getId() == id) {
            selectCardActive.set(false);
        }
        finishedPlayers.put(id, true);
        whatToDoFunctions();
    }

    /**
     * this Method informs a Player what to do during the Programming Phase
     */
    private void whatToDoFunctions() {
        this.whatTodo = "Select 5 of the 9 cards below";
        if (timerRunning) {
            if (selectCardActive.get()) {
                this.whatTodo += " - Hurry up, time is running out";
            } else {
                this.whatTodo = "Wait until all players placed their cards";
            }
        }
    }

    /**
     * This Method is called in the "Turning" Case in the @Updater, it calculates the current Orientation of a robot and stores it in a Hashmap
     *
     * @param figure:      figure-Number of a robot
     * @param orientation: the new orientation the robot faces
     */
    public void setRobotWithOrientation(int figure, String orientation) {
        robotChanged.put(figure, true);
        Double newOrientation = robotOrientation.get(figure);
        switch (orientation) {
            case "clockwise" -> newOrientation += 90;
            case "counterClockwise" -> newOrientation += 270;
            case "UP" -> newOrientation = 270.0;
        }
        if (orientation.equals("UP")) {
            setRobot(47, figure);
        }
        if (newOrientation >= 360) {
            newOrientation -= 360;
        }
        robotOrientation.put(figure, newOrientation);

    }

    /**
     * This Method calculates the X and Y Position of a Robot and stores it in a Hashmap
     *
     * @param position: The number of the field, where the robot have to go
     * @param figure:   the ID of the robot
     */
    protected void setRobot(int position, int figure) {
        currentRoboPosition.put(figure, position);
        if (!robotOrientation.containsKey(figure)) {
            robotOrientation.put(figure, 0.0);
        }
        int y_field = position / 13;
        int x_field = (position % 13);
        robotPositions.put(figure, new int[]{x_field, y_field});
        robotChanged.put(figure, true);
    }

    /**
     * This Method gets the current Round, stores it in a variable, and enables some functions,
     * which were disabled in the former round.
     *
     * @param round: number of the new round
     */
    public void setRound(int round) {
        this.round = round;
        if (round > 1) {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                Platform.runLater(() -> setCardInProgrammingCards("", finalI));
            }
            setSelectionFinished(false);
            allCards.setDisable(false);
            programmingCards.setDisable(false);
        }
    }

    /**
     * This Method gets a List of ActiveCards, which are labled with the name of the Player and the name of the Card.
     * ActiveCards show, which Player plays which Card in the current Register, for every Round there are five Registers.
     * The List will be processed in the doActiveCardDialogue() method.
     * If the Player is not an AI, he has to confirm them by Clicking on a button,
     * the AI sends automaticly a confirmation back to the Server
     *
     * @param activeCards: List of ActiveCards
     */
    public void handleCurrentCards(List<ActiveCard> activeCards) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        Gson gson = gsonBuilder.create();
        this.activeCards = activeCards;
        doActiveCardDialogue(activeCards);
        cardsMessagesQueue.add(gson);
        if (!isAI()) {
            whatTodo = "Confirm the movement by clicking on Play IT";
            activatePlayIT.set(true);
        } else {
            useLastCard();
        }
    }

    /**
     * This Method processes the ActiveCard List, and prepares the Message, which will be shown to each Player.
     * The Message informs every Player of the current card the other players will play in the current round.
     *
     * @param activeCards List of ActiveCards
     */
    protected void doActiveCardDialogue(List<ActiveCard> activeCards) {
        powerUpQueue.clear();
        StringBuilder playerAndCard = new StringBuilder();
        for (ActiveCard activeCard : activeCards) {
            int playerID = activeCard.playerId;
            String playerName = Client.getInstance().getPlayerFromID(playerID).getName();
            playerAndCard.append(playerName).append(": ").append(activeCard.card).append("\n");
            if (activeCard.card.equalsIgnoreCase("PowerUp")) {
                powerUpQueue.add(playerID + ":" + 1);
            }
        }
        String finalPlayerAndCard = playerAndCard.toString();
        cardsMessagesTextQueue.add(finalPlayerAndCard);
        if (!isAI) {
            playItDialog = finalPlayerAndCard;
        }
    }

    /**
     * This Method is for the AI only. It collects all responses in a Queue and sends them out.
     */
    public void useLastCard() {
        if (cardsMessagesQueue.size() > 0) {
            Message playIT = new PlayIt();
            String playITJson = cardsMessagesQueue.get(0).toJson(playIT);
            Client.getInstance().out.println(playITJson);
            cardsMessagesQueue.remove(0);
            cardsMessagesTextQueue.remove(0);
            if (cardsMessagesTextQueue.size() > 0) {
                Platform.runLater(() -> playItTextArea.setText("Next Cards:\n" + cardsMessagesTextQueue.get(0)));
            }
        }
        for (Message message : movementMessageQueue) {
            int tempPlayerID = message.getMessageBody().getId();
            int tempPosition = message.getMessageBody().getTo();
            setRobot(tempPosition, Client.getInstance().getPlayerFromID(tempPlayerID).getFigure());
        }
        movementMessageQueue.clear();
        for (String powerUpMessage : this.powerUpQueue) {
            String[] pumSplit = powerUpMessage.split(":");
            pickedUpCube(Integer.parseInt(pumSplit[0].trim()), 1, true);
        }
        this.powerUpQueue.clear();
    }

    /**
     * This Method just responses to the Server and confirms that the Player knows, which cards will be played.
     * To be honest, I do not know we have to implement this
     */
    protected void sendPlayItMessage() {
        Message playIT = new PlayIt();
        String playITJson = gson.toJson(playIT);
        Client.getInstance().out.println(playITJson);
        logger.info(playITJson);
        whatTodo = "Enjoy";
        activatePlayIT.set(false);
        for (String powerUpMessage : this.powerUpQueue) {
            String[] pumSplit = powerUpMessage.split(":");
            pickedUpCube(Integer.parseInt(pumSplit[0].trim()), 1, true);
        }
        this.powerUpQueue.clear();
    }

    /**
     * This Method modifies the GameBoard after an EnergyCube was picked up
     * The EnergyCubes are just a painted orange square which appear and disappear with a binding
     *
     * @param playerID ID of the Player who picked up the cube. Over the ID we can determine the robot, its position
     *                 and modify the correct "EnergyCube"-Binding in the GameBoardBuilder
     * @param count    number of Energy Cubes the Player picked up
     * @param fromCard determines if the EnergyCube was from a card or a powerstation
     */
    public void pickedUpCube(int playerID, int count, boolean fromCard) {
        String playerName = "You";
        String action = "";
        if (fromCard) {
            action = " got ";
        } else {
            int playerFigure = Client.getInstance().getPlayerFromID(playerID).getFigure();
            int position = currentRoboPosition.get(playerFigure);
            if (GameBoardBuilder.getInstance().energyCubesVisible.get(position).get()) {
                action = " picked up ";
                GameBoardBuilder.getInstance().energyCubesVisible.get(position).set(false);
            }
        }
        if (!action.equals("")) {
            if (playerID != Client.getInstance().getPlayer().getId()) {
                playerName = Client.getInstance().getPlayerFromID(playerID).getName();
            } else {
                Client.getInstance().getPlayer().increaseEnergyCubes(count);
            }
            String actionFinal=action;
            String playerNameFinal = playerName;
            Platform.runLater(() -> Context.getInstance().printGameMessage(playerNameFinal + actionFinal + "one Energy Cube.", Color.ORANGE));
        }
    }

    /**
     * This Method sends a selected Starting Point to the Server.
     * It first checks, if the Position is already Taken.
     * Background-Information: The Server tells every connected Client the starting Position of a Player.
     * This Information is stored in a List in the Client-Singleton
     * If the position is free, the Client tells the Server the Starting Position.
     * If not, a warning window will be shown
     *
     * @param x_field: x-position of the Starting Point
     * @param y_field: y-position of the Starting Point
     */
    public void sendStartingPoint(int x_field, int y_field) {
        int position = y_field * 13 + x_field;

        if (!Client.getInstance().getTakenStartingPositions().contains(position)) {
            Message startingPoint = new SetStartingPoint(position);
            String startingPointJson = gson.toJson(startingPoint);
            Client.getInstance().out.println(startingPointJson);
            logger.info(startingPointJson);
        } else {
            this.warningAlert("Position already taken",
                    "Position already taken",
                    "Choose an other position!");
            logger.warn("Position already taken" + "\nChoose an other position!");
        }
    }

    /**
     * This Method creates a Warning-Window
     *
     * @param title:   title of the window
     * @param header:  header of the warning
     * @param content: a detailed description of the problem and how to avoid it
     */
    public void warningAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(Client.getInstance().roboIcon);
        alertStage.showAndWait();
    }

    /**
     * This Method gets the name of a Card and its Position on the programming deck
     * It sends both Information to the Server
     *
     * @param cardName: name of the Cards
     * @param register: position of the card in the programming deck
     */
    public void sendCard(String cardName, int register) throws Exception {
        if (register + 1 < 1 || register + 1 > 5) {
            throw new Exception("trying to send out of bounds register " + register);
        }
        Message selectCard;
        if (Objects.equals(cardName, "")) {
            selectCard = new SelectCard(null, register + 1);
        } else {
            selectCard = new SelectCard(cardName, register + 1);
        }
        logger.info("Client did send card: " + cardName + " on register " + (register + 1));

        String selectCardJson = gson.toJson(selectCard);
        Client.getInstance().out.println(selectCardJson);
    }

    /**
     * This Method notifies other Players, when a player Disconnects and removes his robot from the field
     *
     * @param figure:     ID of the Robot
     * @param playerName: Name of the Player
     */
    public void removePlayer(int figure, String playerName) {
        if (currentRoboPosition.containsKey(figure)) {
            Platform.runLater(() -> rewriteTileAtPosition(currentRoboPosition.get(figure)));
            robotPositions.remove(figure);
        }
        Platform.runLater(() -> printGameMessage(playerName + " left the Game", Color.RED));
    }

    /**
     * This Method triggers the rewriting of a single tile on the gameboard.
     * the main Reason: When a player moves away from a field, the overlaying Image of the Robot must be deleted,
     * the easiest way is by rewriting the field at a current position.
     * The rewriting happens in the GameGuiBuilder Class
     *
     * @param position: position of the field
     */
    public void rewriteTileAtPosition(int position) {
        List<MessageObjects.Map> singleTileMap = new ArrayList<>();
        for (MessageObjects.Map currentMap : message.getMessageBody().getMap()) {
            if (currentMap.position == position) {
                singleTileMap.add(currentMap);
            }
        }
        Message singleTileMessage = new GameStarted(singleTileMap);
        boolean laserOnPosition = false;
        if (neutralLasersOnMap.containsKey(position)) {
            GameBoardBuilder.getInstance().drawNeutralLaser(gameBoard, position, neutralLasersOnMap.get(position));
            laserOnPosition = true;
        }
        if (endLasersOnMap.containsKey(position)) {
            GameBoardBuilder.getInstance().drawWallLaser(gameBoard, position, endLasersOnMap.get(position));
            laserOnPosition = true;
        }
        GameBoardBuilder.getInstance().build(gameBoard, singleTileMessage, false);
        if (laserOnPosition) {
            for (Map.Entry<Integer, Integer> roboPositionSet : currentRoboPosition.entrySet()) {
                int figure = roboPositionSet.getKey();
                int roboPosition = roboPositionSet.getValue();
                if (roboPosition == position && robotPositions.get(figure) != null) {
                    int x = robotPositions.get(figure)[0];
                    int y = robotPositions.get(figure)[1];
                    ImageView robot = Context.instance.fitCheckPointImage(robotImageDB.getRoboImage(figure));
                    robot.setPreserveRatio(true);
                    robot.setRotate(robotOrientation.get(figure));
                    Platform.runLater(() -> gameBoard.add(robot, x, y));
                }
            }
        }
    }

    /**
     * This Method fits the Imag of a robots to certain Limits and transfer its format to ImageView
     *
     * @param image: Image of the Robot
     * @return ImageView-Object of the image
     */

    public ImageView fitCheckPointImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(this.gameBoard.widthProperty().divide(13));
        imageView.fitHeightProperty().bind(this.gameBoard.heightProperty().divide(10));
        imageView.setPreserveRatio(true);
        return imageView;
    }


    /**
     * This Method fits an Image of a Checkpoint to a 30x50 ImageView in order to show it in a Grid
     *
     * @param imageName: name of the Image, the Image will be retrieved from an Internal Image-Database
     * @return ImageView of the requested image in the wished format
     */
    private ImageView fitCheckPointImage(String imageName) {
        ImageView imageView = new ImageView(ImageResources.getInstance().getImage(imageName));
        imageView.setFitHeight(30);
        imageView.setFitWidth(50);
        return imageView;
    }

    /**
     * When the Spam-Cards are over, the Player has to choose between other Damage-Cards,
     * and sometimes he has to draw more then on Card.
     * The Player selects a Damagecard from a clickable grid in the GameGui, triggering this method.
     * The Method gets the position of the selected Card in the Grid, gets its name with the Switch-Case-Query,
     * and stores it in a List. When the Size of the List reaches the number of requested Damage-Cards,
     * a List of the selected Damage-Cards will be sent to the Server
     *
     * @param xPos: Position of the Damagecard in the damageCards-Grid in the GameGui
     */
    protected void selectDamageCards(int xPos) {
        int count = this.getCount();
        String cardName = switch (xPos) {
            case 0 -> "Worm";
            case 1 -> "Virus";
            case 2 -> "Trojan Horse";
            default -> "SPAM";
        };
        selectDamageCards.add(cardName);
        if (selectDamageCards.size() == count) {
            Platform.runLater(() -> damageCards.setDisable(true));
            String[] cards = selectDamageCards.toArray(new String[count]);
            Message selectDamage = new SelectDamage(cards);
            String selectDamageJson = gson.toJson(selectDamage);
            Client.getInstance().out.println(selectDamageJson);
            logger.info(selectDamageJson);
            damageCards.setStyle("-fx-background-color:null; -fx-opacity:1;");
            selectDamageCards.clear();
            Context.getInstance().setCount(0);
        }
    }

    /**
     * This Method sets the Current Player (choosen by the Game and transmitted from the Server to all clients),
     * notify other Players, and enables functions for the current Player exclusively
     *
     * @param currentPlayerId: ID of the current Player
     */
    public void setCurrentPlayer(int currentPlayerId) {
        String pre = "";
        if (stage == 0) {
            this.currentPlayer = currentPlayerId;
            if (currentPlayerId == Client.getInstance().getPlayer().getId()) {
                pre = " (you)";
                this.whatTodo = "Place your robot on a free, green marked spot";
                GameBoardBuilder.getInstance().greenScreen.set(true);
            } else {
                if (Client.getInstance().startPositionFix) {
                    this.whatTodo = "Wait until all players placed their robots";
                } else {
                    this.whatTodo = "Please wait, you can place your robot soon";
                }
                GameBoardBuilder.getInstance().greenScreen.set(false);
            }
        } else {
            this.whatTodo = "";
        }
        if (isAI && this.aiInstance != null) {
            aiInstance.notifyAI();
        }
        this.currentPlayerName = Client.getInstance().getPlayerFromID(currentPlayerId).getName() + pre;
    }

    public void setDamageSelectable(Boolean isSelectable) {
        if (isAI && this.aiInstance != null) {
            this.damageSelectable = isSelectable;
            aiInstance.notifyAI();
        }
    }

    public void setRobotImageDB(RoboImage robotImageDB) {
        this.robotImageDB = robotImageDB;
    }

    public void setGameBoard(GridPane gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Map<Integer, int[]> getRobotPositions() {
        return robotPositions;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Map<Integer, String> getAllCardsNames() {
        return cardsBuilder.allCardsNames;
    }

    public Map<Integer, String> getAllProgrammingCardsNames() {
        return cardsBuilder.allProgrammingCardsNames;
    }

    public void setCardInAllCards(String card, int index) {
        Platform.runLater(() -> cardsBuilder.setCard(cardsBuilder.allCards, card, index));
        cardsBuilder.allCardsNames.put(index, card);
    }

    /**
     * This Method places Damagecards in the damageCards grid in the GameGui
     *
     * @param card:  name of the Card
     * @param index: position of the Card in the Grid
     */
    public void setCardInDamageCards(String card, int index) {
        this.whatTodo = "Select " + this.getCount() + " damagecard(s)";
        cardsBuilder.damageCards.setStyle("-fx-background-color:rgba(0,255,0,1.0); -fx-opacity:1;");
        Platform.runLater(() -> cardsBuilder.setCard(cardsBuilder.damageCards, card, index));
        cardsBuilder.damageCardsNames.put(index, card);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public int getPhase() {
        return phase;
    }

    public void setGameMessagesDisplay(TextFlow gameMessagesDisplay) {
        this.gameMessagesDisplay = gameMessagesDisplay;
    }

    protected boolean isSelectionFinished() {
        return !isSelectionFinished;
    }

    public void setSelectionFinished(boolean selectionFinished) {
        isSelectionFinished = selectionFinished;
        whatToDoFunctions();
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setCardInProgrammingCards(String card, int index) {
        Platform.runLater(() -> cardsBuilder.setCard(cardsBuilder.programmingCards, card, index));
        cardsBuilder.allProgrammingCardsNames.put(index, card);
    }

    public void setAllCards(GridPane allCards) {
        this.allCards = allCards;
    }

    public GridPane getProgrammingCards() {
        return programmingCards;
    }

    public void setProgrammingCards(GridPane programmingCards) {
        this.programmingCards = programmingCards;
    }

    public GridPane getDamageCards() {
        return damageCards;
    }

    public void setDamageCards(GridPane damageCards) {
        this.damageCards = damageCards;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public void setEnergyCubes(Label energyCubes) {
        this.energyCubes = energyCubes;
    }

    public void setInitRobot(int position, int figure) {
        robotStartPoints.put(figure, position);
        setRobot(position, figure);
    }

    public void setPlayItTextArea(TextArea playItTextArea) {
        this.playItTextArea = playItTextArea;
    }

    public void setAiInstance(AI ai) {
        this.aiInstance = ai;
    }

    public Map<Integer, Double> getRobotOrientation() {
        return robotOrientation;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
        if (timerRunning) {
            whatToDoFunctions();
        }
    }

    public void setCheckpointGridPane(GridPane checkpointImageGridPane) {
        this.checkpointGridPane = checkpointImageGridPane;
    }

    public Label getCheckpointsReached() {
        return checkpointsReached;
    }

    public void setCheckpointsReached(Label checkpointsReached) {
        this.checkpointsReached = checkpointsReached;
    }


    public int getCurrentCheckpoint() {
        return this.currentCheckpoint;
    }


    /**
     * This Method prints the reached Checkpoints on Grid in the GameWindow
     *
     * @param index: number of the Checkpoint
     */
    public void setCheckPoint(int index) {
        switch (index) {
            case 1 -> checkpointGridPane.add(toImageView("checkpoint_" + 1 + "Image"), 0, 0);
            case 2 -> checkpointGridPane.add(toImageView("checkpoint_" + 2 + "Image"), 0, 1);
            case 3 -> checkpointGridPane.add(toImageView("checkpoint_" + 3 + "Image"), 1, 0);
            case 4 -> checkpointGridPane.add(toImageView("checkpoint_" + 4 + "Image"), 1, 1);
        }
        this.currentCheckpoint = index;
        Platform.runLater(() -> checkpointGridPane.setGridLinesVisible(false));
    }

    private ImageView toImageView(String imageName) {
        ImageView imageView = new ImageView(ImageResources.getInstance().getImage(imageName));
        imageView.setFitHeight(30);
        imageView.setFitWidth(50);
        return imageView;
    }

    public int getCardsInDeck() {
        return cardsInDeck;
    }

    public void setCardsInDeck(int cardsInPile) {
        this.cardsInDeck = cardsInPile;
    }

    public void setCardInDeck(Label cardInDeck) {
        this.cardInDeck = cardInDeck;
    }

    public Map<Integer, String> getNeutralLasersOnMap() {
        return neutralLasersOnMap;
    }

    public void putToNeutralLasersOnMap(int position, String orientation) {
        this.neutralLasersOnMap.put(position, orientation);
    }

    public void putToEndLasersOnMap(int position, String orientation) {
        this.endLasersOnMap.put(position, orientation);
    }

    public Map<Integer, String> getEndLasersOnMap() {
        return endLasersOnMap;
    }

    public Boolean isDamageSelectable() {
        return damageSelectable;
    }

    public void addPossibleStartPosition(int[] possiblePosition) {
        possibleStartPositions.add(possiblePosition);
    }

    public List<int[]> getPossibleStartPositions() {
        return possibleStartPositions;
    }
}