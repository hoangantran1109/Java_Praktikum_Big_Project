/**
 * @author Julian Wiemer, Alina Klessinger, Ludwig Kraus, Hoang An Tran
 * Enum Direction
 */


package GameLogic;

public enum Direction {
    DOWN, LEFT, RIGHT, UP;

    /**
     * getOpposite()
     * finds the opposite of a direction
     */
    public Direction getOpposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    /**
     * directionToString()
     * converts an Enum Direction into a String
     */
    public String directionToString() {
        return switch (this) {
            case UP -> "up";
            case DOWN -> "down";
            case RIGHT -> "right";
            case LEFT -> "left";
        };
    }

    /**
     * stringToDirection()
     * @param directionString
     * converts a matching String into an Enum Direction
     */
    public static Direction stringToDirection(String directionString) {
        return switch (directionString) {
            case "up" -> UP;
            case "down" -> DOWN;
            case "right" -> RIGHT;
            case "left" -> LEFT;
            default -> null;
        };
    }

}

