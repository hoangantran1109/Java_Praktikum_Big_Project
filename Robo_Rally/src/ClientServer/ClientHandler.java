package ClientServer;

import BoardTiles.*;
import DecksAndPiles.Card;
import DecksAndPiles.Deck;
import GameLogic.GameSendingMethods;
import GameLogic.Player;
import GameLogic.RoboRallyGame;
import MessageObjects.Error;
import MessageObjects.Map;
import MessageObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class ClientHandler
 * provides the client's dialogue and RoboRallyGame - interactions
 * provides the client's dialogue and RoboRallyGame - interactions
 *
 * @author the whole team
 */
public class ClientHandler extends Server implements Runnable {

    private final Logger logger = LogManager.getLogger(ClientHandler.class);
    private static final String PROTOCOL = "1.0";
    private String group;
    private final String STANDARD_ERROR_MESSAGE = "Ups! That did not work. Try to adjust something.";
    boolean isAI;
    private String name = "missingName";
    private int figure = -1;
    private final Socket SOCKET;
    public Scanner in;
    public PrintWriter out;
    public List<Player> playerList;
    public List<PrintWriter> chatUsers;
    private final List<Integer> userIds;
    private final int currentId;
    private int userId;
    private static RoboRallyGame roboRallyGame;
    private static Thread gameThread;
    private Player player;
    private static Board board;
    private int cardPositionInProgrammingCardHand = 0;
    Gson gson;

    /**
     * ClientHandler()
     *
     * @param s         socket
     * @param chatUsers constructor for the ClientHandler
     */
    public ClientHandler(Socket s, List<Player> playerList, List<PrintWriter> chatUsers, List<Integer> userIds, int currentId) {
        SOCKET = s;
        this.currentId = currentId;
        this.chatUsers = chatUsers;
        this.userIds = userIds;
        this.playerList = playerList;
    }

    /**
     * run()
     * implements the run method of the Runnable interface
     */
    public void run() {


        Boolean connectionSuccess = false;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageAdapter());
        gson = gsonBuilder.create();

