/**
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus, Hoang An Tran
 * Class Robot
 */

package GameLogic;

import ProgrammingCards.MoveMethods;

import static GameLogic.GameSendingMethods.sendPlayerTurningMessage;

public class Robot extends MoveMethods {

    private int x_position;
    private int y_position;
    private int startX;
    private int startY;
    private int distanceToAntenna;
    private String name;
    public Direction orientation;
    private Direction directionOfLastMovement;

    public Robot(String name, int x_position, int y_position, int distanceToAntenna) {
        this.name = name;
        this.x_position = x_position;
        this.y_position = y_position;
        this.distanceToAntenna = distanceToAntenna;
        this.orientation = Direction.RIGHT;
    }

    /**
     * rotate()
     *
     * @param player
     * @param clockwise
     * @param sendMessage
     * rotates the robot of a player clockwise or counterclockwise and sends a PlayerTurning message
     */
    public void rotate(Player player, boolean clockwise, boolean sendMessage) {
        int currentRotation = getDirectionInt(orientation);
        if (clockwise) {
            currentRotation = Math.floorMod(currentRotation + 1, 4);
        } else {
            currentRotation = Math.floorMod(currentRotation - 1, 4);
        }

        switch (currentRotation) {
            case 0 -> orientation = Direction.UP;
            case 1 -> orientation = Direction.RIGHT;
            case 2 -> orientation = Direction.DOWN;
            case 3 -> orientation = Direction.LEFT;
            default -> throw new IllegalStateException("Unexpected value: " + currentRotation);
        }
        if (sendMessage) {
            sendPlayerTurningMessage(player, clockwise, true);
        }
    }

    /**
     * setName()
     * @param figureNumber
     * transforms the integers 0 - 5 into corresponding robot names
     */
    public void setName(int figureNumber) {
        switch (figureNumber) {
            case 0 -> this.name = "TWONKY";
            case 1 -> this.name = "SMASH BOT";
            case 2 -> this.name = "HulkX90";
            case 3 -> this.name = "ZOOM BOT";
            case 4 -> this.name = "HAMMER BOT";
            case 5 -> this.name = "SPIN BOT";
            default -> this.name = "Dummy";
        }
    }

    public Direction getDirectionOfLastMovement() {
        return directionOfLastMovement;
    }

    public void setDirectionOfLastMovement(Direction directionOfLastMovement) {
        this.directionOfLastMovement = directionOfLastMovement;
    }

    public int getX() {
        return x_position;
    }


    public void setX(int x_position) {
        this.x_position = x_position;
    }

    public void increaseX() {
        this.x_position++;
    }

    public void decreaseX() {
        this.x_position--;
    }

    public int getY() {
        return y_position;
    }

    public void setY(int y_position) {
        this.y_position = y_position;
    }

    public void increaseY() {
        this.y_position++;
    }

    public void decreaseY() {
        this.y_position--;
    }

    public void setPosition(int x_position, int y_position) {
        this.x_position = x_position;
        this.y_position = y_position;
    }

    public int getDistanceToAntenna() {
        return this.distanceToAntenna;
    }

    public void setDistanceToAntenna(int distanceToAntenna) {
        this.distanceToAntenna = distanceToAntenna;
    }

    public String getName() {
        return name;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void setOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    public Direction getOrientation() {
        return orientation;
    }
}
