package View;

import ClientServer.Client;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Map;

/**
 * Class Refresher
 * This Class runs in a new Thread and implements methods from Runnable
 * It refreshes the GameGUI Window
 *
 * @author Tobias Celik, Hoang An Tran, Simon Wiethase
 */
public class Refresher implements Runnable {

    public GridPane gameBoard;
    public GridPane allCards;
    public GridPane programmingCards;
    public GridPane damageCards;
    public Label currentPlayer;
    public Label timerCountdown;
    public Label currentPhase;
    public Label cardInDeck;
    public Label energyCubes;
    public Label controlPanel;
    public Button playItButton;
    public TextArea playItTextArea;
    protected RoboImage roboImage;
    public Map<Integer, int[]> currentRoboPosition;

    private static final int TIMER = 200;
    private int milli = 1000, sec = 30;

    /**
     * Constructor to initialize this Class and its variables
     *
     * @param controlPanel        prints instructions to the player
     * @param cardInDeck          shows the number of cards left in the cardDeck
     * @param allCards            grid where the 9 Cards the server sends to the clients are shown
     * @param currentPhase        shows the current phase
     * @param currentRoboPosition map with all current Positions of every robot
     * @param roboImage           Image Database with the Images of the robots
     * @param gameBoard           gridPane which represents the gameBoard
     * @param playItTextArea      shows the ActiveCards
     * @param energyCubes         informs the Player about the number of his EnergyCubes
     * @param timerCountdown      30 Second countdown, which is left after the first player finished his card selection
     * @param currentPlayer       label which shows the current player
     * @param playItButton        button with which the Player can confirm the ActiveCards
     */
    public Refresher(Label controlPanel, Label cardInDeck, GridPane allCards, Label currentPhase,
                     Map<Integer, int[]> currentRoboPosition, RoboImage roboImage, GridPane gameBoard,
                     TextArea playItTextArea, Label energyCubes, Label timerCountdown, Label currentPlayer,
                     Button playItButton) {
        this.allCards = allCards;
        this.currentPhase = currentPhase;
        this.currentRoboPosition = currentRoboPosition;
        this.roboImage = roboImage;
        this.gameBoard = gameBoard;
        this.playItTextArea = playItTextArea;
        this.energyCubes = energyCubes;
        this.timerCountdown = timerCountdown;
        this.currentPlayer = currentPlayer;
        this.playItButton = playItButton;
        this.cardInDeck = cardInDeck;
        this.controlPanel = controlPanel;
    }

    public void run() {
        try {
            refresh();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This Method runs constantly and sleeps for 200 Milliseconds
     * It retrieves the Information from the Context and overwrites the current GUI Elements on the GameBoard
     *
     * @throws InterruptedException throws an explanation why there are any errors
     */
    private void refresh() throws InterruptedException {
        while (Client.getInstance().connected) {
            timer();
            //Show Current Phase
            Platform.runLater(() -> currentPhase.setText(Context.getInstance().currentPhase));
            //Show control
            Platform.runLater(() -> controlPanel.setText(Context.getInstance().whatTodo));
            //Place every Robot on Gridpane
            for (Map.Entry<Integer, int[]> entry : Context.getInstance().getRobotPositions().entrySet()) {
                int figure = entry.getKey();
                if (Context.getInstance().robotChanged.get(figure)) {
                    int x = entry.getValue()[0];
                    int y = entry.getValue()[1];
                    int old_x;
                    int old_y;
                    if (currentRoboPosition.containsKey(figure)) {
                        old_x = currentRoboPosition.get(figure)[0];
                        old_y = currentRoboPosition.get(figure)[1];
                        Platform.runLater(() -> Context.getInstance().rewriteTileAtPosition(old_y * 13 + old_x));
                    }
                    currentRoboPosition.put(figure, entry.getValue());
                    Image robot = roboImage.getRoboImage(figure);
                    ImageView robotImage = new ImageView(robot);
                    robotImage.fitWidthProperty().bind(this.gameBoard.widthProperty().divide(15));
                    robotImage.fitHeightProperty().bind(this.gameBoard.heightProperty().divide(12));

                    robotImage.setRotate(Context.getInstance().robotOrientation.get(figure));
                    Platform.runLater(() -> gameBoard.add(robotImage, x, y));
                    Context.getInstance().robotChanged.put(figure, false);
                }
            }
            if (Context.getInstance().activatePlayIT.get()) {
                Platform.runLater(() -> playItTextArea.setText(Context.getInstance().playItDialog));
            }
            if (Context.getInstance().cardsPlayedConfirmed) {
                Platform.runLater(() -> playItTextArea.appendText("Confirmed!"));
                Context.getInstance().cardsPlayedConfirmed = false;
            }
            //for EnergyCubes
            Platform.runLater(() -> energyCubes.setText(String.valueOf(Client.getInstance().getPlayer().getEnergyCubes())));
            //for CardsInDeck
            Platform.runLater(() -> cardInDeck.setText("Cards in Deck : " + Context.getInstance().getCardsInDeck()));
            //currentPlayerName
            Platform.runLater(() -> this.currentPlayer.setText("Current Player: " + Context.getInstance().getCurrentPlayerName()));

            Thread.sleep(TIMER);
        }
    }

    /**
     * This method models a 30 Second Countdown Timer.
     * It starts at 30 seconds and reduces it with each loop of the refresh Method
     * It is interrupted by a timerEnded Message from the Server or stops when reaching 0 seconds
     */
    private void timer() {
        if (Context.getInstance().isTimerRunning()) {
            if (!timerCountdown.isVisible()) timerCountdown.setVisible(true);
            String secString = "";
            milli -= TIMER;
            if (milli == 0) {
                sec -= 1;
                milli = 1000;
            }
            if (sec < 10) {
                secString = "0";
            }
            secString += sec;
            String finalSecString = secString;
            Platform.runLater(() -> timerCountdown.setText("00:" + finalSecString));
            if (sec == 0) {
                Context.getInstance().setTimerRunning(false);
            }
        } else {
            sec = 30;
            milli = 1000;
            Platform.runLater(() -> timerCountdown.setText("00:30"));
            if (timerCountdown.isVisible()) timerCountdown.setVisible(false);
        }
    }
}
