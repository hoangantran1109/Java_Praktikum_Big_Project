/**
 * @author whole team but mainly Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class RoboRallyGame
 * contains the logic of the Robo Rally game
 */

package GameLogic;


import BoardTiles.Board;
import ClientServer.Server;
import DecksAndPiles.*;
import MessageObjects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static GameLogic.ActivationPhaseMethods.*;
import static GameLogic.GameSendingMethods.*;

public class RoboRallyGame extends Server implements Runnable {

    private static final List<Player> playersInGame = getPlayerList();
    private final List<Integer> neglectPlayers = new ArrayList<>();
    public Player currentPlayer;
    private static Board board;
    private static int register;
    private boolean inStartingPhase = true;
    private boolean inUpgradePhase;
    private boolean inProgrammingPhase;
    private boolean inActivationPhase;
    private static Deck spamCardPile = new SPAMCardPile();
    private static final Deck trojanHorseCardPile = new TrojanHorseCardPile();
    private static final Deck virusCardPile = new VirusCardPile();
    private static final Deck wormCardPile = new WormCardPile();
    public static boolean running;
    public static boolean gameWon;
    private boolean timerStarted = false;
    public boolean isBusy;
    private final int MESSAGE_DELAY = 500;
    public static final boolean CHEATS_ALLOWED = true;
    private static final Logger logger = LogManager.getLogger(RoboRallyGame.class);
    public RoboRallyGame(Board board) {
        RoboRallyGame.board = board;
    }

    /**
     * run()
     * start an existing game and loops through the phases until a player won the game
     */
    public void run() {
        initializeGameSendingMethods(MESSAGE_DELAY);
        running = true;
        if (inStartingPhase) {
            try {
                startStartingPhase();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!isThereAWinner()) {
                if (inUpgradePhase) {
                    startUpgradePhase();
                }
                if (inProgrammingPhase) {
                    startProgrammingPhase();
                }
                if (inActivationPhase) {
                    startActivationPhase();
                }
                if (isThereAWinner()) {
                    break;
                }
            }
            Message gameWonMessage = new GameWon(getWinnerId());
            gameWon = true;
            sendToAll(gson.toJson(gameWonMessage));
            running = false;
            for (Player player : playersInGame) {
                player.reset();
            }
        }
    }


