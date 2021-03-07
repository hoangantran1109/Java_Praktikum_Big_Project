/**
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus, Hoang An Tran
 * Class Player
 */

package GameLogic;

import BoardTiles.BoardTile;
import DecksAndPiles.*;
import ProgrammingCards.MoveMethods;

import java.io.PrintWriter;
import java.util.HashMap;

import static GameLogic.ActivationPhaseMethods.*;
import static GameLogic.GameSendingMethods.sendMoveMessage;
import static GameLogic.GameSendingMethods.sendRebootMessage;


public class Player extends MoveMethods implements Comparable<Player> {

    int id;
    int figure;
    String name;

    private final Robot robot;
    private int energyCubes;
    private int priority;
    private int checkpointsReached = 0;

    private final transient PrintWriter out;
    private final ProgrammingDeck programmingDeck = new ProgrammingDeck();
    private final HashMap<Integer, Card> programmingCardRegister = new HashMap<>();
    private transient ProgrammingDiscardPile programmingDiscardPile = new ProgrammingDiscardPile();
    private HashMap<Integer, Card> programmingCardHand = new HashMap<>();

    private boolean ready = false;
    private boolean active;
    private boolean allRegistersFilled;
    private boolean isAI;
    private boolean playIt;
    private boolean rebooted;
    private boolean placedRobot = false;

    public Player(String playerName, int robotNumber, PrintWriter out) {
        this.out = out;
        this.name = playerName;
        this.figure = robotNumber;
        switch (robotNumber) {
            case 0 -> this.robot = new Robot("TWONKY", 0, 0, 0);
            case 1 -> this.robot = new Robot("SMASH BOT", 0, 0, 0);
            case 2 -> this.robot = new Robot("HulkX90", 0, 0, 0);
            case 3 -> this.robot = new Robot("ZOOM BOT", 0, 0, 0);
            case 4 -> this.robot = new Robot("HAMMER BOT", 0, 0, 0);
            case 5 -> this.robot = new Robot("SPIN BOT", 0, 0, 0);
            default -> this.robot = new Robot("Dummy", 0, 0, 0);
        }
        this.energyCubes = 5;
        for (int i = 0; i < 5; i++) {
            this.programmingCardRegister.put(i, null);
        }
        for (int i = 0; i < 9; i++) {
            this.programmingCardHand.put(i, null);
        }
    }


    /**
     * reset()
     * resets the playerValues to make it possible to play another game
     */
    public void reset() {
        energyCubes = 5;
        checkpointsReached = 0;
        ready = false;
        active = false;
        allRegistersFilled = false;
        playIt = false;
        rebooted = false;
        robot.setPosition(0, 0);
        robot.setStartY(0);
        robot.setStartY(0);
        robot.setDistanceToAntenna(0);
        robot.setOrientation(Direction.RIGHT);
    }


    /**
     * compareTo()
     *
     * @param otherPlayer compares a player to another player based on their distance to the priorityAntenna
     */
    @Override
    public int compareTo(Player otherPlayer) {
        return Integer.compare(this.getRobot().getDistanceToAntenna(),
                otherPlayer.getRobot().getDistanceToAntenna());
    }

    /**
     * reboot()
     * positions a robot on the rebootToken or his original startPosition, rotates the robot up
     * and makes the player draw two spamCards and sends a Reboot message
     */
    public void reboot() {
        this.rebooted = true;
        int rebootX = 0;
        int rebootY = 0;
        boolean rebootSuccessful = false;
        BoardTile restartPoint;
        Direction restartPointOrientation = Direction.DOWN;


        for (int y = 0; y < RoboRallyGame.getBoard().getRows(); y++) {
            for (int x = 0; x < RoboRallyGame.getBoard().getColumns(); x++) {
                if (RoboRallyGame.getBoard().getFields()[y][x][0].getType().equals("restartPoint")) {
                    rebootX = x;
                    rebootY = y;
                    restartPoint = RoboRallyGame.getBoard().getField(rebootY, rebootX, 0);
                    restartPointOrientation = Direction.stringToDirection(restartPoint.getOrientation());
                }
            }
        }
        if (this.getRobot().getX() < 3) {
            rebootX = this.getRobot().getStartX();
            rebootY = this.getRobot().getStartY();
            restartPointOrientation = Direction.UP;
        }

        synchronized (RoboRallyGame.getPlayersInGame()) {
            while (!rebootSuccessful) {
                rebootSuccessful = true;
                for (Player player : RoboRallyGame.getPlayersInGame()) {
                    if (player.getRobot().getX() == rebootX && player.getRobot().getY() == rebootY && !(player == this)) {
                        if (!getPlayerOnField(rebootX, rebootY).moveOne(restartPointOrientation, true)) {
                            rebootSuccessful = false;
                            double random = Math.random();
                            int randomPlayerIndex = (int) random * RoboRallyGame.getPlayersInGame().size();
                            rebootX = RoboRallyGame.getPlayersInGame().get(randomPlayerIndex).getRobot().getStartX();
                            rebootY = RoboRallyGame.getPlayersInGame().get(randomPlayerIndex).getRobot().getStartY();
                        }
                        break;
                    }
                }
            }
        }

        this.getRobot().setPosition(rebootX, rebootY);
        sendMoveMessage(this.getRobot().getX(), this.getRobot().getY(), this.getId(), true);
        int turnTimes = switch (this.getRobot().getOrientation()) {
            case UP -> 0;
            case RIGHT -> 3;
            case DOWN -> 2;
            case LEFT -> 1;
        };
        for (int i = 0; i < turnTimes; i++) {
            this.getRobot().rotate(this, true, true);
        }
        sendRebootMessage(id);
        drawDamageAndSendDamageMessage(this, RoboRallyGame.getSpamCardPile(), 2);
    }

