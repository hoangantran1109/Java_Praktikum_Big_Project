/**
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus
 * Class ActivationPhaseMethods
 */

package GameLogic;

import BoardTiles.Board;
import BoardTiles.BoardTile;
import ClientServer.ClientHandler;
import DecksAndPiles.Deck;
import MessageObjects.CheckpointReached;
import MessageObjects.Energy;
import MessageObjects.Error;
import MessageObjects.Message;
import ProgrammingCards.MoveMethods;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static GameLogic.Direction.stringToDirection;
import static GameLogic.GameSendingMethods.*;


public class ActivationPhaseMethods {
    private static final Board board = RoboRallyGame.getBoard();

    private static final Logger logger = LogManager.getLogger(ActivationPhaseMethods.class);

    /**
     * findNextField()
     *
     * @param orientation direction in which next field should be found
     * @param x           x-coordinate of current field
     * @param y           y-coordinate of current field
     * @return next boardTile
     */
    protected static BoardTile findNextField(Direction orientation, int x, int y) {
        if ((x == 0 && orientation.equals(Direction.LEFT)) || (y == 0 && orientation.equals(Direction.UP))
            || (x == 12 && orientation.equals(Direction.RIGHT)) || (y == 9 && orientation.equals(Direction.DOWN))) {
            return null;
        }
        return switch (orientation) {
            case UP -> board.getField(y - 1, x, 0);
            case DOWN -> board.getField(y + 1, x, 0);
            case LEFT -> board.getField(y, x - 1, 0);
            case RIGHT -> board.getField(y, x + 1, 0);
        };
    }

    /**
     * findNextFieldX()
     *
     * @param orientation direction in which next field should be found
     * @param x           x-coordinate of current field
     * @param y           y-coordinate of current field
     * @return x-coordinate of next field
     */
    protected static int findNextFieldX(Direction orientation, int x, int y) {
        if ((x == 0 && orientation.equals(Direction.LEFT)) || (y == 0 && orientation.equals(Direction.UP))) {
            return -1;
        }
        return switch (orientation) {
            case UP -> x;
            case DOWN -> x;
            case LEFT -> x - 1;
            case RIGHT -> x + 1;
        };
    }

    /**
     * findNextFieldY()
     *
     * @param orientation direction in which next field should be found
     * @param x           x-coordinate of current field
     * @param y           y-coordinate of current field
     * @return y-coordinate of next field
     */
    protected static int findNextFieldY(Direction orientation, int x, int y) {
        if ((x == 0 && orientation.equals(Direction.LEFT)) || (y == 0 && orientation.equals(Direction.UP))) {
            return -1;
        }
        return switch (orientation) {
            case UP -> y - 1;
            case DOWN -> y + 1;
            case LEFT -> y;
            case RIGHT -> y;
        };
    }

