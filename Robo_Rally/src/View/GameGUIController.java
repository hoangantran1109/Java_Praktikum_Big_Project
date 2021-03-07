package View;

import ClientServer.Client;
import MessageObjects.Message;
import MessageObjects.MessageAdapter;
import MessageObjects.SelectCard;
import MessageObjects.SetStartingPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.*;

/**
 * Class GameGuiController
 * This Class serves as Controller for the GameGUI
 *
 * @author Tobias Celik, Hoang An Tran, Simon Wiethase
 */
public class GameGUIController implements Initializable {
    @FXML
    public AnchorPane gamePane;
    public Group gameBoardGroup;
    public Group bottomGroup;
    public GridPane rightGridPane;
    public GridPane bottomGridPane;
    public GridPane gameBoard;
    public GridPane allCards;
    public GridPane programmingCards;
    public GridPane damageCards;
    public GridPane checkpointGridPane;
    public Label currentPlayer;
    public TextFlow gameMessagesDisplay;
    public Label timerCountdown;
    public Label playerName;
    public Label currentPhase;
    public Label checkpointsReached;
    public Label cardInDeck;
    public Pane robotImagePane;
    public Label yourRobotLabel;
    public Label energyCubes;
    public Label controlPanel;
    public TextArea playItTextArea;
    public Button playItButton;
    public ScrollPane messageScrollPane;
    public CheckBox clickMusic;

    public Map<Integer, int[]> currentRoboPosition = new HashMap<>();
    private Audio audio;
    private static final Logger logger = LogManager.getLogger(GameGUIController.class);
    private Gson gson;
    protected RoboImage roboImage;

    /**
     * This method initialize the GameGUI, retrieves und passes Information and Objects from  and to other Classes
     *
     * @param url            standard FXML Parameter
     * @param resourceBundle standard FXML Parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameBoard.autosize();
        bottomGridPane.autosize();
        gamePane.autosize();
        rightGridPane.autosize();
        Context.getInstance().setGameBoard(gameBoard);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();
        roboImage = new RoboImage();
        robotImagePane.getChildren().add(Context.getInstance().fitCheckPointImage(roboImage.getRoboImage(Client.getInstance().getPlayer().getFigure())));
        Message boardMessageFromServer = Context.getInstance().getMessage();
        Message completeBoard = GameBoardBuilder.getInstance().createCompleteBoard(gameBoard, 13, 10, boardMessageFromServer);
        Context.getInstance().setMessage(completeBoard);
        Context.getInstance().initCardsBuilder(allCards, programmingCards, damageCards);
        Context.getInstance().setGameMessagesDisplay(gameMessagesDisplay);
        Context.getInstance().setAllCards(allCards);
        Context.getInstance().setProgrammingCards(programmingCards);
        Context.getInstance().setDamageCards(damageCards);
        Context.getInstance().setEnergyCubes(energyCubes);
        Context.getInstance().setPlayItTextArea(playItTextArea);
        Context.getInstance().setCheckpointGridPane(checkpointGridPane);
        Context.getInstance().setCheckpointsReached(checkpointsReached);
        Context.getInstance().setCardInDeck(cardInDeck);
        Context.getInstance().setRobotImageDB(roboImage);

        energyCubes.setText(String.valueOf(Client.getInstance().getPlayer().getEnergyCubes()));
        damageCards.setDisable(true);
        checkpointsReached.setVisible(false);
        checkpointGridPane.setDisable(true);
        cardInDeck.setVisible(false);

        messageScrollPane.vvalueProperty().bind(gameMessagesDisplay.heightProperty());

        bottomGridPane.scaleYProperty().bind(gamePane.heightProperty().divide(810));
        bottomGridPane.scaleXProperty().bind(gamePane.widthProperty().divide(1000));

        gameBoard.scaleYProperty().bind(gamePane.heightProperty().divide(810));
        gameBoard.scaleXProperty().bind(gamePane.widthProperty().divide(1000));

        rightGridPane.scaleYProperty().bind(gamePane.heightProperty().divide(810));
        rightGridPane.scaleXProperty().bind(gamePane.widthProperty().divide(1000));

        playItButton.styleProperty().bind(Bindings.when(Context.getInstance().activatePlayIT)
                .then("-fx-background-color:rgba(0,255,0,1.0); -fx-opacity:1;")
                .otherwise("-fx-background-color:null; -fx-opacity:1;"));

        allCards.styleProperty().bind(Bindings.when(Context.getInstance().selectCardActive).then
                ("-fx-background-color:rgba(0,255,0,1.0); -fx-opacity:1;").otherwise("-fx-background-color:null; -fx-opacity:1;"));

        if (Context.getInstance().isAI()) {
            playerName.setText("This is the AI: " + Client.getInstance().getPlayer().getName());
            playItButton.setVisible(false);
        } else {
            playerName.setText("You are: " + Client.getInstance().getPlayer().getName());
        }

        if (Context.getInstance().isAI()) {
            AI ai = new AI();
            Context.getInstance().setAiInstance(ai);
            new Thread(ai).start();
        }

        audio = new Audio();
        new Thread(audio).start();

        Refresher refresher = new Refresher(controlPanel, cardInDeck, allCards, currentPhase, currentRoboPosition, roboImage, gameBoard, playItTextArea, energyCubes, timerCountdown, currentPlayer, playItButton);
        new Thread(refresher).start();
    }

    /**
     * Eventhandler for the playIT Button, which send a Confirmation Message to the Server
     */
    public void clickSendPlayIt() {
        if (Context.getInstance().activatePlayIT.get()) {
            Context.getInstance().sendPlayItMessage();
        }
    }