    /**
     * move()
     *
     * @param direction
     * @param sendMessage moves a robot in a certain direction and sends Move message
     */
    public void move(Direction direction, boolean sendMessage) {
        switch (direction) {
            case UP -> this.robot.decreaseY();
            case DOWN -> this.robot.increaseY();
            case LEFT -> this.robot.decreaseX();
            case RIGHT -> this.robot.increaseX();
        }
        this.getRobot().setDirectionOfLastMovement(direction);
        if (!robotOutOfField() && sendMessage) {
            sendMoveMessage(this.getRobot().getX(), this.getRobot().getY(), this.getId(), true);
        }
        if (!robotOutOfField() && RoboRallyGame.getBoard().getField(this.getRobot().getY(), this.getRobot().getX(), 0).getType().equals("pit")) {
            this.reboot();
        }
    }

    /**
     * @param direction the moving direction
     * @return true, if the robot has been moved successfully, false, if it couldn't be moved
     * moves robot one field, if there's no wall/other obstacle or another robot
     * If there's another robot blocking this field, that robot is moved forward, too.
     */
    public boolean moveOne(Direction direction) {
        return moveOne(direction, false);
    }
    /**
     * moveOne()
     * @param direction the moving direction
     * @param ignoreReboot lets robot move even if it already rebooted (false by default)
     * @return true, if the robot has been moved successfully, false, if it couldn't be moved
     * moves robot one field, if there's no wall/other obstacle or another robot
     * If there's another robot blocking this field, that robot is moved forward, too.
     */
    public boolean moveOne(Direction direction, boolean ignoreReboot) {
        if (rebooted && !ignoreReboot) {
            return true;
        }
        int x_position = this.robot.getX();
        int y_position = this.robot.getY();
        BoardTile thisField = RoboRallyGame.getBoard().getField(y_position, x_position, 0);
        BoardTile nextField = findNextField(direction, x_position, y_position);

        Player blockingPlayer = null;

        boolean successfullyMoved = true;

        if (freeOfObstacles(thisField, nextField, direction)) {
            blockingPlayer = switch (direction) {
                case UP -> getPlayerOnField(x_position, y_position - 1);
                case DOWN -> getPlayerOnField(x_position, y_position + 1);
                case LEFT -> getPlayerOnField(x_position - 1, y_position);
                case RIGHT -> getPlayerOnField(x_position + 1, y_position);
            };
            if (blockingPlayer == null) {
                this.move(direction, true);
            } else if (blockingPlayer.getRebooted()){
               successfullyMoved = blockingPlayer.moveOne(direction, true);
               if (successfullyMoved) {this.move(direction, true);}
            } else if (blockingPlayer.moveOne(direction)) {
                this.move(direction, true);
            } else {
                successfullyMoved = false;
            }
        } else {
            successfullyMoved = false;
        }

        if (robotOutOfField()) {
            this.reboot();
        }
        return successfullyMoved;
    }

    /**
     * robotOutOfField()
     *
     * @return true, if the robot has been moved outside the gameboard, false if not
     */
    private boolean robotOutOfField() {
        return (this.getRobot().getX() == -1 || this.getRobot().getY() == -1 ||
                this.getRobot().getX() == RoboRallyGame.getBoard().getColumns() ||
                this.getRobot().getY() == RoboRallyGame.getBoard().getRows());
    }


    public Deck getProgrammingDiscardPile() {
        return programmingDiscardPile;
    }

    public void setProgrammingDiscardPile(ProgrammingDiscardPile programmingDiscardPile) {
        this.programmingDiscardPile = programmingDiscardPile;
    }


    public HashMap<Integer, Card> getProgrammingCardHand() {
        return programmingCardHand;
    }

    public void setProgrammingCardHand(HashMap<Integer, Card> programmingCardHand) {
        this.programmingCardHand = programmingCardHand;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }

    public boolean isPlayIt() {
        return playIt;
    }

    public void setPlayIt(boolean playIt) {
        this.playIt = playIt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public Robot getRobot() {
        return robot;
    }

    public ProgrammingDeck getProgrammingDeck() {
        return programmingDeck;
    }

    public HashMap<Integer, Card> getProgrammingCardRegister() {
        return programmingCardRegister;
    }

    public void setCheckpointsReached(int checkpointsReached) {
        this.checkpointsReached = checkpointsReached;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public PrintWriter getOut() {
        return out;
    }

    public int getCheckpointsReached() {
        return checkpointsReached;
    }

    public void resetCheckpointsReached() {
        this.checkpointsReached = 0;
    }

    public void increaseCheckpointReached() {
        this.checkpointsReached++;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getEnergyCubes() {
        return energyCubes;
    }

    public void increaseEnergyCubes(int energyCubes) {
        this.energyCubes += energyCubes;
    }

    public boolean isAllRegistersFilled() {
        return allRegistersFilled;
    }

    public void setAllRegistersFilled(boolean allRegistersFilled) {
        this.allRegistersFilled = allRegistersFilled;
    }

    public void setRebooted(boolean rebooted) {
        this.rebooted = rebooted;
    }

    public boolean getRebooted() {
        return rebooted;
    }

    public boolean isPlacedRobot() {
        return placedRobot;
    }

    public void setPlacedRobot(boolean placedRobot) {
        this.placedRobot = placedRobot;
    }
}
