package ClientServer;

import GameLogic.Player;
import MessageObjects.Error;
import MessageObjects.Message;
import View.ChatGuiContext;
import View.GameBoardBuilder;
import View.Main;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

/**
 *
 * Class Client
 * creates a client for client-server based chat
 */
public class Client {

    public Socket socket;
    public PrintWriter out;
    public Scanner in;
    Message statusMessage;
    public boolean connected = false, startPositionFix = false, gameWindowOpen = false;
    public BooleanProperty firstReadyPlayer = new SimpleBooleanProperty(false);
    public Stage gameStage;
    public Stage chatStage;
    public List<Player> playerList;
    private final Map<Integer, Player> playerHashMap = new LinkedHashMap<>();
    private static Scene scene;
    List<Integer> takenStartingPositions;
    private Player player;
    private static final Client client = new Client();
    private final Logger logger = LogManager.getLogger(Client.class);
    public Image roboIcon;

    /**
     * Instantiates a new client.
     */
    private Client() {
        try {
            this.socket = new Socket(Main.host, Main.port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new Scanner(socket.getInputStream());
            this.connected = true;
            this.playerList = new ArrayList<>();
            roboIcon = new Image(Client.class.getResourceAsStream("/resources/Icons/lockereLilienRobot.png"));
            ChatGuiContext.getInstance().initialize(this);
            logger.info("Starting registration process...");
        } catch (IOException e) {
            this.statusMessage = new Error("[Chat]: Could not connect to Server, exiting program..");
        }
        this.takenStartingPositions = new ArrayList<>();
    }

    /**
     * getPlayerFromID(ID)
     * gets player whose Id equals the input id
     *
     * @param ID player id
     * @return player
     */
    public Player getPlayerFromID(int ID) {
        return playerList.stream().filter(p -> p.getId() == ID).findFirst().orElse(null);
    }

    /**
     * setPlayerStatus(player, status)
     * sets the status of a player from NotReady to Ready to play
     *
     * @param playerID player who is ready
     * @param status   boolean if player is ready
     */
    public void setPlayerStatus(int playerID, boolean status) {
        if (this.player.getId() == playerID && firstReadyPlayer.get()) {
            firstReadyPlayer.set(status);
        }
        playerHashMap.get(playerID).setReady(status);
        syncMapWithList();
    }

    private void syncMapWithList() {
        playerList.forEach(p -> {
            int index = p.getId();
            p.setReady(playerHashMap.get(index).isReady());
        });
    }

    /**
     * getInstance()
     *
     * @return instance of client
     */
    public static Client getInstance() {
        return client;
    }

    /**
     * openGameWindow()
     * opens the game window, when all player are ready
     */
    public void openGameWindow() {
        if (!gameWindowOpen) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/FXML/gameGui.fxml"));
            try {
                scene = new Scene(fxmlLoader.load(), 985, 810);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameStage = new Stage();
            gameStage.setTitle("Robo Rally Game");
            gameStage.setScene(scene);
            gameStage.getIcons().add(roboIcon);
            gameStage.show();
            gameWindowOpen = true;
            gameStage.setOnCloseRequest(t -> {
                Platform.exit();
                System.exit(-1);
                this.closeEverything();
            });
        }
    }

    /**
     * openChatWindow()
     * opens the chat window when player has logged in
     */
    public void openChatWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/FXML/chatGui.fxml"));
        try {
            scene = new Scene(fxmlLoader.load(), 700, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }

        chatStage = new Stage();
        chatStage.setTitle("Robo Rally Chat");
        chatStage.getIcons().add(roboIcon);
        chatStage.setScene(scene);
        chatStage.show();
        chatStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(-1);
            this.closeEverything();
        });
    }

    /**
     * addState()
     * adds a starting position of a player to the list of taken positions
     *
     * @param position position to be set to startingPosition
     * @param playerID Id of player
     */
    public void addState(int position, int playerID) {
        takenStartingPositions.add(position);
        if (playerID == this.player.getId()) {
            this.startPositionFix = true;
            Platform.runLater(() -> GameBoardBuilder.getInstance().greenScreen.set(false));
        }
    }

    /**
     * addPlayerToList()
     * adds a player to the playerList and in the playerHashMap
     *
     * @param player player to be added
     */
    public void addPlayerToList(Player player) {
        if (!playerList.contains(player) && !playerHashMap.containsKey(player.getId())) {
            playerList.add(player);
            playerHashMap.put(player.getId(), player);
        }
    }

    /**
     * removePlayer()
     * removes a player from the playerList and from the playerHashMap
     *
     * @param playerID
     */
    public void removePlayer(int playerID) {
        playerHashMap.remove(playerID);
        playerList.removeIf(p -> p.getId() == playerID);
    }

    public Message getStatus() {
        return this.statusMessage;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        addPlayerToList(player);
    }

    public List<Integer> getTakenStartingPositions() {
        return takenStartingPositions;
    }

    public void closeEverything() {
        Platform.exit();
        try {
            client.gameStage.close();
            client.chatStage.close();
            client.in.close();
            client.out.close();
            client.socket.close();
        } catch (Exception ignore) {
        }
        System.exit(0);
    }
}