    /**
     * This method is en Eventhandler for Mouseclicks on the Panel with all Cards
     * It registers which card was selected and passes the Information to the Context
     *
     * @param e MouseEvent Handler
     * @throws Exception
     */
    @FXML
    private void clickAllCardsGrid(MouseEvent e) throws Exception {
        boolean spaceLeftOnProgrammingCards = false;
        double x = e.getX();
        int programmingCardPosToAdd = 0;
        int xPos = (int) (x / 62);
        String cardName = Context.getInstance().getAllCardsNames().get(xPos);

        if (Context.getInstance().getPhase() != 2 || !Context.getInstance().selectCardActive.get()) {
            return;
        }

        if (Context.getInstance().isSelectionFinished()) {
            for (int i = 0; i < 5; i++) {
                if (Objects.equals(Context.getInstance().getAllProgrammingCardsNames().get(i), "")) {
                    programmingCardPosToAdd = i;
                    spaceLeftOnProgrammingCards = true;
                    break;
                }
            }
        }

        if (!Objects.equals(cardName, "") && spaceLeftOnProgrammingCards) {
            Context.getInstance().setCardInAllCards("", xPos);
            Context.getInstance().setCardInProgrammingCards(cardName, programmingCardPosToAdd);
        }
        if (Objects.equals(cardName, "Again") && programmingCardPosToAdd == 0) {
            Context.getInstance().whatTodo = "You can't place AgainCard in Register 1.";
            Context.getInstance().setCardInAllCards("Again", xPos);
            Context.getInstance().setCardInProgrammingCards("", 0);
            return;
        }
        if (!Objects.equals(cardName, "") && programmingCardPosToAdd == 4) {
            for (int i = 0; i < 9; i++) {
                int finalI = i;
                Platform.runLater(() -> Context.getInstance().setCardInAllCards("", finalI));
            }
            allCards.setDisable(true);
            programmingCards.setDisable(true);
        }
        int registernumber = programmingCardPosToAdd + 1;
        Context.getInstance().whatTodo = cardName + " placed in Register " + registernumber;
        sendCard(cardName, programmingCardPosToAdd);
    }

    /**
     * This method is en Eventhandler for Mouseclicks on the Panel with all selected Programming Cards
     * It registers which card was selected and passes the Information to the Context
     *
     * @param e MouseEvent Handler
     * @throws Exception
     */
    @FXML
    private void clickProgrammingCardsGrid(MouseEvent e) throws Exception {
        double x = e.getX();
        int allCardPosToAdd = 0;
        int xPos = (int) (x / 62);

        if (Context.getInstance().getPhase() != 2 || !Context.getInstance().selectCardActive.get()) {
            return;
        }

        String cardName = Context.getInstance().getAllProgrammingCardsNames().get(xPos);

        if (Context.getInstance().isSelectionFinished()) {
            for (int i = 0; i < 9; i++) {
                if (Objects.equals(Context.getInstance().getAllCardsNames().get(i), "")) {
                    allCardPosToAdd = i;
                    break;
                }
            }
        } else {
            programmingCards.setDisable(true);
            allCards.setDisable(true);
        }
        if (!Objects.equals(cardName, "")) {
            Context.getInstance().setCardInProgrammingCards("", xPos);
            Context.getInstance().setCardInAllCards(cardName, allCardPosToAdd);
        }
        int registernumber = xPos + 1;
        Context.getInstance().whatTodo = "Register " + registernumber + " is reset.";
        sendCard("", xPos);
    }