    /**
     * getPlayerOnField()
     *
     * @param x x-coordinate of a field
     * @param y y-coordinate of a field
     * @return player whose robot is standing on a certain field
     */
    public static Player getPlayerOnField(int x, int y) {
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player player : RoboRallyGame.getPlayersInGame()) {
                if (player.getRobot().getX() == x && player.getRobot().getY() == y) {
                    return player;
                }
            }
        }
        return null;
    }

    /**
     * conveyorBeltsActivate()
     *
     * @param colour 1 for green conveyors, 2 for blue conveyors
     *               moves robots on conveyors according to the rules of roboRally
     */
    public static void conveyorBeltsActivate(int colour) {
        List<Player> playerOnBeltList = new ArrayList<>();
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player p : RoboRallyGame.getPlayersInGame()) {
                if (board.getField(p.getRobot().getY(), p.getRobot().getX(), 0).getSpeed() == colour) {
                    playerOnBeltList.add(p);
                }
            }
        }
        for (int i = 0; i < colour; i++) {
            boolean playerMoved = false;
            for (Player p : playerOnBeltList) {
                int x = p.getRobot().getX();
                int y = p.getRobot().getY();
                BoardTile currentField = board.getField(y, x, 0);
                Direction beltDirection;
                BoardTile nextField;
                int nextFieldX;
                int nextFieldY;
                if (currentField.getSpeed() == colour) {
                    if (currentField.getType().equals("belt")) {
                        beltDirection = stringToDirection(currentField.getOrientation());
                    } else {
                        beltDirection = stringToDirection(currentField.getOrientations().get(0));
                    }
                    nextField = findNextField(beltDirection, x, y);
                    nextFieldX = findNextFieldX(beltDirection, x, y);
                    nextFieldY = findNextFieldY(beltDirection, x, y);
                    if (nextField == null) throw new AssertionError();
                    if (!(nextField.getType().equals("belt") || nextField.getType().equals("rotatingBelt")) &&
                        getPlayerOnField(nextFieldX, nextFieldY) != null) {
                        continue;
                    } else if (nextField.getType().equals("rotatingBelt") && nextField.isCrossing()) {
                        int opponentX = nextFieldX;
                        int opponentY = nextFieldY;
                        if (beltDirection == stringToDirection(nextField.getOrientations().get(1))) {
                            switch (nextField.getOrientations().get(0)) {
                                case "up" -> opponentY += 1;
                                case "down" -> opponentY -= 1;
                                case "left" -> opponentX += 1;
                                case "right" -> opponentX -= 1;
                            }
                        } else {
                            switch (nextField.getOrientations().get(1)) {
                                case "up" -> opponentY += 1;
                                case "down" -> opponentY -= 1;
                                case "left" -> opponentX += 1;
                                case "right" -> opponentX -= 1;
                            }
                        }
                        boolean dontMove = false;
                        for (Player opponent : playerOnBeltList) {
                            if (opponent.getRobot().getX() == opponentX && opponent.getRobot().getY() == opponentY && opponent != p) {
                                dontMove = true;
                                break;
                            }
                        }
                        if (dontMove) {
                            continue;
                        }
                    }
                    if (beltDirection == null) throw new AssertionError();
                    p.move(beltDirection, false);
                    playerMoved = true;
                    sendMoveMessage(p.getRobot().getX(), p.getRobot().getY(), p.getId(), false);
                    if (nextField.getType().equals("rotatingBelt") &&
                        p.getRobot().getDirectionOfLastMovement() == stringToDirection(nextField.getOrientations().get(1))) {
                        boolean clockwise = calculateRotateDirection(nextField);
                        p.getRobot().rotate(p, clockwise, false);
                        sendPlayerTurningMessage(p, clockwise, false);
                    }
                }
            }
            try {
                if (playerMoved)
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * gearsActivate()
     * rotates robots standing on a gear either clockwise or counterclockwise
     */
    public static void gearsActivate() {
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player player : RoboRallyGame.getPlayersInGame()) {

                int thisFieldX = player.getRobot().getX();
                int thisFieldY = player.getRobot().getY();
                BoardTile thisField = board.getField(thisFieldY, thisFieldX, 0);
                if (thisField.getType().equals("gear")) {
                    player.getRobot().rotate(player, thisField.getOrientation().equals("right"), true);
                }
            }
        }
    }


    /**
     * energySpacesActivate()
     *
     * @param register that is activated
     *                 robots who end a register on an energySpace get an energyCube
     */
    public static void energySpacesActivate(int register) {
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player player : RoboRallyGame.getPlayersInGame()) {
                int playerX = player.getRobot().getX();
                int playerY = player.getRobot().getY();
                BoardTile thisField = board.getField(playerY, playerX, 0);
                if (thisField.getType().equals("energySpace") && thisField.getCount() > 0) {
                    thisField.reduceCount();
                    player.increaseEnergyCubes(1);
                    Message energyMessage = new Energy(player.getId(), 1);
                    sendToAll(gson.toJson(energyMessage));
                } else if (thisField.getType().equals("energySpace") && register == 4) {
                    player.increaseEnergyCubes(1);
                    Message energyMessage = new Energy(player.getId(), 1);
                    sendToAll(gson.toJson(energyMessage));
                }
                thisField = board.getField(playerY, playerX, 1);
                if (thisField != null) {
                    if (thisField.getType().equals("energySpace") && thisField.getCount() > 0) {
                        thisField.reduceCount();
                        player.increaseEnergyCubes(1);
                        Message energyMessage = new Energy(player.getId(), 1);
                        sendToAll(gson.toJson(energyMessage));
                    } else if (thisField.getType().equals("energySpace") && register == 4) {
                        player.increaseEnergyCubes(1);
                        Message energyMessage = new Energy(player.getId(), 1);
                        sendToAll(gson.toJson(energyMessage));
                    }
                }
            }
        }
    }

    /**
     * checkPointsActivate()
     * robots who end a register on a checkPoint have reached this checkPoint and their
     * checkpointsReached attribute is increased
     */
    public static void checkPointsActivate() {
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player player : RoboRallyGame.getPlayersInGame()) {
                int playerX = player.getRobot().getX();
                int playerY = player.getRobot().getY();
                BoardTile thisField = board.getField(playerY, playerX, 0);
                if (board.getBoardType().equals("ExtraCrispy")) {
                    thisField = board.getField(playerY, playerX, 2);
                }
                if (thisField != null && thisField.getType().equals("checkPoint")) {
                    if (player.getCheckpointsReached() == thisField.getCount() - 1) {
                        player.increaseCheckpointReached();
                        Message checkpointMessage = new CheckpointReached(player.getId(), player.getCheckpointsReached());
                        sendToAll(gson.toJson(checkpointMessage));
                    }
                }
            }
        }
    }

    /**
     * boardLasersActivate()
     * robots who end a register on a field that is touched by a laserbeam get a spamCard
     */
    public static void boardLasersActivate() throws InterruptedException {
        for (int x = 0; x < 13; x++) {
            for (int y = 0; y < 10; y++) {
                BoardTile thisField = board.getField(y, x, 1);

                if (thisField != null && thisField.getType().equals("laser")) {
                    Direction laserOrientation = stringToDirection(thisField.getOrientation());
                    int laserCount = thisField.getCount();
                    int laserX = x;
                    int laserY = y;
                    int stopLaserErrorBreak = 0;
                    while (stopLaserErrorBreak < 13) {
                        stopLaserErrorBreak++;
                        thisField = board.getField(laserY, laserX, 0);
                        if (getPlayerOnField(laserX, laserY) != null) {
                            Player player = getPlayerOnField(laserX, laserY);
                            drawDamageAndSendDamageMessage(player, RoboRallyGame.getSpamCardPile(), laserCount);
                            break;
                        } else if (!MoveMethods.freeOfObstacles(thisField, findNextField(laserOrientation, laserX, laserY), laserOrientation)) {
                            break;
                        }
                        if (laserOrientation != null) {
                            switch (laserOrientation) {
                                case UP -> laserY--;
                                case DOWN -> laserY++;
                                case LEFT -> laserX--;
                                case RIGHT -> laserX++;
                            }
                        }
                        if (laserX < 0 || laserX > 12 || laserY < 0 || laserY > 9) {
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * robotLasersActivate()
     * when at the end of a register a robot faces a second robot, the second robot is hit by the first robot's laser
     * and has to draw a spamCard
     */
    public static void robotLasersActivate() throws InterruptedException {
        sendPlayerShootingMessage();
        synchronized (RoboRallyGame.getPlayersInGame()) {
            for (Player player : RoboRallyGame.getPlayersInGame()) {
                Direction laserOrientation = player.getRobot().getOrientation();
                int laserX = player.getRobot().getX();
                int laserY = player.getRobot().getY();
                int stopLaserErrorBreak = 0;
                while (stopLaserErrorBreak < 13) {
                    stopLaserErrorBreak++;
                    BoardTile thisField = board.getField(laserY, laserX, 0);
                    int nextFieldX = findNextFieldX(laserOrientation, laserX, laserY);
                    int nextFieldY = findNextFieldY(laserOrientation, laserX, laserY);
                    if (!MoveMethods.freeOfObstacles(thisField, findNextField(laserOrientation, laserX, laserY), laserOrientation)) {
                        break;
                    } else if (getPlayerOnField(nextFieldX, nextFieldY) != null) {
                        drawDamageAndSendDamageMessage(getPlayerOnField(nextFieldX, nextFieldY), RoboRallyGame.getSpamCardPile(), 1);
                        break;
                    }
                    switch (laserOrientation) {
                        case UP -> laserY--;
                        case DOWN -> laserY++;
                        case LEFT -> laserX--;
                        case RIGHT -> laserX++;
                    }
                    if (laserX < 0 || laserX > board.getColumns() - 1 || laserY < 0 || laserY > board.getRows() - 1) {
                        break;
                    }
                }
            }
        }
    }


    /**
     * calculateRotateDirection()
     *
     * @param boardTile this function analyzes whether a rotating conveyor belt rotates a robot clockwise or anticlockwise
     */
    private static boolean calculateRotateDirection(BoardTile boardTile) {
        String orientation1 = boardTile.getOrientations().get(0);
        String orientation2 = boardTile.getOrientations().get(1);

        return orientation1.equals("up") && orientation2.equals("left")
               || orientation1.equals("right") && orientation2.equals("up")
               || orientation1.equals("down") && orientation2.equals("right")
               || orientation1.equals("left") && orientation2.equals("down");
    }

    /**
     * drawDamageAndSendDamageMessage()
     *
     * @param player         who has to draw a damageCard
     * @param damageCardPile
     * @param cardsToDraw    this function lets a certain player draw a certain amount of damageCards and sends a pickDamage message if
     *                       the corresponding damageCardPile is empty
     */
    public static void drawDamageAndSendDamageMessage(Player player, Deck damageCardPile, int cardsToDraw) {
        int cardsDrawn = 0;
        int cardsStillToDraw = cardsToDraw;
        String kindOfDamageCard = switch (damageCardPile.getName()) {
            case "TrojanHorseCardPile" -> "Trojan Horse";
            case "SPAMCardPile" -> "SPAM";
            case "VirusCardPile" -> "Virus";
            case "WormCardPile" -> "Worm";
            default -> "SomethingWentWrong";
        };
        int maxRepetion = 0;
        while (!damageCardPile.isEmpty() && maxRepetion < 100) {
            maxRepetion++;
            player.getProgrammingDiscardPile().addCard(damageCardPile.drawCard(), 1);
            cardsStillToDraw--;
            cardsDrawn++;
            if (cardsStillToDraw == 0) {
                break;
            }
        }
        if (cardsDrawn > 0) {
            sendDrawDamageMessage(player, kindOfDamageCard, cardsDrawn);
        }
        if (cardsStillToDraw > 0 && !(RoboRallyGame.getSpamCardPile().isEmpty() && RoboRallyGame.getTrojanHorseCardPile().isEmpty()
                                      && RoboRallyGame.getVirusCardPile().isEmpty() && RoboRallyGame.getWormCardPile().isEmpty())) {
            sendPickDamageMessage(player, cardsStillToDraw);
            player.setActive(true);

            logger.info("starting to wait now");
            ClientHandler.getRoboRallyGame().waitThread();
            player.setActive(false);
            logger.info("game is waiting");
        }
    }


    /**
     * cheats()
     *
     * @param player
     * @param input  this function implements the cheats which are used when presenting and testing the game
     */
    public static synchronized void cheats(Player player, String input) {
        if (RoboRallyGame.CHEATS_ALLOWED) {
            String[] inputStrings = input.split(":");
            if (input.startsWith("$$moveTo:") && inputStrings.length == 2) {
                String cheat = inputStrings[1];
                try {
                    String[] coordinates = cheat.split(",");
                    if (coordinates.length == 2) {
                        int xVal = Integer.parseInt(coordinates[0]);
                        int yVal = Integer.parseInt(coordinates[1]);
                        if (0 <= xVal && xVal < 13 && 0 <= yVal && yVal < 10 &&
                            !(board.getField(yVal, xVal, 0).getType().equals("antenna")) && getPlayerOnField(xVal, yVal) == null) {
                            player.getRobot().setX(xVal);
                            player.getRobot().setY(yVal);
                            sendMoveMessage(player.getRobot().getX(), player.getRobot().getY(), player.getId(), false);
                        } else {
                            logger.error(input + ": no valid move position");
                        }
                    } else {
                        logger.error(input + ": no valid move position");
                    }
                } catch (NumberFormatException e) {
                    logger.error(input + ": no valid move position");
                }
            } else if (input.equals("$$rotate")) {
                player.getRobot().rotate(player, true, true);
            } else if (input.equals("$$clearSpamCardPile")) {
                RoboRallyGame.getSpamCardPile().clear();
            } else if (input.equals("$$populateSpamCardPile")) {
                RoboRallyGame.getSpamCardPile().populate();
            } else if (input.startsWith("$$setCheckpoints:") && inputStrings.length == 2) {
                String cheat = inputStrings[1];
                try {
                    int checkpointsReached = Integer.parseInt(cheat);
                    if (checkpointsReached < 0 || checkpointsReached > board.getCheckpoints())
                        throw new NumberFormatException();
                    player.setCheckpointsReached(checkpointsReached);
                } catch (NumberFormatException e) {
                    logger.error(input + ": no valid number of checkpoints");
                }

            } else if (input.equals("$$clearDamageCardPiles")) {
                RoboRallyGame.getSpamCardPile().clear();
                RoboRallyGame.getVirusCardPile().clear();
                RoboRallyGame.getWormCardPile().clear();
                RoboRallyGame.getTrojanHorseCardPile().clear();
            } else if (input.equals("$$populateDamageCardPiles")) {
                RoboRallyGame.getSpamCardPile().populate();
                RoboRallyGame.getVirusCardPile().populate();
                RoboRallyGame.getWormCardPile().populate();
                RoboRallyGame.getTrojanHorseCardPile().populate();
            } else if (input.equals("$$reboot")) {
                player.reboot();
            }
        } else {
            logger.error(input + ": cheats are deactivated at the moment!");
            Message errorMessage = new Error("Cheats are currently deactivated.");
            sendToSinglePlayer(player, gson.toJson(errorMessage));
        }
    }
}