    /**
     * startStartingPhase()
     * contains the logic of the starting phase and waits until all players set their robot on a start position
     */
    public synchronized void startStartingPhase() throws InterruptedException {
        /*
         * game setup
         */
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                p.resetCheckpointsReached();
                p.getProgrammingDeck().populate();
            }
        }

        Message startingPhaseMessage = new ActivePhase(0);
        sendToAll(gson.toJson(startingPhaseMessage));
        /*
         * wait until all players have chosen their starting points
         */
        synchronized (playersInGame) {
            for (Player currentPlayer : playersInGame) {
                if (!neglectPlayers.contains(currentPlayer.getId())) {
                    currentPlayer.setActive(true);
                    Message currentPlayerMessage = new CurrentPlayer(currentPlayer.getId());
                    sendToAll(gson.toJson(currentPlayerMessage));
                    logger.info("wait");
                    this.wait();
                    logger.info("wait suspended");
                    currentPlayer.setActive(false);
                }
            }
        }
        inStartingPhase = false;
        inUpgradePhase = true;
    }

    /**
     * startUpgradePhase()
     * method that would have been fully implemented if there had been upgrade cards
     */
    public void startUpgradePhase() {
        Message upgradePhaseMessage = new ActivePhase(1);
        sendToAll(gson.toJson(upgradePhaseMessage));

        inUpgradePhase = false;
        inProgrammingPhase = true;
    }

    /**
     * startProgrammingPhase()
     * implements the logic of the programming phase
     * all players get 9 programming cards from their programmingCardDeck and assign or get assigned
     * 5 of those 9 cards to their registers
     */
    private synchronized void startProgrammingPhase() {
        Message programmingPhaseMessage = new ActivePhase(2);
        sendToAll(gson.toJson(programmingPhaseMessage));
        /*
         * draw 9 cards from each players programmingDeck and send them to the according player
         * if programming deck is empty, add cards from discardPile
         */
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                String[] cardsToSend = new String[9];
                for (int i = 0; i < 9; i++) {
                    if (p.getProgrammingDeck().getSize() == 0) {
                        logger.info("refill programmingdeck from discard pile");
                        p.getProgrammingDiscardPile().shuffle();
                        sendShuffleCodingMessage(p);
                        int discardPileSize = p.getProgrammingDiscardPile().getSize();
                        for (int j = 0; j < discardPileSize; j++) {
                            p.getProgrammingDeck().addCard(p.getProgrammingDiscardPile().drawCard(), 1);
                        }
                    }
                    p.getProgrammingCardHand().put(i, p.getProgrammingDeck().drawCard());
                    cardsToSend[i] = p.getProgrammingCardHand().get(i).getCardName();
                }
                int cardsInPile = p.getProgrammingDeck().getSize();
                Message yourCardsMessage = new YourCards(cardsToSend, cardsInPile);
                sendToSinglePlayer(p, gson.toJson(yourCardsMessage));
                Message notYourCardsMessage = new NotYourCards(p.getId(), 9, cardsInPile);
                sendToAll(gson.toJson(notYourCardsMessage));
                logger.info("new handcards sent");
            }
        }


        /*
         * Reset timerStarted
         */
        this.setTimerStarted(false);
        /*
         * Wait until Timer does notifyAll()
         * All players program their robots at the same time
         */
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            timerStarted = true;
            logger.info("Timer started.");
            int timeInSeconds = 0;
            while (timerStarted && timeInSeconds <= 30) {
                TimeUnit.SECONDS.sleep(1);
                timeInSeconds++;
            }
            this.setTimerStarted(true);

        } catch (InterruptedException e) {
            timerStarted = false;
            e.printStackTrace();
        }
        logger.info("Timer done.");
        /*
         * Find players who didn't fill their registers
         */
        List<Integer> unreadyPlayerIds = new ArrayList<>();
        List<Player> unreadyPlayers = new ArrayList<>();
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                if (p.getProgrammingCardRegister().size() < 5 || p.getProgrammingCardRegister().containsValue(null)) {
                    unreadyPlayerIds.add(p.getId());
                    unreadyPlayers.add(p);
                }
            }
        }
        int[] unreadyPlayerIdsArray = new int[unreadyPlayerIds.size()];
        for (int i = 0; i < unreadyPlayerIds.size(); i++) {
            unreadyPlayerIdsArray[i] = unreadyPlayerIds.get(i);
        }
        Message timerEndedMessage = new TimerEnded(unreadyPlayerIdsArray);
        sendToAll(gson.toJson(timerEndedMessage));

        /*
         * fill unready player registers randomly
         */
        for (Player p : unreadyPlayers) {
            Message discardHandMessage = new DiscardHand(p.getId());
            sendToAll(gson.toJson(discardHandMessage));
            for (int i = 0; i < 5; i++) {
                if (p.getProgrammingCardRegister().get(i) == null) {

                    if (p.getProgrammingDeck().getSize() == 0) {
                        p.getProgrammingDiscardPile().shuffle();
                        sendShuffleCodingMessage(p);
                        int discardPileSize = p.getProgrammingDiscardPile().getSize();
                        for (int j = 0; j < discardPileSize; j++) {
                            p.getProgrammingDeck().addCard(p.getProgrammingDiscardPile().drawCard(), 1);
                        }
                    }
                    p.getProgrammingCardRegister().put(i, p.getProgrammingDeck().drawCard());
                }
            }
            logger.info("empty registers filled successfully");
            String[] cardsToSend = new String[5];
            for (int i = 0; i < 5; i++) {
                cardsToSend[i] = p.getProgrammingCardRegister().get(i).getCardName();
            }
            Message cardsYouGotNowMessage = new CardsYouGotNow(cardsToSend);
            sendToSinglePlayer(p, gson.toJson(cardsYouGotNowMessage));
        }
        /*
         * add the unused cards to the programmingDiscardPile
         */
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                for (Card handCard : p.getProgrammingCardHand().values()) {
                    if (handCard != null) {
                        p.getProgrammingDiscardPile().addCard(handCard, 1);
                    }
                }
                p.getProgrammingCardHand().clear();
            }
        }

        inProgrammingPhase = false;
        inActivationPhase = true;
    }

    /**
     * startProgrammingPhase()
     * implements the logic of the programming phase
     * all players get 9 programming cards from their programmingCardDeck and assign or get assigned
     * 5 of those 9 cards to their registers
     */
    private synchronized void startActivationPhase() {
        Message activationPhaseMessage = new ActivePhase(3);
        sendToAll(gson.toJson(activationPhaseMessage));
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                p.setRebooted(false);
                p.setActive(true);
            }
        }
        /*
         * Loop through all registers and move Robots accordingly
         */
        for (register = 0; register < 5; register++) {
            sendCurrentCards(register);
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
             * Loop through one register, move robots and activate board elements
             */
            List<Player> prioritizedPlayers = prioritizePlayers(playersInGame);

            for (Player player : prioritizedPlayers) {
                if (!player.getRebooted()) {
                    player.getProgrammingCardRegister().get(register).effect(register, player);
                }
            }

            isBusy = true;
            // Blue conveyor belts
            conveyorBeltsActivate(2);
            logger.info("blueBelts activated");
            // Green conveyor belts
            conveyorBeltsActivate(1);
            logger.info("greenBelts activated");
            //pushPanelsActivate(); // zurueckgestellt (nicht in den maps)
            gearsActivate();
            logger.info("gears");
            try {
                boardLasersActivate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("boardlasers");
            try {
                robotLasersActivate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("robotlasers");
            energySpacesActivate(register);
            logger.info("energyspaces");
            checkPointsActivate();
            logger.info("checkpoints");
            isBusy = false;
            if (isThereAWinner()) {
                return;
            }
        }


        /*
         * Reset programmingCard registers
         */
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                for (int i = 0; i < 5; i++) {
                    if (p.getProgrammingCardRegister().get(i) != null) {
                        p.getProgrammingDiscardPile().addCard(p.getProgrammingCardRegister().get(i), 1);
                    }
                    p.getProgrammingCardRegister().put(i, null);
                }
                p.setAllRegistersFilled(false);
            }
            inActivationPhase = false;
            inUpgradePhase = true;
        }
    }

    /**
     * isThereAWinner()
     *
     * @return true if one of the players in the game has reached all checkpoints and won
     */
    private boolean isThereAWinner() {
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                if (p.getCheckpointsReached() == board.getCheckpoints()) {
                    logger.info("We've got a winner!");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * getWinnerId()
     *
     * @return the ID of the player who won the game
     */
    private int getWinnerId() {
        synchronized (playersInGame) {
            for (Player p : playersInGame) {
                if (p.getCheckpointsReached() == board.getCheckpoints()) {
                    return p.getId();
                }
            }
        }
        return -1;
    }

    /**
     * prioritizePlayers()
     *
     * @param PlayersInGame
     * @return prioritizedPlayers
     * This function sorts the players according to their relative position and distance to the antenna
     */
    public static List<Player> prioritizePlayers(List<Player> PlayersInGame) {
        synchronized (playersInGame) {
            for (Player player : PlayersInGame) {
                player.getRobot().setDistanceToAntenna(calculateDistanceToAntenna(player));
            }
        }
        List<Player> prioritizedPlayers = new ArrayList<>(PlayersInGame);
        Collections.sort(prioritizedPlayers);
        for (int i = 0; i < prioritizedPlayers.size() - 1; i++) {
            for (int j = i + 1; j < prioritizedPlayers.size(); j++) {
                if (prioritizedPlayers.get(i).getRobot().getDistanceToAntenna() ==
                        prioritizedPlayers.get(j).getRobot().getDistanceToAntenna()) {
                    if (secondPlayerHasPriority(prioritizedPlayers.get(i), prioritizedPlayers.get(j))) {
                        Collections.swap(prioritizedPlayers, i, j);
                    }
                }
            }
        }
        return prioritizedPlayers;
    }

    /**
     * calculateDistanceToAntenna()
     *
     * @param player
     * @return distance of a player's robot to the priority antenna
     */
    public static int calculateDistanceToAntenna(Player player) {
        int yDifference = Math.abs(4 - player.getRobot().getY());
        int xDifference = player.getRobot().getX();
        return yDifference + xDifference;
    }

    /**
     * calculateDistanceToAntenna()
     *
     * @param firstPlayer
     * @param secondPlayer
     * @return if two players' robots have the same distance to the antenna and the second player should
     * move his robot first according to the priority rules
     */
    public static boolean secondPlayerHasPriority(Player firstPlayer, Player secondPlayer) {
        if (firstPlayer.getRobot().getY() < 4 && secondPlayer.getRobot().getY() < 4 ||
                firstPlayer.getRobot().getY() >= 4 && secondPlayer.getRobot().getY() >= 4) {
            return secondPlayer.getRobot().getY() < firstPlayer.getRobot().getY();
        } else return firstPlayer.getRobot().getY() < 4 && secondPlayer.getRobot().getY() >= 4;
    }


    /**
     * notifyThread()
     * wakes up all waiting threads
     */
    public synchronized void notifyThread() {
        notifyAll();
        logger.info("notify all");
    }

    /**
     * waitThread()
     * makes the current thread wait
     */
    public synchronized void waitThread() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("wait");
    }

    public static Deck getSpamCardPile() {
        return spamCardPile;
    }

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board board) {
        RoboRallyGame.board = board;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isRunning() {
        return running;
    }

    public static Deck getTrojanHorseCardPile() {
        return trojanHorseCardPile;
    }

    public static Deck getVirusCardPile() {
        return virusCardPile;
    }

    public static Deck getWormCardPile() {
        return wormCardPile;
    }

    public boolean isInStartingPhase() {
        return inStartingPhase;
    }

    public boolean isInProgrammingPhase() {
        return inProgrammingPhase;
    }

    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void setTimerStarted(boolean timerStarted) {
        this.timerStarted = timerStarted;
    }

    public static List<Player> getPlayersInGame() {
        return playersInGame;
    }

    public static int getRegister() {
        return register;
    }

    public void addNeglectedPlayer(int id) {
        this.neglectPlayers.add(id);
    }
}