    /**
     * This method gets the selected CardName and register and passes the Information to the Server
     *
     * @param cardName name of the selected Card
     * @param register position of the Register [1-5]
     */
    private void sendCard(String cardName, int register) throws Exception {

        if (register + 1 < 1 || register + 1 > 5) {
            throw new Exception("trying to send out of bounds register " + register);
        }
        Message selectCard;
        if (Objects.equals(cardName, "")) {
            selectCard = new SelectCard(null, register + 1);
        } else {
            selectCard = new SelectCard(cardName, register + 1);
        }
        String selectCardJson = gson.toJson(selectCard);
        Client.getInstance().out.println(selectCardJson);
        logger.info(selectCardJson);
    }

    /**
     * This Method registers which Damagecard was selected by a mouseclick and passes the Information to the Context
     *
     * @param e MouseEvent
     */

    public void clickDamageCardsGrid(MouseEvent e) {
        double xCoord = e.getX();
        int xPos = (int) (xCoord / 62);
        Context.getInstance().selectDamageCards(xPos);
    }

    /**
     * This method is en Eventhandler for Mouseclicks on the Startpanel to register, from where a Player wants to Start
     * It registers which Position was taken, checks if the Player is the Current Player, the phase is the Starting Phase
     * and if the selected Position is a valid startingPosition and passes the Coordinates to the
     * sendStartingPoint Method or Throws an Alert Warning, if something went wrong
     *
     * @param mouseEvent MouseEvent Handler
     * @throws Exception
     */

    public void selectStartPosition(MouseEvent mouseEvent) {
        if (Context.getInstance().getPhase() != 0) {
            return;
        }
        if (Client.getInstance().getPlayer().getId() == Context.getInstance().currentPlayer) {
            if (Client.getInstance().startPositionFix) {
                this.warningAlert("Startposition already fix",
                        "You already have a startposition",
                        "You can not choose two startpositions!");
                logger.warn("You can not choose two startpositions!");
            } else {
                double x_click = mouseEvent.getX();
                double y_click = mouseEvent.getY();
                List<int[]> startPositions = Context.getInstance().getPossibleStartPositions();
                int x_field = (int) Math.floor(x_click / 50);
                int y_field = (int) Math.floor(y_click / 50);
                for (int[] startPosition : startPositions) {
                    if (startPosition[0] == x_field && startPosition[1] == y_field) {
                        sendStartingPoint(x_field, y_field);
                    }
                }
            }
        } else {
            this.warningAlert("Not the Current Player",
                    "You are not the Current Player",
                    "Wait until it's your turn!");
            logger.warn("Wait until it's your turn!");
        }
    }

    /**
     * This Method gets the X and Y coordination of the selected Startfield
     * It checks if the Field is free and sends a Message to the Server
     * or prints an Error Warning, if something went wrong
     *
     * @param x_field X coordinate of the selected Field
     * @param y_field Y coordinate of the selected Field
     */
    private void sendStartingPoint(int x_field, int y_field) {
        int position = y_field * 13 + x_field;
        if (!Client.getInstance().getTakenStartingPositions().contains(position)) {
            Message startingPoint = new SetStartingPoint(position);
            String startingPointJson = gson.toJson(startingPoint);
            Client.getInstance().out.println(startingPointJson);
        } else {
            this.warningAlert("Position already taken",
                    "Position already taken",
                    "Choose an other position!");
            logger.warn("Position already taken" + "- Choose an other position!");
        }
    }

    /**
     * This Method creates a warning Window
     *
     * @param title   title of the Window
     * @param header  header with little Information about the Problem
     * @param content detailed Information about the Problem and how to avoid it
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
     * Mutes or Unmutes the Music
     */
    public void clickedMuteMusic() {
        audio.mute();
    }
}