        Message error = new Error(STANDARD_ERROR_MESSAGE);
        String errorJson = gson.toJson(error);
        try {
            /*
             * instantiate Scanner and PrintWriter (autoFlush) for in and outgoing messages
             */
            in = new Scanner(SOCKET.getInputStream());
            out = new PrintWriter(SOCKET.getOutputStream(), true);
            player = new Player("John Doe", -1, out);

            /*
             * send HelloClient message to client
             */
            Message helloClient = new HelloClient(PROTOCOL);

            String helloClientString = gson.toJson(helloClient);
            out.println(helloClientString);

            /*
             * check protocol and disconnect client if protocol doesn't match
             * check if playerName is already taken
             */
            if (in.hasNext()) {
                String jsonHelloServer = in.nextLine();
                Message helloServer = gson.fromJson(jsonHelloServer, Message.class);
                if (!helloServer.getMessageBody().getProtocol().equals(PROTOCOL)) {
                    Message protocolError = new Error("Not compatible!");
                    String jsonError = gson.toJson(protocolError);
                    out.println(jsonError);
                    try {
                        SOCKET.close();
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                } else if (roboRallyGame != null && gameThread.isAlive()) {
                    Message gameAlreadyStartedError = new Error("Game is already started!");
                    String jsonError = gson.toJson(gameAlreadyStartedError);
                    out.println(jsonError);
                    try {
                        SOCKET.close();
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    name = helloServer.getMessageBody().getName();
                    group = helloServer.getMessageBody().getGroup();
                    isAI = helloServer.getMessageBody().isAI();
                    player.setAI(isAI);
                    connectionSuccess = true;
                }
            }

            /*
             * allocate userId and send "Welcome" message
             * send list of players
             */

            if (connectionSuccess) {
                Message welcome = new Welcome(currentId);
                String jsonWelcome = gson.toJson(welcome);
                out.println(jsonWelcome);
            }

            /*
             * endless loop for receiving client messageObjects
             */
            while (connectionSuccess) {
                if (in.hasNext()) {
                    String clientInput = in.nextLine();
                    logger.info(clientInput);
                    Message clientMessageObject = gson.fromJson(clientInput, Message.class);
                    String messageType = clientMessageObject.getMessageType();
                    switch (messageType) {
                        case MsgNames.playerValues:
                            sendPlayerList(false);
                            if (clientMessageObject.getMessageBody().getName() != null) {
                                String nameCandidate = clientMessageObject.getMessageBody().getName();
                                int robotCandidate = clientMessageObject.getMessageBody().getFigure();
                                boolean figureIsFree = playerList.stream().noneMatch(o -> o.getFigure() == (robotCandidate));
                                if (playerList != null && playerList.isEmpty() || (figureIsFree)) {
                                    name = nameCandidate;
                                    figure = robotCandidate;
                                    chatUsers.add(out);
                                    player = new Player(name, robotCandidate, out);
                                    player.getRobot().setName(robotCandidate);
                                    player.setId(currentId);
                                    userId = currentId;
                                    userIds.add(userId);
                                    playerList.add(player);
                                    sendPlayerList(true);
                                    for (Message message : ClientHandlerContext.getInstance().getAllMessages()) {
                                        out.println(gson.toJson(message));
                                    }
                                } else {
                                    String errorMessage = "Your ";
                                    errorMessage += "figure ";
                                    Message nameAlreadyTakenError = new Error(errorMessage + "is already taken. Try another one.");
                                    String nameAlreadyTakenErrorJson = gson.toJson(nameAlreadyTakenError);
                                    out.println(nameAlreadyTakenErrorJson);
                                }
                            }
                            break;
                        case MsgNames.setStatus:
                            player.setReady(clientMessageObject.getMessageBody().isReady());
                            if (getOnlyReadyPlayer() != null) {
                                Message selectMapMessage = new SelectMap(new String[]{"DizzyHighway", "ExtraCrispy"});
                                String selectMapMessageToSend = gson.toJson(selectMapMessage);
                                getOnlyReadyPlayer().getOut().println(selectMapMessageToSend);
                            }
                            if (checkIfAllReady() && playerList.size() >= 2) {
                                if (board == null) {
                                    board = new DizzyHighway();
                                }
                                Message gameBoardMessage = createGameBoardMessage(board);
                                String gameBoardMessageToSend = gson.toJson(gameBoardMessage);
                                sendToAll(gameBoardMessageToSend);
                                roboRallyGame = new RoboRallyGame(board);
                                gameThread = new Thread(roboRallyGame, board.getBoardType());
                                gameThread.start();
                            }
                            sendStatusToPlayers();
                            break;
                        case MsgNames.mapSelected:
                            String[] selectedMap = clientMessageObject.getMessageBody().getSelectedMap();
                            if (selectedMap[0].equals("ExtraCrispy")) {
                                board = new ExtraCrispy();
                            } else {
                                board = new DizzyHighway();
                            }
                            break;
                        case MsgNames.sendChat:
                            String message = clientMessageObject.getMessageBody().getMessage();
                            /*
                             * Cheat won't be executed if ActivationPhaseMethods are active, so the game is busy at the
                             * moment
                             */
                            if (RoboRallyGame.running && !roboRallyGame.isBusy &&
                                !roboRallyGame.isInStartingPhase() && message.contains("$$")) {
                                if (message.equals("$$notify")) {
                                    roboRallyGame.notifyThread();
                                } else {
                                    GameLogic.ActivationPhaseMethods.cheats(player, message);
                                    logger.info("Cheat: Player: " + name + " " + message);
                                }
                                break;
                            }
                            int privateMessageTo = clientMessageObject.getMessageBody().getTo();
                            if (!userIds.contains(privateMessageTo) && privateMessageTo != -1) {
                                out.println(errorJson);
                            } else {
                                sendMessage(privateMessageTo, message);
                            }
                            break;
                        case MsgNames.playIt:
                            if (roboRallyGame != null) {
                                player.setPlayIt(true);
                                player.setActive(false);
                                boolean allSetPlayIt = true;
                                logger.info("got a PlayIt Message");
                                for (Player p : playerList) {
                                    if (!p.isPlayIt()) {
                                        allSetPlayIt = false;
                                    }
                                }
                                if (allSetPlayIt) {
                                    for (Player p : playerList) {
                                        p.setPlayIt(false);
                                    }
                                    roboRallyGame.notifyThread();
                                }
                            }
                            break;

                        // This case would be necessary to play upgradeCards
                        case MsgNames.playCard:
                            // activate upgradeCard in RoboRallyGame
                            String cardPlayed = clientMessageObject.getMessageBody().getCard();
                            Message cardPlayedMessage = new CardPlayed(userId, cardPlayed);
                            sendToAll(gson.toJson(cardPlayedMessage));
                            break;
                        case MsgNames.selectCard:
                            if (roboRallyGame != null && roboRallyGame.isInProgrammingPhase()) {

                                int register = clientMessageObject.getMessageBody().getRegister() - 1;
                                String card = clientMessageObject.getMessageBody().getCard();

                                if (clientMessageObject.getMessageBody().getCard() != null) {
                                    for (int i = 0; i < player.getProgrammingCardHand().size(); i++) {
                                        Card currentCard = player.getProgrammingCardHand().get(i);
                                        if (currentCard != null && currentCard.getCardName().equals(card)) {
                                            player.getProgrammingCardRegister().put(register, currentCard);
                                            cardPositionInProgrammingCardHand = i;
                                        }
                                    }

                                    player.getProgrammingCardHand().put(cardPositionInProgrammingCardHand, null);
                                    Message cardSelectedMessage = new CardSelected(player.getId(), register + 1);
                                    sendToAll(gson.toJson(cardSelectedMessage));

                                    if (!player.getProgrammingCardRegister().containsValue(null)) {
                                        player.setAllRegistersFilled(true);
                                        Message selectionFinished = new SelectionFinished(player.getId());
                                        sendToAll(gson.toJson(selectionFinished));
                                        logger.info(gson.toJson(selectionFinished));
                                        boolean allPlayersFilledRegisters = true;
                                        if (!roboRallyGame.isTimerStarted()) {
                                            roboRallyGame.notifyThread();
                                            Message timerStarted = new TimerStarted();
                                            sendToAll(gson.toJson(timerStarted));
                                        }
                                        for (Player player : playerList) {
                                            if (!player.isAllRegistersFilled()) {
                                                allPlayersFilledRegisters = false;
                                                break;
                                            }
                                        }
                                        roboRallyGame.setTimerStarted(!allPlayersFilledRegisters);
                                    }
                                } else {
                                    Card temp = player.getProgrammingCardRegister().get(register);
                                    if (temp != null) {
                                        for (int i = 0; i < player.getProgrammingCardHand().size(); i++) {
                                            if (player.getProgrammingCardHand().get(i) == null) {
                                                player.getProgrammingCardHand().put(i, temp);
                                                break;
                                            }
                                        }
                                    }
                                    player.getProgrammingCardRegister().put(register, null);
                                    Message cardSelectedMessage = new CardSelected(player.getId(), register + 1);
                                    sendToAll(gson.toJson(cardSelectedMessage));
                                }
                            }
                            break;
                        case MsgNames.selectDamage:
                            if (roboRallyGame != null) {
                                String[] cards = clientMessageObject.getMessageBody().getCards();
                                int cardsDrawn = 0;

                                for (String cardName : cards) {
                                    Deck deck = switch (cardName) {
                                        case "Worm" -> RoboRallyGame.getWormCardPile();
                                        case "Trojan Horse" -> RoboRallyGame.getTrojanHorseCardPile();
                                        case "Virus" -> RoboRallyGame.getVirusCardPile();
                                        case "SPAM" -> RoboRallyGame.getSpamCardPile();
                                        default -> null;
                                    };
                                    if (deck == null) throw new AssertionError();
                                    if (!deck.isEmpty()) {
                                        player.getProgrammingDiscardPile().addCard(deck.drawCard(), 1);
                                        GameSendingMethods.sendDrawDamageMessage(player, cardName, 1);
                                        cardsDrawn++;
                                    }
                                }
                                if (cards.length > cardsDrawn) {
                                    logger.info("sending the second pickDamage message");
                                    GameSendingMethods.sendPickDamageMessage(player, cards.length - cardsDrawn);
                                } else {
                                    logger.info("game should continue");
                                    roboRallyGame.notifyThread();
                                }
                            }
                            break;
                        case MsgNames.setStartingPoint:
                            if (roboRallyGame != null && roboRallyGame.isInStartingPhase()) {
                                int position = clientMessageObject.getMessageBody().getPosition();
                                List<Integer> startPositions = Arrays.asList(14, 39, 53, 66, 78, 105);
                                if (startPositions.contains(position) && player.isActive()) {
                                    switch (position) {
                                        case 14 -> {
                                            player.getRobot().setX(1);
                                            player.getRobot().setY(1);
                                            player.getRobot().setStartX(1);
                                            player.getRobot().setStartY(1);
                                        }
                                        case 39 -> {
                                            player.getRobot().setX(0);
                                            player.getRobot().setY(3);
                                            player.getRobot().setStartX(0);
                                            player.getRobot().setStartY(3);
                                        }
                                        case 53 -> {
                                            player.getRobot().setX(1);
                                            player.getRobot().setY(4);
                                            player.getRobot().setStartX(1);
                                            player.getRobot().setStartY(4);
                                        }
                                        case 66 -> {
                                            player.getRobot().setX(1);
                                            player.getRobot().setY(5);
                                            player.getRobot().setStartX(1);
                                            player.getRobot().setStartY(5);
                                        }
                                        case 78 -> {
                                            player.getRobot().setX(0);
                                            player.getRobot().setY(6);
                                            player.getRobot().setStartX(0);
                                            player.getRobot().setStartY(6);
                                        }
                                        case 105 -> {
                                            player.getRobot().setX(1);
                                            player.getRobot().setY(8);
                                            player.getRobot().setStartX(1);
                                            player.getRobot().setStartY(8);
                                        }
                                    }
                                    Message startingPointTakenMessage = new StartingPointTaken(userId, position);
                                    sendToAll(gson.toJson(startingPointTakenMessage));
                                    player.setPlacedRobot(true);
                                    roboRallyGame.notifyThread();
                                    break;
                                }
                            }
                    }
                }

                /*
                 * Removing the username when a client is suddenly canceled
                 */
                if (!in.hasNext()) {
                    connectionSuccess = false;
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         * if chatUser input is "bye" remove the user from the chat and inform the other users
         */
        finally {
            if (out != null) {
                chatUsers.remove(out);
            }
            if (name != null) {
                if (roboRallyGame != null && player.isActive()) {
                    roboRallyGame.notifyThread();
                }
                if (!player.isPlacedRobot() && roboRallyGame!=null) {
                    roboRallyGame.addNeglectedPlayer(userId);
                }
                Message connectionUpdate = new ConnectionUpdate(userId, false, "Remove");
                sendToAll(gson.toJson(connectionUpdate));
                playerList.remove(player);

                if (roboRallyGame == null && checkIfAllReady() && playerList.size() >= 2) {
                    if (board == null) {
                        board = new DizzyHighway();
                    }
                    Message gameBoardMessage = createGameBoardMessage(board);
                    String gameBoardMessageToSend = gson.toJson(gameBoardMessage);
                    sendToAll(gameBoardMessageToSend);
                    roboRallyGame = new RoboRallyGame(board);
                    gameThread = new Thread(roboRallyGame, board.getBoardType());
                    gameThread.start();
                }

                if (playerList.size() < 2 && roboRallyGame != null && !RoboRallyGame.gameWon) {
                    Message gameWonMessage = new GameWon(playerList.get(0).getId());
                    String gameWonMessageToSend = gson.toJson(gameWonMessage);
                    player.setReady(false);
                    roboRallyGame = null;
                    gameThread = null;
                    sendToAll(gameWonMessageToSend);
                }
            }

            try {
                SOCKET.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * sendStatusToPlayers()
     * sends sendPlayer Message to all player
     */
    private void sendStatusToPlayers() {
        if (playerList != null && !playerList.isEmpty()) {
            for (Player player : playerList) {
                Message sendPlayer = new PlayerStatus(player.getId(), player.isReady());
                sendToAll(new Gson().toJson(sendPlayer));
            }
        }
    }


    /**
     * sendPlayerList()
     * sends the PlayerAdded Message
     *
     * @param sendPublic true if message should be sent to all player
     */
    private void sendPlayerList(boolean sendPublic) {
        if (playerList != null && !playerList.isEmpty()) {
            for (Player player : playerList) {
                Message sendPlayer = new PlayerAdded(player);
                if (sendPublic) {
                    sendToAll(new Gson().toJson(sendPlayer));
                } else {
                    out.println(new Gson().toJson(sendPlayer));
                }

            }
        }
    }


    /**
     * sendMessage()
     * sends a message in the chat room or a private message
     * sends the ReceivedChat Message
     *
     * @param id
     * @param message sends a message
     */
    public void sendMessage(int id, String message) {
        if (id == -1) {
            Message messageObject = new ReceivedChat(message, userId, false);
            String messageObjectJson = gson.toJson(messageObject);
            sendToAll(messageObjectJson);

            Message messageObjectLog = new ReceivedChat("sent at " + new SimpleDateFormat("HH:mm").format(new Date()) + ": " + message, this.userId, false);
            ClientHandlerContext.getInstance().addMessage(messageObjectLog);
        } else {
            Message messageObject = new ReceivedChat(message, userId, true);
            String messageObjectJson = gson.toJson(messageObject);
            PrintWriter receiver = chatUsers.get(userIds.indexOf(id));
            receiver.println(messageObjectJson);
        }
    }

    /**
     * sendToAll()
     *
     * @param messageObjectJson sends a message to all Users
     */
    public void sendToAll(String messageObjectJson) {

        for (java.io.PrintWriter chatUser : chatUsers) {
            chatUser.println(messageObjectJson);
        }
        if (!chatUsers.contains(out)) {
            out.println(messageObjectJson);
        }
    }

    /**
     * checkIfAllReady()
     * checks the readiness of all players
     *
     * @return true, if all players are ready to play, false otherwise
     */
    private boolean checkIfAllReady() {
        for (Player player : playerList) {
            if (!player.isReady()) {
                return false;
            }
        }
        return true;
    }

    /**
     * checkIfOnlyReady()
     * checks the readiness of one player
     *
     * @return true, if this.player is the only ready player, false otherwise
     */
    private Player getOnlyReadyPlayer() {
        int readyPlayers = 0;
        Player onlyReadyPlayer = null;
        for (int i = 0; i < RoboRallyGame.getPlayersInGame().size(); i++) {
            if (RoboRallyGame.getPlayersInGame().get(i).isReady()) {
                onlyReadyPlayer = RoboRallyGame.getPlayersInGame().get(i);
                readyPlayers++;
            }
        }
        if (readyPlayers == 1) {
            return onlyReadyPlayer;
        }
        return null;
    }


    /**
     * createGameBoardMessage()
     *
     * @param board board of RoboRally game
     * @return message gameStarted
     */
    public Message createGameBoardMessage(Board board) {

        List<Map> mapsFromDizzyHighway = new ArrayList<>();
        int position = 3;
        /*
        loop through Tiles in Board and add to List of Map
         */
        for (int y = 0; y < board.getFields().length; y++) {
            for (int x = 3; x < board.getFields()[y].length; x++) {
                Map mapFromBoardTiles = new Map();

                List<Field> fields = new ArrayList<>();

                /*
                 * to loop when multiple tiles are above each other
                 */
                for (int z = 0; z < board.getFields()[y][x].length; z++) {
                    Field fieldFromBoardTile = new Field("");
                    if (board.getFields()[y][x][z] == null) {
                        break;
                    } else if (board.getFields()[y][x][z] != null) {
                        fieldFromBoardTile.type = board.getFields()[y][x][z].getClass().getSimpleName();
                        switch (fieldFromBoardTile.type) {
                            case "Belt":
                                Belt belt = (Belt) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientation = belt.getOrientation();
                                fieldFromBoardTile.speed = belt.getSpeed();
                                break;
                            case "RotatingBelt":
                                RotatingBelt rotatingBelt = (RotatingBelt) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientations = rotatingBelt.getOrientations();
                                fieldFromBoardTile.isCrossing = rotatingBelt.isCrossing();
                                fieldFromBoardTile.speed = rotatingBelt.getSpeed();
                                break;
                            case "Wall":
                                Wall wall = (Wall) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientations = wall.getOrientations();
                                break;
                            case "EnergySpace":
                                EnergySpace energySpace = (EnergySpace) board.getFields()[y][x][z];
                                fieldFromBoardTile.count = energySpace.getCount();
                                break;
                            case "StartPoint":
                                StartPoint startPosition = (StartPoint) board.getFields()[y][x][z];
                                fieldFromBoardTile.type = startPosition.getType();
                                break;
                            case "RestartPoint":
                                RestartPoint rebootToken = (RestartPoint) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientation = rebootToken.getOrientation();
                                break;
                            case "CheckPoint":
                                CheckPoint controlPoint = (CheckPoint) board.getFields()[y][x][z];
                                fieldFromBoardTile.count = controlPoint.getCount();
                                break;
                            case "Laser":
                                Laser laser = (Laser) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientation = laser.getOrientation();
                                fieldFromBoardTile.count = laser.getCount();
                                break;
                            case "Antenna":
                                Antenna antenna = (Antenna) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientation = antenna.getOrientation();
                                break;
                            case "Gear":
                                Gear gear = (Gear) board.getFields()[y][x][z];
                                fieldFromBoardTile.orientation = gear.getOrientation();
                                break;
                        }
                    }
                    fields.add(fieldFromBoardTile);
                }
                mapFromBoardTiles.position = position;
                mapFromBoardTiles.field = fields;
                mapsFromDizzyHighway.add(mapFromBoardTiles);
                if ((position + 1) % 13 == 0) {
                    position += 4;
                } else {
                    position++;
                }
            }
        }
        Message message = new GameStarted(mapsFromDizzyHighway);
        return message;
    }

    public static RoboRallyGame getRoboRallyGame() {
        return roboRallyGame;
    }
}

